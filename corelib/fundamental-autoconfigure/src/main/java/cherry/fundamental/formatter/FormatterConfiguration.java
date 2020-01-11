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

package cherry.fundamental.formatter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.validation.Validator;

import cherry.fundamental.validator.DataBinderHelper;
import cherry.fundamental.validator.DataBinderHelperImpl;

@Configuration
@PropertySource({ "classpath:cherry.properties" })
public class FormatterConfiguration {

	@ConditionalOnClass({ NumFormatter.class, DtmFormatter.class })
	public static class FormatterCfg {

		private final ConversionService conversionService = createConversionService();

		@Bean
		public NumFormatter numFormatter() {
			return new NumFormatter(conversionService);
		}

		@Bean
		public DtmFormatter dtmFormatter() {
			return new DtmFormatter(conversionService);
		}
	}

	@ConditionalOnClass({ DataBinderHelper.class })
	public static class DataBinderHelperCfg {

		private final ConversionService conversionService = createConversionService();

		@Bean
		public DataBinderHelper dataBinderHelper(Validator validator) {
			DataBinderHelperImpl impl = new DataBinderHelperImpl();
			impl.setConversionService(conversionService);
			impl.setValidator(validator);
			return impl;
		}
	}

	private static ConversionService createConversionService() {
		FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
		factoryBean.setRegisterDefaultFormatters(true);
		factoryBean.setFormatters(Stream.of(new SupplementalDateTimeFormatterFactory()).collect(Collectors.toSet()));
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}

}
