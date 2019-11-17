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
import java.time.YearMonth;

import org.junit.Test;

public class FileYearMonthStrategyTest {

	@Test
	public void testBizcalYearMonth() {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, new FileYearMonthStrategy(new File("src/test/resources/bizcal")),
				null, null, "standard");
		testSub(impl, dateTimeStrategy, LocalDate.of(2019, 10, 29), LocalDate.of(2019, 11, 28), YearMonth.of(2019, 11));
		testSub(impl, dateTimeStrategy, LocalDate.of(2019, 11, 29), LocalDate.of(2019, 12, 28), YearMonth.of(2019, 12));
		testSub(impl, dateTimeStrategy, LocalDate.of(2019, 12, 29), LocalDate.of(2020, 1, 25), YearMonth.of(2020, 1));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 1, 26), LocalDate.of(2020, 2, 25), YearMonth.of(2020, 2));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 2, 26), LocalDate.of(2020, 3, 25), YearMonth.of(2020, 3));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 3, 26), LocalDate.of(2020, 5, 1), YearMonth.of(2020, 4));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 5, 2), LocalDate.of(2020, 5, 28), YearMonth.of(2020, 5));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 5, 29), LocalDate.of(2020, 6, 28), YearMonth.of(2020, 6));
	}

	@Test
	public void testNoDef() {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, new FileYearMonthStrategy(new File("src/test/resources/bizcal")),
				null, null, "nodef");
		testSub(impl, dateTimeStrategy, LocalDate.of(2019, 11, 1), LocalDate.of(2019, 11, 30), YearMonth.of(2019, 11));
		testSub(impl, dateTimeStrategy, LocalDate.of(2019, 12, 1), LocalDate.of(2019, 12, 31), YearMonth.of(2019, 12));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31), YearMonth.of(2020, 1));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 29), YearMonth.of(2020, 2));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 3, 1), LocalDate.of(2020, 3, 31), YearMonth.of(2020, 3));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30), YearMonth.of(2020, 4));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 5, 1), LocalDate.of(2020, 5, 31), YearMonth.of(2020, 5));
		testSub(impl, dateTimeStrategy, LocalDate.of(2020, 6, 1), LocalDate.of(2020, 6, 30), YearMonth.of(2020, 6));
	}

	private void testSub(Bizcal impl, DateTimeStrategy dateTimeStrategy, LocalDate fst, LocalDate lst, YearMonth ym) {
		assertEquals(fst, impl.firstDayOfYearMonth(ym));
		assertEquals(lst, impl.lastDayOfYearMonth(ym));
		// 起点
		assertEquals(ym, impl.yearMonth(fst));
		assertEquals(fst, impl.firstDayOfYearMonth(fst));
		assertEquals(lst, impl.lastDayOfYearMonth(fst));
		// 終点
		assertEquals(ym, impl.yearMonth(lst));
		assertEquals(fst, impl.firstDayOfYearMonth(lst));
		assertEquals(lst, impl.lastDayOfYearMonth(lst));
		// 起点(現在日付)
		when(dateTimeStrategy.today(anyString())).thenReturn(fst);
		assertEquals(ym, impl.yearMonth());
		assertEquals(fst, impl.firstDayOfYearMonth());
		assertEquals(lst, impl.lastDayOfYearMonth());
		// 起点(現在日付)
		when(dateTimeStrategy.today(anyString())).thenReturn(lst);
		assertEquals(ym, impl.yearMonth());
		assertEquals(fst, impl.firstDayOfYearMonth());
		assertEquals(lst, impl.lastDayOfYearMonth());
	}

}
