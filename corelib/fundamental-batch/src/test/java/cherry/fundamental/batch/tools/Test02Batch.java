/*
 * Copyright 2015,2021 agwlvssainokuni
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

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import cherry.fundamental.batch.ExitStatus;
import cherry.fundamental.batch.IBatch;

@Component("test02Batch")
public class Test02Batch implements IBatch {

	@Override
	public ExitStatus execute(ApplicationArguments args) {
		if (args.getNonOptionArgs().size() <= 1) {
			throw new IllegalArgumentException();
		}
		throw new IllegalStateException(args.getNonOptionArgs().get(1));
	}

}
