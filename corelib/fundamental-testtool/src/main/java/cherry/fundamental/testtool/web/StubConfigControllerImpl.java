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

package cherry.fundamental.testtool.web;

import static cherry.fundamental.testtool.util.ReflectionUtil.getMethodDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import cherry.fundamental.testtool.reflect.ReflectionResolver;
import cherry.fundamental.testtool.stub.StubConfigService;
import cherry.fundamental.testtool.stub.StubRepository;

public class StubConfigControllerImpl implements StubConfigController {

	private final StubConfigService stubConfigService;

	private final StubRepository repository;

	private final ReflectionResolver reflectionResolver;

	public StubConfigControllerImpl(StubConfigService stubConfigService, StubRepository repository,
			ReflectionResolver reflectionResolver) {
		this.stubConfigService = stubConfigService;
		this.repository = repository;
		this.reflectionResolver = reflectionResolver;
	}

	@Override
	public void page() {
		// 何もしない。
	}

	@Override
	public String alwaysReturn(String className, String methodName, int methodIndex, String value, String valueType) {
		if (StringUtils.isEmpty(value)) {
			return stubConfigService.clear(className, methodName, methodIndex);
		} else {
			return stubConfigService.alwaysReturn(className, methodName, methodIndex, value, valueType);
		}
	}

	@Override
	public List<String> peekStub(String className, String methodName, int methodIndex) {
		if (!stubConfigService.hasNext(className, methodName, methodIndex)) {
			return null;
		}
		List<String> list = new ArrayList<>();
		list.add(stubConfigService.peek(className, methodName, methodIndex));
		list.add(stubConfigService.peekType(className, methodName, methodIndex));
		return list;
	}

	@Override
	public List<String> resolveBeanName(String className) {
		try {
			return reflectionResolver.resolveBeanName(className);
		} catch (ClassNotFoundException ex) {
			return new ArrayList<>();
		}
	}

	@Override
	public List<String> resolveMethod(String className, String methodName) {
		try {
			return reflectionResolver.resolveMethod(className, methodName).stream()
					.map(m -> getMethodDescription(m, false, false, false, true, false)).collect(Collectors.toList());
		} catch (ClassNotFoundException ex) {
			return new ArrayList<>();
		}
	}

	@Override
	public List<String> getStubbedMethod(String className) {
		return repository.getStubbedMethod().stream()
				.filter(m -> StringUtils.isEmpty(className) || m.getDeclaringClass().getName().equals(className))
				.map(m -> getMethodDescription(m, false, true, true, true, true)).collect(Collectors.toList());
	}

}
