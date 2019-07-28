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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.fundamental.testtool.ToolTester;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StubConfigLoaderTest.class)
@SpringBootApplication(scanBasePackages = "cherry.fundamental.testtool")
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class StubConfigLoaderTest {

	@Autowired
	private StubRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ToolTester toolTester;

	private Method method;

	@Before
	public void before() throws NoSuchMethodException {
		method = ToolTester.class.getDeclaredMethod("toBeStubbed1", Long.class, Long.class);
	}

	@After
	public void after() {
		for (Method m : repository.getStubbedMethod()) {
			repository.clear(m);
		}
	}

	@Test
	public void testConfigure1() throws IOException {

		assertEquals(Long.valueOf(0L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));

		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"toBeStubbed1(java.lang.Long,java.lang.Long)\":{\"data\":123}}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
		assertEquals(1, repository.getStubbedMethod().size());
		assertEquals(method, repository.getStubbedMethod().get(0));

		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
	}

	@Test
	public void testConfigure1_withType() throws IOException {

		assertEquals(Long.valueOf(0L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));

		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"toBeStubbed1(java.lang.Long,java.lang.Long)\":{\"data\":123,\"type\":\"java.lang.Long\"}}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
		assertEquals(1, repository.getStubbedMethod().size());
		assertEquals(method, repository.getStubbedMethod().get(0));

		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigure1_withInvalidType() throws IOException {
		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"toBeStubbed1(java.lang.Long,java.lang.Long)\":{\"data\":123,\"type\":\"INVALID_TYPE\"}}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
	}

	@Test
	public void testConfigure2() throws IOException {

		assertEquals(Long.valueOf(0L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));

		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"toBeStubbed1(Long,Long)\":{\"data\":123}}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
		assertEquals(1, repository.getStubbedMethod().size());
		assertEquals(method, repository.getStubbedMethod().get(0));

		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
	}

	@Test
	public void testConfigure3() throws IOException {

		assertEquals(Long.valueOf(0L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
		assertEquals(BigDecimal.ZERO, toolTester.toBeStubbed1(BigDecimal.ZERO, BigDecimal.ZERO));

		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"toBeStubbed1\":{\"data\":\"123\"}}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
		assertEquals(2, repository.getStubbedMethod().size());
		for (Method m : ToolTester.class.getDeclaredMethods()) {
			if (m.getName().equals("toBeStubbed1")) {
				assertTrue(repository.getStubbedMethod().contains(m));
			}
		}

		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
		assertEquals(BigDecimal.valueOf(123L), toolTester.toBeStubbed1(BigDecimal.ZERO, BigDecimal.ZERO));
	}

	@Test
	public void testConfigure4() throws IOException {

		assertEquals(LocalDateTime.of(2015, 1, 1, 12, 34, 56),
				toolTester.toBeStubbed2(LocalDate.of(2015, 1, 1), LocalTime.of(12, 34, 56)));

		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"toBeStubbed2\":{\"data\":\"9999-12-31T23:59:59\"}}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
		assertEquals(1, repository.getStubbedMethod().size());
		for (Method m : ToolTester.class.getDeclaredMethods()) {
			if (m.getName().equals("toBeStubbed2")) {
				assertTrue(repository.getStubbedMethod().contains(m));
			}
		}

		assertEquals(LocalDateTime.of(9999, 12, 31, 23, 59, 59),
				toolTester.toBeStubbed2(LocalDate.of(2015, 1, 1), LocalTime.of(12, 34, 56)));
	}

	@Test
	public void testConfigure5() throws IOException {

		assertEquals(Long.valueOf(0L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));

		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"toBeStubbed1(Long,Long)\":[{\"data\":123}]}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
		assertEquals(1, repository.getStubbedMethod().size());
		assertEquals(method, repository.getStubbedMethod().get(0));

		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
	}

	@Test
	public void testConfigure6() throws IOException {

		assertEquals(Long.valueOf(0L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));

		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"toBeStubbed1(Long,Long)\":[{\"data\":123},{\"data\":456},{\"data\":789}]}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
		assertEquals(1, repository.getStubbedMethod().size());
		assertEquals(method, repository.getStubbedMethod().get(0));

		assertEquals(Long.valueOf(123L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
		assertEquals(Long.valueOf(456L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
		assertEquals(Long.valueOf(789L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
		assertEquals(Long.valueOf(0L), toolTester.toBeStubbed1(Long.valueOf(0L), Long.valueOf(0L)));
	}

	@Test
	public void testConfigure_NOMETHOD() throws IOException {
		String json = "{\"cherry.fundamental.testtool.ToolTester\":{\"NOT_EXIST\":{\"data\":123}}}";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
		assertTrue(repository.getStubbedMethod().isEmpty());
	}

	@Test(expected = IOException.class)
	public void testConfigure_NGJSON() throws IOException {
		String json = "NGJSON";
		Resource res = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
		configure(asList(res));
	}

	@Test
	public void testConfigure_NORESOURCE() throws IOException {
		configure(new ArrayList<Resource>());
		assertTrue(repository.getStubbedMethod().isEmpty());
	}

	@Test
	public void testConfigure_RESOURCENOTEXIST() throws IOException {
		Resource res = new FileSystemResource("/not/exist");
		configure(asList(res));
		assertTrue(repository.getStubbedMethod().isEmpty());
	}

	private void configure(List<Resource> resources) throws IOException {
		new StubConfigLoader(repository, objectMapper).load(resources);
	}

}
