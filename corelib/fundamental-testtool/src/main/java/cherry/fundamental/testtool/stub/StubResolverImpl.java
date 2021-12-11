/*
 * Copyright 2015,2021 agwlvssainokuni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cherry.fundamental.testtool.stub;

import java.lang.reflect.Method;
import java.util.Optional;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class StubResolverImpl implements StubResolver {

    private final StubRepository repository;

    private final ScriptProcessor scriptProcessor;

    public StubResolverImpl(StubRepository repository, ScriptProcessor scriptProcessor) {
        this.repository = repository;
        this.scriptProcessor = scriptProcessor;
    }

    @Override
    public Optional<StubInvocation> getStubInvocation(Method method) {
        return Optional.of(method).filter(repository::contains).map(repository::get)
                .filter(StubConfig::hasNext).map(stub -> args -> {
                    if (stub.isThrowable()) {
                        throw stub.nextThrowable().getDeclaredConstructor().newInstance();
                    } else if (stub.isMock()) {
                        return method.invoke(stub.nextMock(), args);
                    } else if (stub.isScript()) {
                        String engine = stub.peekScriptEngine();
                        String script = stub.nextScript();
                        return scriptProcessor.eval(script, engine, args);
                    } else {
                        return stub.next();
                    }
                });
    }

    @Override
    public Optional<StubInvocation> getStubInvocation(MethodInvocation invocation) {
        return Optional.of(invocation).map(MethodInvocation::getMethod)
                .flatMap(this::getStubInvocation);
    }

    @Override
    public Optional<StubInvocation> getStubInvocation(ProceedingJoinPoint pjp) {
        return Optional.of(pjp).map(ProceedingJoinPoint::getSignature)
                .filter(MethodSignature.class::isInstance).map(MethodSignature.class::cast)
                .map(MethodSignature::getMethod)
                .flatMap(this::getStubInvocation);
    }

}
