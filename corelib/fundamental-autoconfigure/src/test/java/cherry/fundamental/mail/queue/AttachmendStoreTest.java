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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
		File file1 = new File("file1.txt");
		File file2 = new File("file2.txt");
		try {
			Files.write("abc".getBytes(), file1);
			Files.write("def".getBytes(), file2);

			attachmentStore.save(0L, //
					new Attachment("filename11", file1, "text/plain"));
			attachmentStore.save(1L, //
					new Attachment("filename21", file2, "text/plain"), //
					new Attachment("filename22", "ABC".getBytes(), "text/csv"));

			List<AttachedEntry> l0 = attachmentStore.get(0L);
			assertEquals(1, l0.size());
			assertEquals("filename11", l0.get(0).getFilename());
			assertEquals("text/plain", l0.get(0).getContentType());
			assertEquals("mailqueue/0000000000000000000/0", l0.get(0).getFile().getPath());
			assertEquals("abc", Files.asCharSource(l0.get(0).getFile(), StandardCharsets.UTF_8).read());

			List<AttachedEntry> l1 = attachmentStore.get(1L);
			assertEquals(2, l1.size());
			assertEquals("filename21", l1.get(0).getFilename());
			assertEquals("text/plain", l1.get(0).getContentType());
			assertEquals("mailqueue/0000000000000000001/0", l1.get(0).getFile().getPath());
			assertEquals("def", Files.asCharSource(l1.get(0).getFile(), StandardCharsets.UTF_8).read());
			assertEquals("filename22", l1.get(1).getFilename());
			assertEquals("text/csv", l1.get(1).getContentType());
			assertEquals("mailqueue/0000000000000000001/1", l1.get(1).getFile().getPath());
			assertEquals("ABC", Files.asCharSource(l1.get(1).getFile(), StandardCharsets.UTF_8).read());

		} finally {
			file1.delete();
			file2.delete();
			attachmentStore.delete(0L);
			attachmentStore.delete(1L);
		}
	}

}
