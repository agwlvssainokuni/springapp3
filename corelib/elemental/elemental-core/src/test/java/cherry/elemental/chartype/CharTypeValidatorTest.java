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

import static cherry.elemental.chartype.CharTypeValidator.ALPHA;
import static cherry.elemental.chartype.CharTypeValidator.BASIC_LATIN;
import static cherry.elemental.chartype.CharTypeValidator.CP932;
import static cherry.elemental.chartype.CharTypeValidator.FULL_ALPHA;
import static cherry.elemental.chartype.CharTypeValidator.FULL_HIRAGANA;
import static cherry.elemental.chartype.CharTypeValidator.FULL_KATAKANA;
import static cherry.elemental.chartype.CharTypeValidator.FULL_LOWER;
import static cherry.elemental.chartype.CharTypeValidator.FULL_NUMERIC;
import static cherry.elemental.chartype.CharTypeValidator.FULL_SPACE;
import static cherry.elemental.chartype.CharTypeValidator.FULL_UPPER;
import static cherry.elemental.chartype.CharTypeValidator.FULL_WIDTH;
import static cherry.elemental.chartype.CharTypeValidator.HALF_KATAKANA;
import static cherry.elemental.chartype.CharTypeValidator.HALF_WIDTH;
import static cherry.elemental.chartype.CharTypeValidator.LOWER;
import static cherry.elemental.chartype.CharTypeValidator.NUMERIC;
import static cherry.elemental.chartype.CharTypeValidator.SPACE;
import static cherry.elemental.chartype.CharTypeValidator.UPPER;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CharTypeValidatorTest {

	@Test
	public void testIsValidNone() {
		assertThat(CharTypeValidator.isValid(' ', asList()), is(false));
		assertThat(CharTypeValidator.isValid('0', asList()), is(false));
		assertThat(CharTypeValidator.isValid('A', asList()), is(false));
		assertThat(CharTypeValidator.isValid('a', asList()), is(false));
	}

	@Test
	public void testIsBasicLatin() {
		assertThat(CharTypeValidator.isValid(' ', asList(BASIC_LATIN)), is(true));
		assertThat(CharTypeValidator.isValid('0', asList(BASIC_LATIN)), is(true));
		assertThat(CharTypeValidator.isValid('A', asList(BASIC_LATIN)), is(true));
		assertThat(CharTypeValidator.isValid('a', asList(BASIC_LATIN)), is(true));
		assertThat(CharTypeValidator.isValid('\uFF71', asList(BASIC_LATIN)), is(false));
		assertThat(CharTypeValidator.isValid('Ａ', asList(BASIC_LATIN)), is(false));
	}

	@Test
	public void testIsHalfWidth() {
		assertThat(CharTypeValidator.isValid(' ', asList(HALF_WIDTH)), is(true));
		assertThat(CharTypeValidator.isValid('0', asList(HALF_WIDTH)), is(true));
		assertThat(CharTypeValidator.isValid('A', asList(HALF_WIDTH)), is(true));
		assertThat(CharTypeValidator.isValid('a', asList(HALF_WIDTH)), is(true));
		assertThat(CharTypeValidator.isValid('\uFF71', asList(HALF_WIDTH)), is(true));
		assertThat(CharTypeValidator.isValid('Ａ', asList(HALF_WIDTH)), is(false));
	}

	@Test
	public void testIsFullWidth() {
		assertThat(CharTypeValidator.isValid(' ', asList(FULL_WIDTH)), is(false));
		assertThat(CharTypeValidator.isValid('0', asList(FULL_WIDTH)), is(false));
		assertThat(CharTypeValidator.isValid('A', asList(FULL_WIDTH)), is(false));
		assertThat(CharTypeValidator.isValid('a', asList(FULL_WIDTH)), is(false));
		assertThat(CharTypeValidator.isValid('\uFF71', asList(FULL_WIDTH)), is(false));
		assertThat(CharTypeValidator.isValid('Ａ', asList(FULL_WIDTH)), is(true));
	}

	@Test
	public void testIsValidSpace() {
		assertThat(CharTypeValidator.isValid(' ', asList(SPACE)), is(true));
		assertThat(CharTypeValidator.isValid('0', asList(SPACE)), is(false));
		assertThat(CharTypeValidator.isValid('A', asList(SPACE)), is(false));
		assertThat(CharTypeValidator.isValid('a', asList(SPACE)), is(false));
	}

	@Test
	public void testIsValidNumeric() {
		assertThat(CharTypeValidator.isValid(' ', asList(NUMERIC)), is(false));
		assertThat(CharTypeValidator.isValid('0', asList(NUMERIC)), is(true));
		assertThat(CharTypeValidator.isValid('A', asList(NUMERIC)), is(false));
		assertThat(CharTypeValidator.isValid('a', asList(NUMERIC)), is(false));
	}

	@Test
	public void testIsValidAlpha() {
		assertThat(CharTypeValidator.isValid(' ', asList(ALPHA)), is(false));
		assertThat(CharTypeValidator.isValid('0', asList(ALPHA)), is(false));
		assertThat(CharTypeValidator.isValid('A', asList(ALPHA)), is(true));
		assertThat(CharTypeValidator.isValid('a', asList(ALPHA)), is(true));
	}

	@Test
	public void testIsValidUpper() {
		assertThat(CharTypeValidator.isValid(' ', asList(ALPHA)), is(false));
		assertThat(CharTypeValidator.isValid('0', asList(ALPHA)), is(false));
		assertThat(CharTypeValidator.isValid('A', asList(UPPER)), is(true));
		assertThat(CharTypeValidator.isValid('a', asList(UPPER)), is(false));
	}

	@Test
	public void testIsValidLower() {
		assertThat(CharTypeValidator.isValid(' ', asList(ALPHA)), is(false));
		assertThat(CharTypeValidator.isValid('0', asList(ALPHA)), is(false));
		assertThat(CharTypeValidator.isValid('A', asList(LOWER)), is(false));
		assertThat(CharTypeValidator.isValid('a', asList(LOWER)), is(true));
	}

	@Test
	public void testIsValidFullSpace() {
		assertThat(CharTypeValidator.isValid('　', asList(FULL_SPACE)), is(true));
		assertThat(CharTypeValidator.isValid('０', asList(FULL_SPACE)), is(false));
		assertThat(CharTypeValidator.isValid('Ａ', asList(FULL_SPACE)), is(false));
		assertThat(CharTypeValidator.isValid('ａ', asList(FULL_SPACE)), is(false));
	}

	@Test
	public void testIsValidFullNumeric() {
		assertThat(CharTypeValidator.isValid('　', asList(FULL_NUMERIC)), is(false));
		assertThat(CharTypeValidator.isValid('０', asList(FULL_NUMERIC)), is(true));
		assertThat(CharTypeValidator.isValid('Ａ', asList(FULL_NUMERIC)), is(false));
		assertThat(CharTypeValidator.isValid('ａ', asList(FULL_NUMERIC)), is(false));
	}

	@Test
	public void testIsValidFullAlpha() {
		assertThat(CharTypeValidator.isValid('　', asList(FULL_ALPHA)), is(false));
		assertThat(CharTypeValidator.isValid('０', asList(FULL_ALPHA)), is(false));
		assertThat(CharTypeValidator.isValid('Ａ', asList(FULL_ALPHA)), is(true));
		assertThat(CharTypeValidator.isValid('ａ', asList(FULL_ALPHA)), is(true));
	}

	@Test
	public void testIsValidFullUpper() {
		assertThat(CharTypeValidator.isValid('　', asList(FULL_UPPER)), is(false));
		assertThat(CharTypeValidator.isValid('０', asList(FULL_UPPER)), is(false));
		assertThat(CharTypeValidator.isValid('Ａ', asList(FULL_UPPER)), is(true));
		assertThat(CharTypeValidator.isValid('ａ', asList(FULL_UPPER)), is(false));
	}

	@Test
	public void testIsValidFullLower() {
		assertThat(CharTypeValidator.isValid('　', asList(FULL_LOWER)), is(false));
		assertThat(CharTypeValidator.isValid('０', asList(FULL_LOWER)), is(false));
		assertThat(CharTypeValidator.isValid('Ａ', asList(FULL_LOWER)), is(false));
		assertThat(CharTypeValidator.isValid('ａ', asList(FULL_LOWER)), is(true));
	}

	@Test
	public void testIsValidFullHiragana() {
		assertThat(CharTypeValidator.isValid('あ', asList(FULL_HIRAGANA)), is(true));
		assertThat(CharTypeValidator.isValid('ア', asList(FULL_HIRAGANA)), is(false));
		assertThat(CharTypeValidator.isValid('\uFF71', asList(FULL_HIRAGANA)), is(false));
	}

	@Test
	public void testIsValidFullKatakana() {
		assertThat(CharTypeValidator.isValid('あ', asList(FULL_KATAKANA)), is(false));
		assertThat(CharTypeValidator.isValid('ア', asList(FULL_KATAKANA)), is(true));
		assertThat(CharTypeValidator.isValid('\uFF71', asList(FULL_KATAKANA)), is(false));
	}

	@Test
	public void testIsValidHalfKatakana() {
		assertThat(CharTypeValidator.isValid('あ', asList(HALF_KATAKANA)), is(false));
		assertThat(CharTypeValidator.isValid('ア', asList(HALF_KATAKANA)), is(false));
		assertThat(CharTypeValidator.isValid('\uFF71', asList(HALF_KATAKANA)), is(true));
	}

	@Test
	public void testIsValidCp932() {
		assertThat(CharTypeValidator.isValid('あ', asList(CP932)), is(true));
		assertThat(CharTypeValidator.isValid('ア', asList(CP932)), is(true));
		assertThat(CharTypeValidator.isValid('\uFF71', asList(CP932)), is(true));
		assertThat(CharTypeValidator.isValid('\uFFFF', asList(CP932)), is(false));
	}

	@Test
	public void testIsValidWithAcceptableNull() {
		assertThat(CharTypeValidator.isValid(' ', asList(SPACE), null), is(true));
		assertThat(CharTypeValidator.isValid('0', asList(SPACE), null), is(false));
		assertThat(CharTypeValidator.isValid('A', asList(SPACE), null), is(false));
		assertThat(CharTypeValidator.isValid('a', asList(SPACE), null), is(false));
	}

	@Test
	public void testIsValidWithAcceptable0() {
		int[] acceptable = new int[] {};
		assertThat(CharTypeValidator.isValid(' ', asList(SPACE), acceptable), is(true));
		assertThat(CharTypeValidator.isValid('0', asList(SPACE), acceptable), is(false));
		assertThat(CharTypeValidator.isValid('A', asList(SPACE), acceptable), is(false));
		assertThat(CharTypeValidator.isValid('a', asList(SPACE), acceptable), is(false));
	}

	@Test
	public void testIsValidWithAcceptable1() {
		int[] acceptable = new int[] { '0' };
		assertThat(CharTypeValidator.isValid(' ', asList(SPACE), acceptable), is(true));
		assertThat(CharTypeValidator.isValid('0', asList(SPACE), acceptable), is(true));
		assertThat(CharTypeValidator.isValid('A', asList(SPACE), acceptable), is(false));
		assertThat(CharTypeValidator.isValid('a', asList(SPACE), acceptable), is(false));
	}

	@Test
	public void testIsValidWithAcceptable2() {
		int[] acceptable = new int[] { '0', 'A' };
		assertThat(CharTypeValidator.isValid(' ', asList(SPACE), acceptable), is(true));
		assertThat(CharTypeValidator.isValid('0', asList(SPACE), acceptable), is(true));
		assertThat(CharTypeValidator.isValid('A', asList(SPACE), acceptable), is(true));
		assertThat(CharTypeValidator.isValid('a', asList(SPACE), acceptable), is(false));
	}

	@Test
	public void testValidate0() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharTypeValidator.validate("ABCDEF", asList(ALPHA), acceptable);
		assertThat(result.isValid(), is(true));
		assertThat(result.getIndex(), is(-1));
		assertThat(result.getCodePoint(), is(-1));
	}

	@Test
	public void testValidate1() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharTypeValidator.validate("ABCあDEF", asList(ALPHA), acceptable);
		assertThat(result.isValid(), is(false));
		assertThat(result.getIndex(), is(3));
		assertThat(result.getCodePoint(), is((int) 'あ'));
	}

	@Test
	public void testValidate2() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharTypeValidator.validate("ABC\uD842\uDF9FDEF", asList(ALPHA), acceptable);
		assertThat(result.isValid(), is(false));
		assertThat(result.getIndex(), is(3));
		assertThat(result.getCodePoint(), is(0x20B9F));
	}

	@Test
	public void testValidate3() {
		int[] acceptable = new int[] { 0x20B9F };
		CharTypeResult result = CharTypeValidator.validate("ABC\uD842\uDF9FDEF", asList(ALPHA), acceptable);
		assertThat(result.isValid(), is(true));
		assertThat(result.getIndex(), is(-1));
		assertThat(result.getCodePoint(), is(-1));
	}

}
