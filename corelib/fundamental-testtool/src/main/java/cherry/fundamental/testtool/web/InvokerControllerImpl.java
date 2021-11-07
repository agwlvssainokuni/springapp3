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

import cherry.fundamental.testtool.invoker.InvokerService;
import cherry.fundamental.testtool.reflect.ReflectionResolver;

public class InvokerControllerImpl implements InvokerController {

	private final InvokerService invokerService;

	private final ReflectionResolver reflectionResolver;

	public InvokerControllerImpl(InvokerService invokerService, ReflectionResolver reflectionResolver) {
		this.invokerService = invokerService;
		this.reflectionResolver = reflectionResolver;
	}

	@Override
	public void page() {
		// 何もしない。
	}

	@Override
	public String invoke(String beanName, String className, String methodName, int methodIndex, String args,
			String argTypes) {
		return invokerService.invoke(beanName, className, methodName, methodIndex, args, argTypes);
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

}
