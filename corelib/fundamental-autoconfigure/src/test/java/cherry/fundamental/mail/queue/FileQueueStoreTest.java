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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileQueueStoreTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class FileQueueStoreTest {

	private final File queuedir = new File("build/mailqueue");

	@Autowired
	private QueueStore queueStore;

	@Before
	public void before() throws IOException {
		Files.createDirectories(queuedir.toPath());
	}

	@After
	public void after() throws IOException {
		Files.deleteIfExists(new File(queuedir, "counter.txt").toPath());
		Files.deleteIfExists(new File(queuedir, "counter.lock").toPath());
	}

	@Test
	public void testSave() {
		try {

			LocalDateTime now = LocalDateTime.now();

			assertFalse(new File(queuedir, "0000000000000000000").exists());
			long id0 = queueStore.save("launcherId", "messageName", now, "from@addr", asList("to@addr"),
					asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "text", "html");
			assertEquals(0L, id0);
			assertTrue(new File(queuedir, "0000000000000000000/message.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000000/scheduledAt.txt").exists());

			assertFalse(new File(queuedir, "0000000000000000001").exists());
			long id1 = queueStore.save("launcherId", "messageName", now.plusMinutes(1L), "from@addr", asList("to@addr"),
					null, null, null, null, "", null);
			assertEquals(1L, id1);
			assertTrue(new File(queuedir, "0000000000000000001/message.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000001/scheduledAt.txt").exists());

		} finally {
			queueStore.delete(0L);
			queueStore.delete(1L);
		}
	}

	@Test
	public void testList() {
		try {

			LocalDateTime now = LocalDateTime.now();

			long id0 = queueStore.save("launcherId", "messageName", now, "from@addr", asList("to@addr"),
					asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "text", "html");
			assertEquals(0L, id0);
			long id1 = queueStore.save("launcherId", "messageName", now.plusMinutes(1), "from@addr", asList("to@addr"),
					null, null, null, null, "", null);
			assertEquals(1L, id1);

			List<Long> list00 = queueStore.list(now.minusSeconds(1L));
			assertTrue(list00.isEmpty());

			List<Long> list01 = queueStore.list(now);
			assertEquals(asList(id0), list01);

			List<Long> list02 = queueStore.list(now.plusSeconds(1L));
			assertEquals(asList(id0), list02);

			List<Long> list10 = queueStore.list(now.plusMinutes(1L).minusSeconds(1L));
			assertEquals(asList(id0), list10);

			List<Long> list11 = queueStore.list(now.plusMinutes(1L));
			assertEquals(asList(id0, id1), list11);

			List<Long> list12 = queueStore.list(now.plusMinutes(1L).plusSeconds(1L));
			assertEquals(asList(id0, id1), list12);

		} finally {
			queueStore.delete(0L);
			queueStore.delete(1L);
		}
	}

	@Test
	public void testGet() {
		try {

			LocalDateTime now = LocalDateTime.now();

			long id0 = queueStore.save("launcherId", "messageName", now, "from@addr", asList("to@addr"),
					asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "text", "html");
			assertEquals(0L, id0);
			long id1 = queueStore.save("launcherId", "messageName", now.plusMinutes(1), "from@addr", asList("to@addr"),
					null, null, null, null, "", null);
			assertEquals(1L, id1);

			QueuedEntry msg0 = queueStore.get(0L);
			assertNotNull(msg0);
			assertEquals("from@addr", msg0.getFrom());
			assertEquals(1, msg0.getTo().size());
			assertEquals("to@addr", msg0.getTo().get(0));
			assertEquals(1, msg0.getCc().size());
			assertEquals("cc@addr", msg0.getCc().get(0));
			assertEquals(1, msg0.getBcc().size());
			assertEquals("bcc@addr", msg0.getBcc().get(0));
			assertEquals("replyTo@addr", msg0.getReplyTo());
			assertEquals("subject", msg0.getSubject());
			assertEquals("text", msg0.getText());
			assertEquals("html", msg0.getHtml());

			QueuedEntry msg1 = queueStore.get(1L);
			assertNotNull(msg1);
			assertEquals("from@addr", msg1.getFrom());
			assertEquals(1, msg1.getTo().size());
			assertEquals("to@addr", msg1.getTo().get(0));
			assertNull(msg1.getCc());
			assertNull(msg1.getBcc());
			assertNull(msg1.getReplyTo());
			assertNull(msg1.getSubject());
			assertEquals("", msg1.getText());
			assertNull(msg1.getHtml());

			assertNull(queueStore.get(2L));

		} finally {
			queueStore.delete(0L);
			queueStore.delete(1L);
		}
	}

	@Test
	public void testFinish() {
		try {

			LocalDateTime now = LocalDateTime.now();

			long id0 = queueStore.save("launcherId", "messageName", now, "from@addr", asList("to@addr"),
					asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "text", null);
			assertEquals(0L, id0);
			long id1 = queueStore.save("launcherId", "messageName", now.plusMinutes(1), "from@addr", asList("to@addr"),
					null, null, null, null, "", null);
			assertEquals(1L, id1);

			LocalDateTime sentAt = now.plusMinutes(5L);

			assertFalse(new File(queuedir, "0000000000000000000/sentAt.txt").exists());
			queueStore.finish(0L, sentAt);
			assertTrue(new File(queuedir, "0000000000000000000/sentAt.txt").exists());

			assertFalse(new File(queuedir, "0000000000000000001/sentAt.txt").exists());
			queueStore.finish(1L, sentAt.plusMinutes(1L));
			assertTrue(new File(queuedir, "0000000000000000001/sentAt.txt").exists());

		} finally {
			queueStore.delete(0L);
			queueStore.delete(1L);
		}
	}

	@Test
	public void testListFinished() {
		try {

			LocalDateTime now = LocalDateTime.now();

			long id0 = queueStore.save("launcherId", "messageName", now, "from@addr", asList("to@addr"),
					asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "text", null);
			assertEquals(0L, id0);
			long id1 = queueStore.save("launcherId", "messageName", now.plusMinutes(1), "from@addr", asList("to@addr"),
					null, null, null, null, "", null);
			assertEquals(1L, id1);

			assertEquals(2, queueStore.list(now.plusMinutes(1L)).size());

			LocalDateTime sentAt = now.plusMinutes(5L);
			queueStore.finish(0L, sentAt);
			queueStore.finish(1L, sentAt.plusMinutes(1L));

			assertEquals(0, queueStore.list(now.plusMinutes(1L)).size());

			List<Long> list00 = queueStore.listFinished(sentAt.minusSeconds(1L));
			assertTrue(list00.isEmpty());

			List<Long> list01 = queueStore.listFinished(sentAt);
			assertEquals(asList(id0), list01);

			List<Long> list02 = queueStore.listFinished(sentAt.plusSeconds(1L));
			assertEquals(asList(id0), list02);

			List<Long> list10 = queueStore.listFinished(sentAt.plusMinutes(1L).minusSeconds(1L));
			assertEquals(asList(id0), list10);

			List<Long> list11 = queueStore.listFinished(sentAt.plusMinutes(1L));
			assertEquals(asList(id0, id1), list11);

			List<Long> list12 = queueStore.listFinished(sentAt.plusMinutes(1L).plusSeconds(1L));
			assertEquals(asList(id0, id1), list12);

		} finally {
			queueStore.delete(0L);
			queueStore.delete(1L);
		}
	}

	@Test
	public void testDelete() {
		try {

			LocalDateTime now = LocalDateTime.now();

			long id0 = queueStore.save("launcherId", "messageName", now, "from@addr", asList("to@addr"),
					asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "text", null);
			assertEquals(0L, id0);
			long id1 = queueStore.save("launcherId", "messageName", now.plusMinutes(1), "from@addr", asList("to@addr"),
					null, null, null, null, "", null);
			assertEquals(1L, id1);

			LocalDateTime sentAt = now.plusMinutes(5L);
			queueStore.finish(0L, sentAt);
			queueStore.finish(1L, sentAt.plusMinutes(1L));

			assertTrue(new File(queuedir, "0000000000000000000/message.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000000/scheduledAt.txt").exists());
			assertTrue(new File(queuedir, "0000000000000000000/sentAt.txt").exists());
			queueStore.delete(0L);
			assertFalse(new File(queuedir, "0000000000000000000").exists());

			assertTrue(new File(queuedir, "0000000000000000001/message.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000001/scheduledAt.txt").exists());
			assertTrue(new File(queuedir, "0000000000000000001/sentAt.txt").exists());
			queueStore.delete(1L);
			assertFalse(new File(queuedir, "0000000000000000001").exists());

		} finally {
			queueStore.delete(0L);
			queueStore.delete(1L);
		}
	}

	@Test
	public void testEmptyDestdir() throws IOException {
		try {

			LocalDateTime now = LocalDateTime.now();

			new File(queuedir, "0000000000000000000").createNewFile();
			new File(queuedir, "0000000000000000001").mkdirs();
			new File(queuedir, "0000000000000000001/dummy").createNewFile();

			assertNull(queueStore.get(0L));
			assertNull(queueStore.get(1L));

			assertFalse(new File(queuedir, "0000000000000000000/sentAt.txt").exists());
			queueStore.finish(0L, now);
			assertFalse(new File(queuedir, "0000000000000000000/sentAt.txt").exists());

			assertTrue(queueStore.list(now).isEmpty());
			new File(queuedir, "0000000000000000001/scheduledAt.txt").createNewFile();
			assertEquals(asList(1L), queueStore.list(now));

			assertTrue(queueStore.listFinished(now).isEmpty());
			new File(queuedir, "0000000000000000001/sentAt.txt").createNewFile();
			assertEquals(asList(1L), queueStore.listFinished(now));

			assertTrue(new File(queuedir, "0000000000000000000").exists());
			queueStore.delete(0L);
			assertFalse(new File(queuedir, "0000000000000000000").exists());

			assertTrue(new File(queuedir, "0000000000000000001").exists());
			queueStore.delete(1L);
			assertTrue(new File(queuedir, "0000000000000000001").exists());
			new File(queuedir, "0000000000000000001/dummy").delete();
			queueStore.delete(1L);
			assertFalse(new File(queuedir, "0000000000000000001").exists());

		} finally {
			queueStore.delete(0L);
			queueStore.delete(1L);
		}
	}

	@Test
	public void testEmptyQueuedir() throws IOException {
		try {

			LocalDateTime now = LocalDateTime.now();

			new File(queuedir, "counter.txt").createNewFile();
			assertFalse(new File(queuedir, "0000000000000000000").exists());
			long id0 = queueStore.save(null, null, now, null, null, null, null, null, null, null, null);
			assertEquals(0L, id0);
			assertTrue(new File(queuedir, "0000000000000000000").exists());
			assertTrue(queueStore.delete(id0));
			new File(queuedir, "counter.txt").delete();
			new File(queuedir, "counter.lock").delete();

			new File(queuedir, "not a number").mkdirs();
			new File(queuedir, "not a number/scheduledAt.txt").createNewFile();
			assertTrue(queueStore.list(now).isEmpty());
			assertTrue(queueStore.listFinished(now).isEmpty());
			new File(queuedir, "not a number/sentAt.txt").createNewFile();
			assertTrue(queueStore.list(now).isEmpty());
			assertTrue(queueStore.listFinished(now).isEmpty());
			new File(queuedir, "not a number/scheduledAt.txt").delete();
			new File(queuedir, "not a number/sentAt.txt").delete();
			new File(queuedir, "not a number").delete();

			Files.delete(queuedir.toPath());
			assertTrue(queueStore.list(now).isEmpty());
			assertTrue(queueStore.listFinished(now).isEmpty());

			queuedir.createNewFile();
			assertTrue(queueStore.list(now).isEmpty());
			assertTrue(queueStore.listFinished(now).isEmpty());

		} finally {
			Files.deleteIfExists(queuedir.toPath());
		}
	}

}
