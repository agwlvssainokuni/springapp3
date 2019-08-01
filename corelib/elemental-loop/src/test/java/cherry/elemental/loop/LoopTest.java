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

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoopTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testIntervalRepeat_0times() {
		@SuppressWarnings("unchecked")
		Consumer<Long> action = mock(Consumer.class);
		Loop.interval(100L).repeat(n -> n < 0L, action);
		verify(action, never()).accept(anyLong());
	}

	@Test
	public void testIntervalRepeat_1times() {
		@SuppressWarnings("unchecked")
		Consumer<Long> action = mock(Consumer.class);
		Loop.interval(100L).repeat(n -> n < 1L, action);
		verify(action, times(1)).accept(anyLong());
		for (long i = 0L; i < 1L; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testIntervalRepeat_2times() {
		@SuppressWarnings("unchecked")
		Consumer<Long> action = mock(Consumer.class);
		Loop.interval(100L).repeat(n -> n < 2L, action);
		verify(action, times(2)).accept(anyLong());
		for (long i = 0L; i < 2L; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testIntervalRepeat_10times() {
		@SuppressWarnings("unchecked")
		Consumer<Long> action = mock(Consumer.class);
		Loop.interval(100L).repeat(n -> n < 10L, action);
		verify(action, times(10)).accept(anyLong());
		for (long i = 0L; i < 10L; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testIntervalRepeat_withlog() {
		logger.info("interval repeat - start");
		Loop.interval(100L).repeat(n -> n < 10L, n -> logger.info("interval repeat {}", n));
		logger.info("interval repeat - end");
	}

	@Test
	public void testIntervalIterate_0times() {
		@SuppressWarnings("unchecked")
		Consumer<Integer> action = mock(Consumer.class);
		Loop.interval(100L).iterate(asList(), action);
		verify(action, never()).accept(anyInt());
	}

	@Test
	public void testIntervalIterate_1times() {
		@SuppressWarnings("unchecked")
		Consumer<Integer> action = mock(Consumer.class);
		Loop.interval(100L).iterate(asList(1), action);
		verify(action, times(1)).accept(anyInt());
		for (int i = 1; i <= 1; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testIntervalIterate_2times() {
		@SuppressWarnings("unchecked")
		Consumer<Integer> action = mock(Consumer.class);
		Loop.interval(100L).iterate(asList(1, 2), action);
		verify(action, times(2)).accept(anyInt());
		for (int i = 1; i <= 2; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testIntervalIterate_10times() {
		@SuppressWarnings("unchecked")
		Consumer<Integer> action = mock(Consumer.class);
		Loop.interval(100L).iterate(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), action);
		verify(action, times(10)).accept(anyInt());
		for (int i = 1; i <= 10; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testIntervalIterate_withlog() {
		logger.info("interval iterate - start");
		Loop.interval(100L).iterate(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), n -> logger.info("interval iterate {}", n));
		logger.info("interval iterate - end");
	}

	@Test
	public void testRateRepeat_0times() {
		@SuppressWarnings("unchecked")
		Consumer<Long> action = mock(Consumer.class);
		Loop.rate(10.0).repeat(n -> n < 0L, action);
		verify(action, never()).accept(anyLong());
	}

	@Test
	public void testRateRepeat_1times() {
		@SuppressWarnings("unchecked")
		Consumer<Long> action = mock(Consumer.class);
		Loop.rate(10.0).repeat(n -> n < 1L, action);
		verify(action, times(1)).accept(anyLong());
		for (long i = 0L; i < 1L; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testRateRepeat_2times() {
		@SuppressWarnings("unchecked")
		Consumer<Long> action = mock(Consumer.class);
		Loop.rate(10.0).repeat(n -> n < 2L, action);
		verify(action, times(2)).accept(anyLong());
		for (long i = 0L; i < 2L; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testRateRepeat_10times() {
		@SuppressWarnings("unchecked")
		Consumer<Long> action = mock(Consumer.class);
		Loop.rate(10.0).repeat(n -> n < 10L, action);
		verify(action, times(10)).accept(anyLong());
		for (long i = 0L; i < 10L; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testRateRepeat_withlog() {
		logger.info("rate repeat - start");
		Loop.rate(10.0).repeat(n -> n < 10L, n -> logger.info("rate repeat {}", n));
		logger.info("rate repeat - end");
	}

	@Test
	public void testRateIterate_0times() {
		@SuppressWarnings("unchecked")
		Consumer<Integer> action = mock(Consumer.class);
		Loop.rate(10.0).iterate(asList(), action);
		verify(action, never()).accept(anyInt());
	}

	@Test
	public void testRateIterate_1times() {
		@SuppressWarnings("unchecked")
		Consumer<Integer> action = mock(Consumer.class);
		Loop.rate(10.0).iterate(asList(1), action);
		verify(action, times(1)).accept(anyInt());
		for (int i = 1; i <= 1; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testRateIterate_2times() {
		@SuppressWarnings("unchecked")
		Consumer<Integer> action = mock(Consumer.class);
		Loop.rate(10.0).iterate(asList(1, 2), action);
		verify(action, times(2)).accept(anyInt());
		for (int i = 1; i <= 2; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testRateIterate_10times() {
		@SuppressWarnings("unchecked")
		Consumer<Integer> action = mock(Consumer.class);
		Loop.rate(10.0).iterate(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), action);
		verify(action, times(10)).accept(anyInt());
		for (int i = 1; i <= 10; i++) {
			verify(action).accept(i);
		}
	}

	@Test
	public void testRateIterate_withlog() {
		logger.info("rate iterate - start");
		Loop.rate(10.0).iterate(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), n -> logger.info("rate iterate {}", n));
		logger.info("rate iterate - end");
	}

}
