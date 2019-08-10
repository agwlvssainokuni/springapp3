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

package cherry.gallery;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/exit")
@RestController
public class ExitController implements ExitCodeGenerator {

	private Integer exitCode = null;

	@RequestMapping()
	public synchronized boolean setExitCode(@RequestParam(value = "code", defaultValue = "0") int code) {
		this.exitCode = Integer.valueOf(code);
		notifyAll();
		return true;
	}

	@Override
	public synchronized int getExitCode() {
		while (true) {
			if (exitCode != null) {
				return exitCode.intValue();
			}
			try {
				wait();
			} catch (InterruptedException ex) {
				// NOTHING TO DO
			}
		}
	}

}
