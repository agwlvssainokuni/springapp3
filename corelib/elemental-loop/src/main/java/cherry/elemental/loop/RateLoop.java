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

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.util.concurrent.RateLimiter;

public class RateLoop implements Loop {

	private final double rate;

	private final TimeUnit timeunit;

	public RateLoop(double rate, TimeUnit timeunit) {
		this.rate = rate;
		this.timeunit = timeunit;
	}

	@Override
	public void repeat(Predicate<Long> condition, Consumer<Long> action) {
		int permits = (int) TimeUnit.SECONDS.convert(1L, timeunit);
		RateLimiter limiter = RateLimiter.create(rate);
		long count = 0;
		while (condition.test(Long.valueOf(count))) {
			limiter.acquire(permits);
			action.accept(Long.valueOf(count));
			count += 1;
		}
	}

	@Override
	public <T> void iterate(Iterable<T> iterable, Consumer<T> action) {
		int permits = (int) TimeUnit.SECONDS.convert(1L, timeunit);
		RateLimiter limiter = RateLimiter.create(rate);
		Iterator<T> it = iterable.iterator();
		while (it.hasNext()) {
			limiter.acquire(permits);
			action.accept(it.next());
		}
	}

}
