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

import org.junit.Test;

public class TemplateTest {

	@Test
	public void testSetterGetter() {

		Template template = new Template();
		template.setFrom("from@addr");
		template.setTo(asList("to@addr"));
		template.setCc(asList("cc@addr"));
		template.setBcc(asList("bcc@addr"));
		template.setReplyTo("replyTo@addr");
		template.setSubject("subject");
		template.setBody("body");

		assertEquals("from@addr", template.getFrom());
		assertEquals(1, template.getTo().size());
		assertEquals("to@addr", template.getTo().get(0));
		assertEquals(1, template.getCc().size());
		assertEquals("cc@addr", template.getCc().get(0));
		assertEquals(1, template.getBcc().size());
		assertEquals("bcc@addr", template.getBcc().get(0));
		assertEquals("replyTo@addr", template.getReplyTo());
		assertEquals("subject", template.getSubject());
		assertEquals("body", template.getBody());
	}

	@Test
	public void testToString() {
		Template template = new Template();
		assertEquals("Template[from=<null>,to=<null>,cc=<null>,bcc=<null>,replyTo=<null>,subject=<null>,body=<null>]",
				template.toString());
	}

}
