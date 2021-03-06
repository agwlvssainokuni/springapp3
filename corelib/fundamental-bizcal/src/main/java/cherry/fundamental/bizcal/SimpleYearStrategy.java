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
import java.time.Year;

/**
 * 業務年。<br />
 */
public class SimpleYearStrategy extends AbstractYearStrategy {

	private final long yearOffset;

	private final long monthOffset;

	private final long dayOffset;

	public SimpleYearStrategy(long yearOffset, long monthOffset, long dayOffset) {
		this.yearOffset = yearOffset;
		this.monthOffset = monthOffset;
		this.dayOffset = dayOffset;
	}

	@Override
	protected LocalDate resolveFirstDate(String name, Year year) {
		return year.atMonth(1).atDay(1).plusYears(yearOffset).plusMonths(monthOffset).plusDays(dayOffset);
	}

}
