/*
 * Copyright 2014,2019 agwlvssainokuni
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
import java.time.LocalDateTime;
import java.time.Year;

/**
 * 業務カレンダー管理機能。<br />
 * <ul>
 * <li>現在の業務日付(業務上の「現在の日付」)</li>
 * <li>現在の業務日時(業務上の「現在の日時」)</li>
 * <li>日付の属する業務年</li>
 * <li>業務年の初日</li>
 * <li>業務年の末日</li>
 * <li>業務年の日数</li>
 * <li>業務年初日からの日数(初日は「1」)</li>
 * <li>日付間の営業日数(起点日と終点日が同じならば「1」)</li>
 * <li>営業日数後の営業日(「1」ならば起点日と同じ)</li>
 * </ul>
 */
public interface Bizcal {

	/**
	 * 現在の業務日付(業務上の「現在の日付」)を照会する。<br />
	 *
	 * @return 業務日付。
	 */
	LocalDate today();

	/**
	 * 現在の業務日付(業務上の「現在の日付」)を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @return 業務日付。
	 */
	LocalDate today(String name);

	/**
	 * 現在の業務日時(業務上の「現在の日時」)を照会する。<br />
	 *
	 * @return 業務日時。
	 */
	LocalDateTime now();

	/**
	 * 現在の業務日時(業務上の「現在の日時」)を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @return 業務日時。
	 */
	LocalDateTime now(String name);

	/**
	 * 現在の業務年(現在の業務日付の業務年)を照会する。<br />
	 *
	 * @return 業務年。
	 */
	Year getBizYear();

	/**
	 * 現在の業務年(現在の業務日付の業務年)を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @return 業務年。
	 */
	Year getBizYear(String name);

	/**
	 * 業務年(指定した業務日付の業務年)を照会する。<br />
	 *
	 * @param dt 日付指定。
	 * @return 業務年。
	 */
	Year getBizYear(LocalDate dt);

	/**
	 * 業務年(指定した業務日付の業務年)を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @param dt 日付指定。
	 * @return 業務年。
	 */
	Year getBizYear(String name, LocalDate dt);

	/**
	 * 現在の業務年(現在の業務日付の業務年)の初日を照会する。<br />
	 *
	 * @return 業務年の初日。
	 */
	LocalDate getFirstOfBizYear();

	/**
	 * 現在の業務年(現在の業務日付の業務年)の初日を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @return 業務年の初日。
	 */
	LocalDate getFirstOfBizYear(String name);

	/**
	 * 業務年(指定した業務日付の業務年)の初日を照会する。<br />
	 *
	 * @param dt 日付指定。
	 * @return 業務年の初日。
	 */
	LocalDate getFirstOfBizYear(LocalDate dt);

	/**
	 * 業務年(指定した業務日付の業務年)の初日を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @param dt 日付指定。
	 * @return 業務年の初日。
	 */
	LocalDate getFirstOfBizYear(String name, LocalDate dt);

	/**
	 * 指定した業務年の初日を照会する。<br />
	 *
	 * @param bizYear 業務年指定。
	 * @return 業務年の初日。
	 */
	LocalDate getFirstOfBizYear(Year bizYear);

	/**
	 * 指定した業務年の初日を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @param bizYear 業務年指定。
	 * @return 業務年の初日。
	 */
	LocalDate getFirstOfBizYear(String name, Year bizYear);

	/**
	 * 現在の業務年(現在の業務日付の業務年)の末日を照会する。<br />
	 *
	 * @return 業務年の末日。
	 */
	LocalDate getLastOfBizYear();

	/**
	 * 現在の業務年(現在の業務日付の業務年)の末日を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @return 業務年の末日。
	 */
	LocalDate getLastOfBizYear(String name);

	/**
	 * 業務年(指定した業務日付の業務年)の末日を照会する。<br />
	 *
	 * @param dt 日付指定。
	 * @return 業務年の末日。
	 */
	LocalDate getLastOfBizYear(LocalDate dt);

	/**
	 * 業務年(指定した業務日付の業務年)の末日を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @param dt 日付指定。
	 * @return 業務年の末日。
	 */
	LocalDate getLastOfBizYear(String name, LocalDate dt);

	/**
	 * 指定した業務年の末日を照会する。<br />
	 *
	 * @param bizYear 業務年指定。
	 * @return 業務年の末日。
	 */
	LocalDate getLastOfBizYear(Year bizYear);

	/**
	 * 指定した業務年の末日を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @param bizYear 業務年指定。
	 * @return 業務年の末日。
	 */
	LocalDate getLastOfBizYear(String name, Year bizYear);

	/**
	 * 現在の業務年(現在の業務日付の業務年)の日数を照会する。<br />
	 *
	 * @return 業務年の日数。
	 */
	int getNumberOfDaysOfBizYear();

	/**
	 * 現在の業務年(現在の業務日付の業務年)の日数を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @return 業務年の日数。
	 */
	int getNumberOfDaysOfBizYear(String name);

