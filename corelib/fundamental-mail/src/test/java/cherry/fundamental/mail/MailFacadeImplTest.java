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

package cherry.fundamental.mail;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.Test;

import cherry.fundamental.mail.message.MessageHandler;
import cherry.fundamental.mail.queue.MailQueue;

public class MailFacadeImplTest {

	private MessageHandler messageHandler;

	private MailQueue mailQueue;

	@Test
	public void testEvaluateByName() {
		LocalDateTime now = LocalDateTime.now();
		MailFacade mailFacade = create(now);

		Object model = new Object();
		mailFacade.evaluate("templateName", asList("to@addr"), model);
		verify(messageHandler).evaluate(eq("templateName"), eq(asList("to@addr")), eq(model));
	}

	@Test
	public void testEvaluateByValue() {
		LocalDateTime now = LocalDateTime.now();
		MailFacade mailFacade = create(now);

		Object model = new Object();
		mailFacade.evaluate("from@addr", asList("to@addr"), asList("cc@addr"), asList("bcc@addr"), "replyTo@addr",
				"subject", "body", model);
		verify(messageHandler).evaluate(eq("from@addr"), eq(asList("to@addr")), eq(asList("cc@addr")),
				eq(asList("bcc@addr")), eq("replyTo@addr"), eq("subject"), eq("body"), eq(model));
	}

	@Test
	public void testSend() {
		LocalDateTime now = LocalDateTime.now();
		MailFacade mailFacade = create(now);

		mailFacade.send("launcherId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
				asList("bcc@addr"), "replyTo@addr", "subject", "body");
		verify(mailQueue).sendLater(eq("launcherId"), eq("messageName"), eq("from@addr"), eq(asList("to@addr")),
				eq(asList("cc@addr")), eq(asList("bcc@addr")), eq("replyTo@addr"), eq("subject"), eq("body"), eq(now));
	}

	@Test
	public void testSendLater() {
		LocalDateTime now = LocalDateTime.now();
		MailFacade mailFacade = create(now);

		mailFacade.sendLater("launcherId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
				asList("bcc@addr"), "replyTo@addr", "subject", "body", now);
		verify(mailQueue).sendLater(eq("launcherId"), eq("messageName"), eq("from@addr"), eq(asList("to@addr")),
				eq(asList("cc@addr")), eq(asList("bcc@addr")), eq("replyTo@addr"), eq("subject"), eq("body"), eq(now));
	}

	@Test
	public void testSendNow() {
		LocalDateTime now = LocalDateTime.now();
		MailFacade mailFacade = create(now);

		mailFacade.sendNow("launcherId", "messageName", "from@addr", asList("to@addr"), asList("cc@addr"),
				asList("bcc@addr"), "replyTo@addr", "subject", "body");
		verify(mailQueue).sendNow(eq("launcherId"), eq("messageName"), eq("from@addr"), eq(asList("to@addr")),
				eq(asList("cc@addr")), eq(asList("bcc@addr")), eq("replyTo@addr"), eq("subject"), eq("body"));
	}

	private MailFacade create(LocalDateTime now) {
		messageHandler = mock(MessageHandler.class);
		mailQueue = mock(MailQueue.class);
		return new MailFacadeImpl(() -> now, messageHandler, mailQueue);
	}

}
