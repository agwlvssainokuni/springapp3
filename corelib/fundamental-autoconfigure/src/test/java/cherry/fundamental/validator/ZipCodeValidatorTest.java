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
@SpringBootTest(classes = ZipCodeValidatorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class ZipCodeValidatorTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testOK() {

		Map<String, String> val = new HashMap<>();
		val.put("zipCode0", null);
		val.put("zipCode1", "");
		val.put("zipCode2", "1234567");
		val.put("zipCode3", null);
		val.put("zipCode4", "");
		val.put("zipCode5", "123-4567");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(0, result.getErrorCount());
		assertNull(dto.getZipCode0());
		assertEquals("", dto.getZipCode1());
		assertEquals("1234567", dto.getZipCode2());
		assertNull(dto.getZipCode3());
		assertEquals("", dto.getZipCode4());
		assertEquals("123-4567", dto.getZipCode5());
	}

	@Test
	public void testNG() {

		Map<String, String> val = new HashMap<>();
		val.put("zipCode0", "123456");
		val.put("zipCode1", "12345678");
		val.put("zipCode2", "abcdefg");
		val.put("zipCode3", "123-456");
		val.put("zipCode4", "123-45678");
		val.put("zipCode5", "abc-defg");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(6, result.getErrorCount());
	}

	public static class TestDto {

		@ZipCode
		private String zipCode0;
		@ZipCode
		private String zipCode1;
		@ZipCode
		private String zipCode2;
		@ZipCode(hyphen = true)
		private String zipCode3;
		@ZipCode(hyphen = true)
		private String zipCode4;
		@ZipCode(hyphen = true)
		private String zipCode5;

		public String getZipCode0() {
			return zipCode0;
		}

		public void setZipCode0(String zipCode0) {
			this.zipCode0 = zipCode0;
		}

		public String getZipCode1() {
			return zipCode1;
		}

		public void setZipCode1(String zipCode1) {
			this.zipCode1 = zipCode1;
		}

		public String getZipCode2() {
			return zipCode2;
		}

		public void setZipCode2(String zipCode2) {
			this.zipCode2 = zipCode2;
		}

		public String getZipCode3() {
			return zipCode3;
		}

		public void setZipCode3(String zipCode3) {
			this.zipCode3 = zipCode3;
		}

		public String getZipCode4() {
			return zipCode4;
		}

		public void setZipCode4(String zipCode4) {
			this.zipCode4 = zipCode4;
		}

		public String getZipCode5() {
			return zipCode5;
		}

		public void setZipCode5(String zipCode5) {
			this.zipCode5 = zipCode5;
		}
	}

}
