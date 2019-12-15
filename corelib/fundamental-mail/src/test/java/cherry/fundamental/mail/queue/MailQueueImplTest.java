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
import static javax.mail.internet.InternetAddress.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.fundamental.mail.Attachment;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailQueueImplTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class MailQueueImplTest {

	@Autowired
	private AttachmentStore attachmentStore;

	private JavaMailSender mailSender;

	@Test
	public void testSendLater() {
		LocalDateTime now = LocalDateTime.now();
		MailQueue handler = create(now);

		long messageId = handler.sendLater("loginId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
				asList("bcc@addr"), "replyTo@addr", "subject", "body", now);
		assertEquals(0L, messageId);

		List<Long> list = handler.list(now);
		assertEquals(1, list.size());
		assertEquals(0L, list.get(0).longValue());

		ArgumentCaptor<SimpleMailMessage> message = ArgumentCaptor.forClass(SimpleMailMessage.class);
		doNothing().when(mailSender).send(message.capture());
		boolean first = handler.send(0L);

		assertTrue(first);
		assertEquals("from@addr", message.getValue().getFrom());
		assertEquals(1, message.getValue().getTo().length);
		assertEquals("to@addr", message.getValue().getTo()[0]);
		assertEquals(1, message.getValue().getCc().length);
		assertEquals("cc@addr", message.getValue().getCc()[0]);
		assertEquals(1, message.getValue().getBcc().length);
		assertEquals("bcc@addr", message.getValue().getBcc()[0]);
		assertEquals("replyTo@addr", message.getValue().getReplyTo());
		assertEquals("subject", message.getValue().getSubject());
		assertEquals("body", message.getValue().getText());

		boolean second = handler.send(0L);
		assertFalse(second);
	}

	@Test
	public void testSendNow() {
		LocalDateTime now = LocalDateTime.now();
		MailQueue handler = create(now);

		ArgumentCaptor<SimpleMailMessage> message = ArgumentCaptor.forClass(SimpleMailMessage.class);
		doNothing().when(mailSender).send(message.capture());

		long messageId = handler.sendNow("loginId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
				asList("bcc@addr"), "replyTo@addr", "subject", "body");
		assertEquals(0L, messageId);

		assertEquals("from@addr", message.getValue().getFrom());
		assertEquals(1, message.getValue().getTo().length);
		assertEquals("to@addr", message.getValue().getTo()[0]);
		assertEquals(1, message.getValue().getCc().length);
		assertEquals("cc@addr", message.getValue().getCc()[0]);
		assertEquals(1, message.getValue().getBcc().length);
		assertEquals("bcc@addr", message.getValue().getBcc()[0]);
		assertEquals("replyTo@addr", message.getValue().getReplyTo());
		assertEquals("subject", message.getValue().getSubject());
		assertEquals("body", message.getValue().getText());

		boolean result = handler.send(0L);
		assertFalse(result);
	}

	@Test
	public void testSendNowAttached() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		MailQueue handler = create(now);

		ArgumentCaptor<MimeMessagePreparator> preparator = ArgumentCaptor.forClass(MimeMessagePreparator.class);
		doNothing().when(mailSender).send(preparator.capture());

		long messageId = 0L;
		File file = File.createTempFile("test_", ".txt", new File("."));
		file.deleteOnExit();
		try {

			try (OutputStream out = new FileOutputStream(file)) {
				out.write("attach2".getBytes());
			}

			messageId = handler.sendNow("loginId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
					asList("bcc@addr"), "replyTo@addr", "subject", "body", //
					new Attachment("name0.txt", new ByteArrayInputStream("attach0".getBytes()), "text/plain"), //
					new Attachment("name1.bin", new ByteArrayInputStream("attach1".getBytes()),
							"application/octet-stream"), //
					new Attachment("name2.txt", file));

			Session session = Session.getDefaultInstance(new Properties());
			MimeMessage message = new MimeMessage(session);
			preparator.getValue().prepare(message);

			assertEquals(0L, messageId);
			assertEquals(1, message.getRecipients(RecipientType.TO).length);
			assertEquals(parse("to@addr")[0], message.getRecipients(RecipientType.TO)[0]);
			assertEquals(1, message.getRecipients(RecipientType.CC).length);
			assertEquals(parse("cc@addr")[0], message.getRecipients(RecipientType.CC)[0]);
			assertEquals(1, message.getRecipients(RecipientType.BCC).length);
			assertEquals(parse("bcc@addr")[0], message.getRecipients(RecipientType.BCC)[0]);
			assertEquals(1, message.getFrom().length);
			assertEquals(parse("from@addr")[0], message.getFrom()[0]);
			assertEquals(parse("replyTo@addr")[0], message.getReplyTo()[0]);
			assertEquals("subject", message.getSubject());

			MimeMultipart mm = (MimeMultipart) message.getContent();
			assertEquals(4, mm.getCount());
			assertEquals("body", ((MimeMultipart) mm.getBodyPart(0).getContent()).getBodyPart(0).getContent());

			assertEquals("name0.txt", mm.getBodyPart(1).getFileName());
			assertEquals("text/plain", mm.getBodyPart(1).getContentType());
			assertEquals("attach0", mm.getBodyPart(1).getContent());

			assertEquals("name1.bin", mm.getBodyPart(2).getDataHandler().getName());
			assertEquals("application/octet-stream", mm.getBodyPart(2).getDataHandler().getContentType());
			byte[] attach1;
			try (InputStream in = (InputStream) mm.getBodyPart(2).getDataHandler().getContent();
					ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				byte[] buff = new byte[4096];
				int len;
				while ((len = in.read(buff)) > 0) {
					out.write(buff, 0, len);
				}
				out.flush();
				attach1 = out.toByteArray();
			}
			assertEquals("attach1", new String(attach1));

			assertEquals("name2.txt", mm.getBodyPart(3).getFileName());
			assertEquals("text/plain", mm.getBodyPart(3).getContentType());
			assertEquals("attach2", mm.getBodyPart(3).getContent());
		} finally {
			attachmentStore.delete(messageId);
			file.delete();
		}
	}

	private MailQueue create(LocalDateTime now) {

		mailSender = mock(JavaMailSender.class);

		SimpleQueueStore queueStore = new SimpleQueueStore();
		return new MailQueueImpl(() -> now, queueStore, new AttachmentStore() {
			@Override
			public boolean save(long messageId, Attachment... attachments) throws UncheckedIOException {
				return attachmentStore.save(messageId, attachments);
			}

			@Override
			public Optional<List<AttachedEntry>> load(long messageId) throws UncheckedIOException {
				return attachmentStore.load(messageId);
			}

			@Override
			public void delete(long messageId) throws UncheckedIOException {
				// 何もしない。
				// 削除タイミングをずらす。
			}
		}, mailSender);
	}

}
