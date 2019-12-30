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

import java.util.List;

import cherry.fundamental.mail.Message;

/**
 * メール送信機能。<br />
 * メールテンプレートを元に、送信するメールデータを生成する機能を提供する。<br />
 * 基本的な運用方法としては、メールテンプレートはストレージに保管されていることを想定し、その名称 (テンプレート名称) によりメールテンプレートを識別する。送信するメールデータを生成する際は、テンプレート名称、宛先 (To)
 * のメールアドレス、テンプレートに埋め込むデータを指定する。<br />
 * 特殊な運用方法として、ストレージに保管されていないメールテンプレートのデータを元に、送信するメールデータを生成することにも対応する。
 */
public interface TemplateProcessor {

	/**
	 * 文字列をテンプレートとして処理する。
	 *
	 * @param content テンプレート文字列。
	 * @param model テンプレートに埋め込むデータ。
	 * @return 結果文字列。
	 */
	String process(String content, Object model);

	/**
	 * ストレージに保管されているメールテンプレートを元に、送信するメールデータを生成する。<br />
	 *
	 * @param templateName テンプレート名称。
	 * @param to 宛先 (To) のメールアドレス。
	 * @param model テンプレートに埋め込むデータ。
	 * @return 送信するメールデータ。
	 */
	Message getMessage(String templateName, List<String> to, Object model);

}
