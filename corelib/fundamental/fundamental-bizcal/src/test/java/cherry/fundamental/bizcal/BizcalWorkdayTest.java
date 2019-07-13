/*
 * Copyright 2015,2019 agwlvssainokuni
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

import static java.time.LocalDate.now;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BizcalConfiguration.class)
public class BizcalWorkdayTest {

	@Autowired
	private Bizcal bizcal;

	@Test
	public void testGetNumberOfWorkday() {
		assertEquals(3, bizcal.getNumberOfWorkday(now().plusDays(2)));
		assertEquals(4, bizcal.getNumberOfWorkday("name1", now().plusDays(3)));
		assertEquals(2, bizcal.getNumberOfWorkday(now().plusDays(1), now().plusDays(2)));
		assertEquals(3, bizcal.getNumberOfWorkday("name1", now().plusDays(1), now().plusDays(3)));
	}

	@Test
	public void testGetNextWorkday() {
		assertEquals(now().plusDays(2), bizcal.getNextWorkday(3));
		assertEquals(now().plusDays(3), bizcal.getNextWorkday("name1", 4));
		assertEquals(now().plusDays(2), bizcal.getNextWorkday(now().plusDays(1), 2));
		assertEquals(now().plusDays(3), bizcal.getNextWorkday("name1", now().plusDays(1), 3));
	}

}
