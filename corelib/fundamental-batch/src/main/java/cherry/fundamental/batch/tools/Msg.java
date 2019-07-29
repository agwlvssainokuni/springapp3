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

package cherry.fundamental.batch.tools;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.ResourceBundleMessageSource;

import cherry.fundamental.batch.ExitStatus;

/**
 * バッチプログラムを起動する際に出力するログの文言を解決する機能を提供する。
 */
class Msg {

	/** 文言定義を保持する。 */
	private MessageSource msgSrc = createMessageSource();

	/**
	 * ログの文言を解決する。
	 * 
	 * @param code ログの文言の識別名。
	 * @param batchId バッチプログラムの識別名。
	 * @return ログの文言。
	 */
	public String resolve(String code, String batchId) {
		MessageSourceResolvable name = getResolvable(batchId);
		MessageSourceResolvable msg = getResolvable(code, name);
		return msgSrc.getMessage(msg, null);
	}

	/**
	 * ログの文言を解決する。
	 * 
	 * @param code ログの文言の識別名。
	 * @param batchId バッチプログラムの識別名。
	 * @param status 終了ステータス。
	 * @return ログの文言。
	 */
	public String resolve(String code, String batchId, ExitStatus status) {
		MessageSourceResolvable name = getResolvable(batchId, batchId);
		MessageSourceResolvable msg = getResolvable(code, name, status.name());
		return msgSrc.getMessage(msg, null);
	}

	/**
	 * 文言を解決するためのデータ構造 ({@link MessageSourceResolvable}) を生成する。
	 * 
	 * @param code 文言の識別名。
	 * @param args 文言に埋込むデータ。
	 * @return 文言。
	 */
	private MessageSourceResolvable getResolvable(String code, Object... args) {
		return new DefaultMessageSourceResolvable(new String[] { code }, args);
	}

	/**
	 * 文言定義を生成する。
	 * 
	 * @return 文言定義。
	 */
	private MessageSource createMessageSource() {
		ResourceBundleMessageSource msgSrc = new ResourceBundleMessageSource();
		msgSrc.setBasenames("message/launcher", "message/batchId");
		msgSrc.setUseCodeAsDefaultMessage(true);
		return msgSrc;
	}

}
