/*
 * Copyright 2016,2021 agwlvssainokuni
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
@SpringBootTest(classes = NoMarkupTagValidatorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class NoMarkupTagValidatorTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testOK() {

		Map<String, String> val = new HashMap<>();
		val.put("text0", null);
		val.put("text1", "");
		val.put("text2", "あいうえお<a>かきくけこ</a>さしすせそ");
		val.put("text3", "あいうえお\n<a>かきくけこ</a>\n<B>さしすせそ</B>\n");
		val.put("text4", "あいうえお\n<h1>かきくけこ</h1>\n<H2>さしすせそ</H2>\n");
		val.put("text5", "あいうえお\n<script>かきくけこ</script>\n<STYLE>さしすせそ</STYLE>\n");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(0, result.getErrorCount());
		assertNull(dto.getText0());
		assertEquals("", dto.getText1());
		assertEquals("あいうえお<a>かきくけこ</a>さしすせそ", dto.getText2());
		assertEquals("あいうえお\n<a>かきくけこ</a>\n<B>さしすせそ</B>\n", dto.getText3());
		assertEquals("あいうえお\n<h1>かきくけこ</h1>\n<H2>さしすせそ</H2>\n", dto.getText4());
		assertEquals("あいうえお\n<script>かきくけこ</script>\n<STYLE>さしすせそ</STYLE>\n", dto.getText5());
	}

	@Test
	public void testNG() {

		Map<String, String> val = new HashMap<>();
		val.put("text1", "あいうえお<a>かきくけこ</a>さしすせそ");
		val.put("text2", "あいうえお<b>かきくけこ</b>さしすせそ");
		val.put("text3", "あいうえお\n<aa>かきくけこ</aa>\n<BB>さしすせそ</BB>\n");
		val.put("text4", "あいうえお\n<h1h1>かきくけこ</h1h1>\n<H2H2>さしすせそ</H2H2>\n");
		val.put("text5", "あいうえお\n<scriptscript>かきくけこ</scriptscript>\n<STYLESTYLE>さしすせそ</STYLESTYLE>\n");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(5, result.getErrorCount());
	}

	public static class TestDto {

		@NoMarkupTag()
		private String text0;
		@NoMarkupTag()
		private String text1;
		@NoMarkupTag(acceptable = "a")
		private String text2;
		@NoMarkupTag(acceptable = { "a", "b" })
		private String text3;
		@NoMarkupTag(acceptable = { "h1", "h2" })
		private String text4;
		@NoMarkupTag(acceptable = { "script", "style" })
		private String text5;

		public String getText0() {
			return text0;
		}

		public void setText0(String text0) {
			this.text0 = text0;
		}

		public String getText1() {
			return text1;
		}

		public void setText1(String text1) {
			this.text1 = text1;
		}

		public String getText2() {
			return text2;
		}

		public void setText2(String text2) {
			this.text2 = text2;
		}

		public String getText3() {
			return text3;
		}

		public void setText3(String text3) {
			this.text3 = text3;
		}

		public String getText4() {
			return text4;
		}

		public void setText4(String text4) {
			this.text4 = text4;
		}

		public String getText5() {
			return text5;
		}

		public void setText5(String text5) {
			this.text5 = text5;
		}
	}

}
