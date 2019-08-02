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

import java.time.LocalDateTime;
import java.util.List;

import cherry.fundamental.bizcal.Bizcal;

public class MailFacadeImpl implements MailFacade {

	private final Bizcal bizcal;

	private final MessageHandler messageHandler;

	private final MailQueue mailQueue;

	public MailFacadeImpl(Bizcal bizcal, MessageHandler messageHandler, MailQueue mailQueue) {
		this.bizcal = bizcal;
		this.messageHandler = messageHandler;
		this.mailQueue = mailQueue;
	}

	@Override
	public Message evaluate(String templateName, List<String> to, Object model) {
		return messageHandler.evaluate(templateName, to, model);
	}

	@Override
	public Message evaluate(String from, List<String> to, List<String> cc, List<String> bcc, String replyTo,
			String subject, String body, Object model) {
		return messageHandler.evaluate(from, to, cc, bcc, replyTo, subject, body, model);
	}

	@Override
	public long send(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String body, Attachment... attachments) {
		return mailQueue.sendLater(loginId, messageName, from, to, cc, bcc, replyTo, subject, body, bizcal.now(),
				attachments);
	}

	@Override
	public long sendLater(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String body, LocalDateTime scheduledAt,
			Attachment... attachments) {
		return mailQueue.sendLater(loginId, messageName, from, to, cc, bcc, replyTo, subject, body, scheduledAt,
				attachments);
	}

	@Override
	public long sendNow(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String body, Attachment... attachments) {
		return mailQueue.sendNow(loginId, messageName, from, to, cc, bcc, replyTo, subject, body, attachments);
	}

}
