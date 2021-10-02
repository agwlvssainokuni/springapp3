/*
 * Copyright 2015,2021 agwlvssainokuni
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

package cherry.fundamental.testtool;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ToolTester {

	void toBeInvoked0();

	long toBeInvoked1(long a, long b);

	Long toBeInvoked2(Long a, Long b);

	LocalDateTime toBeInvoked3(LocalDate dt, LocalTime tm);

	Dto1 toBeInvoked4(Dto1 a, Dto1 b);

	Dto2 toBeInvoked5(Dto2 a, Dto2 b);

	long toBeInvoked6(long a, long b);

	int toBeInvoked6(int a, int b);

	Long toBeStubbed1(Long p1, Long p2);

	BigDecimal toBeStubbed1(BigDecimal p1, BigDecimal p2);

	LocalDateTime toBeStubbed2(LocalDate p1, LocalTime p2);

	public static class Dto1 {

		private Long val1;
		private Long val2;

		public Long getVal1() {
			return val1;
		}

		public void setVal1(Long val1) {
			this.val1 = val1;
		}

		public Long getVal2() {
			return val2;
		}

		public void setVal2(Long val2) {
			this.val2 = val2;
		}
	}

	public static class Dto2 {

		private Dto1 val1;
		private Dto1 val2;

		public Dto1 getVal1() {
			return val1;
		}

		public void setVal1(Dto1 val1) {
			this.val1 = val1;
		}

		public Dto1 getVal2() {
			return val2;
		}

		public void setVal2(Dto1 val2) {
			this.val2 = val2;
		}
	}

}
