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
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageTest {

	@Test
	public void testGetter() {

		Message message = new Message("from@addr", asList("to@addr"), asList("cc@addr"), asList("bcc@addr"),
				"replyTo@addr", "subject", "text", "html");

		assertEquals("from@addr", message.getFrom());
		assertEquals(1, message.getTo().size());
		assertEquals("to@addr", message.getTo().get(0));
		assertEquals(1, message.getCc().size());
		assertEquals("cc@addr", message.getCc().get(0));
		assertEquals(1, message.getBcc().size());
		assertEquals("bcc@addr", message.getBcc().get(0));
		assertEquals("replyTo@addr", message.getReplyTo());
		assertEquals("subject", message.getSubject());
		assertEquals("text", message.getText());
		assertEquals("html", message.getHtml());
	}

	@Test
	public void testToString() {
		Message message = new Message(null, null, null, null, null, null, null, null);
		assertEquals(
				"Message[bcc=<null>,cc=<null>,from=<null>,html=<null>,replyTo=<null>,subject=<null>,text=<null>,to=<null>]",
				message.toString());
	}

}
