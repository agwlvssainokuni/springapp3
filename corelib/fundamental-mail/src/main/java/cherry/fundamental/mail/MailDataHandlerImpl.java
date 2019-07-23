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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MailDataHandlerImpl implements MailDataHandler {

	private TemplateStore templateStore;

	private Configuration configuration;

	public void setTemplateStore(TemplateStore templateStore) {
		this.templateStore = templateStore;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Transactional(readOnly = true)
	@Override
	public MailData createMailData(String templateName, String to, MailModel mailModel) {

		MailData template = templateStore.getTemplate(templateName);

		List<String> toAddr = new ArrayList<>();
		toAddr.add(to);
		if (template.getToAddr() != null) {
			toAddr.addAll(template.getToAddr());
		}

		return createMailData(template.getFromAddr(), toAddr, template.getCcAddr(), template.getBccAddr(),
				template.getReplyToAddr(), template.getSubject(), template.getBody(), mailModel);
	}

	@Override
	public MailData createMailData(String fromAddr, List<String> toAddr, List<String> ccAddr, List<String> bccAddr,
			String replyToAddr, String subject, String body, MailModel mailModel) {

		MailData message = new MailData();
		message.setFromAddr(fromAddr);
		message.setToAddr(toAddr);
		message.setCcAddr(ccAddr);
		message.setBccAddr(bccAddr);
		message.setReplyToAddr(replyToAddr);

		message.setSubject(evaluate(subject, mailModel));
		message.setBody(evaluate(body, mailModel));

		return message;
	}

	private String evaluate(String content, MailModel mailModel) {
		try (StringWriter writer = new StringWriter()) {
			Template template = new Template(null, content, configuration);
			template.process(mailModel, writer);
			return writer.toString();
		} catch (TemplateException | IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
