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

package cherry.fundamental.format;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DtmFormatterTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class DtmFormatterTest {

	@Autowired
	private DtmFormatter dtmFormatter;

	@Test
	public void testYYYY_MM_DD() {
		Function<LocalDate, String> format = dtmFormatter::fmYYYY_MM_DD;
		Function<String, LocalDate> parser = dtmFormatter::toYYYY_MM_DD;
		assertEquals("1000/01/01", format.apply(LocalDate.of(1000, 1, 1)));
		assertEquals("9999/12/31", format.apply(LocalDate.of(9999, 12, 31)));
		assertEquals(LocalDate.of(1000, 1, 1), parser.apply("1000/01/01"));
		assertEquals(LocalDate.of(9999, 12, 31), parser.apply("9999/12/31"));
	}

	@Test
	public void testYYYYMMDD() {
		Function<LocalDate, String> format = dtmFormatter::fmYYYYMMDD;
		Function<String, LocalDate> parser = dtmFormatter::toYYYYMMDD;
		assertEquals("10000101", format.apply(LocalDate.of(1000, 1, 1)));
		assertEquals("99991231", format.apply(LocalDate.of(9999, 12, 31)));
		assertEquals(LocalDate.of(1000, 1, 1), parser.apply("10000101"));
		assertEquals(LocalDate.of(9999, 12, 31), parser.apply("99991231"));
	}

	@Test
	public void testYYYY_MM() {
		Function<YearMonth, String> format = dtmFormatter::fmYYYY_MM;
		Function<String, YearMonth> parser = dtmFormatter::toYYYY_MM;
		assertEquals("1000/01", format.apply(YearMonth.of(1000, 1)));
		assertEquals("9999/12", format.apply(YearMonth.of(9999, 12)));
		assertEquals(YearMonth.of(1000, 1), parser.apply("1000/01"));
		assertEquals(YearMonth.of(9999, 12), parser.apply("9999/12"));
	}

	@Test
	public void testYYYYMM() {
		Function<YearMonth, String> format = dtmFormatter::fmYYYYMM;
		Function<String, YearMonth> parser = dtmFormatter::toYYYYMM;
		assertEquals("100001", format.apply(YearMonth.of(1000, 1)));
		assertEquals("999912", format.apply(YearMonth.of(9999, 12)));
		assertEquals(YearMonth.of(1000, 1), parser.apply("100001"));
		assertEquals(YearMonth.of(9999, 12), parser.apply("999912"));
	}

	@Test
	public void testMM_DD() {
		Function<MonthDay, String> format = dtmFormatter::fmMM_DD;
		Function<String, MonthDay> parser = dtmFormatter::toMM_DD;
		assertEquals("01/01", format.apply(MonthDay.of(1, 1)));
		assertEquals("12/31", format.apply(MonthDay.of(12, 31)));
		assertEquals(MonthDay.of(1, 1), parser.apply("01/01"));
		assertEquals(MonthDay.of(12, 31), parser.apply("12/31"));
	}

	@Test
	public void testMMDD() {
		Function<MonthDay, String> format = dtmFormatter::fmMMDD;
		Function<String, MonthDay> parser = dtmFormatter::toMMDD;
		assertEquals("0101", format.apply(MonthDay.of(1, 1)));
		assertEquals("1231", format.apply(MonthDay.of(12, 31)));
		assertEquals(MonthDay.of(1, 1), parser.apply("0101"));
		assertEquals(MonthDay.of(12, 31), parser.apply("1231"));
	}

	@Test
	public void testHH_MM_SS() {
		Function<LocalTime, String> format = dtmFormatter::fmHH_MM_SS;
		Function<String, LocalTime> parser = dtmFormatter::toHH_MM_SS;
		assertEquals("00:00:00", format.apply(LocalTime.of(0, 0, 0)));
		assertEquals("12:34:56", format.apply(LocalTime.of(12, 34, 56)));
		assertEquals("23:59:59", format.apply(LocalTime.of(23, 59, 59)));
		assertEquals(LocalTime.of(0, 0, 0), parser.apply("00:00:00"));
		assertEquals(LocalTime.of(12, 34, 56), parser.apply("12:34:56"));
		assertEquals(LocalTime.of(23, 59, 59), parser.apply("23:59:59"));
	}

	@Test
	public void testHHMMSS() {
		Function<LocalTime, String> format = dtmFormatter::fmHHMMSS;
		Function<String, LocalTime> parser = dtmFormatter::toHHMMSS;
		assertEquals("000000", format.apply(LocalTime.of(0, 0, 0)));
		assertEquals("123456", format.apply(LocalTime.of(12, 34, 56)));
		assertEquals("235959", format.apply(LocalTime.of(23, 59, 59)));
		assertEquals(LocalTime.of(0, 0, 0), parser.apply("000000"));
		assertEquals(LocalTime.of(12, 34, 56), parser.apply("123456"));
		assertEquals(LocalTime.of(23, 59, 59), parser.apply("235959"));
	}

	@Test
	public void testHH_MM() {
		Function<LocalTime, String> format = dtmFormatter::fmHH_MM;
		Function<String, LocalTime> parser = dtmFormatter::toHH_MM;
		assertEquals("00:00", format.apply(LocalTime.of(0, 0)));
		assertEquals("12:34", format.apply(LocalTime.of(12, 34)));
		assertEquals("23:59", format.apply(LocalTime.of(23, 59)));
		assertEquals(LocalTime.of(0, 0), parser.apply("00:00"));
		assertEquals(LocalTime.of(12, 34), parser.apply("12:34"));
		assertEquals(LocalTime.of(23, 59), parser.apply("23:59"));
	}

	@Test
	public void testHHMM() {
		Function<LocalTime, String> format = dtmFormatter::fmHHMM;
		Function<String, LocalTime> parser = dtmFormatter::toHHMM;
		assertEquals("0000", format.apply(LocalTime.of(0, 0)));
		assertEquals("1234", format.apply(LocalTime.of(12, 34)));
		assertEquals("2359", format.apply(LocalTime.of(23, 59)));
		assertEquals(LocalTime.of(0, 0), parser.apply("0000"));
		assertEquals(LocalTime.of(12, 34), parser.apply("1234"));
		assertEquals(LocalTime.of(23, 59), parser.apply("2359"));
	}

	@Test
	public void testYYYY_MM_DD_HH_MM_SS() {
		Function<LocalDateTime, String> format = dtmFormatter::fmYYYY_MM_DD_HH_MM_SS;
		Function<String, LocalDateTime> parser = dtmFormatter::toYYYY_MM_DD_HH_MM_SS;
		assertEquals("1000/01/01 00:00:00", format.apply(LocalDateTime.of(1000, 1, 1, 0, 0, 0)));
		assertEquals("2020/04/17 12:34:56", format.apply(LocalDateTime.of(2020, 4, 17, 12, 34, 56)));
		assertEquals("9999/12/31 23:59:59", format.apply(LocalDateTime.of(9999, 12, 31, 23, 59, 59)));
		assertEquals(LocalDateTime.of(1000, 1, 1, 0, 0, 0), parser.apply("1000/01/01 00:00:00"));
		assertEquals(LocalDateTime.of(2020, 4, 17, 12, 34, 56), parser.apply("2020/04/17 12:34:56"));
		assertEquals(LocalDateTime.of(9999, 12, 31, 23, 59, 59), parser.apply("9999/12/31 23:59:59"));
	}

	@Test
	public void testYYYYMMDDHHMMSS() {
		Function<LocalDateTime, String> format = dtmFormatter::fmYYYYMMDDHHMMSS;
		Function<String, LocalDateTime> parser = dtmFormatter::toYYYYMMDDHHMMSS;
		assertEquals("10000101000000", format.apply(LocalDateTime.of(1000, 1, 1, 0, 0, 0)));
		assertEquals("20200417123456", format.apply(LocalDateTime.of(2020, 4, 17, 12, 34, 56)));
		assertEquals("99991231235959", format.apply(LocalDateTime.of(9999, 12, 31, 23, 59, 59)));
		assertEquals(LocalDateTime.of(1000, 1, 1, 0, 0, 0), parser.apply("10000101000000"));
		assertEquals(LocalDateTime.of(2020, 4, 17, 12, 34, 56), parser.apply("20200417123456"));
		assertEquals(LocalDateTime.of(9999, 12, 31, 23, 59, 59), parser.apply("99991231235959"));
	}

	@Test
	public void testYYYY_MM_DD_HH_MM() {
		Function<LocalDateTime, String> format = dtmFormatter::fmYYYY_MM_DD_HH_MM;
		Function<String, LocalDateTime> parser = dtmFormatter::toYYYY_MM_DD_HH_MM;
		assertEquals("1000/01/01 00:00", format.apply(LocalDateTime.of(1000, 1, 1, 0, 0)));
		assertEquals("2020/04/17 12:34", format.apply(LocalDateTime.of(2020, 4, 17, 12, 34)));
		assertEquals("9999/12/31 23:59", format.apply(LocalDateTime.of(9999, 12, 31, 23, 59)));
		assertEquals(LocalDateTime.of(1000, 1, 1, 0, 0), parser.apply("1000/01/01 00:00"));
		assertEquals(LocalDateTime.of(2020, 4, 17, 12, 34), parser.apply("2020/04/17 12:34"));
		assertEquals(LocalDateTime.of(9999, 12, 31, 23, 59), parser.apply("9999/12/31 23:59"));
	}

	@Test
	public void testYYYYMMDDHHMM() {
		Function<LocalDateTime, String> format = dtmFormatter::fmYYYYMMDDHHMM;
		Function<String, LocalDateTime> parser = dtmFormatter::toYYYYMMDDHHMM;
		assertEquals("100001010000", format.apply(LocalDateTime.of(1000, 1, 1, 0, 0)));
		assertEquals("202004171234", format.apply(LocalDateTime.of(2020, 4, 17, 12, 34)));
		assertEquals("999912312359", format.apply(LocalDateTime.of(9999, 12, 31, 23, 59)));
		assertEquals(LocalDateTime.of(1000, 1, 1, 0, 0), parser.apply("100001010000"));
		assertEquals(LocalDateTime.of(2020, 4, 17, 12, 34), parser.apply("202004171234"));
		assertEquals(LocalDateTime.of(9999, 12, 31, 23, 59), parser.apply("999912312359"));
	}

}
