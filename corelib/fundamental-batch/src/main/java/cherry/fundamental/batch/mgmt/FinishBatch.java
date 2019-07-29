/*
 * Copyright 2014,2016 agwlvssainokuni
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

package cherry.fundamental.batch.mgmt;

import org.springframework.boot.ApplicationArguments;

import cherry.fundamental.batch.ExitStatus;
import cherry.fundamental.batch.IBatch;

public class FinishBatch implements IBatch {

	private final BatchStatusService batchStatusService;

	public FinishBatch(BatchStatusService batchStatusService) {
		this.batchStatusService = batchStatusService;
	}

	@Override
	public ExitStatus execute(ApplicationArguments args) {

		if (args.getNonOptionArgs().size() < 2) {
			return ExitStatus.ERROR;
		}

		String batchId = args.getNonOptionArgs().get(0);
		ExitStatus status = ExitStatus.valueOf(args.getNonOptionArgs().get(1));

		int exitCode;
		if (args.getNonOptionArgs().size() < 3) {
			exitCode = status.getCode();
		} else {
			exitCode = Integer.parseInt(args.getNonOptionArgs().get(2));
		}

		if (batchStatusService.updateToFinished(batchId, status, exitCode)) {
			return ExitStatus.NORMAL;
		} else {
			return ExitStatus.WARN;
		}
	}

}
