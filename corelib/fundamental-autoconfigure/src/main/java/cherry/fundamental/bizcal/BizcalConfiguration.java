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

package cherry.fundamental.bizcal;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:cherry.properties" })
public class BizcalConfiguration {

	@ConditionalOnClass({ Bizcal.class })
	@ConfigurationProperties(prefix = "cherry.bizcal")
	public static class BizcalCfg {

		private String stdCalName;

		@Bean
		@ConditionalOnMissingBean
		public Bizcal bizcal(DateTimeStrategy dateTimeStrategy, YearMonthStrategy yearMonthStrategy,
				YearStrategy yearStrategy, WorkdayStrategy workdayStrategy) {
			return new BizcalImpl(dateTimeStrategy, yearMonthStrategy, yearStrategy, workdayStrategy, stdCalName);
		}

		public void setStdCalName(String stdCalName) {
			this.stdCalName = stdCalName;
		}
	}

	@ConditionalOnClass({ DateTimeStrategy.class })
	@ConfigurationProperties(prefix = "cherry.bizcal.datetime")
	public static class DefaultDateTimeStrategy {

		@Bean
		@ConditionalOnMissingBean
		public DateTimeStrategy dateTimeStrategy() {
			return new SimpleDateTimeStrategy();
		}
	}

	@ConditionalOnClass({ YearMonthStrategy.class })
	@ConfigurationProperties(prefix = "cherry.bizcal.yearmonth")
	public static class DefaultYearMonthStrategy {

		private long yearOffset = 0L;
		private long monthOffset = 0L;
		private long dayOffset = 0L;

		@Bean
		@ConditionalOnMissingBean
		public YearMonthStrategy yearMonthStrategy() {
			return new SimpleYearMonthStrategy(yearOffset, monthOffset, dayOffset);
		}

		public void setYearOffset(long yearOffset) {
			this.yearOffset = yearOffset;
		}

		public void setMonthOffset(long monthOffset) {
			this.monthOffset = monthOffset;
		}

		public void setDayOffset(long dayOffset) {
			this.dayOffset = dayOffset;
		}
	}

	@ConditionalOnClass({ YearStrategy.class })
	@ConfigurationProperties(prefix = "cherry.bizcal.year")
	public static class DefaultYearStrategy {

		private long yearOffset = 0L;
		private long monthOffset = 0L;
		private long dayOffset = 0L;

		@Bean
		@ConditionalOnMissingBean
		public YearStrategy yearStrategy() {
			return new SimpleYearStrategy(yearOffset, monthOffset, dayOffset);
		}

		public void setYearOffset(long yearOffset) {
			this.yearOffset = yearOffset;
		}

		public void setMonthOffset(long monthOffset) {
			this.monthOffset = monthOffset;
		}

		public void setDayOffset(long dayOffset) {
			this.dayOffset = dayOffset;
		}
	}

	@ConditionalOnClass({ WorkdayStrategy.class })
	@ConfigurationProperties(prefix = "cherry.bizcal.workday")
	public static class DefaultWorkdayStrategy {

		@Bean
		@ConditionalOnMissingBean
		public WorkdayStrategy workdayStrategy() {
			return new SimpleWorkdayStrategy();
		}
	}

}
