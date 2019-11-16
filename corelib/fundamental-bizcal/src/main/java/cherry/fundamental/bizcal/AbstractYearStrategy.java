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

import org.apache.commons.lang3.Range;

/**
 * 業務年ストラテジの抽象実装。<br />
 */
public abstract class AbstractYearStrategy implements YearStrategy {

	@Override
	public Range<LocalDate> rangeOfYear(String name, Year year) {
		LocalDate firstDate = resolveFirstDate(name, year);
		LocalDate lastDate = resolveFirstDate(name, year.plusYears(1L)).minusDays(1L);
		return Range.between(firstDate, lastDate, (o1, o2) -> o1.compareTo(o2));
	}

	protected abstract LocalDate resolveFirstDate(String name, Year year);

}
