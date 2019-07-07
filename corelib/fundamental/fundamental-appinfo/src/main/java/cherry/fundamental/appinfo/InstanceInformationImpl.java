/*
 * Copyright 2016,2019 agwlvssainokuni
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

package cherry.fundamental.appinfo;

import java.io.File;

public class InstanceInformationImpl implements InstanceInformation {

	private final String id;

	private final String environmentName;

	private final File baseDirectory;

	public InstanceInformationImpl(String id, String environmentName, File baseDirectory) {
		this.id = id;
		this.environmentName = environmentName;
		this.baseDirectory = baseDirectory;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getEnvironmentName() {
		return environmentName;
	}

	@Override
	public File getBaseDirectory() {
		return baseDirectory;
	}

}
