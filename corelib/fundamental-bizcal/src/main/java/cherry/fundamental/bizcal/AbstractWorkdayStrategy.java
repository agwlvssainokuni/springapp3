/*
 * Copyright 2015,2019 agwlvssainokuni
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

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * 営業日ストラテジの抽象実装。<br />
 */
public abstract class AbstractWorkdayStrategy implements WorkdayStrategy {

	@Override
	public int getNumberOfWorkday(String name, LocalDate from, LocalDate to) {
		int count = 0;
		for (long i = 0L;; i++) {
			LocalDate ldt = from.plusDays(i);
			if (ldt.isAfter(to)) {
				return count;
			}
			if (isWorkday(name, ldt)) {
				count += 1;
			}
		}
	}

	@Override
	public LocalDate getNextWorkday(String name, LocalDate from, int numberOfWorkday) {
		int count = 0;
		for (long i = 0L;; i++) {
			LocalDate ldt = from.plusDays(i);
			if (isWorkday(name, ldt)) {
				count += 1;
			}
			if (count >= numberOfWorkday) {
				return ldt;
			}
		}
	}

	protected boolean isWorkday(String name, LocalDate ldt) {
		if (isSpecificOn(name, ldt)) {
			return true;
		}
		if (isSpecificOff(name, ldt)) {
			return false;
		}
		return isRegularOn(name, ldt.getDayOfWeek());
	}

	protected abstract boolean isRegularOn(String name, DayOfWeek dow);

	protected abstract boolean isSpecificOn(String name, LocalDate ldt);

	protected abstract boolean isSpecificOff(String name, LocalDate ldt);

}
