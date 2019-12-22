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
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.support.TransactionOperations;

import cherry.fundamental.mail.Message;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class TemplateProcessorImpl implements TemplateProcessor {

	private final TransactionOperations txOps;

	private final TemplateStore templateStore;

	private final Configuration configuration;

	public TemplateProcessorImpl(TransactionOperations txOps, TemplateStore templateStore) {
		this.txOps = txOps;
		this.templateStore = templateStore;
		this.configuration = createConfiguration(Configuration.VERSION_2_3_28);
	}

	@Override
	public String evaluate(String template, Object model) {
		return doEvaluate(template, model);
	}

	@Override
	public Message evaluate(String templateName, List<String> to, Object model) {
		Template template = txOps.execute(status -> templateStore.get(templateName));
		List<String> toAddr = new ArrayList<>(to);
		if (template.getTo() != null) {
			toAddr.addAll(template.getTo());
		}
		return new Message(template.getFrom(), toAddr, template.getCc(), template.getBcc(), template.getReplyTo(),
				doEvaluate(template.getSubject(), model), doEvaluate(template.getBody(), model));
	}

	private String doEvaluate(String content, Object model) {
		try (StringWriter writer = new StringWriter()) {
			freemarker.template.Template template = new freemarker.template.Template(null, content, configuration);
			template.process(model, writer);
			return writer.toString();
		} catch (TemplateException ex) {
			throw new IllegalStateException(ex);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private Configuration createConfiguration(Version version) {
		Configuration cfg = new Configuration(version);
		cfg.setLocalizedLookup(false);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setTemplateLoader(new PassthroughTemplateLoader());
		return cfg;
	}

}
