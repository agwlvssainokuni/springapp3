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
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;

import cherry.fundamental.bizcal.Bizcal;
import cherry.fundamental.mail.message.MessageHandler;
import cherry.fundamental.mail.message.MessageHandlerImpl;
import cherry.fundamental.mail.message.PassthroughTemplateLoader;
import cherry.fundamental.mail.message.SimpleTemplateStore;
import cherry.fundamental.mail.message.TemplateStore;
import cherry.fundamental.mail.queue.AttachmentStore;
import cherry.fundamental.mail.queue.AttachmentStoreImpl;
import cherry.fundamental.mail.queue.MailQueue;
import cherry.fundamental.mail.queue.MailQueueImpl;
import cherry.fundamental.mail.queue.QueueStore;
import cherry.fundamental.mail.queue.SimpleQueueStore;
import freemarker.template.TemplateExceptionHandler;

@Configuration
@PropertySource({ "classpath:cherry/fundamental/mail/Mail.properties" })
public class MailConfiguration {

	// メッセージ形成の系統。

	public static class TemplateStoreConfiguration {
		@Bean
		@ConditionalOnMissingBean(TemplateStore.class)
		public TemplateStore defaultTemplateStore() {
			return new SimpleTemplateStore(new HashMap<>());
		}
	}

	public static class MessageHandlerConfiguration {

		@Bean
		public MessageHandler messageHandler(TemplateStore templateStore) {
			return new MessageHandlerImpl(templateStore, configuration());
		}

		private freemarker.template.Configuration configuration() {
			freemarker.template.Configuration cfg = new freemarker.template.Configuration(
					freemarker.template.Configuration.VERSION_2_3_28);
			cfg.setLocalizedLookup(false);
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			cfg.setLogTemplateExceptions(false);
			cfg.setTemplateLoader(new PassthroughTemplateLoader());
			return cfg;
		}
	}

	// メール送信系。

	public static class QueueStoreConfiguration {
		@Bean
		@ConditionalOnMissingBean(QueueStore.class)
		public QueueStore defaultQueueStore() {
			return new SimpleQueueStore();
		}
	}

	@ConfigurationProperties(prefix = "fundamental.mail.attachment")
	public static class AttachmentStoreConfiguration {

		private File basedir;
		private String pattern;
		private String listname;

		@Bean
		public AttachmentStore attachmentStore() {
			return new AttachmentStoreImpl(basedir, pattern, listname);
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

	public static class MailQueueConfiguration {
		@Bean
		public MailQueue mailQueue(Bizcal bizcal, QueueStore queueStore, AttachmentStore attachmentStore,
				JavaMailSender mailSender) {
			return new MailQueueImpl(bizcal::now, queueStore, attachmentStore, mailSender);
		}
	}

	// 利用者向けAPI窓口。

	public static class MailFacadeConfiguration {
		@Bean
		public MailFacade mailFacade(Bizcal bizcal, MessageHandler messageHandler, MailQueue mailQueue) {
			return new MailFacadeImpl(bizcal::now, messageHandler, mailQueue);
		}
	}

	// 送信実態。

	@ConfigurationProperties(prefix = "fundamental.mail.sendmail")
	public static class SendMailServiceConfiguration {

		private double rateToSend = 2.0;
		private TimeUnit rateUnit = TimeUnit.SECONDS;

		@Bean
		public SendMailService sendMailService(Bizcal bizcal, MailQueue mailQueue) {
			return new SendMailServiceImpl(bizcal::now, mailQueue, rateToSend, rateUnit);
		}

		public void setRateToSend(double rateToSend) {
			this.rateToSend = rateToSend;
		}

		public void setRateUnit(TimeUnit rateUnit) {
			this.rateUnit = rateUnit;
		}
	}

}
