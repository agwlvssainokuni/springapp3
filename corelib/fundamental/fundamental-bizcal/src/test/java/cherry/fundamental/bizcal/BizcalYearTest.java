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
import java.time.temporal.ChronoUnit;

import org.junit.Test;

public class BizcalYearTest {

	private Bizcal bizcal = new BizcalImpl(new SimpleDateTimeStrategy(), new SimpleYearStrategy(0, 4, 1),
			new SimpleWorkdayStrategy(), "standard");

	private Bizcal bizcal0901 = new BizcalImpl(new SimpleDateTimeStrategy(), new SimpleYearStrategy(-1, 9, 1),
			new SimpleWorkdayStrategy(), "standard");

	@Test
	public void testGetBizYear() {
		for (int year = 1900; year < 3000; year++) {
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 4) {
					assertEquals(year - 1, bizcal.getBizYear(dt));
				} else {
					assertEquals(year, bizcal.getBizYear(dt));
				}
			}
		}
	}

	@Test
	public void testGetFirstOfBizYear() {
		for (int year = 1900; year < 3000; year++) {
			assertEquals(LocalDate.of(year, 4, 1), bizcal.getFirstOfBizYear(year));
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 4) {
					assertEquals(LocalDate.of(year - 1, 4, 1), bizcal.getFirstOfBizYear(dt));
				} else {
					assertEquals(LocalDate.of(year, 4, 1), bizcal.getFirstOfBizYear(dt));
				}
			}
		}
	}

	@Test
	public void testGetLastOfBizYear() {
		for (int year = 1900; year < 3000; year++) {
			assertEquals(LocalDate.of(year + 1, 3, 31), bizcal.getLastOfBizYear(year));
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 4) {
					assertEquals(LocalDate.of(year, 3, 31), bizcal.getLastOfBizYear(dt));
				} else {
					assertEquals(LocalDate.of(year + 1, 3, 31), bizcal.getLastOfBizYear(dt));
				}
			}
		}
	}

	@Test
	public void testGetNthDayOfBizYear() {
		for (int year = 1900; year < 3000; year++) {
			assertEquals(1, bizcal.getNthDayOfBizYear(LocalDate.of(year, 4, 1)));
			assertEquals(numberOfDays(year + 1), bizcal.getNthDayOfBizYear(LocalDate.of(year + 1, 3, 31)));
		}
	}

	@Test
	public void testGetNumberOfDaysOfBizYear() {
		for (int year = 1900; year < 3000; year++) {
			assertEquals(numberOfDays(year + 1), bizcal.getNumberOfDaysOfBizYear(year));
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 4) {
					assertEquals(numberOfDays(year), bizcal.getNumberOfDaysOfBizYear(dt));
				} else {
					assertEquals(numberOfDays(year + 1), bizcal.getNumberOfDaysOfBizYear(dt));
				}
			}
		}
	}

	@Test
	public void test0901() {
		for (int year = 1900; year < 3000; year++) {
			for (LocalDate dt = LocalDate.of(year, 1, 1); dt.getYear() == year; dt = dt.plusDays(1)) {
				if (dt.getMonthValue() < 9) {
					assertEquals(year, bizcal0901.getBizYear(dt));
					assertEquals(LocalDate.of(year - 1, 9, 1), bizcal0901.getFirstOfBizYear(dt));
					assertEquals(LocalDate.of(year, 8, 31), bizcal0901.getLastOfBizYear(dt));
				} else {
					assertEquals(year + 1, bizcal0901.getBizYear(dt));
					assertEquals(LocalDate.of(year, 9, 1), bizcal0901.getFirstOfBizYear(dt));
					assertEquals(LocalDate.of(year + 1, 8, 31), bizcal0901.getLastOfBizYear(dt));
				}
			}
		}
	}

	@Test
	public void testForToday() {
		LocalDate today = LocalDate.now();
		if (today.getMonthValue() < 4) {
			assertEquals(today.getYear() - 1, bizcal.getBizYear());
			assertEquals(LocalDate.of(today.getYear() - 1, 4, 1), bizcal.getFirstOfBizYear());
			assertEquals(LocalDate.of(today.getYear(), 3, 31), bizcal.getLastOfBizYear());
			assertEquals(numberOfDays(today.getYear()), bizcal.getNumberOfDaysOfBizYear());
		} else {
			assertEquals(today.getYear(), bizcal.getBizYear());
			assertEquals(LocalDate.of(today.getYear(), 4, 1), bizcal.getFirstOfBizYear());
			assertEquals(LocalDate.of(today.getYear() + 1, 3, 31), bizcal.getLastOfBizYear());
			assertEquals(numberOfDays(today.getYear() + 1), bizcal.getNumberOfDaysOfBizYear());
		}
		assertEquals(bizcal.getFirstOfBizYear().until(today.plusDays(1), ChronoUnit.DAYS), bizcal.getNthDayOfBizYear());
	}

	private int numberOfDays(int year) {
		return year % 400 == 0 ? 366 : year % 100 == 0 ? 365 : year % 4 == 0 ? 366 : 365;
	}

}
