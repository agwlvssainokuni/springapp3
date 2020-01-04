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

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.support.TransactionOperations;

import cherry.fundamental.mail.Attachment;

public class MailQueueImpl implements MailQueue {

	private final TransactionOperations txOps;

	private final QueueStore queueStore;

	private final AttachmentStore attachmentStore;

	private final JavaMailSender mailSender;

	public MailQueueImpl(TransactionOperations txOps, QueueStore queueStore, AttachmentStore attachmentStore,
			JavaMailSender mailSender) {
		this.txOps = txOps;
		this.queueStore = queueStore;
		this.attachmentStore = attachmentStore;
		this.mailSender = mailSender;
	}

	@Override
	public long enqueue(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String text, String html, LocalDateTime scheduledAt,
			Attachment... attachments) {
		return txOps.execute(status -> {
			long messageId = queueStore.save(loginId, messageName, scheduledAt, from, to, cc, bcc, replyTo, subject,
					text, html);
			attachmentStore.save(messageId, attachments);
			return messageId;
		});
	}

	@Override
	public List<Long> listToSend(LocalDateTime dtm) {
		return txOps.execute(status -> queueStore.list(dtm));
	}

	@Override
	public boolean send(long messageId, LocalDateTime sentAt) {
		return txOps.execute(status -> {
			QueuedEntry msg = queueStore.get(messageId);
			if (msg == null) {
				return false;
			}
			List<AttachedEntry> attached = attachmentStore.get(messageId);
			doSend(msg, attached);
			queueStore.finish(messageId, sentAt);
			return true;
		});
	}

	@Override
	public List<Long> listToExpire(LocalDateTime dtm) {
		return txOps.execute(status -> queueStore.listFinished(dtm));
	}

	@Override
	public boolean expire(long messageId) {
		return txOps.execute(status -> {
			boolean result = queueStore.delete(messageId);
			attachmentStore.delete(messageId);
			return result;
		});
	}

	private void doSend(QueuedEntry mail, List<AttachedEntry> attached) {
		if (mail.getHtml() == null && attached.isEmpty()) {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom(mail.getFrom());
			msg.setTo(toArray(mail.getTo()));
			msg.setCc(toArray(mail.getCc()));
			msg.setBcc(toArray(mail.getBcc()));
			msg.setReplyTo(mail.getReplyTo());
			msg.setSubject(mail.getSubject());
			msg.setText(mail.getText());
			mailSender.send(msg);
		} else {
			mailSender.send(msg -> {
				MimeMessageHelper helper = new MimeMessageHelper(msg, true);
				if (mail.getFrom() != null) {
					helper.setFrom(mail.getFrom());
				}
				toArrayAndSet(mail.getTo(), to -> helper.setTo(to));
				toArrayAndSet(mail.getCc(), cc -> helper.setCc(cc));
				toArrayAndSet(mail.getBcc(), bcc -> helper.setBcc(bcc));
				if (mail.getReplyTo() != null) {
					helper.setReplyTo(mail.getReplyTo());
				}
				if (mail.getSubject() != null) {
					helper.setSubject(mail.getSubject());
				}
				if (mail.getHtml() == null) {
					helper.setText(mail.getText());
				} else {
					helper.setText(mail.getText(), mail.getHtml());
				}
				for (AttachedEntry a : attached) {
					helper.addAttachment(a.getFilename(), () -> new FileInputStream(a.getFile()), a.getContentType());
				}
			});
		}
	}

	private String[] toArray(List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.toArray(new String[list.size()]);
	}

	private void toArrayAndSet(List<String> list, Setter setter) throws MessagingException {
		if (list == null || list.isEmpty()) {
			return;
		}
		setter.set(list.toArray(new String[list.size()]));
	}

	private static interface Setter {
		void set(String[] array) throws MessagingException;
	}

}
