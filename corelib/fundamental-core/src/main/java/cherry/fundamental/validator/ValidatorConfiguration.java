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

package cherry.fundamental.validator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.Validator;

@Configuration
@PropertySource({ "classpath:cherry/fundamental/validator/Validator.properties" })
@ConfigurationProperties(prefix = "fundamental.validator")
public class ValidatorConfiguration {

	@Bean
	public DataBinderHelper dataBinderHelper(ConversionService conversionService, Validator validator) {
		DataBinderHelperImpl impl = new DataBinderHelperImpl();
		impl.setConversionService(conversionService);
		impl.setValidator(validator);
		return impl;
	}

}
