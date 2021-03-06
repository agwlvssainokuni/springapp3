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

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * メール送信機能。<br />
 * 送信するメールデータを保持する。以下の項目を持つ。
 * <ul>
 * <li>差出人 (From)</li>
 * <li>宛先 (To, Cc, Bcc)</li>
 * <li>返信先 (Reply-To)</li>
 * <li>件名 (Subject)</li>
 * <li>本文(プレーンテキスト、HTML)</li>
 * </ul>
 */
public class Message {

	/** 差出人 (From) を保持する。 */
	private final String from;

	/** 宛先 (To) を保持する。 */
	private final List<String> to;

	/** 宛先 (Cc) を保持する。 */
	private final List<String> cc;

	/** 宛先 (Bcc) を保持する。 */
	private final List<String> bcc;

	/** 返信先 (Reply-To) を保持する。 */
	private final String replyTo;

	/** 件名 (Subject) を保持する。 */
	private final String subject;

	/** 本文(プレーンテキスト)を保持する。 */
	private final String text;

	/** 本文(HTML)を保持する。 */
	private final String html;

	public Message(String from, List<String> to, List<String> cc, List<String> bcc, String replyTo, String subject,
			String text, String html) {
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.replyTo = replyTo;
		this.subject = subject;
		this.text = text;
		this.html = html;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getFrom() {
		return from;
	}

	public List<String> getTo() {
		return to;
	}

	public List<String> getCc() {
		return cc;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public String getSubject() {
		return subject;
	}

	public String getText() {
		return text;
	}

	public String getHtml() {
		return html;
	}

}
