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

import org.springframework.transaction.support.TransactionOperations;

import cherry.fundamental.mail.queue.MailQueue;
import cherry.fundamental.mail.template.TemplateProcessor;

public class MailServiceImpl implements MailService {

	private final TransactionOperations txOps;

	private final Supplier<LocalDateTime> currentDateTime;

	private final TemplateProcessor templateProcessor;

	private final MailQueue mailQueue;

	public MailServiceImpl(TransactionOperations txOps, Supplier<LocalDateTime> currentDateTime,
			TemplateProcessor templateProcessor, MailQueue mailQueue) {
		this.txOps = txOps;
		this.currentDateTime = currentDateTime;
		this.templateProcessor = templateProcessor;
		this.mailQueue = mailQueue;
	}

	@Override
	public String process(String content, Object model) {
		return txOps.execute(status -> templateProcessor.process(content, model));
	}

	@Override
	public Message getMessage(String templateName, List<String> to, Object model) {
		return txOps.execute(status -> templateProcessor.getMessage(templateName, to, model));
	}

	@Override
	public long send(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String text, String html, Attachment... attachments) {
		return txOps.execute(status -> {
			LocalDateTime now = currentDateTime.get();
			return mailQueue.enqueue(loginId, messageName, from, to, cc, bcc, replyTo, subject, text, html, now,
					attachments);
		});
	}

	@Override
	public long sendLater(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String text, String html, LocalDateTime scheduledAt,
			Attachment... attachments) {
		return txOps.execute(status -> mailQueue.enqueue(loginId, messageName, from, to, cc, bcc, replyTo, subject,
				text, html, scheduledAt, attachments));
	}

	@Override
	public long sendNow(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String text, String html, Attachment... attachments) {
		return txOps.execute(status -> {
			LocalDateTime now = currentDateTime.get();
			long messageId = mailQueue.enqueue(loginId, messageName, from, to, cc, bcc, replyTo, subject, text, html,
					now, attachments);
			mailQueue.send(messageId, now);
			return messageId;
		});
	}

}
