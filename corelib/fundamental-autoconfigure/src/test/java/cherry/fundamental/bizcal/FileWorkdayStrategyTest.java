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
import java.io.IOException;
import java.time.LocalDate;

import org.junit.Test;

public class FileWorkdayStrategyTest {

	@Test
	public void testRegularOn() throws IOException {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, null, null,
				new FileWorkdayStrategy(new File("src/test/resources/bizcal")), "standard");
		// regularOn: 月火水木金。
		new File("src/test/resources/bizcal/standard/workday/90-temp.yaml").createNewFile();
		// 水曜日を起点として営業日数を算出。
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), 1);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2), 2);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 3), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 5), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 6), 4);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 7), 5);
		// 土曜日を起点として営業日数を算出。
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 4), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 5), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 6), 1);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 7), 2);
		// ファイル再読込み。
		new File("src/test/resources/bizcal/standard/workday/90-temp.yaml")
				.setLastModified(System.currentTimeMillis() + 1000L);
		// 水曜日を起点として翌営業日を算出。
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 1, LocalDate.of(2020, 1, 1));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 2, LocalDate.of(2020, 1, 2));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 3, LocalDate.of(2020, 1, 3));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 4, LocalDate.of(2020, 1, 6));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 5, LocalDate.of(2020, 1, 7));
		// 土曜日を起点として翌営業日を算出。
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), 0, LocalDate.of(2020, 1, 4));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), 1, LocalDate.of(2020, 1, 6));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), 2, LocalDate.of(2020, 1, 7));
		// 後始末。
		new File("src/test/resources/bizcal/standard/workday/90-temp.yaml").delete();
	}

	@Test
	public void testSpecificOn() throws IOException {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, null, null,
				new FileWorkdayStrategy(new File("src/test/resources/bizcal")), "standard");
		// regularOn: 月火水木金。
		// specificOn: 2019/01/11(土), 2019/01/12(日)
		new File("src/test/resources/bizcal/standard/workday/90-temp.yaml").createNewFile();
		// 水曜日を起点として営業日数を算出。
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 8), 1);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 9), 2);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 10), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 11), 4);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 12), 5);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 13), 6);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 14), 7);
		// 土曜日を起点として営業日数を算出。
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 11), 1);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 12), 2);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 13), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 14), 4);
		// ファイル再読込み。
		new File("src/test/resources/bizcal/standard/workday/90-temp.yaml").delete();
		// 水曜日を起点として翌営業日を算出。
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), 1, LocalDate.of(2020, 1, 8));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), 2, LocalDate.of(2020, 1, 9));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), 3, LocalDate.of(2020, 1, 10));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), 4, LocalDate.of(2020, 1, 11));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), 5, LocalDate.of(2020, 1, 12));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), 6, LocalDate.of(2020, 1, 13));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 8), 7, LocalDate.of(2020, 1, 14));
		// 土曜日を起点として翌営業日を算出。
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 11), 1, LocalDate.of(2020, 1, 11));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 11), 2, LocalDate.of(2020, 1, 12));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 11), 3, LocalDate.of(2020, 1, 13));
	}

	@Test
	public void testSpecificOff() throws IOException {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, null, null,
				new FileWorkdayStrategy(new File("src/test/resources/bizcal")), "standard");
		// regularOn: 月火水木金。
		// specificOff: 2019/01/20(月), 2019/01/21(火), 2019/01/22(水), 2019/01/23(木), 2019/01/24(金)
		new File("src/test/resources/bizcal/standard/workday/90-temp.yaml").createNewFile();
		// 水曜日を起点として営業日数を算出。
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 15), 1);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 16), 2);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 17), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 18), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 19), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 20), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 21), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 22), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 23), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 24), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 25), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 26), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 27), 4);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 28), 5);
		// 土曜日を起点として営業日数を算出。
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 18), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 19), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 20), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 21), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 22), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 23), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 24), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 25), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 26), 0);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 27), 1);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 28), 2);
		// ファイル再読込み。
		new File("src/test/resources/bizcal/standard/workday/90-temp.yaml")
				.renameTo(new File("src/test/resources/bizcal/standard/workday/99-temp.yaml"));
		// 水曜日を起点として翌営業日を算出。
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), 1, LocalDate.of(2020, 1, 15));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), 2, LocalDate.of(2020, 1, 16));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), 3, LocalDate.of(2020, 1, 17));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), 4, LocalDate.of(2020, 1, 27));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 15), 5, LocalDate.of(2020, 1, 28));
		// 土曜日を起点として翌営業日を算出。
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), 0, LocalDate.of(2020, 1, 18));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), 1, LocalDate.of(2020, 1, 27));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 18), 2, LocalDate.of(2020, 1, 28));
		// 後始末。
		new File("src/test/resources/bizcal/standard/workday/99-temp.yaml").delete();
	}

	@Test
	public void testNoDef() {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, null, null,
				new FileWorkdayStrategy(new File("src/test/resources/bizcal")), "nodef");
		// カレンダー定義なし。
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1), 1);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2), 2);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 3), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4), 4);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 5), 5);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 6), 6);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 7), 7);
		// 土曜日を起点として営業日数を算出。
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 4), 1);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 5), 2);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 6), 3);
		testNumberOfWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 7), 4);
		// 水曜日を起点として翌営業日を算出。
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 1, LocalDate.of(2020, 1, 1));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 2, LocalDate.of(2020, 1, 2));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 3, LocalDate.of(2020, 1, 3));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 4, LocalDate.of(2020, 1, 4));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 5, LocalDate.of(2020, 1, 5));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 6, LocalDate.of(2020, 1, 6));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 1), 7, LocalDate.of(2020, 1, 7));
		// 土曜日を起点として翌営業日を算出。
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), 1, LocalDate.of(2020, 1, 4));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), 2, LocalDate.of(2020, 1, 5));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), 3, LocalDate.of(2020, 1, 6));
		testNextWorkday(impl, dateTimeStrategy, LocalDate.of(2020, 1, 4), 4, LocalDate.of(2020, 1, 7));
	}

	private void testNumberOfWorkday(Bizcal impl, DateTimeStrategy dateTimeStrategy, LocalDate from, LocalDate to,
			int numOfWday) {
		assertEquals(numOfWday, impl.getNumberOfWorkday(from, to));
		when(dateTimeStrategy.today(anyString())).thenReturn(from);
		assertEquals(numOfWday, impl.getNumberOfWorkday(to));
	}

	private void testNextWorkday(Bizcal impl, DateTimeStrategy dateTimeStrategy, LocalDate from, int numOfWday,
			LocalDate to) {
		assertEquals(to, impl.getNextWorkday(from, numOfWday));
		when(dateTimeStrategy.today(anyString())).thenReturn(from);
		assertEquals(to, impl.getNextWorkday(numOfWday));
	}

}
