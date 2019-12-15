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

import java.time.LocalDateTime;
import java.util.List;

import cherry.fundamental.mail.message.MessageHandler;

/**
 * メール送信機能。<br />
 * 業務アプリケーションが直接的に使用する機能を集めたインタフェース。
 */
public interface MailFacade {

	/**
	 * DBに保管されているメールテンプレートを元に、送信するメールデータを生成する。<br />
	 * {@link MessageHandler#evaluate(String, List, Object)}に相当する。
	 *
	 * @param templateName テンプレート名称。
	 * @param to 宛先 (To) のメールアドレス。
	 * @param model テンプレートに埋め込むデータ。
	 * @return 送信するメールデータ。
	 */
	Message evaluate(String templateName, List<String> to, Object model);

	/**
	 * DBに保管されていないメールテンプレートを元に、送信するメールデータを生成する。<br />
	 * {@link MessageHandler#evaluate(String, List, List, List, String, String, String, Object)}に相当する。
	 *
	 * @param from 差出人 (From) のメールアドレス。
	 * @param to 宛先 (To) のメールアドレス。
	 * @param cc 宛先 (Cc) のメールアドレス。
	 * @param bcc 宛先 (Bcc) のメールアドレス。
	 * @param replyTo 返信先 (Reply-To) のメールアドレス。
	 * @param subject 件名 (Subject) のテンプレート。
	 * @param body 本文のテンプレート。
	 * @param model テンプレートに埋め込むデータ。
	 * @return 送信するメールデータ。
	 */
	Message evaluate(String from, List<String> to, List<String> cc, List<String> bcc, String replyTo, String subject,
			String body, Object model);

	/**
	 * 送信するメールデータを、キューに蓄積する。<br />
	 *
	 * @param loginId 当メソッドを呼出した利用者のログインID。
	 * @param messageName メールデータの分類名称。典型的には、メールテンプレート名称。
	 * @param from 差出人 (From) のメールアドレス。
	 * @param to 宛先 (To) のメールアドレス。
	 * @param cc 宛先 (Cc) のメールアドレス。
	 * @param bcc 宛先 (Bcc) のメールアドレス。
	 * @param replyTo 返信先 (Reply-To) のメールアドレス。
	 * @param subject 件名 (Subject)。
	 * @param body 本文。
	 * @param attachments 添付ファイル。
	 * @return メールデータの識別番号。
	 */
	long send(String loginId, String messageName, String from, List<String> to, List<String> cc, List<String> bcc,
			String replyTo, String subject, String body, Attachment... attachments);

	/**
	 * 送信するメールデータを、キューに蓄積する。<br />
	 *
	 * @param loginId 当メソッドを呼出した利用者のログインID。
	 * @param messageName メールデータの分類名称。典型的には、メールテンプレート名称。
	 * @param from 差出人 (From) のメールアドレス。
	 * @param to 宛先 (To) のメールアドレス。
	 * @param cc 宛先 (Cc) のメールアドレス。
	 * @param bcc 宛先 (Bcc) のメールアドレス。
	 * @param replyTo 返信先 (Reply-To) のメールアドレス。
	 * @param subject 件名 (Subject)。
	 * @param body 本文。
	 * @param scheduledAt 送信予定日時。
	 * @param attachments 添付ファイル。
	 * @return メールデータの識別番号。
	 */
	long sendLater(String loginId, String messageName, String from, List<String> to, List<String> cc, List<String> bcc,
			String replyTo, String subject, String body, LocalDateTime scheduledAt, Attachment... attachments);

	/**
	 * メールを即時送信する。<br />
	 *
	 * @param loginId 当メソッドを呼出した利用者のログインID。
	 * @param messageName メールデータの分類名称。典型的には、メールテンプレート名称。
	 * @param from 差出人 (From) のメールアドレス。
	 * @param to 宛先 (To) のメールアドレス。
	 * @param cc 宛先 (Cc) のメールアドレス。
	 * @param bcc 宛先 (Bcc) のメールアドレス。
	 * @param replyTo 返信先 (Reply-To) のメールアドレス。
	 * @param subject 件名 (Subject)。
	 * @param body 本文。
	 * @param attachments 添付ファイル。
	 * @return メールデータの識別番号。
	 */
	long sendNow(String loginId, String messageName, String from, List<String> to, List<String> cc, List<String> bcc,
			String replyTo, String subject, String body, Attachment... attachments);

}
