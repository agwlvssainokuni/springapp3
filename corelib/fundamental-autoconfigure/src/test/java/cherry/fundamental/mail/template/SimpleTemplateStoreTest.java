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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SimpleTemplateStoreTest {

	@Test
	public void testGetTemplate() {

		TemplateStore templateStore = create("name", "from@addr", "to@addr", "cc@addr", "bcc@addr", "subject", "body");

		Template template = templateStore.get("name");
		assertNotNull(template);
		assertEquals("from@addr", template.getFrom());
		assertEquals(1, template.getTo().size());
		assertEquals("to@addr", template.getTo().get(0));
		assertEquals(1, template.getCc().size());
		assertEquals("cc@addr", template.getCc().get(0));
		assertEquals(1, template.getBcc().size());
		assertEquals("bcc@addr", template.getBcc().get(0));
		assertEquals("subject", template.getSubject());
		assertEquals("body", template.getBody());

		assertNull(templateStore.get("none"));
	}

	@Test
	public void testPutTemplate() {

		TemplateStore templateStore = create("name", "from@addr", "to@addr", "cc@addr", "bcc@addr", "subject", "body");

		Template template = new Template();
		template.setFrom("from2@addr");
		template.setTo(asList("to2@addr"));
		template.setCc(asList("cc2@addr"));
		template.setBcc(asList("bcc2@addr"));
		template.setSubject("subject2");
		template.setBody("body2");

		templateStore.put("name2", template);

		Template template2 = templateStore.get("name2");
		assertNotNull(template2);
		assertEquals("from2@addr", template2.getFrom());
		assertEquals(1, template2.getTo().size());
		assertEquals("to2@addr", template2.getTo().get(0));
		assertEquals(1, template2.getCc().size());
		assertEquals("cc2@addr", template2.getCc().get(0));
		assertEquals(1, template2.getBcc().size());
		assertEquals("bcc2@addr", template2.getBcc().get(0));
		assertEquals("subject2", template2.getSubject());
		assertEquals("body2", template2.getBody());
	}

	private TemplateStore create(String name, String fromAddr, String toAddr, String ccAddr, String bccAddr,
			String subject, String body) {

		Template template = new Template();
		template.setFrom(fromAddr);
		template.setTo(asList(toAddr));
		template.setCc(asList(ccAddr));
		template.setBcc(asList(bccAddr));
		template.setSubject(subject);
		template.setBody(body);

		Map<String, Template> map = new HashMap<>();
		map.put(name, template);

		return new SimpleTemplateStore(map);
	}

}
