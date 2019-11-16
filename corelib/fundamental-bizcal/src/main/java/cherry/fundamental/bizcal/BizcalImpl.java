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
import java.time.temporal.ChronoUnit;

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
	public Year getBizYear() {
		return getBizYear(stdCalName);
	}

	@Override
	public Year getBizYear(String name) {
		return getBizYear(name, today(name));
	}

	@Override
	public Year getBizYear(LocalDate dt) {
		return getBizYear(stdCalName, dt);
	}

	@Override
	public Year getBizYear(String name, LocalDate dt) {
		return bizYearByDate(name, dt).getLeft();
	}

	@Override
	public LocalDate getFirstOfBizYear() {
		return getFirstOfBizYear(stdCalName);
	}

	@Override
	public LocalDate getFirstOfBizYear(String name) {
		return getFirstOfBizYear(name, today(name));
	}

	@Override
	public LocalDate getFirstOfBizYear(LocalDate dt) {
		return getFirstOfBizYear(stdCalName, dt);
	}

	@Override
	public LocalDate getFirstOfBizYear(String name, LocalDate dt) {
		return bizYearByDate(name, dt).getRight().getMinimum();
	}

	@Override
	public LocalDate getFirstOfBizYear(Year bizYear) {
		return getFirstOfBizYear(stdCalName, bizYear);
	}

	@Override
	public LocalDate getFirstOfBizYear(String name, Year bizYear) {
		return yearStrategy.rangeOfBizYear(name, bizYear).getMinimum();
	}

	@Override
	public LocalDate getLastOfBizYear() {
		return getLastOfBizYear(stdCalName);
	}

	@Override
	public LocalDate getLastOfBizYear(String name) {
		return getLastOfBizYear(name, today(name));
	}

	@Override
	public LocalDate getLastOfBizYear(LocalDate dt) {
		return getLastOfBizYear(stdCalName, dt);
	}

	@Override
	public LocalDate getLastOfBizYear(String name, LocalDate dt) {
		return bizYearByDate(name, dt).getRight().getMaximum();
	}

	@Override
	public LocalDate getLastOfBizYear(Year bizYear) {
		return getLastOfBizYear(stdCalName, bizYear);
	}

	@Override
	public LocalDate getLastOfBizYear(String name, Year bizYear) {
		return yearStrategy.rangeOfBizYear(name, bizYear).getMaximum();
	}

	@Override
	public int getNumberOfDaysOfBizYear() {
		return getNumberOfDaysOfBizYear(stdCalName);
	}

	@Override
	public int getNumberOfDaysOfBizYear(String name) {
		return getNumberOfDaysOfBizYear(name, today(name));
	}

	@Override
	public int getNumberOfDaysOfBizYear(LocalDate dt) {
		return getNumberOfDaysOfBizYear(stdCalName, dt);
	}

	@Override
	public int getNumberOfDaysOfBizYear(String name, LocalDate dt) {
		Range<LocalDate> range = bizYearByDate(name, dt).getRight();
		return (int) range.getMinimum().until(range.getMaximum().plusDays(1), ChronoUnit.DAYS);
	}

	@Override
	public int getNumberOfDaysOfBizYear(Year bizYear) {
		return getNumberOfDaysOfBizYear(stdCalName, bizYear);
	}

	@Override
	public int getNumberOfDaysOfBizYear(String name, Year bizYear) {
		Range<LocalDate> range = yearStrategy.rangeOfBizYear(name, bizYear);
		return (int) range.getMinimum().until(range.getMaximum().plusDays(1), ChronoUnit.DAYS);
	}

	@Override
	public int getNthDayOfBizYear() {
		return getNthDayOfBizYear(stdCalName);
	}

	@Override
	public int getNthDayOfBizYear(String name) {
		return getNthDayOfBizYear(name, today(name));
	}

	@Override
	public int getNthDayOfBizYear(LocalDate dt) {
		return getNthDayOfBizYear(stdCalName, dt);
	}

	@Override
	public int getNthDayOfBizYear(String name, LocalDate dt) {
		Range<LocalDate> range = bizYearByDate(name, dt).getRight();
		return (int) range.getMinimum().until(dt.plusDays(1), ChronoUnit.DAYS);
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

	private Pair<Year, Range<LocalDate>> bizYearByDate(String name, LocalDate dt) {
		return bizYearByDate(name, Year.of(dt.getYear()), dt);
	}

	private Pair<Year, Range<LocalDate>> bizYearByDate(String name, Year year, LocalDate dt) {
		Range<LocalDate> range = yearStrategy.rangeOfBizYear(name, year);
		if (range.isAfter(dt)) {
			return bizYearByDate(name, year.minusYears(1L), dt);
		}
		if (range.isBefore(dt)) {
			return bizYearByDate(name, year.plusYears(1), dt);
		}
		return Pair.of(year, range);
	}

}
