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

/**
 * 業務カレンダー管理機能。<br />
 * <ul>
 * <li>業務日付(「業務上の本日」の日付)</li>
 * <li>業務日時(「業務上の現在」の日時)</li>
 * <li>日付の属する営業年度</li>
 * <li>営業年度の初日</li>
 * <li>営業年度の末日</li>
 * <li>営業年度の日数</li>
 * <li>営業年度初日からの日数(初日は「1」)</li>
 * <li>日付間の営業日数(起点日と終点日が同じならば「1」)</li>
 * <li>営業日数後の営業日(「1」ならば起点日と同じ)</li>
 * </ul>
 */
public interface Bizcal {

	/**
	 * 業務日付(「業務上の本日」の日付)を照会する。<br />
	 * 
	 * @return 業務日付。
	 */
	LocalDate today();

	/**
	 * 業務日時(「業務上の現在」の日時)を照会する。<br />
	 * 
	 * @return 業務日時。
	 */
	LocalDateTime now();

	/**
	 * 「業務上の本日」の営業年度を照会する。<br />
	 * 
	 * @return 業務上の本日の営業年度。
	 */
	int getBizYear();

	/**
	 * 指定した日付の営業年度を照会する。<br />
	 * 
	 * @param dt 日付指定。
	 * @return 指定した日付の営業年度。
	 */
	int getBizYear(LocalDate dt);

	/**
	 * 「業務上の本日」の営業年度の初日を照会する。<br />
	 * 
	 * @return 業務上の本日の営業年度の初日。
	 */
	LocalDate getFirstOfBizYear();

	/**
	 * 指定した日付の営業年度の初日を照会する。<br />
	 * 
	 * @param dt 日付指定。
	 * @return 指定した日付の営業年度の初日。
	 */
	LocalDate getFirstOfBizYear(LocalDate dt);

	/**
	 * 指定した営業年度の初日を照会する。<br />
	 * 
	 * @param bizYear 営業年度指定。
	 * @return 指定した営業年度の初日。
	 */
	LocalDate getFirstOfBizYear(int bizYear);

	/**
	 * 「業務上の本日」の営業年度の末日を照会する。<br />
	 * 
	 * @return 業務上の本日の営業年度の末日。
	 */
	LocalDate getLastOfBizYear();

	/**
	 * 指定した日付の営業年度の末日を照会する。<br />
	 * 
	 * @param dt 日付指定。
	 * @return 指定した日付の営業年度の末日。
	 */
	LocalDate getLastOfBizYear(LocalDate dt);

	/**
	 * 指定した営業年度の末日を照会する。<br />
	 * 
	 * @param bizYear 営業年度指定。
	 * @return 指定した営業年度の末日。
	 */
	LocalDate getLastOfBizYear(int bizYear);

	/**
	 * 「業務上の本日」の営業年度の日数を照会する。<br />
	 * 
	 * @return 「業務上の本日」の営業年度の日数。
	 */
	int getNumberOfDaysOfBizYear();

	/**
	 * 指定した日付の営業年度の日数を照会する。<br />
	 * 
	 * @param dt 日付指定。
	 * @return 指定した日付の営業年度の日数。
	 */
	int getNumberOfDaysOfBizYear(LocalDate dt);

	/**
	 * 指定した営業年度の日数を照会する。<br />
	 * 
	 * @param bizYear 営業年度指定。
	 * @return 指定した営業年度の日数。
	 */
	int getNumberOfDaysOfBizYear(int bizYear);

	/**
	 * 営業年度の初日から「業務上の本日」までの日数(何日目か)を照会する。<br />
	 * 営業年度の初日は「1」。
	 * 
	 * @return 営業年度の初日から「業務上の本日」までの日数(何日目か)。
	 */
	int getNthDayOfBizYear();

	/**
	 * 営業年度の初日から指定した日付までの日数(何日目か)を照会する。<br />
	 * 営業年度の初日は「1」。
	 * 
	 * @param dt 日付指定。
	 * @return 営業年度の初日から指定した日付までの日数(何日目か)。
	 */
	int getNthDayOfBizYear(LocalDate dt);

	/**
	 * 標準のカレンダーに基づき、「業務上の本日」から指定した終点日までの営業日数を算出する。<br />
	 * 終点日が起点日と同じならば「1」。
	 * 
	 * @param to 終点日指定。
	 * @return 業務上の本日から指定した終点日までの営業日数。
	 */
	int getNumberOfWorkday(LocalDate to);

	/**
	 * 指定したカレンダーに基づき、「業務上の本日」から指定した終点日までの営業日数を算出する。<br />
	 * 終点日が起点日と同じならば「1」。
	 * 
	 * @param name カレンダーの識別名。
	 * @param to 終点日指定。
	 * @return 業務上の本日から指定した終点日までの営業日数。
	 */
	int getNumberOfWorkday(String name, LocalDate to);

	/**
	 * 標準のカレンダーに基づき、指定した起点日から終点日までの営業日数を算出する。<br />
	 * 終点日が起点日と同じならば「1」。
	 * 
	 * @param from 起点日指定。
	 * @param to 終点日指定。
	 * @return 指定した起点日から終点日までの営業日数。
	 */
	int getNumberOfWorkday(LocalDate from, LocalDate to);

	/**
	 * 指定したカレンダーに基づき、指定した起点日から終点日までの営業日数を算出する。<br />
	 * 終点日が起点日と同じならば「1」。
	 * 
	 * @param name カレンダーの識別名。
	 * @param from 起点日指定。
	 * @param to 終点日指定。
	 * @return 指定した起点日から終点日までの営業日数。
	 */
	int getNumberOfWorkday(String name, LocalDate from, LocalDate to);

	/**
	 * 標準のカレンダーに基づき、「業務上の本日」から指定の営業日後の営業日を算出する。<br />
	 * 「1」を指定すると起点日と同じ。
	 * 
	 * @param numberOfWorkday 営業日数指定。
	 * @return <code>numberOfWorkday</code>営業日後の営業日。
	 */
	LocalDate getNextWorkday(int numberOfWorkday);

	/**
	 * 指定したカレンダーに基づき、「業務上の本日」から指定の営業日後の営業日を算出する。<br />
	 * 「1」を指定すると起点日と同じ。
	 * 
	 * @param name カレンダーの識別名。
	 * @param numberOfWorkday 営業日数指定。
	 * @return <code>numberOfWorkday</code>営業日後の営業日。
	 */
	LocalDate getNextWorkday(String name, int numberOfWorkday);

	/**
	 * 標準のカレンダーに基づき、指定した起点日から指定の営業日後の営業日を算出する。<br />
	 * 「1」を指定すると起点日と同じ。
	 * 
	 * @param from 起点日指定。
	 * @param numberOfWorkday 営業日数指定。
	 * @return <code>numberOfWorkday</code>営業日後の営業日。
	 */
	LocalDate getNextWorkday(LocalDate from, int numberOfWorkday);

	/**
	 * 指定したカレンダーに基づき、指定した起点日から指定の営業日後の営業日を算出する。<br />
	 * 「1」を指定すると起点日と同じ。
	 * 
	 * @param name カレンダーの識別名。
	 * @param from 起点日指定。
	 * @param numberOfWorkday 営業日数指定。
	 * @return <code>numberOfWorkday</code>営業日後の営業日。
	 */
	LocalDate getNextWorkday(String name, LocalDate from, int numberOfWorkday);

}
