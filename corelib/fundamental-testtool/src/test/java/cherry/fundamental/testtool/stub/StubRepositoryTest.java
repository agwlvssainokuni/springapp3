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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.fundamental.testtool.ToolTester;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StubRepositoryTest.class)
@SpringBootApplication(scanBasePackages = "cherry.fundamental.testtool")
@ImportResource(locations = { "classpath:spring/appctx-trace.xml", "classpath:spring/appctx-stub.xml" })
public class StubRepositoryTest {

	@Autowired
	private StubRepository repository;

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
	public void test() throws NoSuchMethodException {
		// 事前：定義なし
		assertFalse(repository.contains(method));
		assertTrue(repository.getStubbedMethod().isEmpty());
		// 実行
		repository.get(method).alwaysReturn(Long.valueOf(123L));
		// 検証：定義あり
		assertTrue(repository.contains(method));
		assertNotNull(repository.get(method));
		assertEquals(1, repository.getStubbedMethod().size());
		assertEquals(method, repository.getStubbedMethod().get(0));
		// 実行
		repository.clear(method);
		// 検証：定義なし
		assertFalse(repository.contains(method));
		assertTrue(repository.getStubbedMethod().isEmpty());
	}

}
