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

import static cherry.fundamental.testtool.util.ReflectionUtil.getMethodDescription;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StubConfigLoader {

	private final StubRepository repository;

	private final ObjectMapper objectMapper;

	public StubConfigLoader(StubRepository repository, ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	public void load(List<Resource> resources) throws IOException {
		for (Resource r : resources) {
			if (!r.exists()) {
				continue;
			}
			try (InputStream in = r.getInputStream()) {
				Map<Class<?>, Map<String, Object>> map = objectMapper.readValue(in,
						new TypeReference<LinkedHashMap<Class<?>, Map<String, Object>>>() {
						});
				for (Map.Entry<Class<?>, Map<String, Object>> entry : map.entrySet()) {
					MultiValuedMap<String, Method> methodMap = createMethodMap(entry.getKey().getDeclaredMethods());
					for (Map.Entry<String, Object> ent : entry.getValue().entrySet()) {
						if (!methodMap.containsKey(ent.getKey())) {
							continue;
						}
						for (Method method : methodMap.get(ent.getKey())) {
							JavaType type = objectMapper.getTypeFactory().constructType(method.getGenericReturnType());
							List<Config> cfglist = parseConfig(ent.getValue());
							for (Config cfg : cfglist) {
								Object v = parseValue(cfg, type);
								if (cfglist.size() == 1) {
									repository.get(method).alwaysReturn(v);
								} else {
									repository.get(method).thenReturn(v);
								}
							}
						}
					}
				}
			}
		}
	}

	private List<Config> parseConfig(Object value) throws IOException {
		String deparsed = objectMapper.writeValueAsString(value);
		if (value instanceof List) {
			return objectMapper.readValue(deparsed, new TypeReference<List<Config>>() {
			});
		} else {
			return asList(objectMapper.readValue(deparsed, Config.class));
		}
	}

	private Object parseValue(Config cfg, JavaType defaultType) throws IOException {
		String data = objectMapper.writeValueAsString(cfg.getData());
		JavaType type = defaultType;
		if (StringUtils.isNotEmpty(cfg.getType())) {
			type = objectMapper.getTypeFactory().constructFromCanonical(cfg.getType());
		}
		return objectMapper.readValue(data, type);
	}

	private MultiValuedMap<String, Method> createMethodMap(Method[] methods) {
		MultiValuedMap<String, Method> map = new ArrayListValuedHashMap<>();
		for (Method m : methods) {
			map.put(getMethodDescription(m, false, false, true, false, false), m);
			map.put(getMethodDescription(m, false, false, true, true, false), m);
			map.put(getMethodDescription(m, false, false, true, true, true), m);
		}
		return map;
	}

	public static class Config {

		private String type;

		private Object data;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
	}

}
