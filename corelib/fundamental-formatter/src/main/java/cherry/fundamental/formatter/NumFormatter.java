/*
 * Copyright 2019 agwlvssainokuni
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

package cherry.fundamental.formatter;

import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

public class NumFormatter {

	private final ConversionService conversionService;

	private final TypeDescriptor tdString = new TypeDescriptor(ResolvableType.forClass(String.class), null, null);

	public NumFormatter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	private TypeDescriptor createTypeDescriptor(String name) {
		try {
			return new TypeDescriptor(getClass().getDeclaredField(name));
		} catch (NoSuchFieldException ex) {
			throw new IllegalStateException(ex);
		}
	}

	/** 変換Bigdec1の定義；書式"#0.#"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0.#")
	private final java.math.BigDecimal fdBigdec1 = null;
	/** 変換Bigdec1で使用する型情報。 */
	private final TypeDescriptor tdBigdec1 = createTypeDescriptor("fdBigdec1");

	/** 書式"#0.#"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdec1(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdec1);
	}

	/** {@link java.math.BigDecimal}を書式"#0.#"の文字列に変換する。 */
	public String fmBigdec1(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdec1, tdString);
	}

	/** 変換Bigdec2の定義；書式"#0.##"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0.##")
	private final java.math.BigDecimal fdBigdec2 = null;
	/** 変換Bigdec2で使用する型情報。 */
	private final TypeDescriptor tdBigdec2 = createTypeDescriptor("fdBigdec2");

	/** 書式"#0.##"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdec2(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdec2);
	}

	/** {@link java.math.BigDecimal}を書式"#0.##"の文字列に変換する。 */
	public String fmBigdec2(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdec2, tdString);
	}

	/** 変換Bigdec3の定義；書式"#0.###"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0.###")
	private final java.math.BigDecimal fdBigdec3 = null;
	/** 変換Bigdec3で使用する型情報。 */
	private final TypeDescriptor tdBigdec3 = createTypeDescriptor("fdBigdec3");

	/** 書式"#0.###"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdec3(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdec3);
	}

	/** {@link java.math.BigDecimal}を書式"#0.###"の文字列に変換する。 */
	public String fmBigdec3(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdec3, tdString);
	}

	/** 変換Bigdec01の定義；書式"#0.0"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0.0")
	private final java.math.BigDecimal fdBigdec01 = null;
	/** 変換Bigdec01で使用する型情報。 */
	private final TypeDescriptor tdBigdec01 = createTypeDescriptor("fdBigdec01");

	/** 書式"#0.0"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdec01(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdec01);
	}

	/** {@link java.math.BigDecimal}を書式"#0.0"の文字列に変換する。 */
	public String fmBigdec01(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdec01, tdString);
	}

	/** 変換Bigdec02の定義；書式"#0.00"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0.00")
	private final java.math.BigDecimal fdBigdec02 = null;
	/** 変換Bigdec02で使用する型情報。 */
	private final TypeDescriptor tdBigdec02 = createTypeDescriptor("fdBigdec02");

	/** 書式"#0.00"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdec02(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdec02);
	}

	/** {@link java.math.BigDecimal}を書式"#0.00"の文字列に変換する。 */
	public String fmBigdec02(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdec02, tdString);
	}

	/** 変換Bigdec03の定義；書式"#0.000"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0.000")
	private final java.math.BigDecimal fdBigdec03 = null;
	/** 変換Bigdec03で使用する型情報。 */
	private final TypeDescriptor tdBigdec03 = createTypeDescriptor("fdBigdec03");

	/** 書式"#0.000"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdec03(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdec03);
	}

	/** {@link java.math.BigDecimal}を書式"#0.000"の文字列に変換する。 */
	public String fmBigdec03(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdec03, tdString);
	}

	/** 変換BigdecC1の定義；書式"#,##0.#"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0.#")
	private final java.math.BigDecimal fdBigdecC1 = null;
	/** 変換BigdecC1で使用する型情報。 */
	private final TypeDescriptor tdBigdecC1 = createTypeDescriptor("fdBigdecC1");

	/** 書式"#,##0.#"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdecC1(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdecC1);
	}

	/** {@link java.math.BigDecimal}を書式"#,##0.#"の文字列に変換する。 */
	public String fmBigdecC1(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdecC1, tdString);
	}

	/** 変換BigdecC2の定義；書式"#,##0.##"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0.##")
	private final java.math.BigDecimal fdBigdecC2 = null;
	/** 変換BigdecC2で使用する型情報。 */
	private final TypeDescriptor tdBigdecC2 = createTypeDescriptor("fdBigdecC2");

	/** 書式"#,##0.##"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdecC2(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdecC2);
	}

	/** {@link java.math.BigDecimal}を書式"#,##0.##"の文字列に変換する。 */
	public String fmBigdecC2(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdecC2, tdString);
	}

	/** 変換BigdecC3の定義；書式"#,##0.###"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0.###")
	private final java.math.BigDecimal fdBigdecC3 = null;
	/** 変換BigdecC3で使用する型情報。 */
	private final TypeDescriptor tdBigdecC3 = createTypeDescriptor("fdBigdecC3");

	/** 書式"#,##0.###"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdecC3(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdecC3);
	}

	/** {@link java.math.BigDecimal}を書式"#,##0.###"の文字列に変換する。 */
	public String fmBigdecC3(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdecC3, tdString);
	}

	/** 変換BigdecC01の定義；書式"#,##0.0"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0.0")
	private final java.math.BigDecimal fdBigdecC01 = null;
	/** 変換BigdecC01で使用する型情報。 */
	private final TypeDescriptor tdBigdecC01 = createTypeDescriptor("fdBigdecC01");

	/** 書式"#,##0.0"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdecC01(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdecC01);
	}

	/** {@link java.math.BigDecimal}を書式"#,##0.0"の文字列に変換する。 */
	public String fmBigdecC01(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdecC01, tdString);
	}

	/** 変換BigdecC02の定義；書式"#,##0.00"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0.00")
	private final java.math.BigDecimal fdBigdecC02 = null;
	/** 変換BigdecC02で使用する型情報。 */
	private final TypeDescriptor tdBigdecC02 = createTypeDescriptor("fdBigdecC02");

	/** 書式"#,##0.00"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdecC02(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdecC02);
	}

	/** {@link java.math.BigDecimal}を書式"#,##0.00"の文字列に変換する。 */
	public String fmBigdecC02(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdecC02, tdString);
	}

	/** 変換BigdecC03の定義；書式"#,##0.000"、対象型{@link java.math.BigDecimal}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0.000")
	private final java.math.BigDecimal fdBigdecC03 = null;
	/** 変換BigdecC03で使用する型情報。 */
	private final TypeDescriptor tdBigdecC03 = createTypeDescriptor("fdBigdecC03");

	/** 書式"#,##0.000"の文字列を{@link java.math.BigDecimal}に変換する。 */
	public java.math.BigDecimal toBigdecC03(String src) {
		return (java.math.BigDecimal) conversionService.convert(src, tdString, tdBigdecC03);
	}

	/** {@link java.math.BigDecimal}を書式"#,##0.000"の文字列に変換する。 */
	public String fmBigdecC03(java.math.BigDecimal src) {
		return (String) conversionService.convert(src, tdBigdecC03, tdString);
	}

	/** 変換Integerの定義；書式"#0"、対象型{@link Integer}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0")
	private final Integer fdInteger = null;
	/** 変換Integerで使用する型情報。 */
	private final TypeDescriptor tdInteger = createTypeDescriptor("fdInteger");

	/** 書式"#0"の文字列を{@link Integer}に変換する。 */
	public Integer toInteger(String src) {
		return (Integer) conversionService.convert(src, tdString, tdInteger);
	}

	/** {@link Integer}を書式"#0"の文字列に変換する。 */
	public String fmInteger(Integer src) {
		return (String) conversionService.convert(src, tdInteger, tdString);
	}

	/** 変換IntegerCの定義；書式"#,##0"、対象型{@link Integer}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0")
	private final Integer fdIntegerC = null;
	/** 変換IntegerCで使用する型情報。 */
	private final TypeDescriptor tdIntegerC = createTypeDescriptor("fdIntegerC");

	/** 書式"#,##0"の文字列を{@link Integer}に変換する。 */
	public Integer toIntegerC(String src) {
		return (Integer) conversionService.convert(src, tdString, tdIntegerC);
	}

	/** {@link Integer}を書式"#,##0"の文字列に変換する。 */
	public String fmIntegerC(Integer src) {
		return (String) conversionService.convert(src, tdIntegerC, tdString);
	}

	/** 変換Longの定義；書式"#0"、対象型{@link Long}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0")
	private final Long fdLong = null;
	/** 変換Longで使用する型情報。 */
	private final TypeDescriptor tdLong = createTypeDescriptor("fdLong");

	/** 書式"#0"の文字列を{@link Long}に変換する。 */
	public Long toLong(String src) {
		return (Long) conversionService.convert(src, tdString, tdLong);
	}

	/** {@link Long}を書式"#0"の文字列に変換する。 */
	public String fmLong(Long src) {
		return (String) conversionService.convert(src, tdLong, tdString);
	}

	/** 変換LongCの定義；書式"#,##0"、対象型{@link Long}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0")
	private final Long fdLongC = null;
	/** 変換LongCで使用する型情報。 */
	private final TypeDescriptor tdLongC = createTypeDescriptor("fdLongC");

	/** 書式"#,##0"の文字列を{@link Long}に変換する。 */
	public Long toLongC(String src) {
		return (Long) conversionService.convert(src, tdString, tdLongC);
	}

	/** {@link Long}を書式"#,##0"の文字列に変換する。 */
	public String fmLongC(Long src) {
		return (String) conversionService.convert(src, tdLongC, tdString);
	}

	/** 変換Bigintの定義；書式"#0"、対象型{@link java.math.BigInteger}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#0")
	private final java.math.BigInteger fdBigint = null;
	/** 変換Bigintで使用する型情報。 */
	private final TypeDescriptor tdBigint = createTypeDescriptor("fdBigint");

	/** 書式"#0"の文字列を{@link java.math.BigInteger}に変換する。 */
	public java.math.BigInteger toBigint(String src) {
		return (java.math.BigInteger) conversionService.convert(src, tdString, tdBigint);
	}

	/** {@link java.math.BigInteger}を書式"#0"の文字列に変換する。 */
	public String fmBigint(java.math.BigInteger src) {
		return (String) conversionService.convert(src, tdBigint, tdString);
	}

	/** 変換BigintCの定義；書式"#,##0"、対象型{@link java.math.BigInteger}。 */
	@org.springframework.format.annotation.NumberFormat(pattern = "#,##0")
	private final java.math.BigInteger fdBigintC = null;
	/** 変換BigintCで使用する型情報。 */
	private final TypeDescriptor tdBigintC = createTypeDescriptor("fdBigintC");

	/** 書式"#,##0"の文字列を{@link java.math.BigInteger}に変換する。 */
	public java.math.BigInteger toBigintC(String src) {
		return (java.math.BigInteger) conversionService.convert(src, tdString, tdBigintC);
	}

	/** {@link java.math.BigInteger}を書式"#,##0"の文字列に変換する。 */
	public String fmBigintC(java.math.BigInteger src) {
		return (String) conversionService.convert(src, tdBigintC, tdString);
	}
}
