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

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * メール送信機能。<br />
 * 送信するメールデータの元とするメールテンプレートを保持する。以下の項目を持つ。
 * <ul>
 * <li>差出人 (From)</li>
 * <li>宛先 (To, Cc, Bcc)</li>
 * <li>返信先 (Reply-To)</li>
 * <li>件名 (Subject)</li>
 * <li>本文</li>
 * </ul>
 */
public class Template {

	/** 差出人 (From) を保持する。 */
	private String from;

	/** 宛先 (To) を保持する。 */
	private List<String> to;

	/** 宛先 (Cc) を保持する。 */
	private List<String> cc;

	/** 宛先 (Bcc) を保持する。 */
	private List<String> bcc;

	/** 返信先 (Reply-To) を保持する。 */
	private String replyTo;

	/** 件名 (Subject) を保持する。 */
	private String subject;

	/** 本文を保持する。 */
	private String body;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
