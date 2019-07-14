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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:cherry/fundamental/bizcal/Bizcal.properties" })
@ConfigurationProperties(prefix = "fundamental.bizcal")
public class BizcalConfiguration {

	private String stdCalName;

	@Autowired
	private DateTimeStrategy dateTimeStrategy;

	@Autowired
	private YearStrategy yearStrategy;

	@Autowired
	private WorkdayStrategy workdayStrategy;

	@Bean
	@Primary
	@ConditionalOnMissingBean
	public Bizcal bizcal() {
		return new BizcalImpl(dateTimeStrategy, yearStrategy, workdayStrategy, stdCalName);
	}

	public void setStdCalName(String stdCalName) {
		this.stdCalName = stdCalName;
	}

}
