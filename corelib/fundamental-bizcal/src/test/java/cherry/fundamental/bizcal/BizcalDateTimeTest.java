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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

public class BizcalDateTimeTest {

	@Test
	public void testToday() {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, null, null, null, "standard");
		LocalDate ldt0 = LocalDate.now();
		LocalDate ldt1 = LocalDate.of(2000, 1, 1);
		when(dateTimeStrategy.today(eq("standard"))).thenReturn(ldt0);
		when(dateTimeStrategy.today(eq("extra"))).thenReturn(ldt1);
		assertEquals(ldt0, impl.today());
		assertEquals(ldt1, impl.today("extra"));
	}

	@Test
	public void testNow() {
		DateTimeStrategy dateTimeStrategy = mock(DateTimeStrategy.class);
		Bizcal impl = new BizcalImpl(dateTimeStrategy, null, null, null, "standard");
		LocalDateTime ldtm0 = LocalDateTime.now();
		LocalDateTime ldtm1 = LocalDateTime.of(2000, 1, 1, 12, 34, 56);
		when(dateTimeStrategy.now(eq("standard"))).thenReturn(ldtm0);
		when(dateTimeStrategy.now(eq("extra"))).thenReturn(ldtm1);
		assertEquals(ldtm0, impl.now());
		assertEquals(ldtm1, impl.now("extra"));
	}

}
