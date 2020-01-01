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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.fundamental.mail.Attachment;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileAttachmendStoreTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class FileAttachmendStoreTest {

	private final File queuedir = new File("build/mailqueue");

	@Autowired
	private AttachmentStore attachmentStore;

	@Before
	public void before() throws IOException {
		Files.createDirectories(queuedir.toPath());
	}

	@Test
	public void testSave() throws IOException {
		File file1 = new File("file1.txt");
		File file2 = new File("file2.txt");
		try {
			Files.write(file1.toPath(), "abc".getBytes());
			Files.write(file2.toPath(), "ABC".getBytes());

			assertFalse(new File(queuedir, "0000000000000000000").exists());
			attachmentStore.save(0L);
			assertFalse(new File(queuedir, "0000000000000000000").exists());

			assertFalse(new File(queuedir, "0000000000000000001").exists());
			attachmentStore.save(1L, //
					new Attachment("filename11", file1, "text/plain"));
			assertTrue(new File(queuedir, "0000000000000000001/filelist.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000001/0").exists());
			assertFalse(new File(queuedir, "0000000000000000001/1").exists());

			assertFalse(new File(queuedir, "0000000000000000002").exists());
			attachmentStore.save(2L, //
					new Attachment("filename21", file2, "text/html"), //
					new Attachment("filename22", "DEF".getBytes(), "text/csv"));
			assertTrue(new File(queuedir, "0000000000000000002/filelist.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000002/0").exists());
			assertTrue(new File(queuedir, "0000000000000000002/1").exists());

		} finally {
			attachmentStore.delete(0L);
			attachmentStore.delete(1L);
			attachmentStore.delete(2L);
			Files.deleteIfExists(file1.toPath());
			Files.deleteIfExists(file2.toPath());
		}
	}

	@Test
	public void testGet() throws IOException {
		File file1 = new File("file1.txt");
		File file2 = new File("file2.txt");
		try {
			Files.write(file1.toPath(), "abc".getBytes());
			Files.write(file2.toPath(), "ABC".getBytes());

			attachmentStore.save(0L);
			attachmentStore.save(1L, //
					new Attachment("filename11", file1, "text/plain"));
			attachmentStore.save(2L, //
					new Attachment("filename21", file2, "text/html"), //
					new Attachment("filename22", "DEF".getBytes(), "text/csv"));

			List<AttachedEntry> list0 = attachmentStore.get(0L);
			assertTrue(list0.isEmpty());

			List<AttachedEntry> list1 = attachmentStore.get(1L);
			assertEquals(1, list1.size());
			assertEquals("filename11", list1.get(0).getFilename());
			assertEquals("text/plain", list1.get(0).getContentType());
			assertEquals("abc", new String(Files.readAllBytes(list1.get(0).getFile().toPath())));

			List<AttachedEntry> list2 = attachmentStore.get(2L);
			assertEquals(2, list2.size());
			assertEquals("filename21", list2.get(0).getFilename());
			assertEquals("text/html", list2.get(0).getContentType());
			assertEquals("ABC", new String(Files.readAllBytes(list2.get(0).getFile().toPath())));
			assertEquals("filename22", list2.get(1).getFilename());
			assertEquals("text/csv", list2.get(1).getContentType());
			assertEquals("DEF", new String(Files.readAllBytes(list2.get(1).getFile().toPath())));

		} finally {
			attachmentStore.delete(0L);
			attachmentStore.delete(1L);
			attachmentStore.delete(2L);
			Files.deleteIfExists(file1.toPath());
			Files.deleteIfExists(file2.toPath());
		}
	}

	@Test
	public void testDelete() throws IOException {
		File file1 = new File("file1.txt");
		File file2 = new File("file2.txt");
		try {
			Files.write(file1.toPath(), "abc".getBytes());
			Files.write(file2.toPath(), "ABC".getBytes());

			attachmentStore.save(0L);
			attachmentStore.save(1L, //
					new Attachment("filename11", file1, "text/plain"));
			attachmentStore.save(2L, //
					new Attachment("filename21", file2, "text/html"), //
					new Attachment("filename22", "DEF".getBytes(), "text/csv"));

			assertFalse(new File(queuedir, "0000000000000000000").exists());
			attachmentStore.delete(0L);
			assertFalse(new File(queuedir, "0000000000000000000").exists());

			assertTrue(new File(queuedir, "0000000000000000001/filelist.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000001/0").exists());
			attachmentStore.delete(1L);
			assertFalse(new File(queuedir, "0000000000000000001").exists());

			assertTrue(new File(queuedir, "0000000000000000002/filelist.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000002/0").exists());
			assertTrue(new File(queuedir, "0000000000000000002/1").exists());
			attachmentStore.delete(2L);
			assertFalse(new File(queuedir, "0000000000000000002").exists());

		} finally {
			attachmentStore.delete(0L);
			attachmentStore.delete(1L);
			attachmentStore.delete(2L);
			Files.deleteIfExists(file1.toPath());
			Files.deleteIfExists(file2.toPath());
		}
	}

	@Test
	public void testEmptyDestdir() throws IOException {
		try {

			new File(queuedir, "0000000000000000000").createNewFile();
			new File(queuedir, "0000000000000000001").mkdirs();
			new File(queuedir, "0000000000000000001/dummy").createNewFile();

			assertTrue(attachmentStore.get(0L).isEmpty());
			assertTrue(attachmentStore.get(1L).isEmpty());

			assertTrue(new File(queuedir, "0000000000000000000").exists());
			attachmentStore.delete(0L);
			assertFalse(new File(queuedir, "0000000000000000000").exists());

			assertTrue(new File(queuedir, "0000000000000000001").exists());
			attachmentStore.delete(1L);
			assertTrue(new File(queuedir, "0000000000000000001").exists());
			new File(queuedir, "0000000000000000001/dummy").delete();
			attachmentStore.delete(1L);
			assertFalse(new File(queuedir, "0000000000000000001").exists());

		} finally {
			attachmentStore.delete(0L);
			attachmentStore.delete(1L);
		}
	}

}
