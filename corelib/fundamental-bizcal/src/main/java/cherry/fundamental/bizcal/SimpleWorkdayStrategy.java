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
 * 営業日。<br />
 */
public class SimpleWorkdayStrategy extends AbstractWorkdayStrategy {

	@Override
	protected boolean isOnSpecific(String name, LocalDate ldt) {
		return false;
	}

	protected boolean isOffSpecific(String name, LocalDate ldt) {
		return false;
	}

	protected boolean isOnRegular(String name, DayOfWeek dow) {
		return true;
	}

}
