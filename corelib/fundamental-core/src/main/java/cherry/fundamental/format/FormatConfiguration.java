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

import static cherry.elemental.util.JavaTimeUtil.getLocalDate;
import static cherry.elemental.util.JavaTimeUtil.getLocalDateTime;
import static cherry.elemental.util.JavaTimeUtil.getLocalTime;
import static cherry.elemental.util.JavaTimeUtil.getSqlDate;
import static cherry.elemental.util.JavaTimeUtil.getSqlTime;
import static cherry.elemental.util.JavaTimeUtil.getSqlTimestamp;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

@Configuration
@PropertySource({ "classpath:cherry/fundamental/format/Format.properties" })
@ConfigurationProperties(prefix = "fundamental.format")
public class FormatConfiguration {

	private boolean useDefault;

	@Bean
	public FactoryBean<? extends ConversionService> conversionServiceFactory() {

		FormattingConversionServiceFactoryBean bean = new FormattingConversionServiceFactoryBean();
		if (useDefault) {
			bean.setRegisterDefaultFormatters(true);
			return bean;
		}

		bean.setRegisterDefaultFormatters(false);

		NumberFormatAnnotationFormatterFactory f1 = new NumberFormatAnnotationFormatterFactory();
		NumberStyleFormatter f2 = new NumberStyleFormatter();
		f2.setPattern("###0.#########");
		bean.setFormatters(Stream.of(f1, f2).collect(Collectors.toSet()));

		DateFormatterRegistrar r1 = new DateFormatterRegistrar();
		DateTimeFormatterRegistrar r2 = new DateTimeFormatterRegistrar();
		r2.setDateFormatter(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		r2.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
		r2.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		FormatterRegistrar r3 = new FormatterRegistrar() {
			@Override
			public void registerFormatters(FormatterRegistry registry) {
				registry.addConverter(new LocalDateConverter());
				registry.addConverter(new SqlDateConverter());
				registry.addConverter(new LocalTimeConverter());
				registry.addConverter(new SqlTimeConverter());
				registry.addConverter(new LocalDateTimeConverter());
				registry.addConverter(new SqlTimestampConverter());
			}
		};

		bean.setFormatterRegistrars(Stream.of(r1, r2, r3).collect(Collectors.toSet()));

		return bean;
	}

	static class LocalDateConverter implements Converter<Date, LocalDate> {
		@Override
		public LocalDate convert(Date source) {
			return getLocalDate(source);
		}
	}

	static class SqlDateConverter implements Converter<LocalDate, Date> {
		@Override
		public Date convert(LocalDate source) {
			return getSqlDate(source);
		}
	}

	static class LocalTimeConverter implements Converter<Time, LocalTime> {
		@Override
		public LocalTime convert(Time source) {
			return getLocalTime(source);
		}
	}

	static class SqlTimeConverter implements Converter<LocalTime, Time> {
		@Override
		public Time convert(LocalTime source) {
			return getSqlTime(source);
		}
	}

	static class LocalDateTimeConverter implements Converter<Timestamp, LocalDateTime> {
		@Override
		public LocalDateTime convert(Timestamp source) {
			return getLocalDateTime(source);
		}
	}

	static class SqlTimestampConverter implements Converter<LocalDateTime, Timestamp> {
		@Override
		public Timestamp convert(LocalDateTime source) {
			return getSqlTimestamp(source);
		}
	}

	public void setUseDefault(boolean useDefault) {
		this.useDefault = useDefault;
	}

}
