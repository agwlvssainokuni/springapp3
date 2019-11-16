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
import java.time.YearMonth;

import org.apache.commons.lang3.Range;

/**
 * 業務年月。<br />
 */
public class SimpleYearMonthStrategy implements YearMonthStrategy {

	private final long yearOffset;

	private final long monthOffset;

	private final long dayOffset;

	public SimpleYearMonthStrategy(long yearOffset, long monthOffset, long dayOffset) {
		this.yearOffset = yearOffset;
		this.monthOffset = monthOffset;
		this.dayOffset = dayOffset;
	}

	@Override
	public Range<LocalDate> rangeOfYearMonth(String name, YearMonth ym) {
		LocalDate firstDate = resolveOffsert(ym);
		LocalDate lastDate = resolveOffsert(ym.plusMonths(1L)).minusDays(1L);
		return Range.between(firstDate, lastDate, (o1, o2) -> o1.compareTo(o2));
	}

	private LocalDate resolveOffsert(YearMonth ym) {
		return ym.atDay(1).plusYears(yearOffset).plusMonths(monthOffset).plusDays(dayOffset);
	}

}
