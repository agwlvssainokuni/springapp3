/*
 * Copyright 2014,2021 agwlvssainokuni
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

package cherry.fundamental.batch.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cherry.fundamental.batch.ExitStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LauncherTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class LauncherTest {

	@Autowired
	private Launcher launcher;

	@Test
	public void testExitWithNormal() {
		ExitStatus status = launch("test01Batch", "NORMAL");
		assertEquals(ExitStatus.NORMAL, status);
	}

	@Test
	public void testExitWithWarn() {
		ExitStatus status = launch("test01Batch", "WARN");
		assertEquals(ExitStatus.WARN, status);
	}

	@Test
	public void testExitWithError() {
		ExitStatus status = launch("test01Batch", "ERROR");
		assertEquals(ExitStatus.ERROR, status);
	}

	@Test
	public void testExitWithFatal() {
		ExitStatus status = launch("test01Batch", "FATAL");
		assertEquals(ExitStatus.FATAL, status);
	}

	@Test
	public void testTranslateToNormal() {
		ExitStatus status = launch("test02Batch", "NORMAL");
		assertEquals(ExitStatus.NORMAL, status);
	}

	@Test
	public void testTranslateToWarn() {
		ExitStatus status = launch("test02Batch", "WARN");
		assertEquals(ExitStatus.WARN, status);
	}

	@Test
	public void testTranslateToError() {
		ExitStatus status = launch("test02Batch", "ERROR");
		assertEquals(ExitStatus.ERROR, status);
	}

	@Test
	public void testTranslateToFatal() {
		ExitStatus status = launch("test02Batch", "FATAL");
		assertEquals(ExitStatus.FATAL, status);
	}

	@Test
	public void testTranslateNone() {
		ExitStatus status = launch("test02Batch");
		assertEquals(ExitStatus.FATAL, status);
	}

	private ExitStatus launch(String... args) {
		ApplicationArguments appargs = new DefaultApplicationArguments(args);
		return launcher.launch(appargs);
	}

}
