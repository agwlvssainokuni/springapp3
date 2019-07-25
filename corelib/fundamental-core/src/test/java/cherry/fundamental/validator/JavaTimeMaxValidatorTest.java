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

package cherry.fundamental.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JavaTimeMaxValidatorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class JavaTimeMaxValidatorTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testOK() {

		Map<String, String> val = new HashMap<>();
		val.put("val0", null);
		val.put("val1", "3000/01/01");
		val.put("val2", "2999/12/31");
		val.put("val3", "3000/01/01 00:00:00");
		val.put("val4", "2999/12/31 23:59:59");
		val.put("val5", "00:00:00");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(0, result.getErrorCount());
		assertNull(dto.getVal0());
		assertEquals(LocalDate.of(3000, 1, 1), dto.getVal1());
		assertEquals(LocalDate.of(2999, 12, 31), dto.getVal2());
		assertEquals(LocalDateTime.of(3000, 1, 1, 0, 0, 0), dto.getVal3());
		assertEquals(LocalDateTime.of(2999, 12, 31, 23, 59, 59), dto.getVal4());
		assertEquals(LocalTime.of(0, 0, 0), dto.getVal5());
	}

	@Test
	public void testNG() {

		Map<String, String> val = new HashMap<>();
		val.put("val1", "3000/01/02");
		val.put("val2", "3000/01/01");
		val.put("val3", "3000/01/01 00:00:01");
		val.put("val4", "3000/01/01 00:00:00");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(4, result.getErrorCount());
	}

	public static class TestDto {

		@JavaTimeMax(value = "3000-01-01")
		private LocalDate val0;
		@JavaTimeMax(value = "3000-01-01")
		private LocalDate val1;
		@JavaTimeMax(value = "3000-01-01", inclusive = false)
		private LocalDate val2;
		@JavaTimeMax(value = "3000-01-01T00:00:00")
		private LocalDateTime val3;
		@JavaTimeMax(value = "3000-01-01T00:00:00", inclusive = false)
		private LocalDateTime val4;
		@JavaTimeMax()
		private LocalTime val5;

		public LocalDate getVal0() {
			return val0;
		}

		public void setVal0(LocalDate val0) {
			this.val0 = val0;
		}

		public LocalDate getVal1() {
			return val1;
		}

		public void setVal1(LocalDate val1) {
			this.val1 = val1;
		}

		public LocalDate getVal2() {
			return val2;
		}

		public void setVal2(LocalDate val2) {
			this.val2 = val2;
		}

		public LocalDateTime getVal3() {
			return val3;
		}

		public void setVal3(LocalDateTime val3) {
			this.val3 = val3;
		}

		public LocalDateTime getVal4() {
			return val4;
		}

		public void setVal4(LocalDateTime val4) {
			this.val4 = val4;
		}

		public LocalTime getVal5() {
			return val5;
		}

		public void setVal5(LocalTime val5) {
			this.val5 = val5;
		}
	}

}
