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

package cherry.fundamental.batch.tools;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

public class ProgressLoggingListener implements SpringApplicationRunListener {

	private final Logger log = LoggerFactory.getLogger(Launcher.class);

	private final Msg msg = new Msg();

	@SuppressWarnings("unused")
	private final SpringApplication app;

	private final ApplicationArguments args;

	private final String batchId;

	public ProgressLoggingListener(SpringApplication app, String[] args) {
		this.app = app;
		this.args = new DefaultApplicationArguments(args);
		this.batchId = this.args.getNonOptionArgs().stream().findFirst().orElse("UNKNOWN");
	}

	@Override
	public void starting(ConfigurableBootstrapContext bootstrapContext) {
		log.info(msg.resolve("BATCH {0} INITIALIZING", batchId));
	}

	@Override
	public void started(ConfigurableApplicationContext context, Duration timeTaken) {
		log.info(msg.resolve("BATCH {0} INITIALIZED", batchId));
	}

}
