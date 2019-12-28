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

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.fundamental.mail.Message;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateProcessorImplTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class TemplateProcessorImplTest {

	@Autowired
	private TemplateProcessor templateProcessor;

	@Autowired
	private TemplateStore templateStore;

	@Test
	public void testFullAddress() throws IOException {
		create("name", "from@addr", "other@addr", "cc@addr", "bcc@addr", "replyTo@addr", "subject", "body");
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = templateProcessor.evaluate("name", asList("to@addr"), model);
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
		create("name", "from@addr", null, null, null, null, "", "");
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = templateProcessor.evaluate("name", asList("to@addr"), model);
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
		create("name", "from@addr", null, null, null, null, "param=${param}", "param is ${param}");
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = templateProcessor.evaluate("name", asList("to@addr"), model);
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

	private void create(String name, String fromAddr, String toAddr, String ccAddr, String bccAddr, String replyToAddr,
			String subject, String body) throws IOException {

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
