/*
 * Copyright 2014,2019 agwlvssainokuni
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

package cherry.fundamental.mail.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.io.Files;

import cherry.fundamental.mail.Attachment;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AttachmendStoreTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class AttachmendStoreTest {

	@Autowired
	private AttachmentStore attachmentStore;

	@Test
	public void testAttachment() throws IOException {
		try {

			attachmentStore.save(0L, //
					new Attachment("filename11", new File("file1.txt")));
			attachmentStore.save(1L, //
					new Attachment("filename21", new File("file2.txt")), //
					new Attachment("filename22", "ABC".getBytes(), "text/csv"));

			Optional<List<AttachedEntry>> l0 = attachmentStore.load(0L);
			assertTrue(l0.isPresent());
			assertEquals(1, l0.get().size());
			assertEquals("filename11", l0.get().get(0).getFilename());
			assertEquals(new File("file1.txt").getAbsolutePath(), l0.get().get(0).getFile().getAbsolutePath());
			assertNull(l0.get().get(0).getContentType());
			assertFalse(l0.get().get(0).isStream());

			Optional<List<AttachedEntry>> l1 = attachmentStore.load(1L);
			assertTrue(l1.isPresent());
			assertEquals(2, l1.get().size());
			assertEquals("filename21", l1.get().get(0).getFilename());
			assertEquals(new File("file2.txt").getAbsolutePath(), l1.get().get(0).getFile().getAbsolutePath());
			assertNull(l1.get().get(0).getContentType());
			assertFalse(l1.get().get(0).isStream());
			assertEquals("filename22", l1.get().get(1).getFilename());
			assertNotNull(l1.get().get(1).getFile());
			assertEquals("ABC", new String(Files.toByteArray(l1.get().get(1).getFile())));
			assertEquals("text/csv", l1.get().get(1).getContentType());
			assertTrue(l1.get().get(1).isStream());

		} finally {
			attachmentStore.delete(0L);
			attachmentStore.delete(1L);
		}
	}

}
