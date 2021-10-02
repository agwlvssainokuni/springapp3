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

import org.junit.jupiter.api.Test;

public class LocalDateUtilTest {

	@Test
	public void testRangeFromLocalDate() {
		LocalDate now = LocalDate.now();
		assertNull(LocalDateUtil.rangeFrom((LocalDate) null));
		assertEquals(LocalDateUtil.rangeFrom(now),
				LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
	}

	@Test
	public void testRangeFromLocalDateTime() {
		LocalDateTime now = LocalDateTime.now();
		assertNull(LocalDateUtil.rangeFrom((LocalDateTime) null));
		assertEquals(LocalDateUtil.rangeFrom(now),
				LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
	}

	@Test
	public void testRangeToLocalDate() {
		LocalDate now = LocalDate.now();
		assertNull(LocalDateUtil.rangeTo((LocalDate) null));
		assertEquals(LocalDateUtil.rangeTo(now),
				LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth()).plusDays(1));
	}

	@Test
	public void testRangeToLocalDateTime() {
		LocalDateTime now = LocalDateTime.now();
		assertNull(LocalDateUtil.rangeTo((LocalDateTime) null));
		assertEquals(LocalDateUtil.rangeTo(now),
				LocalDate.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth()).plusDays(1));
	}

	@Test
	public void testNormalizeYm() {
		LocalDate now = LocalDate.now();
		assertNull(LocalDateUtil.normalizeYm(null));
		assertEquals(LocalDateUtil.normalizeYm(now), LocalDate.of(now.getYear(), now.getMonthValue(), 1));
	}

	@Test
	public void testYmRangeFrom() {
		LocalDate now = LocalDate.now();
		assertNull(LocalDateUtil.ymRangeFrom(null));
		assertEquals(LocalDateUtil.ymRangeFrom(now), LocalDate.of(now.getYear(), now.getMonthValue(), 1));
	}

	@Test
	public void testYmRangeTo() {
		LocalDate now = LocalDate.now();
		assertNull(LocalDateUtil.ymRangeTo(null));
		assertEquals(LocalDateUtil.ymRangeTo(now), LocalDate.of(now.getYear(), now.getMonthValue(), 1).plusMonths(1));
	}

	@Test
	public void testMisc() {
		assertNotNull(new LocalDateUtil());
	}

}
