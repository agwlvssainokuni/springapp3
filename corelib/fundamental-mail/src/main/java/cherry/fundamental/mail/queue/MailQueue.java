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

import cherry.fundamental.mail.Attachment;

/**
 * メール送信機能。<br />
 * 送信するメールデータをキューに蓄積する機能、キューに蓄積されているメールデータの識別番号(long)のリストを取得する機能、メールデータの識別番号(long)を指定して当該メールデータを実際に送信する機能を提供する。<br />
 * 基本的な運用方法としては、キューを介して、非同期的にメールを送信する方式を想定する。<br />
 * 特殊な運用方法として、実際のメール送信処理まで実行する方法も提供する。
 */
public interface MailQueue {

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
	 * @param text 本文(プレーンテキスト)。
	 * @param html 本文(HTML)。
	 * @param scheduledAt 送信予定日時。
	 * @param attachments 添付ファイル。
	 * @return メールデータの識別番号。
	 */
	long enqueue(String loginId, String messageName, String from, List<String> to, List<String> cc, List<String> bcc,
			String replyTo, String subject, String text, String html, LocalDateTime scheduledAt,
			Attachment... attachments);

	/**
	 * キューに蓄積されているメールデータの識別番号のリストを取得する。<br />
	 * キューに蓄積する際に指定された送信予定日時が、当メソッドの引数に指定された日時以降のものを取得する。
	 *
	 * @param dtm 基準日時。
	 * @return メールデータの識別番号のリスト。
	 */
	List<Long> listToSend(LocalDateTime dtm);

	/**
	 * メールデータの識別番号を指定して当該メールデータを実際に送信する。
	 *
	 * @param messageId メールデータの識別番号。
	 * @param sentAt 送信日時。
	 * @return 指定された識別番号に該当するメールデータを送信したらtrue、該当するメールデータが存在しない場合はfalse。なお、該当するメールデータが存在したのに、メールの送信処理で異常が発生した場合は例外が発生する。
	 */
	boolean send(long messageId, LocalDateTime sentAt);

	/**
	 * 送信済みのメールデータの識別番号のリストを取得する。<br />
	 * 送信実績日時が、当メソッドの引数に指定された日時以前のものを取得する。
	 *
	 * @param dtm 基準日時。
	 * @return メールデータの識別番号のリスト。
	 */
	List<Long> listToExpire(LocalDateTime dtm);

	/**
	 * メールデータの識別番号を指定して当該メールデータを削除する。
	 *
	 * @param messageId メールデータの識別番号。
	 * @return 指定された識別番号に該当するメールデータを削除したらtrue、該当するメールデータが存在しない場合はfalse。なお、該当するメールデータが存在したのに、メールの削除処理で異常が発生した場合は例外が発生する。
	 */
	boolean expire(long messageId);

}
