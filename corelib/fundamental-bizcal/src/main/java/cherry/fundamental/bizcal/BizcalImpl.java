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
		return dateTimeStrategy.today();
	}

	@Override
	public LocalDateTime now() {
		return dateTimeStrategy.now();
	}

	@Override
	public int getBizYear() {
		return getBizYear(dateTimeStrategy.today());
	}

	@Override
	public int getBizYear(LocalDate dt) {
		return bizYearByDate(dt).getLeft().intValue();
	}

	@Override
	public LocalDate getFirstOfBizYear() {
		return getFirstOfBizYear(dateTimeStrategy.today());
	}

	@Override
	public LocalDate getFirstOfBizYear(LocalDate dt) {
		return bizYearByDate(dt).getRight().getMinimum();
	}

	@Override
	public LocalDate getFirstOfBizYear(int bizYear) {
		return yearStrategy.rangeOfBizYear(bizYear).getMinimum();
	}

	@Override
	public LocalDate getLastOfBizYear() {
		return getLastOfBizYear(dateTimeStrategy.today());
	}

	@Override
	public LocalDate getLastOfBizYear(LocalDate dt) {
		return bizYearByDate(dt).getRight().getMaximum();
	}

	@Override
	public LocalDate getLastOfBizYear(int bizYear) {
		return yearStrategy.rangeOfBizYear(bizYear).getMaximum();
	}

	@Override
	public int getNumberOfDaysOfBizYear() {
		return getNumberOfDaysOfBizYear(dateTimeStrategy.today());
	}

	@Override
	public int getNumberOfDaysOfBizYear(LocalDate dt) {
		Range<LocalDate> range = bizYearByDate(dt).getRight();
		return (int) range.getMinimum().until(range.getMaximum().plusDays(1), ChronoUnit.DAYS);
	}

	@Override
	public int getNumberOfDaysOfBizYear(int bizYear) {
		Range<LocalDate> range = yearStrategy.rangeOfBizYear(bizYear);
		return (int) range.getMinimum().until(range.getMaximum().plusDays(1), ChronoUnit.DAYS);
	}

	@Override
	public int getNthDayOfBizYear() {
		return getNthDayOfBizYear(dateTimeStrategy.today());
	}

	@Override
	public int getNthDayOfBizYear(LocalDate dt) {
		Range<LocalDate> range = bizYearByDate(dt).getRight();
		return (int) range.getMinimum().until(dt.plusDays(1), ChronoUnit.DAYS);
	}

	@Override
	public int getNumberOfWorkday(LocalDate to) {
		return getNumberOfWorkday(stdCalName, dateTimeStrategy.today(), to);
	}

	@Override
	public int getNumberOfWorkday(String name, LocalDate to) {
		return getNumberOfWorkday(name, dateTimeStrategy.today(), to);
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
		return getNextWorkday(stdCalName, dateTimeStrategy.today(), numberOfWorkday);
	}

	@Override
	public LocalDate getNextWorkday(String name, int numberOfWorkday) {
		return getNextWorkday(name, dateTimeStrategy.today(), numberOfWorkday);
	}

	@Override
	public LocalDate getNextWorkday(LocalDate from, int numberOfWorkday) {
		return getNextWorkday(stdCalName, from, numberOfWorkday);
	}

	@Override
	public LocalDate getNextWorkday(String name, LocalDate from, int numberOfWorkday) {
		return workdayStrategy.getNextWorkday(name, from, numberOfWorkday);
	}

	private Pair<Integer, Range<LocalDate>> bizYearByDate(LocalDate dt) {
		return bizYearByDate(dt.getYear(), dt);
	}

	private Pair<Integer, Range<LocalDate>> bizYearByDate(int year, LocalDate dt) {
		Range<LocalDate> range = yearStrategy.rangeOfBizYear(year);
		if (range.isAfter(dt)) {
			return bizYearByDate(year - 1, dt);
		}
		if (range.isBefore(dt)) {
			return bizYearByDate(year + 1, dt);
		}
		return Pair.of(year, range);
	}

}
