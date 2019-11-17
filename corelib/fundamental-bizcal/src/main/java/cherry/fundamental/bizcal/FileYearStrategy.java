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
import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.yaml.snakeyaml.Yaml;

import cherry.fundamental.bizcal.FileConfig.IConfig;

public class FileYearStrategy extends AbstractYearStrategy {

	private final YearConfig yearConfig;

	public FileYearStrategy(File basedir, String subdir) {
		this.yearConfig = new YearConfig(basedir, subdir);
	}

	public FileYearStrategy(File basedir) {
		this(basedir, "year");
	}

	@Override
	protected LocalDate resolveFirstDate(String name, Year year) {
		Optional<LocalDate> ldtOpt = yearConfig.getConfig(name).stream().map(Config::getSpecific).map(m -> m.get(year))
				.filter(ObjectUtils::isNotEmpty).findFirst();
		if (ldtOpt.isPresent()) {
			return ldtOpt.get();
		}
		Long yearOffset = yearConfig.getConfig(name).stream().map(Config::getRegularYear).findFirst().orElse(0L);
		Long monthOffset = yearConfig.getConfig(name).stream().map(Config::getRegularMonth).findFirst().orElse(0L);
		Long dayOffset = yearConfig.getConfig(name).stream().map(Config::getRegularDay).findFirst().orElse(0L);
		return year.atMonth(1).atDay(1).plusYears(yearOffset.longValue()).plusMonths(monthOffset.longValue())
				.plusDays(dayOffset.longValue());
	}

	static class YearConfig extends FileConfig<Config> {

		public YearConfig(File basedir, String subdir) {
			super(basedir, subdir);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Config loadConfig(File file) {
			Yaml yaml = new Yaml();
			try (InputStream in = new FileInputStream(file)) {

				Map<String, ?> map = yaml.load(in);
				if (map == null) {
					return new Config(file.getName(), file.lastModified(), null, null, null, Collections.emptyMap());
				}

				Map<?, ?> reg = (Map<?, ?>) map.get("regular");
				List<Map<?, ?>> spc = (List<Map<?, ?>>) map.get("specific");

				if (reg == null) {
					reg = Collections.emptyMap();
				}
				if (spc == null) {
					spc = Collections.emptyList();
				}

				Long regularYear = Optional.ofNullable(reg.get("year")).map(YamlUtil::getLong).orElse(null);
				Long regularMonth = Optional.ofNullable(reg.get("month")).map(YamlUtil::getLong).orElse(null);
				Long regularDay = Optional.ofNullable(reg.get("day")).map(YamlUtil::getLong).orElse(null);

				Map<Year, LocalDate> specific = spc.stream().collect(Collectors
						.toMap(e -> YamlUtil.getYear(e.get("year")), e -> YamlUtil.getLocalDate(e.get("first"))));

				return new Config(file.getName(), file.lastModified(), regularYear, regularMonth, regularDay, specific);
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}
	}

	static class Config implements IConfig {

		private final String filename;

		private final long lastModified;

		private final Long regularYear;

		private final Long regularMonth;

		private final Long regularDay;

		private final Map<Year, LocalDate> specific;

		public Config(String filename, long lastModified, Long regularYear, Long regularMonth, Long regularDay,
				Map<Year, LocalDate> specific) {
			this.filename = filename;
			this.lastModified = lastModified;
			this.regularYear = regularYear;
			this.regularMonth = regularMonth;
			this.regularDay = regularDay;
			this.specific = specific;
		}

		@Override
		public String getFilename() {
			return filename;
		}

		@Override
		public long getLastModified() {
			return lastModified;
		}

		public Long getRegularYear() {
			return regularYear;
		}

		public Long getRegularMonth() {
			return regularMonth;
		}

		public Long getRegularDay() {
			return regularDay;
		}

		public Map<Year, LocalDate> getSpecific() {
			return specific;
		}
	}

}
