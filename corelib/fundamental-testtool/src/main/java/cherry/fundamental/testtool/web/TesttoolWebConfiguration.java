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

package cherry.fundamental.testtool.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cherry.fundamental.testtool.invoker.InvokerService;
import cherry.fundamental.testtool.reflect.ReflectionResolver;
import cherry.fundamental.testtool.stub.StubConfigService;
import cherry.fundamental.testtool.stub.StubRepository;

@Configuration
public class TesttoolWebConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "fundamental.testtool.web", name = "invoker", havingValue = "true")
	public InvokerController invokerController(@Qualifier("jsonInvokerService") InvokerService jsonInvokerService,
			@Qualifier("yamlInvokerService") InvokerService yamlInvokerService, ReflectionResolver reflectionResolver) {
		return new InvokerControllerImpl(jsonInvokerService, yamlInvokerService, reflectionResolver);
	}

	@Bean
	@ConditionalOnProperty(prefix = "fundamental.testtool.web", name = "stubconfig", havingValue = "true")
	public StubConfigController stubConfigController(
			@Qualifier("jsonStubConfigService") StubConfigService jsonStubConfigService,
			@Qualifier("yamlStubConfigService") StubConfigService yamlStubConfigService, StubRepository repository,
			ReflectionResolver reflectionResolver) {
		return new StubConfigControllerImpl(jsonStubConfigService, yamlStubConfigService, repository,
				reflectionResolver);
	}

}
