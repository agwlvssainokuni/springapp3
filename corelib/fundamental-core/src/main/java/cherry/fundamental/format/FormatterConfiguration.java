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

package cherry.fundamental.format;

import java.time.MonthDay;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.format.datetime.standard.TemporalAccessorParser;
import org.springframework.format.datetime.standard.TemporalAccessorPrinter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.util.StringUtils;

@Configuration
@PropertySource({ "classpath:cherry/fundamental/format/Format.properties" })
public class FormatterConfiguration {

	private final ConversionService conversionService = createConversionService();

	private ConversionService createConversionService() {
		FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
		factoryBean.setRegisterDefaultFormatters(true);
		factoryBean.setFormatters(Stream.of(new SupplementalDateTimeFormatterFactory()).collect(Collectors.toSet()));
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}

	@ConditionalOnProperty(prefix = "fundamental.format", name = "numFormatter", havingValue = "true", matchIfMissing = true)
	@Bean
	public NumFormatter numFormatter() {
		return new NumFormatter(conversionService);
	}

	@ConditionalOnProperty(prefix = "fundamental.format", name = "dtmFormatter", havingValue = "true", matchIfMissing = true)
	@Bean
	public DtmFormatter dtmFormatter() {
		return new DtmFormatter(conversionService);
	}

	static class SupplementalDateTimeFormatterFactory extends EmbeddedValueResolutionSupport
			implements AnnotationFormatterFactory<DateTimeFormat> {

		private static final Set<Class<?>> FIELD_TYPES;

		static {
			Set<Class<?>> fieldTypes = new HashSet<>(2);
			fieldTypes.add(YearMonth.class);
			fieldTypes.add(MonthDay.class);
			FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
		}

		@Override
		public Set<Class<?>> getFieldTypes() {
			return FIELD_TYPES;
		}

		@Override
		public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
			DateTimeFormatter formatter = getFormatter(annotation, fieldType);
			if (fieldType == YearMonth.class) {
				return (YearMonth ym, Locale lc) -> ym.format(formatter);
			} else if (fieldType == MonthDay.class) {
				return (MonthDay md, Locale lc) -> md.format(formatter);
			} else {
				return new TemporalAccessorPrinter(formatter);
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
			DateTimeFormatter formatter = getFormatter(annotation, fieldType);
			if (fieldType == YearMonth.class) {
				return (String text, Locale locale) -> YearMonth.parse(text, formatter);
			} else if (fieldType == MonthDay.class) {
				return (String text, Locale locale) -> MonthDay.parse(text, formatter);
			} else {
				return new TemporalAccessorParser((Class<? extends TemporalAccessor>) fieldType, formatter);
			}
		}

		protected DateTimeFormatter getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
			DateTimeFormatterFactory factory = new DateTimeFormatterFactory();
			String style = resolveEmbeddedValue(annotation.style());
			if (StringUtils.hasLength(style)) {
				factory.setStylePattern(style);
			}
			factory.setIso(annotation.iso());
			String pattern = resolveEmbeddedValue(annotation.pattern());
			if (StringUtils.hasLength(pattern)) {
				factory.setPattern(pattern);
			}
			return factory.createDateTimeFormatter();
		}
	}

}
