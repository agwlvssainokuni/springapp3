/*
 * Copyright 2019 agwlvssainokuni
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

package cherry.elemental.loop;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 繰り返し実行機能。<br/>
 */
public interface Loop {

	/**
	 * 条件を指定して繰り返し実行する。<br/>
	 * 指定された「条件」がtrueの間、指定された「処理」を繰り返し実行する。
	 * 
	 * @param condition 繰り返す「条件」。
	 * @param action 実行する「処理」。
	 */
	void repeat(Predicate<Long> condition, Consumer<Long> action);

	/**
	 * 対象を指定して繰り返し実行する。<br/>
	 * 指定された「対象」の各要素について、指定された「処理」を実行する。
	 * 
	 * @param iterable 処理の「対象」。
	 * @param action 実行する「処理」。
	 */
	<T> void iterate(Iterable<T> iterable, Consumer<T> action);

	/**
	 * 処理の「間隔」を指定して繰り返す機能を取得する。<br />
	 * 
	 * @param interval 処理の「間隔」(ミリ秒)。
	 * @return 繰り返し実行機能。
	 */
	public static Loop interval(long interval) {
		return interval(interval, TimeUnit.MILLISECONDS);
	}

	/**
	 * 処理の「間隔」を指定して繰り返す機能を取得する。<br />
	 * 
	 * @param interval 処理の「間隔」。
	 * @param timeunit 処理の間隔の「単位」。
	 * @return　繰り返し実行機能。
	 */
	public static Loop interval(long interval, TimeUnit timeunit) {
		return new IntervalLoop(interval, timeunit);
	}

	/**
	 * 処理の「頻度」を指定して繰り返す機能を取得する。<br/>
	 * 
	 * @param rate 処理の「頻度」(回/秒)。
	 * @return 繰り返し実行機能。
	 */
	public static Loop rate(double rate) {
		return rate(rate, TimeUnit.SECONDS);
	}

	/**
	 * 処理の「頻度」を指定して繰り返す機能を取得する。<br/>
	 * 
	 * @param rate 処理の「頻度」(回/timeunit)。
	 * @param timeunit 処理の頻度の「単位」。
	 * @return 繰り返し実行機能。
	 */
	public static Loop rate(double rate, TimeUnit timeunit) {
		return new RateLoop(rate, timeunit);
	}

}
