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
@SpringBootTest(classes = TelNoValidatorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class TelNoValidatorTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testOK() {

		Map<String, String> val = new HashMap<>();
		val.put("telNo0", null);
		val.put("telNo1", "");
		val.put("telNo2", "01-234-5678");
		val.put("telNo3", "01-2345-6789");
		val.put("telNo4", "012-345-6789");
		val.put("telNo5", "0123-45-6789");
		val.put("telNo6", "01234-5-6789");
		val.put("telNo7", "090-1234-5678");
		val.put("telNo8", "0120-123-456");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(0, result.getErrorCount());
		assertNull(dto.getTelNo0());
		assertEquals("", dto.getTelNo1());
		assertEquals("01-234-5678", dto.getTelNo2());
		assertEquals("01-2345-6789", dto.getTelNo3());
		assertEquals("012-345-6789", dto.getTelNo4());
		assertEquals("0123-45-6789", dto.getTelNo5());
		assertEquals("01234-5-6789", dto.getTelNo6());
		assertEquals("090-1234-5678", dto.getTelNo7());
		assertEquals("0120-123-456", dto.getTelNo8());
	}

	@Test
	public void testNG() {

		Map<String, String> val = new HashMap<>();
		val.put("telNo0", "01-234-567");
		val.put("telNo1", "0123-1234-5678");
		val.put("telNo2", "0a-2345-6789");
		val.put("telNo3", "01-a345-6789");
		val.put("telNo4", "01-234a-6789");
		val.put("telNo5", "01-2345-a789");
		val.put("telNo6", "01-2345-678a");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(7, result.getErrorCount());
	}

	public static class TestDto {

		@TelNo
		private String telNo0;
		@TelNo
		private String telNo1;
		@TelNo
		private String telNo2;
		@TelNo
		private String telNo3;
		@TelNo
		private String telNo4;
		@TelNo
		private String telNo5;
		@TelNo
		private String telNo6;
		@TelNo
		private String telNo7;
		@TelNo
		private String telNo8;

		public String getTelNo0() {
			return telNo0;
		}

		public void setTelNo0(String telNo0) {
			this.telNo0 = telNo0;
		}

		public String getTelNo1() {
			return telNo1;
		}

		public void setTelNo1(String telNo1) {
			this.telNo1 = telNo1;
		}

		public String getTelNo2() {
			return telNo2;
		}

		public void setTelNo2(String telNo2) {
			this.telNo2 = telNo2;
		}

		public String getTelNo3() {
			return telNo3;
		}

		public void setTelNo3(String telNo3) {
			this.telNo3 = telNo3;
		}

		public String getTelNo4() {
			return telNo4;
		}

		public void setTelNo4(String telNo4) {
			this.telNo4 = telNo4;
		}

		public String getTelNo5() {
			return telNo5;
		}

		public void setTelNo5(String telNo5) {
			this.telNo5 = telNo5;
		}

		public String getTelNo6() {
			return telNo6;
		}

		public void setTelNo6(String telNo6) {
			this.telNo6 = telNo6;
		}

		public String getTelNo7() {
			return telNo7;
		}

		public void setTelNo7(String telNo7) {
			this.telNo7 = telNo7;
		}

		public String getTelNo8() {
			return telNo8;
		}

		public void setTelNo8(String telNo8) {
			this.telNo8 = telNo8;
		}
	}

}
