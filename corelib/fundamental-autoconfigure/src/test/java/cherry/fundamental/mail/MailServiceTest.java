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

package cherry.fundamental.mail;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;

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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailServiceTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class MailServiceTest {

	private final File queuedir = new File("build/mailqueue");

	@Autowired
	private MailService mailService;

	@Autowired
	private BackendService backendService;

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
	public void testSend() throws MessagingException, IOException {
		try {

			long id0 = mailService.send("loginId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
					asList("bcc@addr"), "replyTo@addr", "subject", "text", "html", //
					new Attachment("filename", "CONTENT".getBytes(), "content/type"));
			assertEquals(0L, id0);

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {
				backendService.flushMail();
				MimeMessage[] ms = greenMail.getReceivedMessages();
				assertEquals(3, ms.length);
				for (MimeMessage msg : ms) {
					assertEquals(new InternetAddress("from@addr"), msg.getFrom()[0]);
					assertEquals(new InternetAddress("to@addr"), msg.getRecipients(RecipientType.TO)[0]);
					assertEquals(new InternetAddress("cc@addr"), msg.getRecipients(RecipientType.CC)[0]);
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertEquals(new InternetAddress("replyTo@addr"), msg.getReplyTo()[0]);
					assertEquals("subject", msg.getSubject());

					MimeMultipart mp = (MimeMultipart) msg.getContent();
					assertEquals(2, mp.getCount());

					MimeMultipart p0 = (MimeMultipart) mp.getBodyPart(0).getContent();
					assertEquals(1, p0.getCount());
					MimeMultipart p00 = (MimeMultipart) p0.getBodyPart(0).getContent();
					assertEquals(2, p00.getCount());
					assertEquals("text/plain; charset=UTF-8", p00.getBodyPart(0).getContentType());
					assertEquals("text", p00.getBodyPart(0).getContent());
					assertEquals("text/html;charset=UTF-8", p00.getBodyPart(1).getContentType());
					assertEquals("html", p00.getBodyPart(1).getContent());

					assertEquals("content/type; name=filename", mp.getBodyPart(1).getContentType());
					assertEquals("CONTENT", //
							new String(ByteStreams.toByteArray((InputStream) mp.getBodyPart(1).getContent())));
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			backendService.expireMail(LocalDateTime.now());
		}
	}

	@Test
	public void testSendLater() throws MessagingException, IOException {
		try {

			long id0 = mailService.sendLater("loginId", "messageName", "from@addr", asList("to@addr"),
					asList("cc@addr"), asList("bcc@addr"), "replyTo@addr", "subject", "text", "html",
					LocalDateTime.now(), //
					new Attachment("filename", "CONTENT".getBytes(), "content/type"));
			assertEquals(0L, id0);

			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();
			try {
				backendService.flushMail();
				MimeMessage[] ms = greenMail.getReceivedMessages();
				assertEquals(3, ms.length);
				for (MimeMessage msg : ms) {
					assertEquals(new InternetAddress("from@addr"), msg.getFrom()[0]);
					assertEquals(new InternetAddress("to@addr"), msg.getRecipients(RecipientType.TO)[0]);
					assertEquals(new InternetAddress("cc@addr"), msg.getRecipients(RecipientType.CC)[0]);
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertEquals(new InternetAddress("replyTo@addr"), msg.getReplyTo()[0]);
					assertEquals("subject", msg.getSubject());

					MimeMultipart mp = (MimeMultipart) msg.getContent();
					assertEquals(2, mp.getCount());

					MimeMultipart p0 = (MimeMultipart) mp.getBodyPart(0).getContent();
					assertEquals(1, p0.getCount());
					MimeMultipart p00 = (MimeMultipart) p0.getBodyPart(0).getContent();
					assertEquals(2, p00.getCount());
					assertEquals("text/plain; charset=UTF-8", p00.getBodyPart(0).getContentType());
					assertEquals("text", p00.getBodyPart(0).getContent());
					assertEquals("text/html;charset=UTF-8", p00.getBodyPart(1).getContentType());
					assertEquals("html", p00.getBodyPart(1).getContent());

					assertEquals("content/type; name=filename", mp.getBodyPart(1).getContentType());
					assertEquals("CONTENT", //
							new String(ByteStreams.toByteArray((InputStream) mp.getBodyPart(1).getContent())));
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			backendService.expireMail(LocalDateTime.now());
		}
	}

	@Test
	public void testSendNow() throws MessagingException, IOException {
		try {
			GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
			greenMail.start();

			long id0 = mailService.sendNow("loginId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
					asList("bcc@addr"), "replyTo@addr", "subject", "text", "html", //
					new Attachment("filename", "CONTENT".getBytes(), "content/type"));
			assertEquals(0L, id0);

			try {
				// backendService.flushMail();
				MimeMessage[] ms = greenMail.getReceivedMessages();
				assertEquals(3, ms.length);
				for (MimeMessage msg : ms) {
					assertEquals(new InternetAddress("from@addr"), msg.getFrom()[0]);
					assertEquals(new InternetAddress("to@addr"), msg.getRecipients(RecipientType.TO)[0]);
					assertEquals(new InternetAddress("cc@addr"), msg.getRecipients(RecipientType.CC)[0]);
					assertNull(msg.getRecipients(RecipientType.BCC));
					assertEquals(new InternetAddress("replyTo@addr"), msg.getReplyTo()[0]);
					assertEquals("subject", msg.getSubject());

					MimeMultipart mp = (MimeMultipart) msg.getContent();
					assertEquals(2, mp.getCount());

					MimeMultipart p0 = (MimeMultipart) mp.getBodyPart(0).getContent();
					assertEquals(1, p0.getCount());
					MimeMultipart p00 = (MimeMultipart) p0.getBodyPart(0).getContent();
					assertEquals(2, p00.getCount());
					assertEquals("text/plain; charset=UTF-8", p00.getBodyPart(0).getContentType());
					assertEquals("text", p00.getBodyPart(0).getContent());
					assertEquals("text/html;charset=UTF-8", p00.getBodyPart(1).getContentType());
					assertEquals("html", p00.getBodyPart(1).getContent());

					assertEquals("content/type; name=filename", mp.getBodyPart(1).getContentType());
					assertEquals("CONTENT", //
							new String(ByteStreams.toByteArray((InputStream) mp.getBodyPart(1).getContent())));
				}
			} finally {
				greenMail.stop();
			}
		} finally {
			backendService.expireMail(LocalDateTime.now());
		}
	}

}
