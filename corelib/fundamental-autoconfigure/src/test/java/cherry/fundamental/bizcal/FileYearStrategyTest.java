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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.time.LocalDate;
import java.time.Year;

import org.junit.Test;

public class FileYearStrategyTest {

	@Test
	public void testBizcalYear() {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, null,
				new FileYearStrategy(new File("src/test/resources/bizcal")), null, "standard");
		testSub(impl, dateTimeStrategy, LocalDate.of(2018, 4, 1), LocalDate.of(2019, 3, 31), Year.of(2018));
		testSub(impl, dateTimeStrategy, LocalDate.of(2019, 4, 1), LocalDate.of(2020, 3, 31), Year.of(2019));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 4, 1), LocalDate.of(2021, 4, 1), Year.of(2020));
		testSub(impl, dateTimeStrategy, LocalDate.of(2021, 4, 2), LocalDate.of(2021, 8, 31), Year.of(2021));
		testSub(impl, dateTimeStrategy, LocalDate.of(2021, 9, 1), LocalDate.of(2023, 3, 31), Year.of(2022));
		testSub(impl, dateTimeStrategy, LocalDate.of(2023, 4, 1), LocalDate.of(2024, 3, 31), Year.of(2023));
	}

	@Test
	public void testBizcalYearNodef() {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, null,
				new FileYearStrategy(new File("src/test/resources/bizcal")), null, "nodef");
		testSub(impl, dateTimeStrategy, LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), Year.of(2018));
		testSub(impl, dateTimeStrategy, LocalDate.of(2019, 1, 1), LocalDate.of(2019, 12, 31), Year.of(2019));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31), Year.of(2020));
		testSub(impl, dateTimeStrategy, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 31), Year.of(2021));
		testSub(impl, dateTimeStrategy, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31), Year.of(2022));
		testSub(impl, dateTimeStrategy, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), Year.of(2023));
	}

	private void testSub(Bizcal impl, DateTimeStrategy dateTimeStrategy, LocalDate fst, LocalDate lst, Year year) {
		assertEquals(fst, impl.firstDayOfYear(year));
		assertEquals(lst, impl.lastDayOfYear(year));
		// 起点
		assertEquals(year, impl.year(fst));
		assertEquals(fst, impl.firstDayOfYear(fst));
		assertEquals(lst, impl.lastDayOfYear(fst));
		// 終点
		assertEquals(year, impl.year(lst));
		assertEquals(fst, impl.firstDayOfYear(lst));
		assertEquals(lst, impl.lastDayOfYear(lst));
		// 起点(現在日付)
		when(dateTimeStrategy.today(anyString())).thenReturn(fst);
		assertEquals(year, impl.year());
		assertEquals(fst, impl.firstDayOfYear());
		assertEquals(lst, impl.lastDayOfYear());
		// 起点(現在日付)
		when(dateTimeStrategy.today(anyString())).thenReturn(lst);
		assertEquals(year, impl.year());
		assertEquals(fst, impl.firstDayOfYear());
		assertEquals(lst, impl.lastDayOfYear());
	}

}
