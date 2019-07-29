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

package cherry.fundamental.batch.tools;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;

import cherry.fundamental.batch.ExitStatus;

public class LauncherRunner implements ApplicationRunner, ExitCodeGenerator {

	private final Launcher launcher;

	private ExitStatus exitStatus = null;

	public LauncherRunner(Launcher launcher) {
		this.launcher = launcher;
	}

	/**
	 * バッチプログラムを起動する。
	 * 
	 * @param args 起動時にコマンドラインに指定された引数。
	 * @return バッチプログラムの終了ステータス。
	 */
	@Override
	public void run(ApplicationArguments args) {
		ExitStatus status = launcher.launch(args);
		setExitStatus(status);
	}

	private synchronized void setExitStatus(ExitStatus exitStatus) {
		this.exitStatus = exitStatus;
		notifyAll();
	}

	@Override
	public synchronized int getExitCode() {
		while (true) {
			if (exitStatus != null) {
				return exitStatus.getCode();
			}
			try {
				wait();
			} catch (InterruptedException ex) {
				// NOTHING TO DO
			}
		}
	}

}
