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
import java.util.function.Supplier;

import org.springframework.transaction.annotation.Transactional;

import cherry.fundamental.mail.queue.MailQueue;
import cherry.fundamental.mail.template.TemplateHandler;

public class MailFacadeImpl implements MailFacade {

	private final Supplier<LocalDateTime> currentDateTime;

	private final TemplateHandler templateHandler;

	private final MailQueue mailQueue;

	public MailFacadeImpl(Supplier<LocalDateTime> currentDateTime, TemplateHandler templateHandler,
			MailQueue mailQueue) {
		this.currentDateTime = currentDateTime;
		this.templateHandler = templateHandler;
		this.mailQueue = mailQueue;
	}

	@Transactional
	@Override
	public Message evaluate(String templateName, List<String> to, Object model) {
		return templateHandler.evaluate(templateName, to, model);
	}

	@Transactional
	@Override
	public Message evaluate(String from, List<String> to, List<String> cc, List<String> bcc, String replyTo,
			String subject, String body, Object model) {
		return templateHandler.evaluate(from, to, cc, bcc, replyTo, subject, body, model);
	}

	@Transactional
	@Override
	public long send(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String body, Attachment... attachments) {
		return mailQueue.enqueue(loginId, messageName, from, to, cc, bcc, replyTo, subject, body, currentDateTime.get(),
				attachments);
	}

	@Transactional
	@Override
	public long sendLater(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String body, LocalDateTime scheduledAt,
			Attachment... attachments) {
		return mailQueue.enqueue(loginId, messageName, from, to, cc, bcc, replyTo, subject, body, scheduledAt,
				attachments);
	}

	@Transactional
	@Override
	public long sendNow(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String body, Attachment... attachments) {
		long messageId = mailQueue.enqueue(loginId, messageName, from, to, cc, bcc, replyTo, subject, body,
				currentDateTime.get(), attachments);
		mailQueue.send(messageId);
		return messageId;
	}

}
