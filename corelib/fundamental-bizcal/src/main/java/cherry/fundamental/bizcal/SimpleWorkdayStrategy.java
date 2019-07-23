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

package cherry.fundamental.bizcal;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 営業日。<br />
 */
public class SimpleWorkdayStrategy implements WorkdayStrategy {

	@Override
	public int getNumberOfWorkday(String name, LocalDate from, LocalDate to) {
		return (int) from.until(to.plusDays(1), ChronoUnit.DAYS);
	}

	@Override
	public LocalDate getNextWorkday(String name, LocalDate from, int numberOfWorkday) {
		return from.plusDays(numberOfWorkday - 1);
	}

}
