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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailFacadeTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class MailFacadeTest {

	@Autowired
	private MailFacade mailFacade;

	@Autowired
	private MailService mailService;

	@Test
	public void test() throws MessagingException, IOException {
		GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
		greenMail.start();
		try {

			mailFacade.send("loginId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
					asList("bcc@addr"), "replyTo@addr", "subject", "body");
			mailService.send();

			assertTrue(greenMail.waitForIncomingEmail(5000, 1));
			MimeMessage[] ms = greenMail.getReceivedMessages();
			assertEquals(3, ms.length);
			for (MimeMessage msg : ms) {
				assertEquals(new InternetAddress("from@addr"), msg.getFrom()[0]);
				assertEquals(new InternetAddress("to@addr"), msg.getRecipients(RecipientType.TO)[0]);
				assertEquals(new InternetAddress("cc@addr"), msg.getRecipients(RecipientType.CC)[0]);
				assertNull(msg.getRecipients(RecipientType.BCC));
				assertEquals(new InternetAddress("replyTo@addr"), msg.getReplyTo()[0]);
				assertEquals("subject", msg.getSubject());
				assertEquals("body\r\n", msg.getContent());
			}
		} finally {
			LocalDateTime now = LocalDateTime.now();
			mailService.expire(now);
			greenMail.stop();
		}
	}

}
