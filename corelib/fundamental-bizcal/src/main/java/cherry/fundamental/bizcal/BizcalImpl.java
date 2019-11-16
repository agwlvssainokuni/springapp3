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

package cherry.fundamental.bizcal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

public class BizcalImpl implements Bizcal {

	private final DateTimeStrategy dateTimeStrategy;

	private final YearStrategy yearStrategy;

	private final WorkdayStrategy workdayStrategy;

	private final String stdCalName;

	public BizcalImpl(DateTimeStrategy dateTimeStrategy, YearStrategy yearStrategy, WorkdayStrategy workdayStrategy,
			String stdCalName) {
		this.dateTimeStrategy = dateTimeStrategy;
		this.yearStrategy = yearStrategy;
		this.workdayStrategy = workdayStrategy;
		this.stdCalName = stdCalName;
	}

	@Override
	public LocalDate today() {
		return today(stdCalName);
	}

	@Override
	public LocalDate today(String name) {
		return dateTimeStrategy.today(name);
	}

	@Override
	public LocalDateTime now() {
		return now(stdCalName);
	}

	@Override
	public LocalDateTime now(String name) {
		return dateTimeStrategy.now(name);
	}

	@Override
	public Year year() {
		return year(stdCalName);
	}

	@Override
	public Year year(String name) {
		return year(name, today(name));
	}

	@Override
	public Year year(LocalDate dt) {
		return year(stdCalName, dt);
	}

	@Override
	public Year year(String name, LocalDate dt) {
		return yearByDate(name, dt).getLeft();
	}

	@Override
	public LocalDate firstDayOfYear() {
		return firstDayOfYear(stdCalName);
	}

	@Override
	public LocalDate firstDayOfYear(String name) {
		return firstDayOfYear(name, today(name));
	}

	@Override
	public LocalDate firstDayOfYear(LocalDate dt) {
		return firstDayOfYear(stdCalName, dt);
	}

	@Override
	public LocalDate firstDayOfYear(String name, LocalDate dt) {
		return yearByDate(name, dt).getRight().getMinimum();
	}

	@Override
	public LocalDate firstDayOfYear(Year year) {
		return firstDayOfYear(stdCalName, year);
	}

	@Override
	public LocalDate firstDayOfYear(String name, Year year) {
		return yearStrategy.rangeOfYear(name, year).getMinimum();
	}

	@Override
	public LocalDate lastDayOfYear() {
		return lastDayOfYear(stdCalName);
	}

	@Override
	public LocalDate lastDayOfYear(String name) {
		return lastDayOfYear(name, today(name));
	}

	@Override
	public LocalDate lastDayOfYear(LocalDate dt) {
		return lastDayOfYear(stdCalName, dt);
	}

	@Override
	public LocalDate lastDayOfYear(String name, LocalDate dt) {
		return yearByDate(name, dt).getRight().getMaximum();
	}

	@Override
	public LocalDate lastDayOfYear(Year year) {
		return lastDayOfYear(stdCalName, year);
	}

	@Override
	public LocalDate lastDayOfYear(String name, Year year) {
		return yearStrategy.rangeOfYear(name, year).getMaximum();
	}

	@Override
	public int getNumberOfWorkday(LocalDate to) {
		return getNumberOfWorkday(stdCalName, to);
	}

	@Override
	public int getNumberOfWorkday(String name, LocalDate to) {
		return getNumberOfWorkday(name, today(name), to);
	}

	@Override
	public int getNumberOfWorkday(LocalDate from, LocalDate to) {
		return getNumberOfWorkday(stdCalName, from, to);
	}

	@Override
	public int getNumberOfWorkday(String name, LocalDate from, LocalDate to) {
		return workdayStrategy.getNumberOfWorkday(name, from, to);
	}

	@Override
	public LocalDate getNextWorkday(int numberOfWorkday) {
		return getNextWorkday(stdCalName, numberOfWorkday);
	}

	@Override
	public LocalDate getNextWorkday(String name, int numberOfWorkday) {
		return getNextWorkday(name, today(name), numberOfWorkday);
	}

	@Override
	public LocalDate getNextWorkday(LocalDate from, int numberOfWorkday) {
		return getNextWorkday(stdCalName, from, numberOfWorkday);
	}

	@Override
	public LocalDate getNextWorkday(String name, LocalDate from, int numberOfWorkday) {
		return workdayStrategy.getNextWorkday(name, from, numberOfWorkday);
	}

	private Pair<Year, Range<LocalDate>> yearByDate(String name, LocalDate dt) {
		return yearByDate(name, Year.of(dt.getYear()), dt);
	}

	private Pair<Year, Range<LocalDate>> yearByDate(String name, Year year, LocalDate dt) {
		Range<LocalDate> range = yearStrategy.rangeOfYear(name, year);
		if (range.isAfter(dt)) {
			return yearByDate(name, year.minusYears(1L), dt);
		}
		if (range.isBefore(dt)) {
			return yearByDate(name, year.plusYears(1), dt);
		}
		return Pair.of(year, range);
	}

}
