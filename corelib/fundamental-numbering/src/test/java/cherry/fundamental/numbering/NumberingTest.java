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

package cherry.fundamental.numbering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NumberingTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class NumberingTest {

	@Autowired
	private Numbering numbering;

	@Autowired
	private SimpleNumberingStoreConfiguration simpleNumberingStoreConfiguration;

	@Test
	public void testIssueAsString_OK() {
		reset();
		NumberFormat fmt = new DecimalFormat("0000");
		for (int i = 0; i < 100; i++) {
			assertEquals(fmt.format(1 + i), numbering.issueAsString("TEST00"));
		}
		List<String> n100 = numbering.issueAsString("TEST00", 100);
		for (int i = 0; i < 100; i++) {
			assertEquals(fmt.format(101 + i), n100.get(i));
		}
	}

	@Test
	public void testIssueAsString_NG() {
		reset();

		try {
			numbering.issueAsString((String) null);
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("java.lang.IllegalArgumentException: numberName must not be null", ex.toString());
		}
		try {
			numbering.issueAsString("TEST01");
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertEquals("java.lang.IllegalStateException: TEST01 must not be < 2", ex.toString());
		}
		try {
			numbering.issueAsString("TEST02");
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertEquals("java.lang.IllegalStateException: TEST02 must not be > 0", ex.toString());
		}

		try {
			numbering.issueAsString((String) null, 1);
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("java.lang.IllegalArgumentException: numberName must not be null", ex.toString());
		}
		try {
			numbering.issueAsString("TEST00", 0);
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("java.lang.IllegalArgumentException: count must not be <= 0", ex.toString());
		}
		try {
			numbering.issueAsString("TEST01", 1);
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertEquals("java.lang.IllegalStateException: TEST01 must not be < 2", ex.toString());
		}
		try {
			numbering.issueAsString("TEST02", 1);
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertEquals("java.lang.IllegalStateException: TEST02 must not be > 0", ex.toString());
		}
	}

	@Test
	public void testIssueAsLong_OK() {
		reset();
		for (int i = 0; i < 100; i++) {
			assertEquals(Long.valueOf(1 + i), numbering.issueAsLong("TEST00"));
		}
		List<Long> n100 = numbering.issueAsLong("TEST00", 100);
		for (int i = 0; i < 100; i++) {
			assertEquals(Long.valueOf(101 + i), n100.get(i));
		}
	}

	@Test
	public void testIssueAsLong_NG() {
		reset();
		try {
			numbering.issueAsLong((String) null);
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("java.lang.IllegalArgumentException: numberName must not be null", ex.toString());
		}
		try {
			numbering.issueAsLong("TEST01");
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertEquals("java.lang.IllegalStateException: TEST01 must not be < 2", ex.toString());
		}
		try {
			numbering.issueAsLong("TEST02");
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertEquals("java.lang.IllegalStateException: TEST02 must not be > 0", ex.toString());
		}

		try {
			numbering.issueAsLong((String) null, 1);
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("java.lang.IllegalArgumentException: numberName must not be null", ex.toString());
		}
		try {
			numbering.issueAsLong("TEST00", 0);
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("java.lang.IllegalArgumentException: count must not be <= 0", ex.toString());
		}
		try {
			numbering.issueAsLong("TEST01", 1);
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertEquals("java.lang.IllegalStateException: TEST01 must not be < 2", ex.toString());
		}
		try {
			numbering.issueAsLong("TEST02", 1);
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertEquals("java.lang.IllegalStateException: TEST02 must not be > 0", ex.toString());
		}
	}

	@Test
	public void testNoName() {
		reset();
		try {
			numbering.issueAsString("NONE");
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("java.lang.IllegalArgumentException: NONE must be defined", ex.toString());
		}
	}

	private void reset() {

		Map<String, Definition> map = new HashMap<>();

		Definition dto0 = new Definition();
		dto0.setTemplate("{0,number,0000}");
		dto0.setMinValue(1L);
		dto0.setMaxValue(1000L);
		map.put("TEST00", dto0);

		Definition dto1 = new Definition();
		dto1.setTemplate("{0,number,0000}");
		dto1.setMinValue(2L);
		dto1.setMaxValue(2L);
		map.put("TEST01", dto1);

		Definition dto2 = new Definition();
		dto2.setTemplate("{0,number,0000}");
		dto2.setMinValue(0L);
		dto2.setMaxValue(0L);
		map.put("TEST02", dto2);

		simpleNumberingStoreConfiguration.reset(map);
	}

}
