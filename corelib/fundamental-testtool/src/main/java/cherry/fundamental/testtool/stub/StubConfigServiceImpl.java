/*
 * Copyright 2015,2019 agwlvssainokuni
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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import cherry.fundamental.testtool.reflect.ReflectionResolver;
import cherry.fundamental.testtool.util.ToMapUtil;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StubConfigServiceImpl implements StubConfigService {

	private final StubRepository repository;

	private final ObjectMapper objectMapper;

	private final ReflectionResolver reflectionResolver;

	public StubConfigServiceImpl(StubRepository repository, ObjectMapper objectMapper,
			ReflectionResolver reflectionResolver) {
		this.repository = repository;
		this.objectMapper = objectMapper;
		this.reflectionResolver = reflectionResolver;
	}

	@Override
	public boolean hasNext(String className, String methodName, int methodIndex) {
		return execute(className, methodName, methodIndex, (Predicate<Method>) (method) -> {
			return repository.get(method).hasNext();
		});
	}

	@Override
	public String peek(String className, String methodName, int methodIndex) {
		return executeWithMapping(className, methodName, methodIndex, (Function<Method, Object>) (method) -> {
			return repository.get(method).peek();
		});
	}

	@Override
	public String peekType(String className, String methodName, int methodIndex) {
		return execute(className, methodName, methodIndex, (Function<Method, String>) (method) -> {
			return repository.get(method).peekType();
		});
	}

	@Override
	public boolean isThrowable(String className, String methodName, int methodIndex) {
		return execute(className, methodName, methodIndex, (Predicate<Method>) (method) -> {
			return repository.get(method).isThrowable();
		});
	}

	@Override
	public String peekThrowable(String className, String methodName, int methodIndex) {
		return execute(className, methodName, methodIndex, (Function<Method, String>) (method) -> {
			return repository.get(method).peekThrowable().getCanonicalName();
		});
	}

	@Override
	public String clear(String className, String methodName, int methodIndex) {
		return executeWithMapping(className, methodName, methodIndex, (Function<Method, Boolean>) (method) -> {
			repository.clear(method);
			return Boolean.TRUE;
		});
	}

	@Override
	public String alwaysReturn(String className, String methodName, int methodIndex, String value, String valueType) {
		return executeWithMapping(className, methodName, methodIndex, (Function<Method, Object>) (method) -> {
			JavaType returnType = objectMapper.getTypeFactory().constructType(method.getGenericReturnType());
			if (StringUtils.isNotEmpty(valueType)) {
				returnType = objectMapper.getTypeFactory().constructFromCanonical(valueType);
			}
			try {
				Object v = objectMapper.readValue(value, returnType);
				repository.get(method).alwaysReturn(v, returnType.toCanonical());
				return Boolean.TRUE;
			} catch (IOException ex) {
				return ToMapUtil.fromThrowable(ex, Integer.MAX_VALUE);
			}
		});
	}

	@Override
	public String thenReturn(String className, String methodName, int methodIndex, String value, String valueType) {
		return executeWithMapping(className, methodName, methodIndex, (Function<Method, Object>) (method) -> {
			JavaType returnType = objectMapper.getTypeFactory().constructType(method.getGenericReturnType());
			if (StringUtils.isNotEmpty(valueType)) {
				returnType = objectMapper.getTypeFactory().constructFromCanonical(valueType);
			}
			try {
				Object v = objectMapper.readValue(value, returnType);
				repository.get(method).thenReturn(v, returnType.toCanonical());
				return Boolean.TRUE;
			} catch (IOException ex) {
				return ToMapUtil.fromThrowable(ex, Integer.MAX_VALUE);
			}
		});
	}

	@Override
	public String alwaysThrows(String className, String methodName, int methodIndex, String throwableClassName) {
		return executeWithMapping(className, methodName, methodIndex, (Function<Method, Boolean>) (method) -> {
			JavaType type = objectMapper.getTypeFactory().constructFromCanonical(throwableClassName);
			@SuppressWarnings("unchecked")
			Class<? extends Throwable> klass = (Class<? extends Throwable>) type.getRawClass();
			repository.get(method).alwaysThrows(klass);
			return Boolean.TRUE;
		});
	}

	@Override
	public String thenThrows(String className, String methodName, int methodIndex, String throwableClassName) {
		return executeWithMapping(className, methodName, methodIndex, (Function<Method, Boolean>) (method) -> {
			JavaType type = objectMapper.getTypeFactory().constructFromCanonical(throwableClassName);
			@SuppressWarnings("unchecked")
			Class<? extends Throwable> klass = (Class<? extends Throwable>) type.getRawClass();
			repository.get(method).thenThrows(klass);
			return Boolean.TRUE;
		});
	}

	private boolean execute(String className, String methodName, int methodIndex, Predicate<Method> predicate) {
		try {
			List<Method> list = reflectionResolver.resolveMethod(className, methodName);
			if (methodIndex >= list.size()) {
				return false;
			}
			return predicate.test(list.get(methodIndex));
		} catch (ClassNotFoundException ex) {
			return false;
		}
	}

	private String execute(String className, String methodName, int methodIndex, Function<Method, String> function) {
		try {
			List<Method> list = reflectionResolver.resolveMethod(className, methodName);
			if (methodIndex >= list.size()) {
				return String.valueOf(false);
			}
			return function.apply(list.get(methodIndex));
		} catch (ClassNotFoundException ex) {
			return ex.getMessage();
		}
	}

	private <T> String executeWithMapping(String className, String methodName, int methodIndex,
			Function<Method, T> function) {
		try {
			List<Method> list = reflectionResolver.resolveMethod(className, methodName);
			if (methodIndex >= list.size()) {
				return objectMapper.writeValueAsString(Boolean.FALSE);
			}
			T result = function.apply(list.get(methodIndex));
			return objectMapper.writeValueAsString(result);
		} catch (ClassNotFoundException | IOException | IllegalArgumentException ex) {
			Map<String, ?> map = ToMapUtil.fromThrowable(ex, Integer.MAX_VALUE);
			try {
				return objectMapper.writeValueAsString(map);
			} catch (IOException ex2) {
				return ex.getMessage();
			}
		}
	}

}
