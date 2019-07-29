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

import cherry.fundamental.batch.ExitStatus;

public interface BatchStatusStore {

	boolean isBatchRunning(String batchId);

	void updateToRunning(String batchId, LocalDateTime dtm);

	void updateToFinished(String batchId, LocalDateTime dtm, ExitStatus status, int exitCode);

}
