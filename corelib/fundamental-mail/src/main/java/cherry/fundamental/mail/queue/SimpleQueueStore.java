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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SimpleQueueStore implements QueueStore {

	private final AtomicLong nextMessageId = new AtomicLong(0L);

	private final Map<Long, Item> entryMap = Collections.synchronizedMap(new LinkedHashMap<Long, Item>());

	@Override
	public long save(String loginId, String messageName, LocalDateTime scheduledAt, String from, List<String> to,
			List<String> cc, List<String> bcc, String replyTo, String subject, String body) {

		long messageId = nextMessageId.getAndIncrement();

		QueuedEntry message = new QueuedEntry(from, to, cc, bcc, replyTo, subject, body);

		Item entry = new Item();
		entry.setScheduledAt(scheduledAt);
		entry.setQueuedEntry(message);
		entryMap.put(Long.valueOf(messageId), entry);

		return messageId;
	}

	@Override
	public List<Long> listToSend(LocalDateTime dtm) {
		return entryMap.entrySet().stream().filter(e -> e.getValue().getScheduledAt().compareTo(dtm) <= 0)
				.map(Map.Entry::getKey).collect(Collectors.toList());
	}

	@Override
	public QueuedEntry get(long messageId) {
		return Optional.ofNullable(entryMap.get(Long.valueOf(messageId))).map(Item::getQueuedEntry).orElse(null);
	}

	@Override
	public void finish(long messageId) {
		entryMap.remove(Long.valueOf(messageId));
	}

	public static class Item {

		private LocalDateTime scheduledAt;

		private QueuedEntry queuedEntry;

		public LocalDateTime getScheduledAt() {
			return scheduledAt;
		}

		public void setScheduledAt(LocalDateTime scheduledAt) {
			this.scheduledAt = scheduledAt;
		}

		public QueuedEntry getQueuedEntry() {
			return queuedEntry;
		}

		public void setQueuedEntry(QueuedEntry queuedEntry) {
			this.queuedEntry = queuedEntry;
		}
	}

}
