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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.yaml.snakeyaml.Yaml;

import cherry.fundamental.bizcal.FileConfig.IConfig;

public class FileWorkdayStrategy extends AbstractWorkdayStrategy {

	private final WorkdayConfig workdayConfig;

	public FileWorkdayStrategy(File basedir, String subdir) {
		this.workdayConfig = new WorkdayConfig(basedir, subdir);
	}

	public FileWorkdayStrategy(File basedir) {
		this(basedir, "workday");
	}

	@Override
	protected boolean isRegularOn(String name, DayOfWeek dow) {
		List<Set<DayOfWeek>> s = workdayConfig.getConfig(name).stream().map(Config::getRegularOn)
				.collect(Collectors.toList());
		if (s.stream().allMatch(Set::isEmpty)) {
			return true;
		}
		return s.stream().anyMatch(l -> l.contains(dow));
	}

	@Override
	protected boolean isSpecificOn(String name, LocalDate ldt) {
		return workdayConfig.getConfig(name).stream().map(Config::getSpecificOn).anyMatch(l -> l.contains(ldt));
	}

	@Override
	protected boolean isSpecificOff(String name, LocalDate ldt) {
		return workdayConfig.getConfig(name).stream().map(Config::getSpecificOff).anyMatch(l -> l.contains(ldt));
	}

	static class WorkdayConfig extends FileConfig<Config> {

		public WorkdayConfig(File basedir, String subdir) {
			super(basedir, subdir);
		}

		@Override
		protected Config loadConfig(File file) {
			Yaml yaml = new Yaml();
			try (InputStream in = new FileInputStream(file)) {

				Map<String, List<?>> map = yaml.load(in);
				if (map == null) {
					return new Config(file.getName(), file.lastModified(), Collections.emptySet(),
							Collections.emptySet(), Collections.emptySet());
				}

				List<?> ron = map.get("regularOn");
				List<?> son = map.get("specificOn");
				List<?> soff = map.get("specificOff");

				if (ron == null) {
					ron = Collections.emptyList();
				}
				if (son == null) {
					son = Collections.emptyList();
				}
				if (soff == null) {
					soff = Collections.emptyList();
				}

				Set<DayOfWeek> regularOn = ron.stream().filter(ObjectUtils::isNotEmpty).map(Object::toString)
						.map(DayOfWeek::valueOf).collect(Collectors.toSet());
				Set<LocalDate> specificOn = son.stream().filter(ObjectUtils::isNotEmpty).map(YamlUtil::getLocalDate)
						.collect(Collectors.toSet());
				Set<LocalDate> specificOff = soff.stream().filter(ObjectUtils::isNotEmpty).map(YamlUtil::getLocalDate)
						.collect(Collectors.toSet());

				return new Config(file.getName(), file.lastModified(), regularOn, specificOn, specificOff);
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}
	}

	static class Config implements IConfig {

		private final String filename;

		private final long lastModified;

		private final Set<DayOfWeek> regularOn;

		private final Set<LocalDate> specificOn;

		private final Set<LocalDate> specificOff;

		public Config(String filename, long lastModified, Set<DayOfWeek> regularOn, Set<LocalDate> specificOn,
				Set<LocalDate> specificOff) {
			this.filename = filename;
			this.lastModified = lastModified;
			this.regularOn = regularOn;
			this.specificOn = specificOn;
			this.specificOff = specificOff;
		}

		@Override
		public String getFilename() {
			return filename;
		}

		@Override
		public long getLastModified() {
			return lastModified;
		}

		public Set<DayOfWeek> getRegularOn() {
			return regularOn;
		}

		public Set<LocalDate> getSpecificOn() {
			return specificOn;
		}

		public Set<LocalDate> getSpecificOff() {
			return specificOff;
		}
	}

}
