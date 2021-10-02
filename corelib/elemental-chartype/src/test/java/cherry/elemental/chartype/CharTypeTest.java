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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import cherry.elemental.chartype.TableReader.Entry;

public class CharTypeTest {

	private TableReader tableReader = new TableReader();

	@Test
	public void testIsBasicLatin() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (win31j <= 0x007F) {
				assertTrue(CharType.isBasicLatin(unicode));
			} else {
				assertFalse(CharType.isBasicLatin(unicode));
			}
		}
	}

	@Test
	public void testIsHalfWidth() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (win31j <= 0x007F) {
				assertTrue(CharType.isHalfWidth(unicode));
			} else if (win31j >= 0x00A1 && win31j <= 0x00DF) {
				assertTrue(CharType.isHalfWidth(unicode));
			} else {
				assertFalse(CharType.isHalfWidth(unicode));
			}
		}
	}

	@Test
	public void testIsFullWidth() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (win31j <= 0x007F) {
				assertFalse(CharType.isFullWidth(unicode));
			} else if (win31j >= 0x00A1 && win31j <= 0x00DF) {
				assertFalse(CharType.isFullWidth(unicode));
			} else {
				assertTrue(CharType.isFullWidth(unicode));
			}
		}
	}

	@Test
	public void testIsSpace() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			switch (win31j) {
				case 0x0009: // HT
				case 0x000A: // LF
				case 0x000B: // VT
				case 0x000C: // NP
				case 0x000D: // CR
				case 0x001C: // FS
				case 0x001D: // GS
				case 0x001E: // RS
				case 0x001F: // US
				case 0x0020: // SPC
					assertTrue(CharType.isSpace(unicode));
					break;
				default:
					assertFalse(CharType.isSpace(unicode));
					break;
			}
		}
	}

	@Test
	public void testIsNumeric() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			switch (win31j) {
				case 0x0030: // '0'
				case 0x0031: // '1'
				case 0x0032: // '2'
				case 0x0033: // '3'
				case 0x0034: // '4'
				case 0x0035: // '5'
				case 0x0036: // '6'
				case 0x0037: // '7'
				case 0x0038: // '8'
				case 0x0039: // '9'
					assertTrue(CharType.isNumeric(unicode));
					break;
				default:
					assertFalse(CharType.isNumeric(unicode));
					break;
			}
		}
	}

	@Test
	public void testIsAlpha() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x0041 <= win31j && 0x005A >= win31j) {// A-Z
				assertTrue(CharType.isAlpha(unicode));
			} else if (0x0061 <= win31j && 0x007A >= win31j) {// a-z
				assertTrue(CharType.isAlpha(unicode));
			} else {
				assertFalse(CharType.isAlpha(unicode));
			}
		}
	}

	@Test
	public void testIsUpper() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x0041 <= win31j && 0x005A >= win31j) {// A-Z
				assertTrue(CharType.isUpper(unicode));
			} else {
				assertFalse(CharType.isUpper(unicode));
			}
		}
	}

	@Test
	public void testIsLower() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x0061 <= win31j && 0x007A >= win31j) {// a-z
				assertTrue(CharType.isLower(unicode));
			} else {
				assertFalse(CharType.isLower(unicode));
			}
		}
	}

	@Test
	public void testIsFullSpace() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			switch (win31j) {
				case 0x8140: // IDEOGRAPHIC SPACE " "
					assertTrue(CharType.isFullSpace(unicode));
					break;
				default:
					assertFalse(CharType.isFullSpace(unicode));
					break;
			}
		}
	}

	@Test
	public void testIsFullNumeric() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			switch (win31j) {
				case 0x824F: // FULLWIDTH DIGIT ZERO "０"
				case 0x8250: // FULLWIDTH DIGIT ZERO "１"
				case 0x8251: // FULLWIDTH DIGIT ZERO "２"
				case 0x8252: // FULLWIDTH DIGIT ZERO "３"
				case 0x8253: // FULLWIDTH DIGIT ZERO "４"
				case 0x8254: // FULLWIDTH DIGIT ZERO "５"
				case 0x8255: // FULLWIDTH DIGIT ZERO "６"
				case 0x8256: // FULLWIDTH DIGIT ZERO "７"
				case 0x8257: // FULLWIDTH DIGIT ZERO "８"
				case 0x8258: // FULLWIDTH DIGIT ZERO "９"
					assertTrue(CharType.isFullNumeric(unicode));
					break;
				default:
					assertFalse(CharType.isFullNumeric(unicode));
					break;
			}
		}
	}

	@Test
	public void testIsFullAlpha() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x8260 <= win31j && 0x8279 >= win31j) {// Ａ-Ｚ
				assertTrue(CharType.isFullAlpha(unicode));
			} else if (0x8281 <= win31j && 0x829A >= win31j) {// ａ-ｚ
				assertTrue(CharType.isFullAlpha(unicode));
			} else {
				assertFalse(CharType.isFullAlpha(unicode));
			}
		}
	}

	@Test
	public void testIsFullUpper() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x8260 <= win31j && 0x8279 >= win31j) {// Ａ-Ｚ
				assertTrue(CharType.isFullUpper(unicode));
			} else {
				assertFalse(CharType.isFullUpper(unicode));
			}
		}
	}

	@Test
	public void testIsFullLower() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x8281 <= win31j && 0x829A >= win31j) {// ａ-ｚ
				assertTrue(CharType.isFullLower(unicode));
			} else {
				assertFalse(CharType.isFullLower(unicode));
			}
		}
	}

	@Test
	public void testIsFullHiragana() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x829F <= win31j && 0x82F1 >= win31j) {
				assertTrue(CharType.isFullHiragana(unicode));
			} else {
				switch (win31j) {
					case 0x8145: // '・'
					case 0x814A: // '゛'
					case 0x814B: // '゜'
					case 0x8154: // 'ゝ'
					case 0x8155: // 'ゞ'
					case 0x815B: // 'ー'
					case 0x8141: // '、'
					case 0x8142: // '。'
					case 0x8175: // '「'
					case 0x8176: // '」'
					case 0x8177: // '『'
					case 0x8178: // '』'
						assertTrue(CharType.isFullHiragana(unicode));
						break;
					default:
						assertFalse(CharType.isFullHiragana(unicode));
						break;
				}
			}
		}
	}

	@Test
	public void testIsFullKatakana() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x8340 <= win31j && 0x8396 >= win31j) {
				assertTrue(CharType.isFullKatakana(unicode));
			} else {
				switch (win31j) {
					case 0x8145: // '・'
					case 0x814A: // '゛'
					case 0x814B: // '゜'
					case 0x8152: // 'ヽ'
					case 0x8153: // 'ヾ'
					case 0x815B: // 'ー'
					case 0x8141: // '、'
					case 0x8142: // '。'
					case 0x8175: // '「'
					case 0x8176: // '」'
					case 0x8177: // '『'
					case 0x8178: // '』'
						assertTrue(CharType.isFullKatakana(unicode));
						break;
					default:
						assertFalse(CharType.isFullKatakana(unicode));
						break;
				}
			}
		}
	}

	@Test
	public void testIsHalfKatakana() {
		for (Entry entry : tableReader.getEntries()) {
			int win31j = entry.getWin31j();
			int unicode = entry.getUnicode();
			if (0x00A1 <= win31j && 0x00DF >= win31j) {
				assertTrue(CharType.isHalfKatakana(unicode));
			} else {
				assertFalse(CharType.isHalfKatakana(unicode));
			}
		}
	}

	@Test
	public void testIsCp932() {
		Map<Integer, Boolean> map = new HashMap<>();
		for (Entry entry : tableReader.getEntries()) {
			map.put(entry.getUnicode(), Boolean.TRUE);
		}
		for (int i = 0; i <= 0xFFFF; i++) {
			if (map.containsKey(Integer.valueOf(i))) {
				assertTrue(CharType.isCp932(i));
			} else {
				assertFalse(CharType.isCp932(i));
			}
		}
	}

}
