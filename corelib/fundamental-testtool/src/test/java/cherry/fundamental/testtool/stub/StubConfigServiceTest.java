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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cherry.fundamental.testtool.ToolTester;
import cherry.fundamental.testtool.reflect.ReflectionResolver;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StubConfigServiceTest.class)
@SpringBootApplication(scanBasePackages = "cherry.fundamental.testtool")
@ImportResource(locations = { "classpath:spring/appctx-trace.xml", "classpath:spring/appctx-stub.xml" })
public class StubConfigServiceTest {

	@Autowired
	private StubConfigService stubConfigService;

	@Autowired
	private ReflectionResolver resolver;

	@Autowired
	private StubRepository repository;

	private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().modules(new JavaTimeModule())
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).factory(new YAMLFactory()).build();

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
	public void testPeek() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- true\n",
				stubConfigService.alwaysReturn(ToolTester.class.getName(), "toBeStubbed1", index, "123", null));
		assertTrue(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertFalse(stubConfigService.isThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- 123\n", stubConfigService.peek(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("java.lang.Long", stubConfigService.peekType(ToolTester.class.getName(), "toBeStubbed1", index));
	}

	@Test
	public void testPeekThrowable() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- true\n", stubConfigService.alwaysThrows(ToolTester.class.getName(), "toBeStubbed1", index,
				IllegalArgumentException.class.getName()));
		assertTrue(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertTrue(stubConfigService.isThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals(IllegalArgumentException.class.getName(),
				stubConfigService.peekThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
	}

	@Test
	public void testAlwaysReturn1() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- true\n",
				stubConfigService.alwaysReturn(ToolTester.class.getName(), "toBeStubbed1", index, "123", null));
		for (int i = 0; i < 100; i++) {
			assertTrue(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
			assertFalse(stubConfigService.isThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
			assertEquals(Long.class.getName(),
					stubConfigService.peekType(ToolTester.class.getName(), "toBeStubbed1", index));
			assertEquals("--- 123\n", stubConfigService.peek(ToolTester.class.getName(), "toBeStubbed1", index));
			assertEquals(Long.valueOf(123L), repository.get(method).next());
		}
		assertEquals("--- true\n", stubConfigService.clear(ToolTester.class.getName(), "toBeStubbed1", index));
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
	}

	@Test
	public void testAlwaysReturn2() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- true\n",
				stubConfigService.alwaysReturn(ToolTester.class.getName(), "toBeStubbed1", index, "123", "long"));
		for (int i = 0; i < 100; i++) {
			assertTrue(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
			assertFalse(stubConfigService.isThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
			assertEquals("long", stubConfigService.peekType(ToolTester.class.getName(), "toBeStubbed1", index));
			assertEquals("--- 123\n", stubConfigService.peek(ToolTester.class.getName(), "toBeStubbed1", index));
			assertEquals(Long.valueOf(123L), repository.get(method).next());
		}
		assertEquals("--- true\n", stubConfigService.clear(ToolTester.class.getName(), "toBeStubbed1", index));
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
	}

	@Test
	public void testThenReturn1() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- true\n",
				stubConfigService.thenReturn(ToolTester.class.getName(), "toBeStubbed1", index, "123", null));

		assertTrue(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertFalse(stubConfigService.isThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals(Long.class.getName(),
				stubConfigService.peekType(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- 123\n", stubConfigService.peek(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals(Long.valueOf(123L), repository.get(method).next());

		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
	}

	@Test
	public void testThenReturn2() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- true\n",
				stubConfigService.thenReturn(ToolTester.class.getName(), "toBeStubbed1", index, "123", "long"));

		assertTrue(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertFalse(stubConfigService.isThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("long", stubConfigService.peekType(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- 123\n", stubConfigService.peek(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals(Long.valueOf(123L), repository.get(method).next());

		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
	}

	@Test
	public void testAlwaysThrows() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- true\n", stubConfigService.alwaysThrows(ToolTester.class.getName(), "toBeStubbed1", index,
				IllegalArgumentException.class.getName()));
		for (int i = 0; i < 100; i++) {
			assertTrue(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
			assertTrue(stubConfigService.isThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
			assertEquals(IllegalArgumentException.class.getName(),
					stubConfigService.peekThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
			assertEquals(IllegalArgumentException.class, repository.get(method).nextThrowable());
		}
		assertEquals("--- true\n", stubConfigService.clear(ToolTester.class.getName(), "toBeStubbed1", index));
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
	}

	@Test
	public void testThenThrows() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals("--- true\n", stubConfigService.thenThrows(ToolTester.class.getName(), "toBeStubbed1", index,
				IllegalArgumentException.class.getName()));

		assertTrue(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
		assertTrue(stubConfigService.isThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals(IllegalArgumentException.class.getName(),
				stubConfigService.peekThrowable(ToolTester.class.getName(), "toBeStubbed1", index));
		assertEquals(IllegalArgumentException.class, repository.get(method).nextThrowable());

		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", index));
	}

	@Test
	public void testExecutePredicate_methodIndex() {
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName(), "toBeStubbed1", 4));
	}

	@Test
	public void testExecutePredicate_ClassNotFound() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertFalse(stubConfigService.hasNext(ToolTester.class.getName() + "NotExist", "toBeStubbed1", index));
	}

	@Test
	public void testExecuteFunction_methodIndex() {
		assertEquals("false", stubConfigService.peekThrowable(ToolTester.class.getName(), "toBeStubbed1", 3));
	}

	@Test
	public void testExecuteFunction_ClassNotFound() {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		assertEquals(ToolTester.class.getName() + "NotExist",
				stubConfigService.peekThrowable(ToolTester.class.getName() + "NotExist", "toBeStubbed1", index));
	}

	@Test
	public void testExecuteWithMapping_methodIndex() throws IOException {
		assertEquals("--- false\n",
				stubConfigService.alwaysReturn(ToolTester.class.getName(), "toBeStubbed1", 3, "1", null));
	}

	@Test
	public void testExecuteWithMapping_ClassNotFound() throws IOException {
		int index = getMethodIndex(ToolTester.class, "toBeStubbed1", Long.class);
		String result = stubConfigService.alwaysReturn(ToolTester.class.getName() + "NotExist", "toBeStubbed1", index,
				"1", null);
		Map<?, ?> map = objectMapper.readValue(result, Map.class);
		assertEquals(ClassNotFoundException.class.getName(), map.get("type"));
		assertEquals(ToolTester.class.getName() + "NotExist", map.get("message"));
	}

	private int getMethodIndex(Class<?> beanClass, String methodName, Class<?> returnType) {
		int i = 0;
		for (Method m : resolver.resolveMethod(ToolTester.class, methodName)) {
			if (m.getReturnType() == returnType) {
				return i;
			}
			i++;
		}
		return i;
	}

}
