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

package cherry.fundamental.testtool.stub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.fundamental.testtool.ToolTester;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StubInterceptorTest.class)
@SpringBootApplication(scanBasePackages = "cherry.fundamental.testtool")
@ImportResource(locations = { "classpath:spring/appctx-trace.xml", "classpath:spring/appctx-stub.xml" })
public class StubInterceptorTest {

	@Autowired
	private StubRepository repository;

	@Autowired
	private ToolTester tester;

	@After
	public void after() {
		for (Method m : repository.getStubbedMethod()) {
			repository.clear(m);
		}
	}

	@Test
	public void testMethodLong_RETURN() throws NoSuchMethodException {
		Method method = ToolTester.class.getDeclaredMethod("toBeStubbed1", Long.class, Long.class);

		assertEquals(Long.valueOf(1234L), tester.toBeStubbed1(1030L, 204L));

		repository.get(method).thenReturn(1L);
		assertEquals(Long.valueOf(1L), tester.toBeStubbed1(1030L, 204L));
		assertEquals(Long.valueOf(1234L), tester.toBeStubbed1(1030L, 204L));

		repository.get(method).thenReturn(1L).thenReturn(2L).thenReturn(3L);
		assertEquals(Long.valueOf(1L), tester.toBeStubbed1(1030L, 204L));
		assertEquals(Long.valueOf(2L), tester.toBeStubbed1(1030L, 204L));
		assertEquals(Long.valueOf(3L), tester.toBeStubbed1(1030L, 204L));
		assertEquals(Long.valueOf(1234L), tester.toBeStubbed1(1030L, 204L));

		repository.get(method).alwaysReturn(1L);
		assertEquals(Long.valueOf(1L), tester.toBeStubbed1(1030L, 204L));
		assertEquals(Long.valueOf(1L), tester.toBeStubbed1(1030L, 204L));
		repository.get(method).clear();
		assertEquals(Long.valueOf(1234L), tester.toBeStubbed1(1030L, 204L));
	}

	@Test
	public void testMethodBigDecimal_THROWABLE() throws NoSuchMethodException {
		Method method = ToolTester.class.getDeclaredMethod("toBeStubbed1", BigDecimal.class, BigDecimal.class);

		assertEquals(BigDecimal.valueOf(1234L),
				tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L)));

		repository.get(method).thenThrows(IllegalArgumentException.class);
		try {
			tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L));
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			// OK
		}
		assertEquals(BigDecimal.valueOf(1234L),
				tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L)));

		repository.get(method).thenThrows(IllegalArgumentException.class).thenThrows(Throwable.class);
		try {
			tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L));
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			// OK
		}
		try {
			tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L));
			fail("Exception must be thrown");
		} catch (Throwable ex) {
			// OK
		}
		assertEquals(BigDecimal.valueOf(1234L),
				tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L)));

		repository.get(method).alwaysThrows(IllegalArgumentException.class);
		try {
			tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L));
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			// OK
		}
		try {
			tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L));
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			// OK
		}
		repository.get(method).clear();
		assertEquals(BigDecimal.valueOf(1234L),
				tester.toBeStubbed1(BigDecimal.valueOf(1030L), BigDecimal.valueOf(204L)));
	}

	@Test
	public void testMocked() throws NoSuchMethodException {

		Method method = ToolTester.class.getDeclaredMethod("toBeStubbed1", Long.class, Long.class);
		ToolTester mock = mock(ToolTester.class);
		when(mock.toBeStubbed1(any(Long.class), any(Long.class))).thenReturn(Long.valueOf(1234L));

		repository.get(method).thenMock(mock);
		assertEquals(Long.valueOf(1234L), tester.toBeStubbed1(Long.valueOf(1L), Long.valueOf(2L)));
		assertEquals(Long.valueOf(3L), tester.toBeStubbed1(Long.valueOf(1L), Long.valueOf(2L)));

		repository.get(method).alwaysMock(mock);
		assertEquals(Long.valueOf(1234L), tester.toBeStubbed1(Long.valueOf(1L), Long.valueOf(2L)));
		assertEquals(Long.valueOf(1234L), tester.toBeStubbed1(Long.valueOf(1L), Long.valueOf(2L)));
		assertEquals(Long.valueOf(1234L), tester.toBeStubbed1(Long.valueOf(1L), Long.valueOf(2L)));
		repository.get(method).clear();
		assertEquals(Long.valueOf(3L), tester.toBeStubbed1(Long.valueOf(1L), Long.valueOf(2L)));

		verify(mock, times(4)).toBeStubbed1(eq(Long.valueOf(1L)), eq(Long.valueOf(2L)));
	}

}
