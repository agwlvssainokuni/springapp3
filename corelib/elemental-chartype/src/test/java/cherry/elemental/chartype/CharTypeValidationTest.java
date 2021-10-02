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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CharTypeValidationTest {

	@Test
	public void testIsValidNone() {
		assertFalse(CharType.isValid(' ', asList()));
		assertFalse(CharType.isValid('0', asList()));
		assertFalse(CharType.isValid('A', asList()));
		assertFalse(CharType.isValid('a', asList()));
	}

	@Test
	public void testIsBasicLatin() {
		assertTrue(CharType.isValid(' ', asList(BASIC_LATIN)));
		assertTrue(CharType.isValid('0', asList(BASIC_LATIN)));
		assertTrue(CharType.isValid('A', asList(BASIC_LATIN)));
		assertTrue(CharType.isValid('a', asList(BASIC_LATIN)));
		assertFalse(CharType.isValid('\uFF71', asList(BASIC_LATIN)));
		assertFalse(CharType.isValid('Ａ', asList(BASIC_LATIN)));
	}

	@Test
	public void testIsHalfWidth() {
		assertTrue(CharType.isValid(' ', asList(HALF_WIDTH)));
		assertTrue(CharType.isValid('0', asList(HALF_WIDTH)));
		assertTrue(CharType.isValid('A', asList(HALF_WIDTH)));
		assertTrue(CharType.isValid('a', asList(HALF_WIDTH)));
		assertTrue(CharType.isValid('\uFF71', asList(HALF_WIDTH)));
		assertFalse(CharType.isValid('Ａ', asList(HALF_WIDTH)));
	}

	@Test
	public void testIsFullWidth() {
		assertFalse(CharType.isValid(' ', asList(FULL_WIDTH)));
		assertFalse(CharType.isValid('0', asList(FULL_WIDTH)));
		assertFalse(CharType.isValid('A', asList(FULL_WIDTH)));
		assertFalse(CharType.isValid('a', asList(FULL_WIDTH)));
		assertFalse(CharType.isValid('\uFF71', asList(FULL_WIDTH)));
		assertTrue(CharType.isValid('Ａ', asList(FULL_WIDTH)));
	}

	@Test
	public void testIsValidSpace() {
		assertTrue(CharType.isValid(' ', asList(SPACE)));
		assertFalse(CharType.isValid('0', asList(SPACE)));
		assertFalse(CharType.isValid('A', asList(SPACE)));
		assertFalse(CharType.isValid('a', asList(SPACE)));
	}

	@Test
	public void testIsValidNumeric() {
		assertFalse(CharType.isValid(' ', asList(NUMERIC)));
		assertTrue(CharType.isValid('0', asList(NUMERIC)));
		assertFalse(CharType.isValid('A', asList(NUMERIC)));
		assertFalse(CharType.isValid('a', asList(NUMERIC)));
	}

	@Test
	public void testIsValidAlpha() {
		assertFalse(CharType.isValid(' ', asList(ALPHA)));
		assertFalse(CharType.isValid('0', asList(ALPHA)));
		assertTrue(CharType.isValid('A', asList(ALPHA)));
		assertTrue(CharType.isValid('a', asList(ALPHA)));
	}

	@Test
	public void testIsValidUpper() {
		assertFalse(CharType.isValid(' ', asList(ALPHA)));
		assertFalse(CharType.isValid('0', asList(ALPHA)));
		assertTrue(CharType.isValid('A', asList(UPPER)));
		assertFalse(CharType.isValid('a', asList(UPPER)));
	}

	@Test
	public void testIsValidLower() {
		assertFalse(CharType.isValid(' ', asList(ALPHA)));
		assertFalse(CharType.isValid('0', asList(ALPHA)));
		assertFalse(CharType.isValid('A', asList(LOWER)));
		assertTrue(CharType.isValid('a', asList(LOWER)));
	}

	@Test
	public void testIsValidFullSpace() {
		assertTrue(CharType.isValid('　', asList(FULL_SPACE)));
		assertFalse(CharType.isValid('０', asList(FULL_SPACE)));
		assertFalse(CharType.isValid('Ａ', asList(FULL_SPACE)));
		assertFalse(CharType.isValid('ａ', asList(FULL_SPACE)));
	}

	@Test
	public void testIsValidFullNumeric() {
		assertFalse(CharType.isValid('　', asList(FULL_NUMERIC)));
		assertTrue(CharType.isValid('０', asList(FULL_NUMERIC)));
		assertFalse(CharType.isValid('Ａ', asList(FULL_NUMERIC)));
		assertFalse(CharType.isValid('ａ', asList(FULL_NUMERIC)));
	}

	@Test
	public void testIsValidFullAlpha() {
		assertFalse(CharType.isValid('　', asList(FULL_ALPHA)));
		assertFalse(CharType.isValid('０', asList(FULL_ALPHA)));
		assertTrue(CharType.isValid('Ａ', asList(FULL_ALPHA)));
		assertTrue(CharType.isValid('ａ', asList(FULL_ALPHA)));
	}

	@Test
	public void testIsValidFullUpper() {
		assertFalse(CharType.isValid('　', asList(FULL_UPPER)));
		assertFalse(CharType.isValid('０', asList(FULL_UPPER)));
		assertTrue(CharType.isValid('Ａ', asList(FULL_UPPER)));
		assertFalse(CharType.isValid('ａ', asList(FULL_UPPER)));
	}

	@Test
	public void testIsValidFullLower() {
		assertFalse(CharType.isValid('　', asList(FULL_LOWER)));
		assertFalse(CharType.isValid('０', asList(FULL_LOWER)));
		assertFalse(CharType.isValid('Ａ', asList(FULL_LOWER)));
		assertTrue(CharType.isValid('ａ', asList(FULL_LOWER)));
	}

	@Test
	public void testIsValidFullHiragana() {
		assertTrue(CharType.isValid('あ', asList(FULL_HIRAGANA)));
		assertFalse(CharType.isValid('ア', asList(FULL_HIRAGANA)));
		assertFalse(CharType.isValid('\uFF71', asList(FULL_HIRAGANA)));
	}

	@Test
	public void testIsValidFullKatakana() {
		assertFalse(CharType.isValid('あ', asList(FULL_KATAKANA)));
		assertTrue(CharType.isValid('ア', asList(FULL_KATAKANA)));
		assertFalse(CharType.isValid('\uFF71', asList(FULL_KATAKANA)));
	}

	@Test
	public void testIsValidHalfKatakana() {
		assertFalse(CharType.isValid('あ', asList(HALF_KATAKANA)));
		assertFalse(CharType.isValid('ア', asList(HALF_KATAKANA)));
		assertTrue(CharType.isValid('\uFF71', asList(HALF_KATAKANA)));
	}

	@Test
	public void testIsValidCp932() {
		assertTrue(CharType.isValid('あ', asList(CP932)));
		assertTrue(CharType.isValid('ア', asList(CP932)));
		assertTrue(CharType.isValid('\uFF71', asList(CP932)));
		assertFalse(CharType.isValid('\uFFFF', asList(CP932)));
	}

	@Test
	public void testIsValidWithAcceptableNull() {
		assertTrue(CharType.isValid(' ', asList(SPACE), null));
		assertFalse(CharType.isValid('0', asList(SPACE), null));
		assertFalse(CharType.isValid('A', asList(SPACE), null));
		assertFalse(CharType.isValid('a', asList(SPACE), null));
	}

	@Test
	public void testIsValidWithAcceptable0() {
		int[] acceptable = new int[] {};
		assertTrue(CharType.isValid(' ', asList(SPACE), acceptable));
		assertFalse(CharType.isValid('0', asList(SPACE), acceptable));
		assertFalse(CharType.isValid('A', asList(SPACE), acceptable));
		assertFalse(CharType.isValid('a', asList(SPACE), acceptable));
	}

	@Test
	public void testIsValidWithAcceptable1() {
		int[] acceptable = new int[] { '0' };
		assertTrue(CharType.isValid(' ', asList(SPACE), acceptable));
		assertTrue(CharType.isValid('0', asList(SPACE), acceptable));
		assertFalse(CharType.isValid('A', asList(SPACE), acceptable));
		assertFalse(CharType.isValid('a', asList(SPACE), acceptable));
	}

	@Test
	public void testIsValidWithAcceptable2() {
		int[] acceptable = new int[] { '0', 'A' };
		assertTrue(CharType.isValid(' ', asList(SPACE), acceptable));
		assertTrue(CharType.isValid('0', asList(SPACE), acceptable));
		assertTrue(CharType.isValid('A', asList(SPACE), acceptable));
		assertFalse(CharType.isValid('a', asList(SPACE), acceptable));
	}

	@Test
	public void testValidate0() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharType.validate("ABCDEF", asList(ALPHA), acceptable);
		assertTrue(result.isValid());
		assertEquals(result.getIndex(), -1);
		assertEquals(result.getCodePoint(), -1);
	}

	@Test
	public void testValidate1() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharType.validate("ABCあDEF", asList(ALPHA), acceptable);
		assertFalse(result.isValid());
		assertEquals(result.getIndex(), 3);
		assertEquals(result.getCodePoint(), (int) 'あ');
	}

	@Test
	public void testValidate2() {
		int[] acceptable = new int[] {};
		CharTypeResult result = CharType.validate("ABC\uD842\uDF9FDEF", asList(ALPHA), acceptable);
		assertFalse(result.isValid());
		assertEquals(result.getIndex(), 3);
		assertEquals(result.getCodePoint(), 0x20B9F);
	}

	@Test
	public void testValidate3() {
		int[] acceptable = new int[] { 0x20B9F };
		CharTypeResult result = CharType.validate("ABC\uD842\uDF9FDEF", asList(ALPHA), acceptable);
		assertTrue(result.isValid());
		assertEquals(result.getIndex(), -1);
		assertEquals(result.getCodePoint(), -1);
	}

}
