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

package cherry.fundamental.mail.queue;

import java.time.LocalDateTime;
import java.util.List;

/**
 * メール送信機能。<br />
 * 送信するメールデータのキューを管理する機能を表すインタフェース。
 */
public interface QueueStore {

	/**
	 * 送信するメールデータをキューに蓄積する。<br />
	 *
	 * @param loginId 当メソッドを呼出した利用者のログインID。
	 * @param messageName メールデータの分類名称。典型的には、メールテンプレート名称。
	 * @param scheduledAt 送信予定日時。
	 * @param from 差出人 (From) のメールアドレス。
	 * @param to 宛先 (To) のメールアドレス。
	 * @param cc 宛先 (Cc) のメールアドレス。
	 * @param bcc 宛先 (Bcc) のメールアドレス。
	 * @param replyTo 返信先 (Reply-To) のメールアドレス。
	 * @param subject 件名 (Subject)。
	 * @param body 本文。
	 * @return メールデータの識別番号。
	 */
	long create(String loginId, String messageName, LocalDateTime scheduledAt, String from, List<String> to,
			List<String> cc, List<String> bcc, String replyTo, String subject, String body);

	/**
	 * キューに蓄積されているメールデータの識別番号のリストを取得する。<br />
	 * キューに蓄積する際に指定された送信予定日時が、当メソッドの引数に指定された日時以降のものを取得する。
	 *
	 * @param dtm 基準日時。
	 * @return メールデータの識別番号のリスト。
	 */
	List<Long> list(LocalDateTime dtm);

	/**
	 * キューに蓄積されているメールデータを取得する。<br />
	 *
	 * @param messageId メールデータの識別番号。
	 * @return メールメッセージ。
	 */
	QueuedEntry get(long messageId);

	/**
	 * メールデータを処理済みに更新する。<br />
	 *
	 * @param messageId メールデータの識別番号。
	 */
	void finish(long messageId);

}
