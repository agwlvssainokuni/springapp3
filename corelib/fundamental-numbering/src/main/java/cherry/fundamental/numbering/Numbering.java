/*
 * Copyright 2015,2019 agwlvssainokuni
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

package cherry.fundamental.numbering;

import java.util.List;

/**
 * 発番管理機能。<br />
 * 番号を発行する機能を提供する。番号は、基本的に、文字列として発行する。ただし、数値 (Long) として発行することも可能とする。
 */
public interface Numbering {

	/**
	 * 文字列形式で番号を発行する。
	 * 
	 * @param numberName 番号を識別する名前。
	 * @return 発行した番号 (文字列)。
	 */
	String issueAsString(String numberName);

	/**
	 * 文字列形式で番号を発行する。
	 * 
	 * @param numberName 番号を識別する名前。
	 * @return 発行した番号 (文字列)。
	 */
	String issueAsStringInTx(String numberName);

	/**
	 * 文字列形式で番号を発行する。<br />
	 * 発行する番号の数を指定する。返却値は番号のリストであり、要素数は、引数に指定した数と同じである。
	 * 
	 * @param numberName 番号を識別する名前。
	 * @param count 発行する番号の数。
	 * @return 発行した番号 (文字列) のリスト。
	 */
	List<String> issueAsString(String numberName, int count);

	/**
	 * 文字列形式で番号を発行する。<br />
	 * 発行する番号の数を指定する。返却値は番号のリストであり、要素数は、引数に指定した数と同じである。
	 * 
	 * @param numberName 番号を識別する名前。
	 * @param count 発行する番号の数。
	 * @return 発行した番号 (文字列) のリスト。
	 */
	List<String> issueAsStringInTx(String numberName, int count);

	/**
	 * 整数値形式で番号を発行する。
	 * 
	 * @param numberName 番号を識別する名前。
	 * @return 発行した番号 (整数値)。
	 */
	Long issueAsLong(String numberName);

	/**
	 * 整数値形式で番号を発行する。
	 * 
	 * @param numberName 番号を識別する名前。
	 * @return 発行した番号 (整数値)。
	 */
	Long issueAsLongInTx(String numberName);

	/**
	 * 整数値形式で番号を発行する。<br />
	 * 発行する番号の数を指定する。返却値は番号のリストであり、要素数は、引数に指定した数と同じである。
	 * 
	 * @param numberName 番号を識別する名前。
	 * @param count 発行する番号の数。
	 * @return 発行した番号 (整数値) のリスト。
	 */
	List<Long> issueAsLong(String numberName, int count);

	/**
	 * 整数値形式で番号を発行する。<br />
	 * 発行する番号の数を指定する。返却値は番号のリストであり、要素数は、引数に指定した数と同じである。
	 * 
	 * @param numberName 番号を識別する名前。
	 * @param count 発行する番号の数。
	 * @return 発行した番号 (整数値) のリスト。
	 */
	List<Long> issueAsLongInTx(String numberName, int count);

}
