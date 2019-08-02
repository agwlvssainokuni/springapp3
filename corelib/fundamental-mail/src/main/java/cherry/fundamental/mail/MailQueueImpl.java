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

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import cherry.fundamental.bizcal.Bizcal;

public class MailQueueImpl implements MailQueue {

	private final Bizcal bizcal;

	private final QueueStore queueStore;

	private final AttachmentStore attachmentStore;

	private final JavaMailSender mailSender;

	public MailQueueImpl(Bizcal bizcal, QueueStore queueStore, AttachmentStore attachmentStore,
			JavaMailSender mailSender) {
		this.bizcal = bizcal;
		this.queueStore = queueStore;
		this.attachmentStore = attachmentStore;
		this.mailSender = mailSender;
	}

	@Transactional
	@Override
	public long sendLater(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String body, LocalDateTime scheduledAt,
			Attachment... attachments) {
		long messageId = queueStore.create(loginId, messageName, scheduledAt, from, to, cc, bcc, replyTo, subject,
				body);
		attachmentStore.save(messageId, attachments);
		return messageId;
	}

	@Transactional
	@Override
	public long sendNow(String loginId, String messageName, String from, List<String> to, List<String> cc,
			List<String> bcc, String replyTo, String subject, String body, Attachment... attachments) {
		LocalDateTime now = bizcal.now();
		long messageId = queueStore.create(loginId, messageName, now, from, to, cc, bcc, replyTo, subject, body);
		attachmentStore.save(messageId, attachments);
		QueuedEntry msg = queueStore.get(messageId);
		Optional<List<AttachedEntry>> attached = attachmentStore.load(messageId);
		queueStore.finish(messageId);
		doSend(msg, attached);
		attachmentStore.delete(messageId);
		return messageId;
	}

	@Transactional
	@Override
	public List<Long> list(LocalDateTime dtm) {
		return queueStore.list(dtm);
	}

	@Transactional
	@Override
	public boolean send(long messageId) {
		QueuedEntry msg = queueStore.get(messageId);
		if (msg == null) {
			return false;
		}
		Optional<List<AttachedEntry>> attached = attachmentStore.load(messageId);
		queueStore.finish(messageId);
		doSend(msg, attached);
		attachmentStore.delete(messageId);
		return true;
	}

	private void doSend(QueuedEntry mail, Optional<List<AttachedEntry>> attached) {
		if (!attached.isPresent()) {
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
				helper.setFrom(mail.getFrom());
				toArrayAndSet(mail.getTo(), to -> helper.setTo(to));
				toArrayAndSet(mail.getCc(), cc -> helper.setCc(cc));
				toArrayAndSet(mail.getBcc(), bcc -> helper.setBcc(bcc));
				helper.setReplyTo(mail.getReplyTo());
				helper.setSubject(mail.getSubject());
				helper.setText(mail.getText());
				for (AttachedEntry a : attached.get()) {
					if (a.isStream()) {
						helper.addAttachment(a.getFilename(), () -> new FileInputStream(a.getFile()),
								a.getContentType());
					} else {
						helper.addAttachment(a.getFilename(), a.getFile());
					}
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
