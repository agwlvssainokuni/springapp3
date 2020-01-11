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

package cherry.elemental.chartype;

import static cherry.elemental.chartype.CharType.ALPHA;
import static cherry.elemental.chartype.CharType.BASIC_LATIN;
import static cherry.elemental.chartype.CharType.CP932;
import static cherry.elemental.chartype.CharType.FULL_ALPHA;
import static cherry.elemental.chartype.CharType.FULL_HIRAGANA;
import static cherry.elemental.chartype.CharType.FULL_KATAKANA;
import static cherry.elemental.chartype.CharType.FULL_LOWER;
import static cherry.elemental.chartype.CharType.FULL_NUMERIC;
import static cherry.elemental.chartype.CharType.FULL_SPACE;
import static cherry.elemental.chartype.CharType.FULL_UPPER;
import static cherry.elemental.chartype.CharType.FULL_WIDTH;
import static cherry.elemental.chartype.CharType.HALF_KATAKANA;
import static cherry.elemental.chartype.CharType.HALF_WIDTH;
import static cherry.elemental.chartype.CharType.LOWER;
import static cherry.elemental.chartype.CharType.NUMERIC;
import static cherry.elemental.chartype.CharType.SPACE;
import static cherry.elemental.chartype.CharType.UPPER;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CharTypeValidationTest {

	@Test
	public void testIsValidNone() {
		assertThat(CharType.isValid(' ', asList()), is(false));
		assertThat(CharType.isValid('0', asList()), is(false));
		assertThat(CharType.isValid('A', asList()), is(false));
		assertThat(CharType.isValid('a', asList()), is(false));
	}

	@Test
	public void testIsBasicLatin() {
		assertThat(CharType.isValid(' ', asList(BASIC_LATIN)), is(true));
		assertThat(CharType.isValid('0', asList(BASIC_LATIN)), is(true));
		assertThat(CharType.isValid('A', asList(BASIC_LATIN)), is(true));
		assertThat(CharType.isValid('a', asList(BASIC_LATIN)), is(true));
		assertThat(CharType.isValid('\uFF71', asList(BASIC_LATIN)), is(false));
		assertThat(CharType.isValid('Ａ', asList(BASIC_LATIN)), is(false));
	}

	@Test
	public void testIsHalfWidth() {
		assertThat(CharType.isValid(' ', asList(HALF_WIDTH)), is(true));
		assertThat(CharType.isValid('0', asList(HALF_WIDTH)), is(true));
		assertThat(CharType.isValid('A', asList(HALF_WIDTH)), is(true));
		assertThat(CharType.isValid('a', asList(HALF_WIDTH)), is(true));
		assertThat(CharType.isValid('\uFF71', asList(HALF_WIDTH)), is(true));
		assertThat(CharType.isValid('Ａ', asList(HALF_WIDTH)), is(false));
	}

	@Test
	public void testIsFullWidth() {
		assertThat(CharType.isValid(' ', asList(FULL_WIDTH)), is(false));
		assertThat(CharType.isValid('0', asList(FULL_WIDTH)), is(false));
		assertThat(CharType.isValid('A', asList(FULL_WIDTH)), is(false));
		assertThat(CharType.isValid('a', asList(FULL_WIDTH)), is(false));
		assertThat(CharType.isValid('\uFF71', asList(FULL_WIDTH)), is(false));
		assertThat(CharType.isValid('Ａ', asList(FULL_WIDTH)), is(true));
	}

	@Test
	public void testIsValidSpace() {
		assertThat(CharType.isValid(' ', asList(SPACE)), is(true));
		assertThat(CharType.isValid('0', asList(SPACE)), is(false));
		assertThat(CharType.isValid('A', asList(SPACE)), is(false));
		assertThat(CharType.isValid('a', asList(SPACE)), is(false));
	}

	@Test
	public void testIsValidNumeric() {
		assertThat(CharType.isValid(' ', asList(NUMERIC)), is(false));
		assertThat(CharType.isValid('0', asList(NUMERIC)), is(true));
		assertThat(CharType.isValid('A', asList(NUMERIC)), is(false));
		assertThat(CharType.isValid('a', asList(NUMERIC)), is(false));
	}

	@Test
	public void testIsValidAlpha() {
		assertThat(CharType.isValid(' ', asList(ALPHA)), is(false));
		assertThat(CharType.isValid('0', asList(ALPHA)), is(false));
		assertThat(CharType.isValid('A', asList(ALPHA)), is(true));
		assertThat(CharType.isValid('a', asList(ALPHA)), is(true));
	}

	@Test
	public void testIsValidUpper() {
		assertThat(CharType.isValid(' ', asList(ALPHA)), is(false));
		assertThat(CharType.isValid('0', asList(ALPHA)), is(false));
		assertThat(CharType.isValid('A', asList(UPPER)), is(true));
		assertThat(CharType.isValid('a', asList(UPPER)), is(false));
	}

	@Test
	public void testIsValidLower() {
		assertThat(CharType.isValid(' ', asList(ALPHA)), is(false));
		assertThat(CharType.isValid('0', asList(ALPHA)), is(false));
		assertThat(CharType.isValid('A', asList(LOWER)), is(false));
		assertThat(CharType.isValid('a', asList(LOWER)), is(true));
	}

	@Test
	public void testIsValidFullSpace() {
		assertThat(CharType.isValid('　', asList(FULL_SPACE)), is(true));
		assertThat(CharType.isValid('０', asList(FULL_SPACE)), is(false));
		assertThat(CharType.isValid('Ａ', asList(FULL_SPACE)), is(false));
		assertThat(CharType.isValid('ａ', asList(FULL_SPACE)), is(false));
	}

	@Test
	public void testIsValidFullNumeric() {
		assertThat(CharType.isValid('　', asList(FULL_NUMERIC)), is(false));
		assertThat(CharType.isValid('０', asList(FULL_NUMERIC)), is(true));
		assertThat(CharType.isValid('Ａ', asList(FULL_NUMERIC)), is(false));
		assertThat(CharType.isValid('ａ', asList(FULL_NUMERIC)), is(false));
	}

	@Test
	public void testIsValidFullAlpha() {
		assertThat(CharType.isValid('　', asList(FULL_ALPHA)), is(false));
		assertThat(CharType.isValid('０', asList(FULL_ALPHA)), is(false));
		assertThat(CharType.isValid('Ａ', asList(FULL_ALPHA)), is(true));
		assertThat(CharType.isValid('ａ', asList(FULL_ALPHA)), is(true));
	}

	@Test
	public void testIsValidFullUpper() {
		assertThat(CharType.isValid('　', asList(FULL_UPPER)), is(false));
		assertThat(CharType.isValid('０', asList(FULL_UPPER)), is(false));
		assertThat(CharType.isValid('Ａ', asList(FULL_UPPER)), is(true));
		assertThat(CharType.isValid('ａ', asList(FULL_UPPER)), is(false));
	}

	@Test
	public void testIsValidFullLower() {
		assertThat(CharType.isValid('　', asList(FULL_LOWER)), is(false));
		assertThat(CharType.isValid('０', asList(FULL_LOWER)), is(false));
		assertThat(CharType.isValid('Ａ', asList(FULL_LOWER)), is(false));
		assertThat(CharType.isValid('ａ', asList(FULL_LOWER)), is(true));
	}

	@Test
	public void testIsValidFullHiragana() {
		assertThat(CharType.isValid('あ', asList(FULL_HIRAGANA)), is(true));
		assertThat(CharType.isValid('ア', asList(FULL_HIRAGANA)), is(false));
		assertThat(CharType.isValid('\uFF71', asList(FULL_HIRAGANA)), is(false));
	}

	@Test
	public void testIsValidFullKatakana() {
		assertThat(CharType.isValid('あ', asList(FULL_KATAKANA)), is(false));
		assertThat(CharType.isValid('ア', asList(FULL_KATAKANA)), is(true));
		assertThat(CharType.isValid('\uFF71', asList(FULL_KATAKANA)), is(false));
	}

	@Test
	public void testIsValidHalfKatakana() {
		assertThat(CharType.isValid('あ', asList(HALF_KATAKANA)), is(false));
		assertThat(CharType.isValid('ア', asList(HALF_KATAKANA)), is(false));
		assertThat(CharType.isValid('\uFF71', asList(HALF_KATAKANA)), is(true));
	}

	@Test
	public void testIsValidCp932() {
		assertThat(CharType.isValid('あ', asList(CP932)), is(true));
		assertThat(CharType.isValid('ア', asList(CP932)), is(true));
		assertThat(CharType.isValid('\uFF71', asList(CP932)), is(true));
		assertThat(CharType.isValid('\uFFFF', asList(CP932)), is(false));
	}

	@Test
	public void testIsValidWithAcceptableNull() {
		assertThat(CharType.isValid(' ', asList(SPACE), null), is(true));
		assertThat(CharType.isValid('0', asList(SPACE), null), is(false));
		assertThat(CharType.isValid('A', asList(SPACE), null), is(false));
		assertThat(CharType.isValid('a', asList(SPACE), null), is(false));
	}

	@Test
	public void testIsValidWithAcceptable0() {
		int[] acceptable = new int[] {};
		assertThat(CharType.isValid(' ', asList(SPACE), acceptable), is(true));
		assertThat(CharType.isValid('0', asList(SPACE), acceptable), is(false));
		assertThat(CharType.isValid('A', asList(SPACE), acceptable), is(false));
		assertThat(CharType.isValid('a', asList(SPACE), acceptable), is(false));
	}

	@Test
	public void testIsValidWithAcceptable1() {
		int[] acceptable = new int[] { '0' };
		assertThat(CharType.isValid(' ', asList(SPACE), acceptable), is(true));
		assertThat(CharType.isValid('0', asList(SPACE), acceptable), is(true));
		assertThat(CharType.isValid('A', asList(SPACE), acceptable), is(false));
		assertThat(CharType.isValid('a', asList(SPACE), acceptable), is(false));
	}

	@Test
	public void testIsValidWithAcceptable2() {
		int[] acceptable = new int[] { '0', 'A' };
		assertThat(CharType.isValid(' ', asList(SPACE), acceptable), is(true));
		assertThat(CharType.isValid('0', asList(SPACE), acceptable), is(true));
		assertThat(CharType.isValid('A', asList(SPACE), acceptable), is(true));
		assertThat(CharType.isValid('a', asList(SPACE), acceptable), is(false));
	}

	@Test
	public void testValidate0() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharType.validate("ABCDEF", asList(ALPHA), acceptable);
		assertThat(result.isValid(), is(true));
		assertThat(result.getIndex(), is(-1));
		assertThat(result.getCodePoint(), is(-1));
	}

	@Test
	public void testValidate1() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharType.validate("ABCあDEF", asList(ALPHA), acceptable);
		assertThat(result.isValid(), is(false));
		assertThat(result.getIndex(), is(3));
		assertThat(result.getCodePoint(), is((int) 'あ'));
	}

	@Test
	public void testValidate2() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharType.validate("ABC\uD842\uDF9FDEF", asList(ALPHA), acceptable);
		assertThat(result.isValid(), is(false));
		assertThat(result.getIndex(), is(3));
		assertThat(result.getCodePoint(), is(0x20B9F));
	}

	@Test
	public void testValidate3() {
		int[] acceptable = new int[] { 0x20B9F };
		CharTypeResult result = CharType.validate("ABC\uD842\uDF9FDEF", asList(ALPHA), acceptable);
		assertThat(result.isValid(), is(true));
		assertThat(result.getIndex(), is(-1));
		assertThat(result.getCodePoint(), is(-1));
	}

}
