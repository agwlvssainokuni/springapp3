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
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class MailDataHandlerImplTest {

	@Test
	public void testFullAddress() throws IOException {
		MailDataHandler handler = create("name", "from@addr", "other@addr", "cc@addr", "bcc@addr", "replyTo@addr",
				"subject", "body", Mode.NORMAL);
		Model model = new Model();
		model.setParam("PARAM");
		MailData mailData = handler.createMailData("name", "to@addr", model);
		assertNotNull(mailData);
		assertEquals("from@addr", mailData.getFromAddr());
		assertEquals(2, mailData.getToAddr().size());
		assertEquals("to@addr", mailData.getToAddr().get(0));
		assertEquals("other@addr", mailData.getToAddr().get(1));
		assertEquals(1, mailData.getCcAddr().size());
		assertEquals("cc@addr", mailData.getCcAddr().get(0));
		assertEquals(1, mailData.getBccAddr().size());
		assertEquals("bcc@addr", mailData.getBccAddr().get(0));
		assertEquals("replyTo@addr", mailData.getReplyToAddr());
		assertEquals("subject", mailData.getSubject());
		assertEquals("body", mailData.getBody());
	}

	@Test
	public void testEmptyTemplate() throws IOException {
		MailDataHandler handler = create("name", "from@addr", null, null, null, null, "", "", Mode.NORMAL);
		Model model = new Model();
		model.setParam("PARAM");
		MailData mailData = handler.createMailData("name", "to@addr", model);
		assertNotNull(mailData);
		assertEquals("from@addr", mailData.getFromAddr());
		assertEquals(1, mailData.getToAddr().size());
		assertEquals("to@addr", mailData.getToAddr().get(0));
		assertNull(mailData.getCcAddr());
		assertNull(mailData.getBccAddr());
		assertNull(mailData.getReplyToAddr());
		assertEquals("", mailData.getSubject());
		assertEquals("", mailData.getBody());
	}

	@Test
	public void testTemplateEvaluation() throws IOException {
		MailDataHandler handler = create("name", "from@addr", null, null, null, null, "param=${param}",
				"param is ${param}", Mode.NORMAL);
		Model model = new Model();
		model.setParam("PARAM");
		MailData mailData = handler.createMailData("name", "to@addr", model);
		assertNotNull(mailData);
		assertEquals("from@addr", mailData.getFromAddr());
		assertEquals(1, mailData.getToAddr().size());
		assertEquals("to@addr", mailData.getToAddr().get(0));
		assertNull(mailData.getCcAddr());
		assertNull(mailData.getBccAddr());
		assertNull(mailData.getReplyToAddr());
		assertEquals("param=PARAM", mailData.getSubject());
		assertEquals("param is PARAM", mailData.getBody());
	}

	@Test
	public void testTemplateEvaluationFalse() throws IOException {
		MailDataHandler handler = create("name", "from@addr", null, null, null, null, "param=${param}",
				"param is ${param}<#include \"dummy\">", Mode.MOCK_FALSE);
		Model model = new Model();
		model.setParam("PARAM");
		try {
			handler.createMailData("name", "to@addr", model);
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			assertTrue(ex.getCause() instanceof TemplateException);
			assertTrue(ex.getCause().getCause() instanceof IOException);
		}
	}

	static enum Mode {
		NORMAL, MOCK_FALSE
	}

	private MailDataHandler create(String name, String fromAddr, String toAddr, String ccAddr, String bccAddr,
			String replyToAddr, String subject, String body, Mode mode) throws IOException {

		MailData mailData = new MailData();
		mailData.setFromAddr(fromAddr);
		if (isNotEmpty(toAddr)) {
			mailData.setToAddr(asList(toAddr));
		}
		if (isNotEmpty(ccAddr)) {
			mailData.setCcAddr(asList(ccAddr));
		}
		if (isNotEmpty(bccAddr)) {
			mailData.setBccAddr(asList(bccAddr));
		}
		mailData.setReplyToAddr(replyToAddr);
		mailData.setSubject(subject);
		mailData.setBody(body);

		Map<String, MailData> map = new HashMap<>();
		map.put(name, mailData);

		SimpleTemplateStore templateStore = new SimpleTemplateStore();
		templateStore.setMailDataMap(map);

		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		configuration.setLocalizedLookup(false);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		configuration.setLogTemplateExceptions(false);
		switch (mode) {
		case NORMAL:
			configuration.setTemplateLoader(new PassthroughTemplateLoader());
			break;
		default:
			TemplateLoader loader = mock(TemplateLoader.class);
			when(loader.findTemplateSource(anyString())).thenThrow(new IOException());
			configuration.setTemplateLoader(loader);
			break;
		}

		MailDataHandlerImpl impl = new MailDataHandlerImpl();
		impl.setTemplateStore(templateStore);
		impl.setConfiguration(configuration);
		return impl;
	}

	public static class Model implements MailModel {

		private String param;

		public String getParam() {
			return param;
		}

		public void setParam(String param) {
			this.param = param;
		}
	}

}
