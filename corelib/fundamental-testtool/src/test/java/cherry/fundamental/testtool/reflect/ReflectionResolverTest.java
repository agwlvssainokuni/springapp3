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

package cherry.fundamental.testtool.reflect;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.fundamental.testtool.ToolTester;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReflectionResolverTest.class)
@SpringBootApplication(scanBasePackages = "cherry.fundamental.testtool")
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class ReflectionResolverTest {

	@Autowired
	@Qualifier("reflectionResolver")
	private ReflectionResolver resolver;

	@Test
	public void testResolveBeanName() throws Exception {
		List<String> list = resolver.resolveBeanName(ToolTester.class.getName());
		assertEquals(1, list.size());
		assertEquals("toolTesterImpl", list.get(0));
	}

	@Test(expected = ClassNotFoundException.class)
	public void testResolveBeanName_ClassNotFound() throws Exception {
		resolver.resolveBeanName(ToolTester.class.getName() + "NotExist");
	}

	@Test
	public void testResolveMethod() throws Exception {
		List<Method> list = resolver.resolveMethod(ToolTester.class.getName(), "toBeInvoked0");
		assertEquals(1, list.size());
		Method m = list.get(0);
		assertEquals(0, m.getParameterTypes().length);
		assertEquals(Void.TYPE, m.getReturnType());
	}

	@Test(expected = ClassNotFoundException.class)
	public void testResolveMethod_ClassNotFound() throws Exception {
		resolver.resolveMethod(ToolTester.class.getName() + "NotExist", "toBeInvoked6");
	}

}
