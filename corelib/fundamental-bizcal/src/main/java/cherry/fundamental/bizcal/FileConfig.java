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

package cherry.fundamental.bizcal;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cherry.fundamental.bizcal.FileConfig.IConfig;

public abstract class FileConfig<CFG extends IConfig> {

	private final Map<String, List<CFG>> configCache = new HashMap<>();

	private final File basedir;

	private final String subdir;

	public FileConfig(File basedir, String subdir) {
		this.basedir = basedir;
		this.subdir = subdir;
	}

	public List<CFG> getConfig(String name) {

		File dir = new File(new File(basedir, name), subdir);
		if (!dir.isDirectory()) {
			configCache.remove(name);
			return Collections.<CFG> emptyList();
		}

		Set<String> filenames = Stream.of(dir.list()).filter(n -> new File(dir, n).isFile())
				.collect(Collectors.toSet());
		if (configCache.containsKey(name)) {
			List<CFG> current = configCache.get(name);
			if (filenames.size() == current.size() && current.stream().allMatch(c -> filenames.contains(c.getFilename())
					&& new File(dir, c.getFilename()).lastModified() == c.getLastModified())) {
				return current;
			}
		}

		List<CFG> list = filenames.stream().sorted().map(n -> loadConfig(new File(dir, n)))
				.collect(Collectors.toList());
		configCache.put(name, list);
		return list;
	}

	protected abstract CFG loadConfig(File file);

	interface IConfig {

		String getFilename();

		long getLastModified();
	}

}
