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

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

import com.google.common.util.concurrent.RateLimiter;

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
	void repeat(LongPredicate condition, LongConsumer action);

	/**
	 * 対象を指定して繰り返し実行する。<br/>
	 * 指定された「対象」の各要素について、指定された「処理」を実行する。
	 *
	 * @param iterable 処理の「対象」。
	 * @param action 実行する「処理」。
	 */
	default <T> void iterate(Iterable<T> iterable, Consumer<T> action) {
		Iterator<T> it = iterable.iterator();
		repeat(n -> it.hasNext(), n -> action.accept(it.next()));
	}

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
	 * @return 繰り返し実行機能。
	 */
	public static Loop interval(long interval, TimeUnit timeunit) {
		return (condition, action) -> {
			long count = 0L;
			if (!condition.test(count)) {
				return;
			}
			action.accept(count);
			count += 1L;
			while (condition.test(count)) {
				sleepUninterruptibly(interval, timeunit);
				action.accept(count);
				count += 1L;
			}
		};
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
		return (condition, action) -> {
			int permits = (int) TimeUnit.SECONDS.convert(1L, timeunit);
			RateLimiter limiter = RateLimiter.create(rate);
			long count = 0L;
			while (condition.test(count)) {
				limiter.acquire(permits);
				action.accept(count);
				count += 1L;
			}
		};
	}

}
