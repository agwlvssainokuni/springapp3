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

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Year;

import org.apache.commons.lang3.Range;
import org.junit.Test;

public class SimpleYearStrategyTest {

	@Test
	public void test0101() {
		YearStrategy strategy = new SimpleYearStrategy(0, 1, 1);
		for (int year = 1900; year < 3000; year++) {
			Range<LocalDate> range = strategy.rangeOfBizYear("", Year.of(year));
			assertEquals(LocalDate.of(year, 1, 1), range.getMinimum());
			assertEquals(LocalDate.of(year, 12, 31), range.getMaximum());
		}
	}

	@Test
	public void test0401() {
		YearStrategy strategy = new SimpleYearStrategy(0, 4, 1);
		for (int year = 1900; year < 3000; year++) {
			Range<LocalDate> range = strategy.rangeOfBizYear("", Year.of(year));
			assertEquals(LocalDate.of(year, 4, 1), range.getMinimum());
			assertEquals(LocalDate.of(year + 1, 3, 31), range.getMaximum());
		}
	}

	@Test
	public void test0901() {
		YearStrategy strategy = new SimpleYearStrategy(-1, 9, 1);
		for (int year = 1900; year < 3000; year++) {
			Range<LocalDate> range = strategy.rangeOfBizYear("", Year.of(year));
			assertEquals(LocalDate.of(year - 1, 9, 1), range.getMinimum());
			assertEquals(LocalDate.of(year, 8, 31), range.getMaximum());
		}
	}

}