	/**
	 * 業務年(指定した業務日付の業務年)の日数を照会する。<br />
	 *
	 * @param dt 日付指定。
	 * @return 業務年の日数。
	 */
	int getNumberOfDaysOfBizYear(LocalDate dt);

	/**
	 * 業務年(指定した業務日付の業務年)の日数を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @param dt 日付指定。
	 * @return 業務年の日数。
	 */
	int getNumberOfDaysOfBizYear(String name, LocalDate dt);

	/**
	 * 指定した業務年の日数を照会する。<br />
	 *
	 * @param bizYear 業務年指定。
	 * @return 業務年の日数。
	 */
	int getNumberOfDaysOfBizYear(Year bizYear);

	/**
	 * 指定した業務年の日数を照会する。<br />
	 *
	 * @param name カレンダーの識別名。
	 * @param bizYear 業務年指定。
	 * @return 業務年の日数。
	 */
	int getNumberOfDaysOfBizYear(String name, Year bizYear);

	/**
	 * 業務年の初日から業務上の「現在の日付」までの日数(何日目か)を照会する。<br />
	 * 業務年の初日は「1」。
	 *
	 * @return 業務年初日からの日数(何日目か)。
	 */
	int getNthDayOfBizYear();

	/**
	 * 業務年の初日から業務上の「現在の日付」までの日数(何日目か)を照会する。<br />
	 * 業務年の初日は「1」。
	 *
	 * @param name カレンダーの識別名。
	 * @return 業務年初日からの日数(何日目か)。
	 */
	int getNthDayOfBizYear(String name);

	/**
	 * 業務年の初日から指定した日付までの日数(何日目か)を照会する。<br />
	 * 業務年の初日は「1」。
	 *
	 * @param dt 日付指定。
	 * @return 業務年初日からの日数(何日目か)。
	 */
	int getNthDayOfBizYear(LocalDate dt);

	/**
	 * 業務年の初日から指定した日付までの日数(何日目か)を照会する。<br />
	 * 業務年の初日は「1」。
	 *
	 * @param name カレンダーの識別名。
	 * @param dt 日付指定。
	 * @return 業務年初日からの日数(何日目か)。
	 */
	int getNthDayOfBizYear(String name, LocalDate dt);

	/**
	 * 業務上の「現在の日付」から指定した終点日までの営業日数を算出する。<br />
	 * 終点日が起点日と同じならば「1」。
	 *
	 * @param to 終点日指定。
	 * @return 営業日数。
	 */
	int getNumberOfWorkday(LocalDate to);

	/**
	 * 業務上の「現在の日付」から指定した終点日までの営業日数を算出する。<br />
	 * 終点日が起点日と同じならば「1」。
	 *
	 * @param name カレンダーの識別名。
	 * @param to 終点日指定。
	 * @return 営業日数。
	 */
	int getNumberOfWorkday(String name, LocalDate to);

	/**
	 * 指定した起点日から終点日までの営業日数を算出する。<br />
	 * 終点日が起点日と同じならば「1」。
	 *
	 * @param from 起点日指定。
	 * @param to 終点日指定。
	 * @return 営業日数。
	 */
	int getNumberOfWorkday(LocalDate from, LocalDate to);

	/**
	 * 指定した起点日から終点日までの営業日数を算出する。<br />
	 * 終点日が起点日と同じならば「1」。
	 *
	 * @param name カレンダーの識別名。
	 * @param from 起点日指定。
	 * @param to 終点日指定。
	 * @return 営業日数。
	 */
	int getNumberOfWorkday(String name, LocalDate from, LocalDate to);

	/**
	 * 業務上の「現在の日付」から指定の営業日後の営業日を算出する。<br />
	 * 「1」を指定すると起点日と同じ。
	 *
	 * @param numberOfWorkday 営業日数指定。
	 * @return 指定日数後の営業日。
	 */
	LocalDate getNextWorkday(int numberOfWorkday);

	/**
	 * 業務上の「現在の日付」から指定の営業日後の営業日を算出する。<br />
	 * 「1」を指定すると起点日と同じ。
	 *
	 * @param name カレンダーの識別名。
	 * @param numberOfWorkday 営業日数指定。
	 * @return 指定日数後の営業日。
	 */
	LocalDate getNextWorkday(String name, int numberOfWorkday);

	/**
	 * 指定した起点日から指定の営業日後の営業日を算出する。<br />
	 * 「1」を指定すると起点日と同じ。
	 *
	 * @param from 起点日指定。
	 * @param numberOfWorkday 営業日数指定。
	 * @return 指定日数後の営業日。
	 */
	LocalDate getNextWorkday(LocalDate from, int numberOfWorkday);

	/**
	 * 指定した起点日から指定の営業日後の営業日を算出する。<br />
	 * 「1」を指定すると起点日と同じ。
	 *
	 * @param name カレンダーの識別名。
	 * @param from 起点日指定。
	 * @param numberOfWorkday 営業日数指定。
	 * @return 指定日数後の営業日。
	 */
	LocalDate getNextWorkday(String name, LocalDate from, int numberOfWorkday);

}
