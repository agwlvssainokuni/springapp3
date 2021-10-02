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

package cherry.fundamental.validator;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
@SpringBootTest(classes = DataBinderHelperImplTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class DataBinderHelperImplTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testBindAndValidate_OK() {

		Map<String, String> val = new HashMap<>();
		val.put("alpha", "aaa");
		val.put("number", "123");

		TestDto dto1 = new TestDto();
		BindingResult binding1 = dataBinderHelper.bindAndValidate(dto1, new MutablePropertyValues(val));
		assertEquals(0, binding1.getErrorCount());
		assertEquals("aaa", dto1.getAlpha());
		assertEquals("123", dto1.getNumber());

		TestDto dto2 = new TestDto();
		BindingResult binding2 = dataBinderHelper.bindAndValidate(dto2, "testDto", new MutablePropertyValues(val));
		assertEquals(0, binding2.getErrorCount());
		assertEquals("aaa", dto2.getAlpha());
		assertEquals("123", dto2.getNumber());
	}

	@Test
	public void testBindAndValidate_NG() {

		Map<String, String> val = new HashMap<>();
		val.put("alpha", "1234567890");
		val.put("number", "aa");

		TestDto dto1 = new TestDto();
		BindingResult binding1 = dataBinderHelper.bindAndValidate(dto1, new MutablePropertyValues(val));
		assertEquals(4, binding1.getErrorCount());
		assertEquals(0, binding1.getGlobalErrorCount());
		assertEquals(4, binding1.getFieldErrorCount());
		assertEquals(2, binding1.getFieldErrorCount("alpha"));
		assertEquals(2, binding1.getFieldErrorCount("number"));
		assertEquals("1234567890", dto1.getAlpha());
		assertEquals("aa", dto1.getNumber());

		TestDto dto2 = new TestDto();
		BindingResult binding2 = dataBinderHelper.bindAndValidate(dto2, "testDto", new MutablePropertyValues(val));
		assertEquals(4, binding2.getErrorCount());
		assertEquals(0, binding2.getGlobalErrorCount());
		assertEquals(4, binding2.getFieldErrorCount());
		assertEquals(2, binding2.getFieldErrorCount("alpha"));
		assertEquals(2, binding2.getFieldErrorCount("number"));
		assertEquals("1234567890", dto2.getAlpha());
		assertEquals("aa", dto2.getNumber());
	}

	@Test
	public void testResolveAllMessage() {

		Map<String, String> val = new HashMap<>();
		val.put("alpha", "1234567890");
		val.put("number", "aa");

		TestDto dto = new TestDto();
		BindingResult binding = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		List<String> msglist = dataBinderHelper.resolveAllMessage(binding, Locale.getDefault());
		assertEquals(4, msglist.size());
		assertEqualsList(asList("{cherry.fundamental.validator.CharTypeAlpha.message}",
				"{cherry.fundamental.validator.MaxLength.message}",
				"{cherry.fundamental.validator.CharTypeNumeric.message}",
				"{cherry.fundamental.validator.MinLength.message}"), msglist);
	}

	@Test
	public void testResolveGlobalMessage() {

		Map<String, String> val = new HashMap<>();
		val.put("alpha", "1234567890");
		val.put("number", "aa");

		TestDto dto = new TestDto();
		BindingResult binding = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		List<String> msglist = dataBinderHelper.resolveGlobalMessage(binding, Locale.getDefault());
		assertEquals(0, msglist.size());
	}

	@Test
	public void testResolveFiledMessage_ALL() {

		Map<String, String> val = new HashMap<>();
		val.put("alpha", "1234567890");
		val.put("number", "aa");

		TestDto dto = new TestDto();
		BindingResult binding = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		List<String> msglist = dataBinderHelper.resolveFieldMessage(binding, Locale.getDefault());
		assertEquals(4, msglist.size());
		assertEqualsList(asList("{cherry.fundamental.validator.CharTypeAlpha.message}",
				"{cherry.fundamental.validator.MaxLength.message}",
				"{cherry.fundamental.validator.CharTypeNumeric.message}",
				"{cherry.fundamental.validator.MinLength.message}"), msglist);
	}

	@Test
	public void testResolveFiledMessage_EACH() {

		Map<String, String> val = new HashMap<>();
		val.put("alpha", "1234567890");
		val.put("number", "aa");

		TestDto dto = new TestDto();
		BindingResult binding = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		List<String> msglist1 = dataBinderHelper.resolveFieldMessage(binding, "alpha", Locale.getDefault());
		assertEquals(2, msglist1.size());
		assertEqualsList(asList("{cherry.fundamental.validator.CharTypeAlpha.message}",
				"{cherry.fundamental.validator.MaxLength.message}"), msglist1);
		List<String> msglist2 = dataBinderHelper.resolveFieldMessage(binding, "number", Locale.getDefault());
		assertEquals(2, msglist2.size());
		assertEqualsList(asList("{cherry.fundamental.validator.CharTypeNumeric.message}",
				"{cherry.fundamental.validator.MinLength.message}"), msglist2);
	}

	private void assertEqualsList(List<String> expected, List<String> result) {
		List<String> expectedSorted = new ArrayList<>(expected);
		Collections.sort(expectedSorted);
		List<String> resultSorted = new ArrayList<>(result);
		Collections.sort(resultSorted);
		assertEquals(expectedSorted, resultSorted);
	}

	public static class TestDto {

		@CharTypeAlpha
		@MaxLength(5)
		private String alpha;

		@CharTypeNumeric
		@MinLength(3)
		private String number;

		public String getAlpha() {
			return alpha;
		}

		public void setAlpha(String alpha) {
			this.alpha = alpha;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}
	}

}
