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
@SpringBootTest(classes = NotBlankValidatorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class NotBlankValidatorTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testOK() {

		Map<String, String> val = new HashMap<>();
		val.put("notBlank0", "a");
		val.put("notBlank1", " a ");
		val.put("notBlank2", "　a　");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(0, result.getErrorCount());
		assertEquals("a", dto.getNotBlank0());
		assertEquals(" a ", dto.getNotBlank1());
		assertEquals("　a　", dto.getNotBlank2());
	}

	@Test
	public void testNG() {

		Map<String, String> val = new HashMap<>();
		val.put("notBlank0", "");
		val.put("notBlank1", "  ");
		val.put("notBlank2", "　　");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(3, result.getErrorCount());
		assertEquals("", dto.getNotBlank0());
		assertEquals("  ", dto.getNotBlank1());
		assertEquals("　　", dto.getNotBlank2());
	}

	public static class TestDto {

		@NotBlank()
		private String notBlank0;
		@NotBlank()
		private String notBlank1;
		@NotBlank()
		private String notBlank2;

		public String getNotBlank0() {
			return notBlank0;
		}

		public void setNotBlank0(String notBlank0) {
			this.notBlank0 = notBlank0;
		}

		public String getNotBlank1() {
			return notBlank1;
		}

		public void setNotBlank1(String notBlank1) {
			this.notBlank1 = notBlank1;
		}

		public String getNotBlank2() {
			return notBlank2;
		}

		public void setNotBlank2(String notBlank2) {
			this.notBlank2 = notBlank2;
		}
	}

}
