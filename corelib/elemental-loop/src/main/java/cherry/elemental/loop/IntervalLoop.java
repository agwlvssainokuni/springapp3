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
import java.util.function.Predicate;

public class IntervalLoop implements Loop {

	private final long interval;

	private final TimeUnit timeunit;

	public IntervalLoop(long interval, TimeUnit timeunit) {
		this.interval = interval;
		this.timeunit = timeunit;
	}

	@Override
	public void repeat(Predicate<Long> condition, Consumer<Long> action) {
		long count = 0;
		if (!condition.test(Long.valueOf(count))) {
			return;
		}
		action.accept(Long.valueOf(count));
		count += 1;
		while (condition.test(Long.valueOf(count))) {
			sleepUninterruptibly(interval, timeunit);
			action.accept(Long.valueOf(count));
			count += 1;
		}
	}

	@Override
	public <T> void iterate(Iterable<T> iterable, Consumer<T> action) {
		Iterator<T> it = iterable.iterator();
		if (!it.hasNext()) {
			return;
		}
		action.accept(it.next());
		while (it.hasNext()) {
			sleepUninterruptibly(interval, timeunit);
			action.accept(it.next());
		}
	}

}
