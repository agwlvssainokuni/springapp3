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

import org.junit.Test;

public class BizcalYearTest {

	private Bizcal bizcal = new BizcalImpl(new SimpleDateTimeStrategy(), new SimpleYearMonthStrategy(0L, 0L, 0L),
			new SimpleYearStrategy(0L, 3L, 0L), new SimpleWorkdayStrategy(), "standard");

	private Bizcal bizcal0901 = new BizcalImpl(new SimpleDateTimeStrategy(), new SimpleYearMonthStrategy(0L, 0L, 0L),
			new SimpleYearStrategy(-1L, 8L, 0L), new SimpleWorkdayStrategy(), "standard");

	@Test
	public void testYear() {
		for (int year = 1900; year < 3000; year++) {
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 4) {
					assertEquals(Year.of(year).minusYears(1), bizcal.year(dt));
				} else {
					assertEquals(Year.of(year), bizcal.year(dt));
				}
			}
		}
	}

	@Test
	public void testFirstDayOfYear() {
		for (int year = 1900; year < 3000; year++) {
			assertEquals(LocalDate.of(year, 4, 1), bizcal.firstDayOfYear(Year.of(year)));
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 4) {
					assertEquals(LocalDate.of(year - 1, 4, 1), bizcal.firstDayOfYear(dt));
				} else {
					assertEquals(LocalDate.of(year, 4, 1), bizcal.firstDayOfYear(dt));
				}
			}
		}
	}

	@Test
	public void testLastDayOfYear() {
		for (int year = 1900; year < 3000; year++) {
			assertEquals(LocalDate.of(year + 1, 3, 31), bizcal.lastDayOfYear(Year.of(year)));
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 4) {
					assertEquals(LocalDate.of(year, 3, 31), bizcal.lastDayOfYear(dt));
				} else {
					assertEquals(LocalDate.of(year + 1, 3, 31), bizcal.lastDayOfYear(dt));
				}
			}
		}
	}

	@Test
	public void test0901() {
		for (int year = 1900; year < 3000; year++) {
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 9) {
					assertEquals(Year.of(year), bizcal0901.year(dt));
					assertEquals(LocalDate.of(year - 1, 9, 1), bizcal0901.firstDayOfYear(dt));
					assertEquals(LocalDate.of(year, 8, 31), bizcal0901.lastDayOfYear(dt));
				} else {
					assertEquals(Year.of(year).plusYears(1), bizcal0901.year(dt));
					assertEquals(LocalDate.of(year, 9, 1), bizcal0901.firstDayOfYear(dt));
					assertEquals(LocalDate.of(year + 1, 8, 31), bizcal0901.lastDayOfYear(dt));
				}
			}
		}
	}

	@Test
	public void testForToday() {
		LocalDate today = LocalDate.now();
		if (today.getMonthValue() < 4) {
			assertEquals(Year.of(today.getYear()).minusYears(1), bizcal.year());
			assertEquals(LocalDate.of(today.getYear() - 1, 4, 1), bizcal.firstDayOfYear());
			assertEquals(LocalDate.of(today.getYear(), 3, 31), bizcal.lastDayOfYear());
		} else {
			assertEquals(Year.of(today.getYear()), bizcal.year());
			assertEquals(LocalDate.of(today.getYear(), 4, 1), bizcal.firstDayOfYear());
			assertEquals(LocalDate.of(today.getYear() + 1, 3, 31), bizcal.lastDayOfYear());
		}
	}

}
