/*
 * Copyright 2014,2021 agwlvssainokuni
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class LocalDateTimeUtilTest {

	@Test
	public void testRangeFromLocalDate() {
		LocalDate now = LocalDate.now();
		assertNull(LocalDateTimeUtil.rangeFrom((LocalDate) null));
		assertEquals(LocalDateTimeUtil.rangeFrom(now),
				LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0, 0));
	}

	@Test
	public void testRangeFromLocalDateLocalTime() {
		LocalDate nowD = LocalDate.now();
		LocalTime nowT = LocalTime.now();
		assertNull(LocalDateTimeUtil.rangeFrom((LocalDate) null, (LocalTime) null));
		assertEquals(LocalDateTimeUtil.rangeFrom(nowD, (LocalTime) null),
				LocalDateTime.of(nowD.getYear(), nowD.getMonthValue(), nowD.getDayOfMonth(), 0, 0, 0, 0));
		assertEquals(LocalDateTimeUtil.rangeFrom(nowD, nowT), LocalDateTime.of(nowD.getYear(), nowD.getMonthValue(),
				nowD.getDayOfMonth(), nowT.getHour(), nowT.getMinute(), nowT.getSecond(), nowT.getNano()));
	}

	@Test
	public void testRangeFromLocalDateTime() {
		LocalDateTime now = LocalDateTime.now();
		assertNull(LocalDateTimeUtil.rangeFrom((LocalDateTime) null));
		assertEquals(LocalDateTimeUtil.rangeFrom(now), LocalDateTime.of(now.getYear(), now.getMonthValue(),
				now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond(), now.getNano()));
	}

	@Test
	public void testRangeToLocalDate() {
		LocalDate now = LocalDate.now();
		assertNull(LocalDateTimeUtil.rangeTo((LocalDate) null));
		assertEquals(LocalDateTimeUtil.rangeTo(now),
				LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0, 0).plusDays(1));
	}

	@Test
	public void testRangeToLocalDateLocalTime() {
		LocalDate nowD = LocalDate.now();
		LocalTime nowT = LocalTime.now();
		assertNull(LocalDateTimeUtil.rangeTo((LocalDate) null, (LocalTime) null, null));
		assertEquals(LocalDateTimeUtil.rangeTo(nowD, (LocalTime) null, null),
				LocalDateTime.of(nowD.getYear(), nowD.getMonthValue(), nowD.getDayOfMonth(), 0, 0, 0, 0).plusDays(1));
		assertEquals(LocalDateTimeUtil.rangeTo(nowD, nowT, ChronoUnit.SECONDS),
				LocalDateTime.of(nowD.getYear(), nowD.getMonthValue(), nowD.getDayOfMonth(), nowT.getHour(),
						nowT.getMinute(), nowT.getSecond(), 0).plusSeconds(1));
	}

	@Test
	public void testSetUnitOfTimeAndRangeToLocalDateLocalTime() {
		LocalDate nowD = LocalDate.now();
		LocalTime nowT = LocalTime.now();
		assertEquals(LocalDateTimeUtil.rangeTo(nowD, nowT, ChronoUnit.MINUTES), LocalDateTime
				.of(nowD.getYear(), nowD.getMonthValue(), nowD.getDayOfMonth(), nowT.getHour(), nowT.getMinute(), 0, 0)
				.plusMinutes(1));
	}

	@Test
	public void testRangeToLocalDateTime() {
		LocalDateTime now = LocalDateTime.now();
		assertNull(LocalDateTimeUtil.rangeTo((LocalDateTime) null, null));
		assertEquals(LocalDateTimeUtil.rangeTo(now, ChronoUnit.SECONDS),
				LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),
						now.getMinute(), now.getSecond(), 0).plusSeconds(1));
	}

	@Test
	public void testSetUnitOfTimeAndRangeToLocalDateTime() {
		LocalDateTime now = LocalDateTime.now();
		assertEquals(LocalDateTimeUtil.rangeTo(now, ChronoUnit.MINUTES), LocalDateTime
				.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), 0, 0)
				.plusMinutes(1));
	}

	@Test
	public void testMisc() {
		assertNotNull(new LocalDateTimeUtil());
	}

}
