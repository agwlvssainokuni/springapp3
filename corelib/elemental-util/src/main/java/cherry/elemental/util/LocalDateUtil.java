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

package cherry.elemental.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日付操作ユーティリティ。<br />
 * 下記の機能を提供する。
 * <ul>
 * <li>日付範囲(FROM)の条件指定値決定 ({@link #rangeFrom(LocalDate)}, {@link #rangeFrom(LocalDateTime)},
 * {@link #ymRangeFrom(LocalDate)}</li>
 * <li>日付範囲(TO)の条件指定値決定 ({@link #rangeTo(LocalDate)}, {@link #rangeTo(LocalDateTime)}, {@link #ymRangeTo(LocalDate)}</li>
 * </ul>
 */
public class LocalDateUtil {

	/**
	 * 日付範囲(FROM)の条件指定値を決定する。
	 * 
	 * @param from 入力された日付値。
	 * @return 入力された日付に対する日付範囲(FROM)の条件指定値。
	 */
	public static LocalDate rangeFrom(LocalDate from) {
		return from;
	}

	/**
	 * 日付範囲(FROM)の条件指定値を決定する。
	 * 
	 * @param from 入力された日時値。
	 * @return 入力された日時に対する日付範囲(FROM)の条件指定値。
	 */
	public static LocalDate rangeFrom(LocalDateTime from) {
		if (from == null) {
			return null;
		}
		return from.toLocalDate();
	}

	/**
	 * 日付範囲(TO)の条件指定値を決定する。
	 * 
	 * @param to 入力された日付値。
	 * @return 入力された日付に対する日付範囲(TO)の条件指定値。
	 */
	public static LocalDate rangeTo(LocalDate to) {
		if (to == null) {
			return null;
		}
		return to.plusDays(1);
	}

	/**
	 * 日付範囲(TO)の条件指定値を決定する。
	 * 
	 * @param to 入力された日時値。
	 * @return 入力された日時に対する日付範囲(TO)の条件指定値。
	 */
	public static LocalDate rangeTo(LocalDateTime to) {
		if (to == null) {
			return null;
		}
		return to.toLocalDate().plusDays(1);
	}

	/**
	 * 年月(日なし)を保持する形式の日付データ(Y/M/1形式)に正規化する。
	 * 
	 * @param dt 入力された年月データ。
	 * @return 年月(日なし)を正規化した日付値。
	 */
	public static LocalDate normalizeYm(LocalDate dt) {
		if (dt == null) {
			return null;
		}
		return LocalDate.of(dt.getYear(), dt.getMonthValue(), 1);
	}

	/**
	 * 年月(日なし)のデータから日付範囲(FROM)の条件指定値を決定する。
	 * 
	 * @param from 入力された年月(日なし)。
	 * @return 入力された年月(日なし)に対する日付範囲(FROM)の条件指定値。
	 */
	public static LocalDate ymRangeFrom(LocalDate from) {
		return normalizeYm(from);
	}

	/**
	 * 年月(日なし)のデータから日付範囲(TO)の条件指定値を決定する。
	 * 
	 * @param to 入力された年月(日なし)。
	 * @return 入力された年月(日なし)に対する日付範囲(TO)の条件指定値。
	 */
	public static LocalDate ymRangeTo(LocalDate to) {
		if (to == null) {
			return null;
		}
		return normalizeYm(to).plusMonths(1);
	}

}
