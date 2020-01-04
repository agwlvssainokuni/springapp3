/*
 * Copyright 2014,2016 agwlvssainokuni
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.io.ByteStreams;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import cherry.fundamental.mail.Attachment;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailQueueImplTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class MailQueueImplTest {

	private final File queuedir = new File("build/mailqueue");

	@Autowired
	private MailQueue mailQueue;

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
	public void testEnqueue() throws IOException {
		File file1 = new File("file1.txt");
		File file2 = new File("file2.txt");
		try {
			Files.write(file1.toPath(), "abc".getBytes());
			Files.write(file2.toPath(), "ABC".getBytes());

			LocalDateTime now = LocalDateTime.now();

			assertFalse(new File(queuedir, "0000000000000000000").exists());
			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, now);
			assertEquals(0L, id0);
			assertTrue(new File(queuedir, "0000000000000000000/message.yaml").exists());
			assertFalse(new File(queuedir, "0000000000000000000/filelist.yaml").exists());

			assertFalse(new File(queuedir, "0000000000000000001").exists());
			long id1 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(1L), new Attachment("filename11", file1, "text/plain"));
			assertEquals(1L, id1);
			assertTrue(new File(queuedir, "0000000000000000001/message.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000001/filelist.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000001/0").exists());
			assertFalse(new File(queuedir, "0000000000000000001/1").exists());

			assertFalse(new File(queuedir, "0000000000000000002").exists());
			long id2 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(2L), //
					new Attachment("filename21", file2, "text/html"),
					new Attachment("filename22", "DEF".getBytes(), "text/csv"));
			assertEquals(2L, id2);
			assertTrue(new File(queuedir, "0000000000000000002/message.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000002/filelist.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000002/0").exists());
			assertTrue(new File(queuedir, "0000000000000000002/1").exists());

		} finally {
			mailQueue.expire(0L);
			mailQueue.expire(1L);
			mailQueue.expire(2L);
			Files.deleteIfExists(file1.toPath());
			Files.deleteIfExists(file2.toPath());
		}
	}

	@Test
	public void testListToSend() throws IOException {
		File file1 = new File("file1.txt");
		File file2 = new File("file2.txt");
		try {
			Files.write(file1.toPath(), "abc".getBytes());
			Files.write(file2.toPath(), "ABC".getBytes());

			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, now);
			assertEquals(0L, id0);
			long id1 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(1L), new Attachment("filename11", file1, "text/plain"));
			assertEquals(1L, id1);
			long id2 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(2L), //
					new Attachment("filename21", file2, "text/html"),
					new Attachment("filename22", "DEF".getBytes(), "text/csv"));
			assertEquals(2L, id2);

			List<Long> list00 = mailQueue.listToSend(now.minusSeconds(1L));
			assertTrue(list00.isEmpty());

			List<Long> list01 = mailQueue.listToSend(now);
			assertEquals(asList(id0), list01);

			List<Long> list02 = mailQueue.listToSend(now.plusSeconds(1L));
			assertEquals(asList(id0), list02);

			List<Long> list10 = mailQueue.listToSend(now.plusMinutes(1L).minusSeconds(1L));
			assertEquals(asList(id0), list10);

			List<Long> list11 = mailQueue.listToSend(now.plusMinutes(1L));
			assertEquals(asList(id0, id1), list11);

			List<Long> list12 = mailQueue.listToSend(now.plusMinutes(1L).plusSeconds(1L));
			assertEquals(asList(id0, id1), list12);

			List<Long> list20 = mailQueue.listToSend(now.plusMinutes(2L).minusSeconds(1L));
			assertEquals(asList(id0, id1), list20);

			List<Long> list21 = mailQueue.listToSend(now.plusMinutes(2L));
			assertEquals(asList(id0, id1, id2), list21);

			List<Long> list22 = mailQueue.listToSend(now.plusMinutes(2L).plusSeconds(1L));
			assertEquals(asList(id0, id1, id2), list22);

		} finally {
			mailQueue.expire(0L);
			mailQueue.expire(1L);
			mailQueue.expire(2L);
			Files.deleteIfExists(file1.toPath());
			Files.deleteIfExists(file2.toPath());
		}
	}

	@Test
	public void testSend() throws IOException {
		File file1 = new File("file1.txt");
		File file2 = new File("file2.txt");
		try {
			Files.write(file1.toPath(), "abc".getBytes());
			Files.write(file2.toPath(), "ABC".getBytes());

			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, now);
			assertEquals(0L, id0);
			long id1 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(1L), new Attachment("filename11", file1, "text/plain"));
			assertEquals(1L, id1);
			long id2 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(2L), //
					new Attachment("filename21", file2, "text/html"),
					new Attachment("filename22", "DEF".getBytes(), "text/csv"));
			assertEquals(2L, id2);

			LocalDateTime sentAt = now.plusMinutes(5L);
			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {

				assertEquals(0, greenMail.getReceivedMessages().length);

				assertFalse(new File(queuedir, "0000000000000000000/sentAt.txt").exists());
				assertTrue(mailQueue.send(0L, sentAt));
				assertTrue(new File(queuedir, "0000000000000000000/sentAt.txt").exists());
				assertEquals(1, greenMail.getReceivedMessages().length);

				assertFalse(new File(queuedir, "0000000000000000001/sentAt.txt").exists());
				assertTrue(mailQueue.send(1L, sentAt.plusMinutes(1L)));
				assertTrue(new File(queuedir, "0000000000000000001/sentAt.txt").exists());
				assertEquals(2, greenMail.getReceivedMessages().length);

				assertFalse(new File(queuedir, "0000000000000000002/sentAt.txt").exists());
				assertTrue(mailQueue.send(2L, sentAt.plusMinutes(2L)));
				assertTrue(new File(queuedir, "0000000000000000002/sentAt.txt").exists());
				assertEquals(3, greenMail.getReceivedMessages().length);

				assertFalse(mailQueue.send(3L, sentAt));

			} finally {
				greenMail.stop();
			}
		} finally {
			mailQueue.expire(0L);
			mailQueue.expire(1L);
			mailQueue.expire(2L);
			Files.deleteIfExists(file1.toPath());
			Files.deleteIfExists(file2.toPath());
		}
	}

	@Test
	public void testListToExpire() throws IOException {
		File file1 = new File("file1.txt");
		File file2 = new File("file2.txt");
		try {
			Files.write(file1.toPath(), "abc".getBytes());
			Files.write(file2.toPath(), "ABC".getBytes());

			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, now);
			assertEquals(0L, id0);
			long id1 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(1L), new Attachment("filename11", file1, "text/plain"));
			assertEquals(1L, id1);
			long id2 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(2L), //
					new Attachment("filename21", file2, "text/html"),
					new Attachment("filename22", "DEF".getBytes(), "text/csv"));
			assertEquals(2L, id2);

			LocalDateTime sentAt = now.plusMinutes(5L);

			assertEquals(3, mailQueue.listToSend(now.plusMinutes(2L)).size());
			assertEquals(0, mailQueue.listToExpire(sentAt.plusMinutes(2L)).size());

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {
				assertEquals(0, greenMail.getReceivedMessages().length);
				assertTrue(mailQueue.send(0L, sentAt));
				assertTrue(mailQueue.send(1L, sentAt.plusMinutes(1L)));
				assertTrue(mailQueue.send(2L, sentAt.plusMinutes(2L)));
				assertEquals(3, greenMail.getReceivedMessages().length);
			} finally {
				greenMail.stop();
			}

			assertEquals(0, mailQueue.listToSend(now.plusMinutes(2L)).size());

			List<Long> list00 = mailQueue.listToExpire(sentAt.minusSeconds(1L));
			assertTrue(list00.isEmpty());

			List<Long> list01 = mailQueue.listToExpire(sentAt);
			assertEquals(asList(id0), list01);

			List<Long> list02 = mailQueue.listToExpire(sentAt.plusSeconds(1L));
			assertEquals(asList(id0), list02);

			List<Long> list10 = mailQueue.listToExpire(sentAt.plusMinutes(1L).minusSeconds(1L));
			assertEquals(asList(id0), list10);

			List<Long> list11 = mailQueue.listToExpire(sentAt.plusMinutes(1L));
			assertEquals(asList(id0, id1), list11);

			List<Long> list12 = mailQueue.listToExpire(sentAt.plusMinutes(1L).plusSeconds(1L));
			assertEquals(asList(id0, id1), list12);

			List<Long> list20 = mailQueue.listToExpire(sentAt.plusMinutes(2L).minusSeconds(1L));
			assertEquals(asList(id0, id1), list20);

			List<Long> list21 = mailQueue.listToExpire(sentAt.plusMinutes(2L));
			assertEquals(asList(id0, id1, id2), list21);

			List<Long> list22 = mailQueue.listToExpire(sentAt.plusMinutes(2L).plusSeconds(1L));
			assertEquals(asList(id0, id1, id2), list22);

		} finally {
			mailQueue.expire(0L);
			mailQueue.expire(1L);
			mailQueue.expire(2L);
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

			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, now);
			assertEquals(0L, id0);
			long id1 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(1L), new Attachment("filename11", file1, "text/plain"));
			assertEquals(1L, id1);
			long id2 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null, null,
					"subject", "text", null, //
					now.plusMinutes(2L), //
					new Attachment("filename21", file2, "text/html"),
					new Attachment("filename22", "DEF".getBytes(), "text/csv"));
			assertEquals(2L, id2);

			LocalDateTime sentAt = now.plusMinutes(5L);

			assertEquals(3, mailQueue.listToSend(now.plusMinutes(2L)).size());
			assertEquals(0, mailQueue.listToExpire(sentAt.plusMinutes(2L)).size());

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {
				assertEquals(0, greenMail.getReceivedMessages().length);
				assertTrue(mailQueue.send(0L, sentAt));
				assertTrue(mailQueue.send(1L, sentAt.plusMinutes(1L)));
				assertTrue(mailQueue.send(2L, sentAt.plusMinutes(2L)));
				assertEquals(3, greenMail.getReceivedMessages().length);
			} finally {
				greenMail.stop();
			}

			assertEquals(0, mailQueue.listToSend(now.plusMinutes(2L)).size());
			assertEquals(3, mailQueue.listToExpire(sentAt.plusMinutes(2L)).size());

			assertTrue(new File(queuedir, "0000000000000000000/message.yaml").exists());
			assertFalse(new File(queuedir, "0000000000000000000/filelist.yaml").exists());
			assertTrue(mailQueue.expire(0L));
			assertFalse(new File(queuedir, "0000000000000000000").exists());

			assertTrue(new File(queuedir, "0000000000000000001/message.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000001/filelist.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000001/0").exists());
			assertFalse(new File(queuedir, "0000000000000000001/1").exists());
			assertTrue(mailQueue.expire(1L));
			assertFalse(new File(queuedir, "0000000000000000001").exists());

			assertTrue(new File(queuedir, "0000000000000000002/message.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000002/filelist.yaml").exists());
			assertTrue(new File(queuedir, "0000000000000000002/0").exists());
			assertTrue(new File(queuedir, "0000000000000000002/1").exists());
			assertTrue(mailQueue.expire(2L));
			assertFalse(new File(queuedir, "0000000000000000002").exists());

			assertFalse(mailQueue.expire(3L));

		} finally {
			mailQueue.expire(0L);
			mailQueue.expire(1L);
			mailQueue.expire(2L);
			Files.deleteIfExists(file1.toPath());
			Files.deleteIfExists(file2.toPath());
		}
	}

	@Test
	public void testSend_00() throws MessagingException, IOException {
		try {
			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null,
					"replyTo@addr", "subject", "text", null, now);
			assertEquals(0L, id0);

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {

				assertEquals(0, greenMail.getReceivedMessages().length);
				assertTrue(mailQueue.send(0L, now));
				assertEquals(1, greenMail.getReceivedMessages().length);

				for (MimeMessage msg : greenMail.getReceivedMessages()) {
					assertEquals(new InternetAddress("from@addr"), msg.getFrom()[0]);
					assertEquals(new InternetAddress("to@addr"), msg.getRecipients(RecipientType.TO)[0]);
					assertNull(msg.getRecipients(RecipientType.CC));
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertEquals(new InternetAddress("replyTo@addr"), msg.getReplyTo()[0]);
					assertEquals("subject", msg.getSubject());
					assertEquals("text\r\n", msg.getContent());
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			mailQueue.expire(0L);
		}
	}

	@Test
	public void testSend_01() throws MessagingException, IOException {
		try {
			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", null, null, asList("cc@addr"), asList("bcc@addr"),
					null, null, "", null, now);
			assertEquals(0L, id0);

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {

				assertEquals(0, greenMail.getReceivedMessages().length);
				assertTrue(mailQueue.send(0L, now));
				assertEquals(2, greenMail.getReceivedMessages().length);

				for (MimeMessage msg : greenMail.getReceivedMessages()) {
					assertNull(msg.getFrom());
					assertNull(msg.getRecipients(RecipientType.TO));
					assertEquals(new InternetAddress("cc@addr"), msg.getRecipients(RecipientType.CC)[0]);
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertNull(msg.getReplyTo());
					assertNull(msg.getSubject());
					assertEquals("", msg.getContent());
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			mailQueue.expire(0L);
		}
	}

	@Test
	public void testSend_02() throws MessagingException, IOException {
		try {
			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null,
					"replyTo@addr", "subject", "text", "html", now);
			assertEquals(0L, id0);

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {

				assertEquals(0, greenMail.getReceivedMessages().length);
				assertTrue(mailQueue.send(0L, now));
				assertEquals(1, greenMail.getReceivedMessages().length);

				for (MimeMessage msg : greenMail.getReceivedMessages()) {
					assertEquals(new InternetAddress("from@addr"), msg.getFrom()[0]);
					assertEquals(new InternetAddress("to@addr"), msg.getRecipients(RecipientType.TO)[0]);
					assertNull(msg.getRecipients(RecipientType.CC));
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertEquals(new InternetAddress("replyTo@addr"), msg.getReplyTo()[0]);
					assertEquals("subject", msg.getSubject());

					MimeMultipart mpart = (MimeMultipart) msg.getContent();
					assertEquals(1, mpart.getCount());

					MimeMultipart p0 = (MimeMultipart) mpart.getBodyPart(0).getContent();
					assertEquals(1, p0.getCount());
					MimeMultipart p00 = (MimeMultipart) p0.getBodyPart(0).getContent();
					assertEquals(2, p00.getCount());
					assertEquals("text/plain; charset=UTF-8", p00.getBodyPart(0).getContentType());
					assertEquals("text", p00.getBodyPart(0).getContent());
					assertEquals("text/html;charset=UTF-8", p00.getBodyPart(1).getContentType());
					assertEquals("html", p00.getBodyPart(1).getContent());
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			mailQueue.expire(0L);
		}
	}

	@Test
	public void testSend_10() throws MessagingException, IOException {
		try {
			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null,
					"replyTo@addr", "subject", "text", null, //
					now, new Attachment("file10", "FILE10".getBytes(), "application/ctype10"));
			assertEquals(0L, id0);

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {

				assertEquals(0, greenMail.getReceivedMessages().length);
				assertTrue(mailQueue.send(0L, now));
				assertEquals(1, greenMail.getReceivedMessages().length);

				for (MimeMessage msg : greenMail.getReceivedMessages()) {
					assertEquals(new InternetAddress("from@addr"), msg.getFrom()[0]);
					assertEquals(new InternetAddress("to@addr"), msg.getRecipients(RecipientType.TO)[0]);
					assertNull(msg.getRecipients(RecipientType.CC));
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertEquals(new InternetAddress("replyTo@addr"), msg.getReplyTo()[0]);
					assertEquals("subject", msg.getSubject());

					MimeMultipart mpart = (MimeMultipart) msg.getContent();
					assertEquals(2, mpart.getCount());

					MimeMultipart p0 = (MimeMultipart) mpart.getBodyPart(0).getContent();
					assertEquals(1, p0.getCount());
					assertEquals("text/plain; charset=UTF-8", p0.getBodyPart(0).getContentType());
					assertEquals("text", p0.getBodyPart(0).getContent());

					assertEquals("application/ctype10; name=file10", mpart.getBodyPart(1).getContentType());
					assertEquals("FILE10", //
							new String(ByteStreams.toByteArray((InputStream) mpart.getBodyPart(1).getContent())));
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			mailQueue.expire(0L);
		}
	}

	@Test
	public void testSend_11() throws MessagingException, IOException {
		try {
			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", null, null, asList("cc@addr"), asList("bcc@addr"),
					null, null, "", null, //
					now, new Attachment("file11", "FILE11".getBytes(), "application/ctype11"));
			assertEquals(0L, id0);

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {

				assertEquals(0, greenMail.getReceivedMessages().length);
				assertTrue(mailQueue.send(0L, now));
				assertEquals(2, greenMail.getReceivedMessages().length);

				for (MimeMessage msg : greenMail.getReceivedMessages()) {
					assertNull(msg.getFrom());
					assertNull(msg.getRecipients(RecipientType.TO));
					assertEquals(new InternetAddress("cc@addr"), msg.getRecipients(RecipientType.CC)[0]);
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertNull(msg.getReplyTo());
					assertNull(msg.getSubject());

					MimeMultipart mpart = (MimeMultipart) msg.getContent();
					assertEquals(2, mpart.getCount());

					MimeMultipart p0 = (MimeMultipart) mpart.getBodyPart(0).getContent();
					assertEquals(1, p0.getCount());
					assertEquals("text/plain; charset=UTF-8", p0.getBodyPart(0).getContentType());
					assertEquals("", p0.getBodyPart(0).getContent());

					assertEquals("application/ctype11; name=file11", mpart.getBodyPart(1).getContentType());
					assertEquals("FILE11", //
							new String(ByteStreams.toByteArray((InputStream) mpart.getBodyPart(1).getContent())));
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			mailQueue.expire(0L);
		}
	}

	@Test
	public void testSend_12() throws MessagingException, IOException {
		try {
			LocalDateTime now = LocalDateTime.now();

			long id0 = mailQueue.enqueue("loginId", "messageName", "from@addr", asList("to@addr"), null, null,
					"replyTo@addr", "subject", "text", "html", //
					now, new Attachment("file12", "FILE12".getBytes(), "application/ctype12"));
			assertEquals(0L, id0);

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {

				assertEquals(0, greenMail.getReceivedMessages().length);
				assertTrue(mailQueue.send(0L, now));
				assertEquals(1, greenMail.getReceivedMessages().length);

				for (MimeMessage msg : greenMail.getReceivedMessages()) {
					assertEquals(new InternetAddress("from@addr"), msg.getFrom()[0]);
					assertEquals(new InternetAddress("to@addr"), msg.getRecipients(RecipientType.TO)[0]);
					assertNull(msg.getRecipients(RecipientType.CC));
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertEquals(new InternetAddress("replyTo@addr"), msg.getReplyTo()[0]);
					assertEquals("subject", msg.getSubject());

					MimeMultipart mpart = (MimeMultipart) msg.getContent();
					assertEquals(2, mpart.getCount());

					MimeMultipart p0 = (MimeMultipart) mpart.getBodyPart(0).getContent();
					assertEquals(1, p0.getCount());
					MimeMultipart p00 = (MimeMultipart) p0.getBodyPart(0).getContent();
					assertEquals(2, p00.getCount());
					assertEquals("text/plain; charset=UTF-8", p00.getBodyPart(0).getContentType());
					assertEquals("text", p00.getBodyPart(0).getContent());
					assertEquals("text/html;charset=UTF-8", p00.getBodyPart(1).getContentType());
					assertEquals("html", p00.getBodyPart(1).getContent());

					assertEquals("application/ctype12; name=file12", mpart.getBodyPart(1).getContentType());
					assertEquals("FILE12", //
							new String(ByteStreams.toByteArray((InputStream) mpart.getBodyPart(1).getContent())));
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			mailQueue.expire(0L);
		}
	}

}
