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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QueueStoreTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class QueueStoreTest {

	@Autowired
	private QueueStore queueStore;

	@Before
	public void before() {
		File dir = new File("mailqueue");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File counter = new File(dir, "counter.txt");
		if (counter.exists()) {
			counter.delete();
		}
	}

	@Test
	public void testCreate() {
		long id0 = queueStore.save("launcherId", "messageName", LocalDateTime.now(), "from@addr", asList("to@addr"),
				asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "body");
		assertEquals(0L, id0);
		long id1 = queueStore.save("launcherId", "messageName", LocalDateTime.now().plusMinutes(1), "from@addr",
				asList("to@addr"), null, null, null, "subject", "body");
		assertEquals(1L, id1);
	}

	@Test
	public void testList() {

		long id0 = queueStore.save("launcherId", "messageName", LocalDateTime.now(), "from@addr", asList("to@addr"),
				asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "body");
		assertEquals(0L, id0);
		long id1 = queueStore.save("launcherId", "messageName", LocalDateTime.now().plusMinutes(1), "from@addr",
				asList("to@addr"), null, null, null, "subject", "body");
		assertEquals(1L, id1);

		List<Long> list0 = queueStore.list(LocalDateTime.now().plusSeconds(1));
		assertEquals(1, list0.size());
		assertEquals(0L, list0.get(0).longValue());

		List<Long> list1 = queueStore.list(LocalDateTime.now().plusHours(1));
		assertEquals(2, list1.size());
		assertEquals(0L, list1.get(0).longValue());
		assertEquals(1L, list1.get(1).longValue());
	}

	@Test
	public void testGet() {

		long id0 = queueStore.save("launcherId", "messageName", LocalDateTime.now(), "from@addr", asList("to@addr"),
				asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "body");
		assertEquals(0L, id0);
		long id1 = queueStore.save("launcherId", "messageName", LocalDateTime.now().plusMinutes(1), "from@addr",
				asList("to@addr"), null, null, null, "subject", "body");
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
		assertEquals("body", msg0.getText());

		assertNull(queueStore.get(10L));
	}

	@Test
	public void testFinish() {

		long id0 = queueStore.save("launcherId", "messageName", LocalDateTime.now(), "from@addr", asList("to@addr"),
				asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "body");
		assertEquals(0L, id0);
		long id1 = queueStore.save("launcherId", "messageName", LocalDateTime.now().plusMinutes(1), "from@addr",
				asList("to@addr"), null, null, null, "subject", "body");
		assertEquals(1L, id1);

		assertNotNull(queueStore.get(0L));
		assertNotNull(queueStore.get(1L));

		queueStore.finish(0L, LocalDateTime.now());
		queueStore.finish(1L, LocalDateTime.now());

		assertNotNull(queueStore.get(0L));
		assertNotNull(queueStore.get(1L));

		queueStore.delete(0L);
		queueStore.delete(1L);

		assertNull(queueStore.get(0L));
		assertNull(queueStore.get(1L));
	}

}
