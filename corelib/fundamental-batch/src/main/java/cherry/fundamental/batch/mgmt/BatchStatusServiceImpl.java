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

package cherry.fundamental.batch.mgmt;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import cherry.fundamental.batch.ExitStatus;

public class BatchStatusServiceImpl implements BatchStatusService {

	private final BatchStatusStore batchStatusStore;

	private final Supplier<LocalDateTime> currentDateTime;

	public BatchStatusServiceImpl(BatchStatusStore batchStatusStore, Supplier<LocalDateTime> currentDateTime) {
		this.batchStatusStore = batchStatusStore;
		this.currentDateTime = currentDateTime;
	}

	@Override
	public boolean updateToRunning(String batchId) {
		if (batchStatusStore.isBatchRunning(batchId)) {
			return false;
		}
		batchStatusStore.updateToRunning(batchId, currentDateTime.get());
		return true;
	}

	@Override
	public boolean updateToFinished(String batchId, ExitStatus status, int exitCode) {
		if (!batchStatusStore.isBatchRunning(batchId)) {
			return false;
		}
		batchStatusStore.updateToFinished(batchId, currentDateTime.get(), status, exitCode);
		return true;
	}

}
