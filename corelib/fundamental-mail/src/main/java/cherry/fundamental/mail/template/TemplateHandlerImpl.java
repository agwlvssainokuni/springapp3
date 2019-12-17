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

package cherry.fundamental.mail.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import cherry.fundamental.mail.Message;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class TemplateHandlerImpl implements TemplateHandler {

	private final TemplateStore templateStore;

	private final Configuration configuration;

	public TemplateHandlerImpl(TemplateStore templateStore, Configuration configuration) {
		this.templateStore = templateStore;
		this.configuration = configuration;
	}

	@Override
	public Message evaluate(String templateName, List<String> to, Object model) {
		Template template = templateStore.get(templateName);
		List<String> toAddr = new ArrayList<>(to);
		if (template.getTo() != null) {
			toAddr.addAll(template.getTo());
		}
		return evaluate(template.getFrom(), toAddr, template.getCc(), template.getBcc(), template.getReplyTo(),
				template.getSubject(), template.getBody(), model);
	}

	@Override
	public Message evaluate(String from, List<String> to, List<String> cc, List<String> bcc, String replyTo,
			String subject, String body, Object model) {
		return new Message(from, to, cc, bcc, replyTo, doEvaluate(subject, model), doEvaluate(body, model));
	}

	private String doEvaluate(String content, Object model) {
		try (StringWriter writer = new StringWriter()) {
			freemarker.template.Template template = new freemarker.template.Template(null, content, configuration);
			template.process(model, writer);
			return writer.toString();
		} catch (TemplateException | IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
