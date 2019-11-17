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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;

import org.apache.commons.lang3.Range;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BizcalConfigurationTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class BizcalConfigurationTest {

	@Autowired
	private Bizcal bizcal;

	@Test
	public void testDateTime() {
		// today()
		LocalDate ldtFm = LocalDate.now();
		LocalDate curdt = bizcal.today();
		LocalDate ldtTo = LocalDate.now();
		assertTrue(Range.between(ldtFm, ldtTo, (c1, c2) -> c1.compareTo(c2)).contains(curdt));
		// now()
		LocalDateTime ldtmFm = LocalDateTime.now();
		LocalDateTime curdtm = bizcal.now();
		LocalDateTime ldtmTo = LocalDateTime.now();
		assertTrue(Range.between(ldtmFm, ldtmTo, (c1, c2) -> c1.compareTo(c2)).contains(curdtm));
	}

	@Test
	public void testYearMonth() {
		assertEquals(YearMonth.of(2020, 1), bizcal.yearMonth(LocalDate.of(2020, 1, 1)));
		assertEquals(YearMonth.of(2020, 1), bizcal.yearMonth(LocalDate.of(2020, 1, 31)));
		assertEquals(YearMonth.of(2020, 2), bizcal.yearMonth(LocalDate.of(2020, 2, 1)));
		assertEquals(YearMonth.of(2020, 2), bizcal.yearMonth(LocalDate.of(2020, 2, 29)));
	}

	@Test
	public void testYear() {
		assertEquals(Year.of(2020), bizcal.year(LocalDate.of(2020, 4, 1)));
		assertEquals(Year.of(2020), bizcal.year(LocalDate.of(2021, 3, 31)));
		assertEquals(Year.of(2021), bizcal.year(LocalDate.of(2021, 4, 1)));
		assertEquals(Year.of(2021), bizcal.year(LocalDate.of(2022, 3, 31)));
	}

	@Test
	public void testWorkday() {
		assertEquals(32, bizcal.getNumberOfWorkday(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1)));
		assertEquals(30, bizcal.getNumberOfWorkday(LocalDate.of(2020, 2, 1), LocalDate.of(2020, 3, 1)));
		assertEquals(32, bizcal.getNumberOfWorkday(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 1)));
		assertEquals(31, bizcal.getNumberOfWorkday(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 5, 1)));
	}

}
