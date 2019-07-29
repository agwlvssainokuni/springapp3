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

package cherry.fundamental.batch.tools;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;

import cherry.fundamental.batch.ExitStatus;
import cherry.fundamental.batch.IBatch;

/**
 * バッチプログラムを起動する機能を提供する。
 */
public class LauncherImpl implements Launcher {

	private final Logger log = LoggerFactory.getLogger(Launcher.class);

	private final Msg msg = new Msg();

	private final ApplicationContext appctx;

	public LauncherImpl(ApplicationContext appctx) {
		this.appctx = appctx;
	}

	@Override
	public ExitStatus launch(ApplicationArguments args) {

		if (args.getNonOptionArgs().isEmpty()) {
			return ExitStatus.FATAL;
		}

		String batchId = args.getNonOptionArgs().get(0);
		IBatch batch = appctx.getBean(batchId, IBatch.class);

		log.info(msg.resolve("BATCH {0} STARTING", batchId));
		for (String arg : args.getSourceArgs()) {
			log.info("  {}", arg);
		}

		try {
			ExitStatus status = batch.execute(args);
			switch (status) {
			case NORMAL:
				log.info(msg.resolve("BATCH {0} ENDED WITH {1}", batchId, status));
				break;
			case WARN:
				log.warn(msg.resolve("BATCH {0} ENDED WITH {1}", batchId, status));
				break;
			case ERROR:
				log.error(msg.resolve("BATCH {0} ENDED WITH {1}", batchId, status));
				break;
			default:
				log.error(msg.resolve("BATCH {0} ENDED WITH {1}", batchId, status));
				break;
			}
			return status;
		} catch (Exception ex) {
			Optional<ExitStatus> statusOpt = translateExceptionToExitStatus(appctx, ex);
			return statusOpt.map(status -> {
				switch (status) {
				case NORMAL:
					log.info(msg.resolve("BATCH {0} ENDED WITH {1}", batchId, status), ex);
					break;
				case WARN:
					log.warn(msg.resolve("BATCH {0} ENDED WITH {1}", batchId, status), ex);
					break;
				case ERROR:
					log.error(msg.resolve("BATCH {0} ENDED WITH {1}", batchId, status), ex);
					break;
				default:
					log.error(msg.resolve("BATCH {0} ENDED WITH {1}", batchId, status), ex);
					break;
				}
				return status;
			}).orElseGet(() -> {
				log.error(msg.resolve("BATCH {0} ENDED WITH EXCEPTION", batchId), ex);
				return ExitStatus.FATAL;
			});
		}
	}

	private Optional<ExitStatus> translateExceptionToExitStatus(ApplicationContext appctx, Exception ex) {
		for (ExceptionExitStatusTranslator translator : appctx.getBeansOfType(ExceptionExitStatusTranslator.class)
				.values()) {
			Optional<ExitStatus> status = translator.translate(ex);
			if (status.isPresent()) {
				return status;
			}
		}
		return Optional.empty();
	}

}
