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
@SpringBootTest(classes = FreemarkerTemplateProcessorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class FreemarkerTemplateProcessorTest {

	@Autowired
	private TemplateProcessor templateProcessor;

	@Test
	public void testFullAddress() throws IOException {
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = templateProcessor.getMessage("FullAddress", asList("to@addr"), model);
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
		assertEquals("text", msg.getText());
		assertEquals("html", msg.getHtml());
	}

	@Test
	public void testEmptyTemplate() throws IOException {
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = templateProcessor.getMessage("Empty", null, model);
		assertNotNull(msg);
		assertNull(msg.getFrom());
		assertEquals(0, msg.getTo().size());
		assertNull(msg.getCc());
		assertNull(msg.getBcc());
		assertNull(msg.getReplyTo());
		assertNull(msg.getSubject());
		assertNull(msg.getText());
		assertNull(msg.getHtml());
	}

	@Test
	public void testProcessTemplate() throws IOException {
		Model model = new Model();
		model.setParam("PARAM");
		Message msg = templateProcessor.getMessage("Process", asList("to@addr"), model);
		assertNotNull(msg);
		assertEquals("from@addr", msg.getFrom());
		assertEquals(1, msg.getTo().size());
		assertEquals("to@addr", msg.getTo().get(0));
		assertNull(msg.getCc());
		assertNull(msg.getBcc());
		assertNull(msg.getReplyTo());
		assertEquals("subject is PARAM", msg.getSubject());
		assertEquals("text is PARAM", msg.getText());
		assertEquals("html is PARAM", msg.getHtml());
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
