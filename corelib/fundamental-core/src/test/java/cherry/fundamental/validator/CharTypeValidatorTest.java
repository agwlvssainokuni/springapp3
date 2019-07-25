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

import static cherry.elemental.chartype.CharType.ALPHA;
import static cherry.elemental.chartype.CharType.FULL_WIDTH;
import static cherry.elemental.chartype.CharType.LOWER;
import static cherry.elemental.chartype.CharType.NUMERIC;
import static cherry.elemental.chartype.CharType.SPACE;
import static cherry.elemental.chartype.CharType.UPPER;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
@SpringBootTest(classes = CharTypeValidatorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class CharTypeValidatorTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testOK() {

		Map<String, String> val = new HashMap<>();
		val.put("none", "");
		val.put("space", " \t\r\n");
		val.put("numeric", "0123456789");
		val.put("alpha", "ABCabc");
		val.put("upper", "ABC");
		val.put("lower", "abc");
		val.put("surrogate", "\uD842\uDF9F");
		val.put("cp932fw", "あいうえお");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertThat(result.getErrorCount(), is(0));
		assertThat(dto.getSpace(), is(" \t\r\n"));
		assertThat(dto.getNumeric(), is("0123456789"));
		assertThat(dto.getAlpha(), is("ABCabc"));
		assertThat(dto.getUpper(), is("ABC"));
		assertThat(dto.getLower(), is("abc"));
		assertThat(dto.getSurrogate(), is("\uD842\uDF9F"));
		assertThat(dto.getCp932fw(), is("あいうえお"));
	}

	@Test
	public void testNG() {

		Map<String, String> val = new HashMap<>();
		val.put("none", "0");
		val.put("space", " \t\r\n0");
		val.put("numeric", "0123456789A");
		val.put("alpha", "ABCabc0");
		val.put("upper", "ABCa");
		val.put("lower", "abcA");
		val.put("cp932fw", "あいうえお\u30A0");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertThat(result.getErrorCount(), is(7));
	}

	public static class TestDto {

		@CharTypeCheck({})
		private String none;

		@CharTypeCheck(SPACE)
		private String space;

		@CharTypeCheck(NUMERIC)
		private String numeric;

		@CharTypeCheck(ALPHA)
		private String alpha;

		@CharTypeCheck(UPPER)
		private String upper;

		@CharTypeCheck(LOWER)
		private String lower;

		@CharTypeCheck(value = {}, acceptable = "\uD842\uDF9F" /* \u20B9F */)
		private String surrogate;

		@CharTypeCp932
		@CharTypeCheck(FULL_WIDTH)
		private String cp932fw;

		public String getNone() {
			return none;
		}

		public void setNone(String none) {
			this.none = none;
		}

		public String getSpace() {
			return space;
		}

		public void setSpace(String space) {
			this.space = space;
		}

		public String getNumeric() {
			return numeric;
		}

		public void setNumeric(String numeric) {
			this.numeric = numeric;
		}

		public String getAlpha() {
			return alpha;
		}

		public void setAlpha(String alpha) {
			this.alpha = alpha;
		}

		public String getUpper() {
			return upper;
		}

		public void setUpper(String upper) {
			this.upper = upper;
		}

		public String getLower() {
			return lower;
		}

		public void setLower(String lower) {
			this.lower = lower;
		}

		public String getSurrogate() {
			return surrogate;
		}

		public void setSurrogate(String surrogate) {
			this.surrogate = surrogate;
		}

		public String getCp932fw() {
			return cp932fw;
		}

		public void setCp932fw(String cp932fw) {
			this.cp932fw = cp932fw;
		}
	}

}
