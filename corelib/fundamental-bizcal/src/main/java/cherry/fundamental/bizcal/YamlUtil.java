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

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

public class YamlUtil {

	public static Long getLong(Object v) {
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		return Long.parseLong(v.toString());
	}

	public static Year getYear(Object v) {
		if (v instanceof Number) {
			int num = ((Number) v).intValue();
			int year = num; // 末尾から一桁目以上
			return Year.of(year);
		}
		try {
			return Year.parse(v.toString(), DateTimeFormatter.ofPattern("yyyy"));
		} catch (DateTimeParseException ex1) {
			return Year.parse(v.toString());
		}
	}

	public static YearMonth getYearMonth(Object v) {
		if (v instanceof Number) {
			int num = ((Number) v).intValue();
			int month = num % 100; // 末尾二桁
			int year = num / 100; // 末尾から三桁目以上
			return YearMonth.of(year, month);
		}
		try {
			return YearMonth.parse(v.toString(), DateTimeFormatter.ofPattern("yyyy/MM"));
		} catch (DateTimeParseException ex1) {
			try {
				return YearMonth.parse(v.toString(), DateTimeFormatter.ofPattern("yyyyMM"));
			} catch (DateTimeParseException ex2) {
				return YearMonth.parse(v.toString());
			}
		}
	}

	public static LocalDate getLocalDate(Object v) {
		if (v instanceof Date) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(((Date) v).getTime());
			return LocalDate.of(cal.get(YEAR), cal.get(MONTH) + 1, cal.get(DAY_OF_MONTH));
		}
		if (v instanceof Number) {
			int num = ((Number) v).intValue();
			int dayOfMonth = num % 100; // 末尾二桁
			int month = (num / 100) % 100; // 末尾から三桁目と四桁目
			int year = num / 10000; // 末尾から五桁目以上
			return LocalDate.of(year, month, dayOfMonth);
		}
		try {
			return LocalDate.parse(v.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		} catch (DateTimeParseException ex1) {
			try {
				return LocalDate.parse(v.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
			} catch (DateTimeParseException ex2) {
				return LocalDate.parse(v.toString());
			}
		}
	}

}
