/*
 * Copyright 2015,2021 agwlvssainokuni
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cherry.fundamental.testtool.ToolTester;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StubConfigTest.class)
@SpringBootApplication(scanBasePackages = "cherry.fundamental.testtool")
@ImportResource(locations = { "classpath:spring/appctx-trace.xml", "classpath:spring/appctx-stub.xml" })
public class StubConfigTest {

	@Autowired
	private StubRepository repository;

	private Method method;

	@BeforeEach
	public void before() throws NoSuchMethodException {
		method = ToolTester.class.getDeclaredMethod("toBeStubbed1", Long.class, Long.class);
	}

	@AfterEach
	public void after() {
		for (Method m : repository.getStubbedMethod()) {
			repository.clear(m);
		}
	}

	@Test
	public void testNext() {
		StubConfig stub = repository.get(method).thenReturn(Long.valueOf(123L), Long.class.getCanonicalName());
		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(Long.class.getCanonicalName(), stub.peekType());
		assertEquals(Long.valueOf(123L), stub.next());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testNextWhenRepeatedFalse() {
		StubConfig stub = repository.get(method).thenReturn(Long.valueOf(123L), Long.class.getCanonicalName())
				.setRepeated(false);
		assertFalse(repository.get(method).isRepeated());

		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(Long.class.getCanonicalName(), stub.peekType());
		assertEquals(Long.valueOf(123L), stub.next());

		assertFalse(stub.hasNext());
	}

	@Test
	public void testNextWhenRepeatedTrue() {
		StubConfig stub = repository.get(method).thenReturn(Long.valueOf(123L), Long.class.getCanonicalName())
				.setRepeated(true);
		assertTrue(repository.get(method).isRepeated());

		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(Long.class.getCanonicalName(), stub.peekType());
		assertEquals(Long.valueOf(123L), stub.next());

		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(Long.class.getCanonicalName(), stub.peekType());
		assertEquals(Long.valueOf(123L), stub.next());
	}

	@Test
	public void testPeek() {
		StubConfig stub = repository.get(method).thenReturn(Long.valueOf(123L), Long.class.getCanonicalName());
		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(Long.class.getCanonicalName(), stub.peekType());
		assertEquals(Long.valueOf(123L), stub.peek());
		assertTrue(stub.hasNext());
		assertEquals(Long.valueOf(123L), stub.next());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testNextWhenEmpty() {
		assertThrows(IllegalStateException.class, () -> {
			StubConfig stub = repository.get(method);
			assertFalse(stub.hasNext());
			stub.next();
		});
	}

	@Test
	public void testPeekWhenEmpty() {
		assertThrows(IllegalStateException.class, () -> {
			StubConfig stub = repository.get(method);
			assertFalse(stub.hasNext());
			stub.peek();
		});
	}

	@Test
	public void testNextMock() {
		ToolTester mock = mock(ToolTester.class);
		StubConfig stub = repository.get(method).thenMock(mock);
		assertTrue(stub.hasNext());
		assertTrue(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(mock, stub.nextMock());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testPeekMock() {
		ToolTester mock = mock(ToolTester.class);
		StubConfig stub = repository.get(method).thenMock(mock);
		assertTrue(stub.hasNext());
		assertTrue(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(mock, stub.peekMock());
		assertTrue(stub.hasNext());
		assertEquals(mock, stub.nextMock());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testNextThrowable() {
		StubConfig stub = repository.get(method).thenThrows(IllegalArgumentException.class);
		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertTrue(stub.isThrowable());
		assertEquals(IllegalArgumentException.class, stub.nextThrowable());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testPeekThrowable() {
		StubConfig stub = repository.get(method).thenThrows(IllegalArgumentException.class);
		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertTrue(stub.isThrowable());
		assertEquals(IllegalArgumentException.class, stub.peekThrowable());
		assertTrue(stub.hasNext());
		assertEquals(IllegalArgumentException.class, stub.nextThrowable());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testAlwaysReturn1() {
		StubConfig stub = repository.get(method).alwaysReturn(Long.valueOf(123L));
		for (int i = 0; i < 100; i++) {
			assertTrue(stub.hasNext());
			assertFalse(stub.isMock());
			assertFalse(stub.isThrowable());
			assertEquals(Long.class.getCanonicalName(), stub.peekType());
			assertEquals(Long.valueOf(123L), stub.peek());
			assertEquals(Long.valueOf(123L), stub.next());
		}
		stub.clear();
		assertFalse(stub.hasNext());
	}

	@Test
	public void testAlwaysReturn1_null() {
		StubConfig stub = repository.get(method).alwaysReturn((Long) null);
		for (int i = 0; i < 100; i++) {
			assertTrue(stub.hasNext());
			assertFalse(stub.isMock());
			assertFalse(stub.isThrowable());
			assertNull(stub.peekType());
			assertNull(stub.peek());
			assertNull(stub.next());
		}
		stub.clear();
		assertFalse(stub.hasNext());
	}

	@Test
	public void testAlwaysReturn2() {
		StubConfig stub = repository.get(method).alwaysReturn(Long.valueOf(123L), "long");
		for (int i = 0; i < 100; i++) {
			assertTrue(stub.hasNext());
			assertFalse(stub.isMock());
			assertFalse(stub.isThrowable());
			assertEquals("long", stub.peekType());
			assertEquals(Long.valueOf(123L), stub.peek());
			assertEquals(Long.valueOf(123L), stub.next());
		}
		stub.clear();
		assertFalse(stub.hasNext());
	}

	@Test
	public void testThenReturn1() {
		StubConfig stub = repository.get(method).thenReturn(Long.valueOf(123L));
		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(Long.class.getCanonicalName(), stub.peekType());
		assertEquals(Long.valueOf(123L), stub.peek());
		assertEquals(Long.valueOf(123L), stub.next());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testThenReturn1_null() {
		StubConfig stub = repository.get(method).thenReturn((Long) null);
		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertFalse(stub.isThrowable());
		assertNull(stub.peekType());
		assertNull(stub.peek());
		assertNull(stub.next());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testThenReturn2() {
		StubConfig stub = repository.get(method).thenReturn(Long.valueOf(123L), "long");
		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals("long", stub.peekType());
		assertEquals(Long.valueOf(123L), stub.peek());
		assertEquals(Long.valueOf(123L), stub.next());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testAlwaysMock() {
		ToolTester mock = mock(ToolTester.class);
		StubConfig stub = repository.get(method).alwaysMock(mock);
		for (int i = 0; i < 100; i++) {
			assertTrue(stub.hasNext());
			assertTrue(stub.isMock());
			assertFalse(stub.isThrowable());
			assertEquals(mock, stub.peekMock());
			assertEquals(mock, stub.nextMock());
		}
		stub.clear();
		assertFalse(stub.hasNext());
	}

	@Test
	public void testThenMock() {
		ToolTester mock = mock(ToolTester.class);
		StubConfig stub = repository.get(method).thenMock(mock);
		assertTrue(stub.hasNext());
		assertTrue(stub.isMock());
		assertFalse(stub.isThrowable());
		assertEquals(mock, stub.peekMock());
		assertEquals(mock, stub.nextMock());
		assertFalse(stub.hasNext());
	}

	@Test
	public void testAlwaysThrows() {
		StubConfig stub = repository.get(method).alwaysThrows(IllegalArgumentException.class);
		for (int i = 0; i < 100; i++) {
			assertTrue(stub.hasNext());
			assertFalse(stub.isMock());
			assertTrue(stub.isThrowable());
			assertEquals(IllegalArgumentException.class, stub.peekThrowable());
			assertEquals(IllegalArgumentException.class, stub.nextThrowable());
		}
		stub.clear();
		assertFalse(stub.hasNext());
	}

	@Test
	public void testThenThrows() {
		StubConfig stub = repository.get(method).thenThrows(IllegalArgumentException.class);
		assertTrue(stub.hasNext());
		assertFalse(stub.isMock());
		assertTrue(stub.isThrowable());
		assertEquals(IllegalArgumentException.class, stub.peekThrowable());
		assertEquals(IllegalArgumentException.class, stub.nextThrowable());
		assertFalse(stub.hasNext());
	}

}
