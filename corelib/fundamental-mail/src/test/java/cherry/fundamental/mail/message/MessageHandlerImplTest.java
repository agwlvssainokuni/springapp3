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

package cherry.fundamental.mail.message;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.fundamental.mail.Message;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessageHandlerImplTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class MessageHandlerImplTest {

	@Autowired
	private MessageHandler messageHandler;

	@Autowired
	private TemplateStore templateStore;

	@Test
	public void testFullAddress() throws IOException {
		MessageHandler handler = create("name", "from@addr", "other@addr", "cc@addr", "bcc@addr", "replyTo@addr",
				"subject", "body", Mode.NORMAL);
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = handler.evaluate("name", asList("to@addr"), model);
		assertNotNull(msg);
		assertEquals("from@addr", msg.getFrom());
		assertEquals(2, msg.getTo().size());
		assertEquals("to@addr", msg.getTo().get(0));
		assertEquals("other@addr", msg.getTo().get(1));
		assertEquals(1, msg.getCc().size());
		assertEquals("cc@addr", msg.getCc().get(0));
		assertEquals(1, msg.getBcc().size());
		assertEquals("bcc@addr", msg.getBcc().get(0));
		assertEquals("replyTo@addr", msg.getReplyTo());
		assertEquals("subject", msg.getSubject());
		assertEquals("body", msg.getBody());
	}

	@Test
	public void testEmptyTemplate() throws IOException {
		MessageHandler handler = create("name", "from@addr", null, null, null, null, "", "", Mode.NORMAL);
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = handler.evaluate("name", asList("to@addr"), model);
		assertNotNull(msg);
		assertEquals("from@addr", msg.getFrom());
		assertEquals(1, msg.getTo().size());
		assertEquals("to@addr", msg.getTo().get(0));
		assertNull(msg.getCc());
		assertNull(msg.getBcc());
		assertNull(msg.getReplyTo());
		assertEquals("", msg.getSubject());
		assertEquals("", msg.getBody());
	}

	@Test
	public void testTemplateEvaluation() throws IOException {
		MessageHandler handler = create("name", "from@addr", null, null, null, null, "param=${param}",
				"param is ${param}", Mode.NORMAL);
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = handler.evaluate("name", asList("to@addr"), model);
		assertNotNull(msg);
		assertEquals("from@addr", msg.getFrom());
		assertEquals(1, msg.getTo().size());
		assertEquals("to@addr", msg.getTo().get(0));
		assertNull(msg.getCc());
		assertNull(msg.getBcc());
		assertNull(msg.getReplyTo());
		assertEquals("param=PARAM", msg.getSubject());
		assertEquals("param is PARAM", msg.getBody());
	}

	@Test
	public void testTemplateEvaluationFalse() throws IOException {
		MessageHandler handler = create("name", "from@addr", null, null, null, null, "param=${param}",
				"param is ${param}<#include \"dummy\">", Mode.MOCK_FALSE);
		Model model = new Model();
		model.setParam("PARAM");
		try {
			handler.evaluate("name", asList("to@addr"), model);
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertTrue(ex.getCause() instanceof TemplateException);
			assertTrue(ex.getCause().getCause() instanceof IOException);
		}
	}

	static enum Mode {
		NORMAL, MOCK_FALSE
	}

	private MessageHandler create(String name, String fromAddr, String toAddr, String ccAddr, String bccAddr,
			String replyToAddr, String subject, String body, Mode mode) throws IOException {

		Template template = new Template();
		template.setFrom(fromAddr);
		if (isNotEmpty(toAddr)) {
			template.setTo(asList(toAddr));
		}
		if (isNotEmpty(ccAddr)) {
			template.setCc(asList(ccAddr));
		}
		if (isNotEmpty(bccAddr)) {
			template.setBcc(asList(bccAddr));
		}
		template.setReplyTo(replyToAddr);
		template.setSubject(subject);
		template.setBody(body);

		templateStore.put(name, template);

		switch (mode) {
		case NORMAL:
			return messageHandler;
		default:
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
			configuration.setLocalizedLookup(false);
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			configuration.setLogTemplateExceptions(false);
			configuration.setTemplateLoader(new PassthroughTemplateLoader());
			TemplateLoader loader = mock(TemplateLoader.class);
			when(loader.findTemplateSource(anyString())).thenThrow(new IOException());
			configuration.setTemplateLoader(loader);
			return new MessageHandlerImpl(templateStore, configuration);
		}

	}

	public static class Model {

		private String param;

		public String getParam() {
			return param;
		}

		public void setParam(String param) {
			this.param = param;
		}
	}

}
