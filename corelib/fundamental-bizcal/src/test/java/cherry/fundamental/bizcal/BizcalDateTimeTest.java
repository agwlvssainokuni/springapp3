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

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BizcalDateTimeTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class BizcalDateTimeTest {

	@Autowired
	private Bizcal bizcal;

	@Test
	public void testToday() {
		LocalDate dt0 = LocalDate.now();
		LocalDate today = bizcal.today();
		LocalDate dt1 = LocalDate.now();
		assertTrue(today.compareTo(dt0) >= 0);
		assertTrue(today.compareTo(dt1) <= 0);
	}

	@Test
	public void testNow() {
		LocalDateTime dtm0 = LocalDateTime.now();
		LocalDateTime now = bizcal.now();
		LocalDateTime dtm1 = LocalDateTime.now();
		assertTrue(now.compareTo(dtm0) >= 0);
		assertTrue(now.compareTo(dtm1) <= 0);
	}

}