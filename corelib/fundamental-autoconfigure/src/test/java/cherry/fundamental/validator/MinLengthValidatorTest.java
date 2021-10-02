/*
 * Copyright 2014,2021 agwlvssainokuni
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MinLengthValidatorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class MinLengthValidatorTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testOK() {

		Map<String, String> val = new HashMap<>();
		val.put("val0", null);
		val.put("val1", "");
		val.put("val2", "1");
		val.put("val3", "12345");
		val.put("val4", "123456");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(0, result.getErrorCount());
		assertNull(dto.getVal0());
		assertEquals("", dto.getVal1());
		assertEquals("1", dto.getVal2());
		assertEquals("12345", dto.getVal3());
		assertEquals("123456", dto.getVal4());
	}

	@Test
	public void testNG() {

		Map<String, String> val = new HashMap<>();
		val.put("val3", "1234");
		val.put("val4", "123");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(2, result.getErrorCount());
	}

	public static class TestDto {

		@MinLength(1)
		private String val0;
		@MinLength(1)
		private String val1;
		@MinLength(1)
		private String val2;
		@MinLength(5)
		private String val3;
		@MinLength(5)
		private String val4;

		public String getVal0() {
			return val0;
		}

		public void setVal0(String val0) {
			this.val0 = val0;
		}

		public String getVal1() {
			return val1;
		}

		public void setVal1(String val1) {
			this.val1 = val1;
		}

		public String getVal2() {
			return val2;
		}

		public void setVal2(String val2) {
			this.val2 = val2;
		}

		public String getVal3() {
			return val3;
		}

		public void setVal3(String val3) {
			this.val3 = val3;
		}

		public String getVal4() {
			return val4;
		}

		public void setVal4(String val4) {
			this.val4 = val4;
		}
	}

}
