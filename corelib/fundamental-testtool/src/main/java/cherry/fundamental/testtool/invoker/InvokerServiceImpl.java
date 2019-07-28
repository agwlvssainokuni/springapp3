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

package cherry.fundamental.testtool.invoker;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cherry.fundamental.testtool.reflect.ReflectionResolver;
import cherry.fundamental.testtool.util.ToMapUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InvokerServiceImpl implements InvokerService, ApplicationContextAware {

	private final ObjectMapper objectMapper;

	private final ReflectionResolver reflectionResolver;

	private ApplicationContext appCtx;

	public InvokerServiceImpl(ObjectMapper objectMapper, ReflectionResolver reflectionResolver) {
		this.objectMapper = objectMapper;
		this.reflectionResolver = reflectionResolver;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.appCtx = applicationContext;
	}

	@Override
	public String invoke(String beanName, Class<?> beanClass, Method method, List<String> args, List<String> argTypes) {
		try {

			Object targetBean;
			if (StringUtils.isEmpty(beanName)) {
				targetBean = appCtx.getBean(beanClass);
			} else {
				targetBean = appCtx.getBean(beanName, beanClass);
			}

			Type[] paramType = method.getGenericParameterTypes();
			Object[] param = new Object[paramType.length];
			for (int i = 0; i < paramType.length; i++) {
				String arg = args == null || args.size() <= i ? null : args.get(i);
				String argType = argTypes == null || argTypes.size() <= i ? null : argTypes.get(i);
				JavaType javaType;
				if (StringUtils.isNotEmpty(argType)) {
					javaType = objectMapper.getTypeFactory().constructFromCanonical(argType);
				} else {
					javaType = objectMapper.getTypeFactory().constructType(paramType[i]);
				}
				param[i] = arg == null ? null : objectMapper.readValue(arg, javaType);
			}

			Object result = method.invoke(targetBean, param);
			return objectMapper.writeValueAsString(result);
		} catch (InvocationTargetException | IllegalAccessException | IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public String invoke(String beanName, String className, String methodName, int methodIndex, String args,
			String argTypes) {
		try {

			Class<?> beanClass = getClass().getClassLoader().loadClass(className);
			List<Method> methodList = reflectionResolver.resolveMethod(beanClass, methodName);
			if (methodList.isEmpty()) {
				throw new NoSuchMethodException(format("{0}#{1}() not found", className, methodName));
			}
			Method method = (methodList.size() == 1 ? methodList.get(0) : methodList.get(methodIndex));

			return invoke(beanName, beanClass, method, resolveArgs(args), resolveArgTypes(argTypes));
		} catch (ClassNotFoundException | NoSuchMethodException ex) {
			return fromThrowableToString(ex);
		} catch (IOException ex) {
			return fromThrowableToString(ex);
		} catch (IllegalStateException ex) {
			if (ex.getCause() instanceof InvocationTargetException || ex.getCause() instanceof IllegalAccessException
					|| ex.getCause() instanceof IOException) {
				return fromThrowableToString(ex.getCause());
			} else {
				return fromThrowableToString(ex);
			}
		} catch (Exception ex) {
			return fromThrowableToString(ex);
		}
	}

	private List<String> resolveArgs(String param) throws JsonProcessingException, IOException {
		if (StringUtils.isEmpty(param)) {
			return null;
		}
		List<String> list = new ArrayList<>();
		for (Object value : objectMapper.readValue(param, List.class)) {
			list.add(objectMapper.writeValueAsString(value));
		}
		return list;
	}

	private List<String> resolveArgTypes(String param) throws JsonProcessingException, IOException {
		if (StringUtils.isEmpty(param)) {
			return null;
		}
		return objectMapper.readValue(param, new TypeReference<List<String>>() {
		});
	}

	private String fromThrowableToString(Throwable ex) {
		Map<String, ?> map = ToMapUtil.fromThrowable(ex, Integer.MAX_VALUE);
		try {
			return objectMapper.writeValueAsString(map);
		} catch (IOException ex2) {
			return ex.getMessage();
		}
	}

}
