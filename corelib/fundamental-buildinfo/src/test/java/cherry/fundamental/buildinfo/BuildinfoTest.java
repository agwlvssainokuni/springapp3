/*
 * Copyright 2019 agwlvssainokuni
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

package cherry.fundamental.buildinfo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BuildinfoTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class BuildinfoTest {

	@Autowired
	private Buildinfo buildinfo;

	@Test
	public void testGetLines() {
		List<String> lines = buildinfo.getLines();
		assertEquals(5, lines.size());
		assertEquals("ジョブ名  : @JOB_NAME@", lines.get(0));
		assertEquals("ビルド番号: @BUILD_NUMBER@", lines.get(1));
		assertEquals("ビルド日時: @BUILD_TIMESTAMP@", lines.get(2));
		assertEquals("コミットID: @GIT_COMMIT@", lines.get(3));
		assertEquals("ブランチ名: @GIT_BRANCH@", lines.get(4));
	}

	@Test
	public void testGetText() {
		assertEquals("ジョブ名  : @JOB_NAME@\r\n" + "ビルド番号: @BUILD_NUMBER@\r\n" + "ビルド日時: @BUILD_TIMESTAMP@\r\n"
				+ "コミットID: @GIT_COMMIT@\r\n" + "ブランチ名: @GIT_BRANCH@\r\n", buildinfo.getText());
	}

}
