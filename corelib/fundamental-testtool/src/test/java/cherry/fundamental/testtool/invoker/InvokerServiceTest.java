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

package cherry.fundamental.testtool.invoker;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
@SpringBootTest(classes = InvokerServiceTest.class)
@SpringBootApplication(scanBasePackages = "cherry.fundamental.testtool")
@ImportResource(locations = { "classpath:spring/appctx-trace.xml", "classpath:spring/appctx-stub.xml" })
public class InvokerServiceTest {

	@Autowired
	private InvokerService invokerService;

	@Autowired
	private ReflectionResolver resolver;

	private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().modules(new JavaTimeModule())
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).factory(new YAMLFactory()).build();

	@Test
	public void testNoArgNoRet() throws Exception {
		List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked0");
		assertEquals("--- null\n", invokerService.invoke(null, ToolTester.class, list.get(0), null, null));
		assertEquals("--- null\n", invokerService.invoke("toolTesterImpl", ToolTester.class, list.get(0), null, null));
	}

	@Test
	public void testPrimitive() throws Exception {
		List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked1");
		assertEquals("--- 579\n",
				invokerService.invoke(null, ToolTester.class, list.get(0), asList("123", "456"), null));
	}

	@Test
	public void testLong() throws Exception {
		List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked2");
		assertEquals("--- 579\n",
				invokerService.invoke(null, ToolTester.class, list.get(0), asList("123", "456"), null));
		assertEquals("--- null\n",
				invokerService.invoke(null, ToolTester.class, list.get(0), asList(null, "456"), null));
		assertEquals("--- null\n", invokerService.invoke(null, ToolTester.class, list.get(0), asList("123"), null));
		assertEquals("--- null\n", invokerService.invoke(null, ToolTester.class, list.get(0), null, null));
	}

	@Test
	public void testJodaTime() throws Exception {
		List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked3");
		assertEquals("--- \"2015-01-23T12:34:56\"\n", invokerService.invoke(null, ToolTester.class, list.get(0),
				asList("\"2015-01-23\"", "\"12:34:56\""), null));
	}

	@Test
	public void testFlatDto() throws Exception {
		List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked4");
		assertEquals("---\nval1: 68\nval2: 112\n",
				invokerService.invoke(null, ToolTester.class, list.get(0),
						asList("---\nval1: 12\nval2: 34\n", "---\nval1: 56\nval2: 78\n"),
						asList(ToolTester.Dto1.class.getName())));
	}

	@Test
	public void testNestedDto() throws Exception {
		List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked5");
		assertEquals("---\nval1:\n  val1: 6\n  val2: 8\nval2:\n  val1: 10\n  val2: 12\n",
				invokerService.invoke(null, ToolTester.class, list.get(0),
						asList("---\nval1:\n  val1: 1\n  val2: 2\nval2:\n  val1: 3\n  val2: 4\n",
								"---\nval1:\n  val1: 5\n  val2: 6\nval2:\n  val1: 7\n  val2: 8\n"),
						null));
	}

	@Test
	public void testMethodIndex() throws Exception {
		List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked6");
		int m0;
		int m1;
		if (list.get(0).getReturnType() == Long.TYPE) {
			m0 = 0;
			m1 = 1;
		} else {
			m0 = 1;
			m1 = 0;
		}
		assertEquals("--- -1\n", invokerService.invoke(null, ToolTester.class, list.get(m0), asList("1", "2"), null));
		assertEquals("--- 1\n", invokerService.invoke(null, ToolTester.class, list.get(m1), asList("1", "2"), null));
	}

	@Test
	public void testArgClassNotFound() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> {
			List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked4");
			invokerService.invoke(null, ToolTester.class, list.get(0),
					asList("---\nval1: 12\nval2: 34\n", "---\nval1: 56\nval2: 78\n"), asList("NoClass"));
		});
	}

	@Test
	public void testInvoke_NORMAL1() {
		assertEquals("--- 579\n", invokerService.invoke("toolTesterImpl", ToolTester.class.getName(), "toBeInvoked1", 0,
				"---\n- 123\n- 456\n", "---\n- java.lang.Long\n- java.lang.Long\n"));
	}

	@Test
	public void testInvoke_NORMAL2_NullArgs() {
		assertEquals("--- 579\n", invokerService.invoke("toolTesterImpl", ToolTester.class.getName(), "toBeInvoked2", 0,
				"---\n- 123\n- 456\n", "---\n- java.lang.Long\n- java.lang.Long\n"));
		assertEquals("--- null\n",
				invokerService.invoke("toolTesterImpl", ToolTester.class.getName(), "toBeInvoked2", 0, null, null));
	}

	@Test
	public void testInvoke_NORMAL3_MultiMethod() {
		List<Method> list = resolver.resolveMethod(ToolTester.class, "toBeInvoked6");
		int index0 = list.get(0).getReturnType() == Integer.TYPE ? 0 : 1;
		assertEquals("--- 333\n", invokerService.invoke("toolTesterImpl", ToolTester.class.getName(), "toBeInvoked6",
				index0, "---\n- 123\n- 456\n", null));
		int index1 = list.get(0).getReturnType() == Long.TYPE ? 0 : 1;
		assertEquals("--- -333\n", invokerService.invoke("toolTesterImpl", ToolTester.class.getName(), "toBeInvoked6",
				index1, "---\n- 123\n- 456\n", null));
	}

	@Test
	public void testInvoke_ClassNotFound() throws IOException {
		String result = invokerService.invoke("toolTesterImpl", ToolTester.class.getName() + "NotExist", "toBeInvoked1",
				0, "---\n- 123\n- 456\n", "---\n- java.lang.Long\n- java.lang.Long\n");
		Map<?, ?> map = objectMapper.readValue(result, Map.class);
		assertEquals("java.lang.ClassNotFoundException", map.get("type"));
	}

	@Test
	public void testInvoke_NoSuchMethod() throws IOException {
		String result = invokerService.invoke("toolTesterImpl", ToolTester.class.getName(), "noSuchMethod", 0, null,
				null);
		Map<?, ?> map = objectMapper.readValue(result, Map.class);
		assertEquals("java.lang.NoSuchMethodException", map.get("type"));
	}

}
