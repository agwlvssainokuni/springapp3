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

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
@ConfigurationProperties(prefix = "fundamental.bizcal")
public class BizcalConfiguration {

	private int yearOfFirstOffset;

	private int monthOfFirst;

	private int dayOfFirst;

	private String stdCalName;

	@Bean
	public Bizcal bizcal() {
		return new BizcalImpl(new SimpleDateTimeStrategy(), new SimpleYearStrategy(yearOfFirstOffset, monthOfFirst,
				dayOfFirst), new SimpleWorkdayStrategy(), stdCalName);
	}

	public void setYearOfFirstOffset(int yearOfFirstOffset) {
		this.yearOfFirstOffset = yearOfFirstOffset;
	}

	public void setMonthOfFirst(int monthOfFirst) {
		this.monthOfFirst = monthOfFirst;
	}

	public void setDayOfFirst(int dayOfFirst) {
		this.dayOfFirst = dayOfFirst;
	}

	public void setStdCalName(String stdCalName) {
		this.stdCalName = stdCalName;
	}

}
