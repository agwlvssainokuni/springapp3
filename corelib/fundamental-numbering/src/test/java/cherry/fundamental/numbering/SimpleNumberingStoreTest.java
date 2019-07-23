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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SimpleNumberingStoreTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class SimpleNumberingStoreTest {

	@Autowired
	private NumberingStore numberingStore;

	@Autowired
	private SimpleNumberingStoreConfiguration simpleNumberingStoreConfiguration;

	@Test
	public void testGetDefinition() {
		reset();
		assertNotNull(numberingStore.getDefinition("TEST00"));
		try {
			numberingStore.getDefinition("NONE");
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("NONE must be defined", ex.getMessage());
		}
	}

	@Test
	public void testLoadSave() {
		reset();
		for (long i = 0L; i < 100L; i++) {
			long current = numberingStore.loadAndLock("TEST00");
			try {
				assertEquals(i, current);
			} finally {
				numberingStore.saveAndUnlock("TEST00", current + 1L);
			}
		}
	}

	private void reset() {
		Definition dto = new Definition();
		dto.setTemplate("0000");
		dto.setMinValue(1L);
		dto.setMaxValue(9999L);
		Map<String, Definition> map = new HashMap<>();
		map.put("TEST00", dto);
		simpleNumberingStoreConfiguration.reset(map);
	}

}
