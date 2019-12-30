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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import cherry.fundamental.mail.Message;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

public class FreemarkerTemplateProcessor implements TemplateProcessor {

	private final File basedir;

	private final String addressFile;

	private final String subjectFile;

	private final String textFile;

	private final String htmlFile;

	private final Configuration configuration;

	public FreemarkerTemplateProcessor(File basedir, String addressFile, String subjectFile, String textFile,
			String htmlFile) throws IOException {
		this.basedir = basedir;
		this.addressFile = addressFile;
		this.subjectFile = subjectFile;
		this.textFile = textFile;
		this.htmlFile = htmlFile;
		this.configuration = createConfiguration(this.basedir, Configuration.VERSION_2_3_28);
	}

	@Override
	public String process(String content, Object model) {
		if (content == null) {
			return null;
		}
		if (content.length() <= 0) {
			return "";
		}
		freemarker.template.Template template;
		try {
			template = new Template(null, content, configuration);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return doProcess(template, model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Message getMessage(String templateName, List<String> to, Object model) {
		Map<String, ?> addr = getAddrMap(templateName);
		String fromAddr = (String) addr.get("from");
		List<String> toAddr = new ArrayList<>();
		if (to != null) {
			toAddr.addAll(to);
		}
		if (addr.containsKey("to")) {
			toAddr.addAll((List<String>) addr.get("to"));
		}
		List<String> ccAddr = (List<String>) addr.get("cc");
		List<String> bccAddr = (List<String>) addr.get("bcc");
		String replyToAddr = (String) addr.get("replyTo");
		String subject = doProcess(getTemplate(templateName, subjectFile), model);
		String text = doProcess(getTemplate(templateName, textFile), model);
		String html = doProcess(getTemplate(templateName, htmlFile), model);
		return new Message(fromAddr, toAddr, ccAddr, bccAddr, replyToAddr, subject, text, html);
	}

	private Map<String, ?> getAddrMap(String templateName) {
		File file = new File(new File(basedir, templateName), addressFile);
		if (!file.isFile()) {
			return Collections.emptyMap();
		}
		try (InputStream in = new FileInputStream(file); Reader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
			Yaml yaml = new Yaml();
			return yaml.load(r);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private Template getTemplate(String templateName, String filename) {
		try {
			return configuration.getTemplate(new StringBuilder(templateName).append("/").append(filename).toString());
		} catch (TemplateNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private String doProcess(Template template, Object model) {
		if (template == null) {
			return null;
		}
		try (StringWriter writer = new StringWriter()) {
			template.process(model, writer);
			return writer.toString();
		} catch (TemplateException ex) {
			throw new IllegalStateException(ex);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private Configuration createConfiguration(File templatedir, Version version) throws IOException {
		Configuration cfg = new Configuration(version);
		cfg.setLocalizedLookup(false);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setDirectoryForTemplateLoading(templatedir);
		cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
		return cfg;
	}

}
