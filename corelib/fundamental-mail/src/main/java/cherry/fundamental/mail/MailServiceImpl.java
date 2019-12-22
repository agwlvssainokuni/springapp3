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

package cherry.fundamental.mail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;

import cherry.elemental.loop.Loop;
import cherry.fundamental.mail.queue.MailQueue;

public class MailServiceImpl implements MailService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Supplier<LocalDateTime> currentDateTime;

	private final MailQueue mailQueue;

	private final double rateToSend;

	private final TimeUnit rateUnit;

	public MailServiceImpl(Supplier<LocalDateTime> currentDateTime, MailQueue mailQueue, double rateToSend,
			TimeUnit rateUnit) {
		this.currentDateTime = currentDateTime;
		this.mailQueue = mailQueue;
		this.rateToSend = rateToSend;
		this.rateUnit = rateUnit;
	}

	@Override
	public void send() {
		try {
			LocalDateTime now = currentDateTime.get();
			List<Long> list = mailQueue.list(now);
			Loop.rate(rateToSend, rateUnit).iterate(list, messageId -> {
				mailQueue.send(messageId, now);
			});
		} catch (MailException | DataAccessException ex) {
			if (log.isDebugEnabled()) {
				log.debug("failed to send mail", ex);
			}
		}
	}

	@Override
	public void expire(LocalDateTime ldtm) {
		List<Long> list = mailQueue.listSent(ldtm);
		for (Long messageId : list) {
			mailQueue.delete(messageId);
		}
	}

}
