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

import java.io.File;
import java.time.LocalDate;

import org.junit.Test;

public class FileWorkdayStrategyTest {

	@Test
	public void testRegularOn() {
		// regularOn: 月火水木金。
		FileWorkdayStrategy impl = new FileWorkdayStrategy(new File("src/test/resources/workday"));
		// 水曜日を起点として営業日数を算出。
		assertEquals(1, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1)));
		assertEquals(2, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 3)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 5)));
		assertEquals(4, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 6)));
		assertEquals(5, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 7)));
		// 土曜日を起点として営業日数を算出。
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 4)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 5)));
		assertEquals(1, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 6)));
		assertEquals(2, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 7)));
		// ファイル再読込み。
		new File("src/test/resources/workday/standard/00-regularOn.yaml").setLastModified(System.currentTimeMillis());
		// 水曜日を起点として翌営業日を算出。
		assertEquals(LocalDate.of(2020, 1, 1), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 1), 1));
		assertEquals(LocalDate.of(2020, 1, 2), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 1), 2));
		assertEquals(LocalDate.of(2020, 1, 3), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 1), 3));
		assertEquals(LocalDate.of(2020, 1, 6), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 1), 4));
		assertEquals(LocalDate.of(2020, 1, 7), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 1), 5));
		// 土曜日を起点として翌営業日を算出。
		assertEquals(LocalDate.of(2020, 1, 4), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 4), 0));
		assertEquals(LocalDate.of(2020, 1, 6), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 4), 1));
		assertEquals(LocalDate.of(2020, 1, 7), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 4), 2));
	}

	@Test
	public void testSpecificOn() {
		// regularOn: 月火水木金。
		// specificOn: 2019/01/11(土), 2019/01/12(日)
		FileWorkdayStrategy impl = new FileWorkdayStrategy(new File("src/test/resources/workday"));
		// 水曜日を起点として営業日数を算出。
		assertEquals(1, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 8)));
		assertEquals(2, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 9)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 10)));
		assertEquals(4, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 11)));
		assertEquals(5, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 12)));
		assertEquals(6, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 13)));
		assertEquals(7, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 8), LocalDate.of(2020, 1, 14)));
		// 土曜日を起点として営業日数を算出。
		assertEquals(1, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 11)));
		assertEquals(2, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 12)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 13)));
		assertEquals(4, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 11), LocalDate.of(2020, 1, 14)));
		// ファイル再読込み。
		new File("src/test/resources/workday/standard/10-specificOn.yaml").setLastModified(System.currentTimeMillis());
		// 水曜日を起点として翌営業日を算出。
		assertEquals(LocalDate.of(2020, 1, 8), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 8), 1));
		assertEquals(LocalDate.of(2020, 1, 9), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 8), 2));
		assertEquals(LocalDate.of(2020, 1, 10), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 8), 3));
		assertEquals(LocalDate.of(2020, 1, 11), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 8), 4));
		assertEquals(LocalDate.of(2020, 1, 12), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 8), 5));
		assertEquals(LocalDate.of(2020, 1, 13), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 8), 6));
		assertEquals(LocalDate.of(2020, 1, 14), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 8), 7));
		// 土曜日を起点として翌営業日を算出。
		assertEquals(LocalDate.of(2020, 1, 11), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 11), 1));
		assertEquals(LocalDate.of(2020, 1, 12), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 11), 2));
		assertEquals(LocalDate.of(2020, 1, 13), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 11), 3));
	}

	@Test
	public void testSpecificOff() {
		// regularOn: 月火水木金。
		// specificOff: 2019/01/20(月), 2019/01/21(火), 2019/01/22(水), 2019/01/23(木), 2019/01/24(金)
		FileWorkdayStrategy impl = new FileWorkdayStrategy(new File("src/test/resources/workday"));
		// 水曜日を起点として営業日数を算出。
		assertEquals(1, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 15)));
		assertEquals(2, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 16)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 17)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 18)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 19)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 20)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 21)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 22)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 23)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 24)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 25)));
		assertEquals(3, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 26)));
		assertEquals(4, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 27)));
		assertEquals(5, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 15), LocalDate.of(2020, 1, 28)));
		// 土曜日を起点として営業日数を算出。
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 18)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 19)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 20)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 21)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 22)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 23)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 24)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 25)));
		assertEquals(0, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 26)));
		assertEquals(1, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 27)));
		assertEquals(2, impl.getNumberOfWorkday("standard", LocalDate.of(2020, 1, 18), LocalDate.of(2020, 1, 28)));
		// ファイル再読込み。
		new File("src/test/resources/workday/standard/20-specificOff.yaml").setLastModified(System.currentTimeMillis());
		// 水曜日を起点として翌営業日を算出。
		assertEquals(LocalDate.of(2020, 1, 15), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 15), 1));
		assertEquals(LocalDate.of(2020, 1, 16), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 15), 2));
		assertEquals(LocalDate.of(2020, 1, 17), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 15), 3));
		assertEquals(LocalDate.of(2020, 1, 27), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 15), 4));
		assertEquals(LocalDate.of(2020, 1, 28), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 15), 5));
		// 土曜日を起点として翌営業日を算出。
		assertEquals(LocalDate.of(2020, 1, 18), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 18), 0));
		assertEquals(LocalDate.of(2020, 1, 27), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 18), 1));
		assertEquals(LocalDate.of(2020, 1, 28), impl.getNextWorkday("standard", LocalDate.of(2020, 1, 18), 2));
	}

	@Test
	public void testNoDef() {
		// カレンダー定義なし。
		FileWorkdayStrategy impl = new FileWorkdayStrategy(new File("src/test/resources/workday"));
		assertEquals(1, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1)));
		assertEquals(2, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2)));
		assertEquals(3, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 3)));
		assertEquals(4, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 4)));
		assertEquals(5, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 5)));
		assertEquals(6, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 6)));
		assertEquals(7, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 7)));
		// 土曜日を起点として営業日数を算出。
		assertEquals(1, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 4)));
		assertEquals(2, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 5)));
		assertEquals(3, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 6)));
		assertEquals(4, impl.getNumberOfWorkday("nodef", LocalDate.of(2020, 1, 4), LocalDate.of(2020, 1, 7)));
		// 水曜日を起点として翌営業日を算出。
		assertEquals(LocalDate.of(2020, 1, 1), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 1), 1));
		assertEquals(LocalDate.of(2020, 1, 2), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 1), 2));
		assertEquals(LocalDate.of(2020, 1, 3), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 1), 3));
		assertEquals(LocalDate.of(2020, 1, 4), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 1), 4));
		assertEquals(LocalDate.of(2020, 1, 5), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 1), 5));
		assertEquals(LocalDate.of(2020, 1, 6), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 1), 6));
		assertEquals(LocalDate.of(2020, 1, 7), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 1), 7));
		// 土曜日を起点として翌営業日を算出。
		assertEquals(LocalDate.of(2020, 1, 4), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 4), 1));
		assertEquals(LocalDate.of(2020, 1, 5), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 4), 2));
		assertEquals(LocalDate.of(2020, 1, 6), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 4), 3));
		assertEquals(LocalDate.of(2020, 1, 7), impl.getNextWorkday("nodef", LocalDate.of(2020, 1, 4), 4));
	}

}
