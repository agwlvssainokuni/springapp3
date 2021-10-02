/*
 * Copyright 2019,2021 agwlvssainokuni
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

package cherry.elemental.logback;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class OnMemoryAppenderTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void test() {

		// 実行
		OnMemoryAppender.begin();
		for (int i = 0; i < 10; i++) {
			logger.info("ログ{}", i);
		}
		List<ILoggingEvent> event = OnMemoryAppender.getEvents();
		OnMemoryAppender.end();

		// 検証
		assertEquals(10, event.size());
		int i = 0;
		for (ILoggingEvent ev : event) {
			assertEquals(Level.INFO, ev.getLevel());
			assertEquals(getClass().getName(), ev.getLoggerName());
			assertEquals("ログ{}", ev.getMessage());
			assertEquals(Integer.valueOf(i), ev.getArgumentArray()[0]);
			i++;
		}
	}

}
