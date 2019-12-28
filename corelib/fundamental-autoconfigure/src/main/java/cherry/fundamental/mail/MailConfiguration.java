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

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.support.TransactionOperations;

import cherry.fundamental.bizcal.Bizcal;
import cherry.fundamental.mail.queue.AttachmentStore;
import cherry.fundamental.mail.queue.FileAttachmentStore;
import cherry.fundamental.mail.queue.FileQueueStore;
import cherry.fundamental.mail.queue.MailQueue;
import cherry.fundamental.mail.queue.MailQueueImpl;
import cherry.fundamental.mail.queue.QueueStore;
import cherry.fundamental.mail.template.SimpleTemplateStore;
import cherry.fundamental.mail.template.TemplateProcessor;
import cherry.fundamental.mail.template.TemplateProcessorImpl;
import cherry.fundamental.mail.template.TemplateStore;

@Configuration
@PropertySource({ "classpath:cherry.properties" })
public class MailConfiguration {

	// メッセージ形成の系統。

	@ConditionalOnClass({ TemplateStore.class })
	public static class TemplateStoreCfg {
		@Bean
		@ConditionalOnMissingBean(TemplateStore.class)
		public TemplateStore defaultTemplateStore() {
			return new SimpleTemplateStore(new HashMap<>());
		}
	}

	@ConditionalOnClass({ TemplateProcessor.class })
	public static class TemplateProcessorCfg {
		@Bean
		public TemplateProcessor templateProcessor(TransactionOperations txOps, TemplateStore templateStore) {
			return new TemplateProcessorImpl(txOps, templateStore);
		}
	}

	// メール送信系。

	@ConditionalOnClass({ QueueStore.class })
	@ConfigurationProperties(prefix = "cherry.mail.queue")
	public static class QueueStoreCfg {

		private File basedir;
		private String counterFile;
		private String counterLock;
		private String pattern;
		private String messageFile;
		private String scheduleFile;
		private String sentFile;

		@Bean
		@ConditionalOnMissingBean(QueueStore.class)
		public QueueStore defaultQueueStore() {
			return new FileQueueStore(basedir, counterFile, counterLock, pattern, messageFile, scheduleFile, sentFile);
		}

		public void setBasedir(File basedir) {
			this.basedir = basedir;
		}

		public void setCounterFile(String counterFile) {
			this.counterFile = counterFile;
		}

		public void setCounterLock(String counterLock) {
			this.counterLock = counterLock;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		public void setMessageFile(String messageFile) {
			this.messageFile = messageFile;
		}

		public void setScheduleFile(String scheduleFile) {
			this.scheduleFile = scheduleFile;
		}

		public void setSentFile(String sentFile) {
			this.sentFile = sentFile;
		}
	}

	@ConditionalOnClass({ AttachmentStore.class })
	@ConfigurationProperties(prefix = "cherry.mail.attachment")
	public static class AttachmentStoreCfg {

		private File basedir;
		private String pattern;
		private String listname;

		@Bean
		@ConditionalOnMissingBean({ AttachmentStore.class })
		public AttachmentStore defaultAttachmentStore() {
			return new FileAttachmentStore(basedir, pattern, listname);
		}

		public void setBasedir(File basedir) {
			this.basedir = basedir;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		public void setListname(String listname) {
			this.listname = listname;
		}
	}

	@ConditionalOnClass({ MailQueue.class })
	public static class MailQueueCfg {
		@Bean
		public MailQueue mailQueue(TransactionOperations txOps, QueueStore queueStore, AttachmentStore attachmentStore,
				JavaMailSender mailSender) {
			return new MailQueueImpl(txOps, queueStore, attachmentStore, mailSender);
		}
	}

	// 利用者向けAPI窓口。

	@ConditionalOnClass({ MailFacade.class })
	public static class MailFacadeCfg {

		@Bean
		@ConditionalOnBean({ Bizcal.class })
		public MailFacade mailFacade(TransactionOperations txOps, Bizcal bizcal, TemplateProcessor templateProcessor,
				MailQueue mailQueue) {
			return new MailFacadeImpl(txOps, bizcal::now, templateProcessor, mailQueue);
		}

		@Bean
		@ConditionalOnMissingBean({ Bizcal.class })
		public MailFacade mailFacade(TransactionOperations txOps, TemplateProcessor templateProcessor,
				MailQueue mailQueue) {
			return new MailFacadeImpl(txOps, LocalDateTime::now, templateProcessor, mailQueue);
		}
	}

	// 送信実態。

	@ConditionalOnClass({ MailService.class })
	@ConfigurationProperties(prefix = "cherry.mail.sendmail")
	public static class MailServiceCfg {

		private double rateToSend = 2.0;
		private TimeUnit rateUnit = TimeUnit.SECONDS;

		@Bean
		@ConditionalOnBean({ Bizcal.class })
		public MailService mailService(Bizcal bizcal, MailQueue mailQueue) {
			return new MailServiceImpl(bizcal::now, mailQueue, rateToSend, rateUnit);
		}

		@Bean
		@ConditionalOnMissingBean({ Bizcal.class })
		public MailService mailService(MailQueue mailQueue) {
			return new MailServiceImpl(LocalDateTime::now, mailQueue, rateToSend, rateUnit);
		}

		public void setRateToSend(double rateToSend) {
			this.rateToSend = rateToSend;
		}

		public void setRateUnit(TimeUnit rateUnit) {
			this.rateUnit = rateUnit;
		}
	}

}
