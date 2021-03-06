/*
 * Copyright 2019 agwlvssainokuni
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

package cherry.fundamental.testtool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import cherry.fundamental.testtool.invoker.InvokerService;
import cherry.fundamental.testtool.invoker.InvokerServiceImpl;
import cherry.fundamental.testtool.reflect.ReflectionResolver;
import cherry.fundamental.testtool.reflect.ReflectionResolverImpl;
import cherry.fundamental.testtool.stub.StubConfigService;
import cherry.fundamental.testtool.stub.StubConfigServiceImpl;
import cherry.fundamental.testtool.stub.StubInterceptor;
import cherry.fundamental.testtool.stub.StubRepository;
import cherry.fundamental.testtool.stub.StubRepositoryImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class TesttoolConfiguration {

	private final ReflectionResolver reflectionResolver = new ReflectionResolverImpl();

	private final StubRepository repository = new StubRepositoryImpl();

	private final ObjectMapper jsonObjectMapper = Jackson2ObjectMapperBuilder.json().modules(new JavaTimeModule())
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();

	private final ObjectMapper yamlObjectMapper = Jackson2ObjectMapperBuilder.json().modules(new JavaTimeModule())
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).factory(new YAMLFactory()).build();

	@Bean
	public ReflectionResolver reflectionResolver() {
		return reflectionResolver;
	}

	@Bean
	public StubRepository stubRepository() {
		return repository;
	}

	@Bean
	@Primary
	public InvokerService jsonInvokerService() {
		return new InvokerServiceImpl(jsonObjectMapper, reflectionResolver);
	}

	@Bean
	public InvokerService yamlInvokerService() {
		return new InvokerServiceImpl(yamlObjectMapper, reflectionResolver);
	}

	@Bean
	public StubInterceptor stubInterceptor() {
		return new StubInterceptor(repository);
	}

	@Bean
	@Primary
	public StubConfigService jsonStubConfigService() {
		return new StubConfigServiceImpl(repository, jsonObjectMapper, reflectionResolver);
	}

	@Bean
	public StubConfigService yamlStubConfigService() {
		return new StubConfigServiceImpl(repository, yamlObjectMapper, reflectionResolver);
	}

}
