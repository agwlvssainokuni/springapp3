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

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;
import org.yaml.snakeyaml.Yaml;

public class FileWorkdayStrategy extends AbstractWorkdayStrategy {

	private final Map<String, List<Config>> configCache = new HashMap<>();

	private final File basedir;

	private final String subdir;

	public FileWorkdayStrategy(File basedir, String subdir) {
		this.basedir = basedir;
		this.subdir = subdir;
	}

	public FileWorkdayStrategy(File basedir) {
		this(basedir, "workday");
	}

	@Override
	protected boolean isRegularOn(String name, DayOfWeek dow) {
		List<Set<DayOfWeek>> s = getConfig(name).stream().map(Config::getRegularOn).collect(Collectors.toList());
		if (s.stream().allMatch(Set::isEmpty)) {
			return true;
		}
		return s.stream().anyMatch(l -> l.contains(dow));
	}

	@Override
	protected boolean isSpecificOn(String name, LocalDate ldt) {
		return getConfig(name).stream().map(Config::getSpecificOn).anyMatch(l -> l.contains(ldt));
	}

	@Override
	protected boolean isSpecificOff(String name, LocalDate ldt) {
		return getConfig(name).stream().map(Config::getSpecificOff).anyMatch(l -> l.contains(ldt));
	}

	private List<Config> getConfig(String name) {

		File dir = new File(new File(basedir, name), subdir);
		if (!dir.isDirectory()) {
			configCache.remove(name);
			return Collections.<Config> emptyList();
		}

		Set<String> filenames = Stream.of(dir.list()).filter(n -> new File(dir, n).isFile())
				.collect(Collectors.toSet());
		if (configCache.containsKey(name)) {
			List<Config> current = configCache.get(name);
			if (filenames.size() == current.size() && current.stream().allMatch(c -> filenames.contains(c.getFilename())
					&& new File(dir, c.getFilename()).lastModified() == c.getLastModified())) {
				return current;
			}
		}

		List<Config> list = filenames.stream().sorted().map(n -> loadConfig(new File(dir, n)))
				.collect(Collectors.toList());
		configCache.put(name, list);
		return list;
	}

	private Config loadConfig(File file) {
		Yaml yaml = new Yaml();
		try (InputStream in = new FileInputStream(file)) {

			Map<String, List<?>> map = yaml.load(in);
			if (map == null) {
				return new Config(file.getName(), file.lastModified(), Collections.emptySet(), Collections.emptySet(),
						Collections.emptySet());
			}

			List<?> ron = (List<?>) map.get("regularOn");
			List<?> son = (List<?>) map.get("specificOn");
			List<?> soff = (List<?>) map.get("specificOff");

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
			Set<LocalDate> specificOn = son.stream().filter(ObjectUtils::isNotEmpty).map(this::getLocalDate)
					.collect(Collectors.toSet());
			Set<LocalDate> specificOff = soff.stream().filter(ObjectUtils::isNotEmpty).map(this::getLocalDate)
					.collect(Collectors.toSet());

			return new Config(file.getName(), file.lastModified(), regularOn, specificOn, specificOff);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private LocalDate getLocalDate(Object v) {
		if (v instanceof Date) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(((Date) v).getTime());
			return LocalDate.of(cal.get(YEAR), cal.get(MONTH) + 1, cal.get(DAY_OF_MONTH));
		}
		if (v instanceof Number) {
			int num = ((Number) v).intValue();
			int dayOfMonth = num % 100; // 末尾二桁
			int month = (num / 100) % 100; // 末尾から三桁目と四桁目
			int year = num / 10000; // 末尾から五桁目以上
			return LocalDate.of(year, month, dayOfMonth);
		}
		try {
			return LocalDate.parse(v.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		} catch (DateTimeParseException ex1) {
			try {
				return LocalDate.parse(v.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
			} catch (DateTimeParseException ex2) {
				return LocalDate.parse(v.toString());
			}
		}
	}

	static class Config {

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

		public String getFilename() {
			return filename;
		}

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
