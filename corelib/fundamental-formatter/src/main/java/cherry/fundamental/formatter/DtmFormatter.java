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

public class DtmFormatter {

	private final ConversionService conversionService;

	private final TypeDescriptor tdString = new TypeDescriptor(ResolvableType.forClass(String.class), null, null);

	public DtmFormatter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	private TypeDescriptor createTypeDescriptor(String name) {
		try {
			return new TypeDescriptor(getClass().getDeclaredField(name));
		} catch (NoSuchFieldException ex) {
			throw new IllegalStateException(ex);
		}
	}

	/** 変換YYYY_MM_DDの定義；書式"yyyy/MM/dd"、対象型{@link java.time.LocalDate}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy/MM/dd")
	private final java.time.LocalDate fdYYYY_MM_DD = null;
	/** 変換YYYY_MM_DDで使用する型情報。 */
	private final TypeDescriptor tdYYYY_MM_DD = createTypeDescriptor("fdYYYY_MM_DD");

	/** 書式"yyyy/MM/dd"の文字列を{@link java.time.LocalDate}に変換する。 */
	public java.time.LocalDate toYYYY_MM_DD(String src) {
		return (java.time.LocalDate) conversionService.convert(src, tdString, tdYYYY_MM_DD);
	}

	/** {@link java.time.LocalDate}を書式"yyyy/MM/dd"の文字列に変換する。 */
	public String fmYYYY_MM_DD(java.time.LocalDate src) {
		return (String) conversionService.convert(src, tdYYYY_MM_DD, tdString);
	}

	/** 変換YYYYMMDDの定義；書式"yyyyMMdd"、対象型{@link java.time.LocalDate}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyyMMdd")
	private final java.time.LocalDate fdYYYYMMDD = null;
	/** 変換YYYYMMDDで使用する型情報。 */
	private final TypeDescriptor tdYYYYMMDD = createTypeDescriptor("fdYYYYMMDD");

	/** 書式"yyyyMMdd"の文字列を{@link java.time.LocalDate}に変換する。 */
	public java.time.LocalDate toYYYYMMDD(String src) {
		return (java.time.LocalDate) conversionService.convert(src, tdString, tdYYYYMMDD);
	}

	/** {@link java.time.LocalDate}を書式"yyyyMMdd"の文字列に変換する。 */
	public String fmYYYYMMDD(java.time.LocalDate src) {
		return (String) conversionService.convert(src, tdYYYYMMDD, tdString);
	}

	/** 変換YYYY_MMの定義；書式"yyyy/MM"、対象型{@link java.time.YearMonth}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy/MM")
	private final java.time.YearMonth fdYYYY_MM = null;
	/** 変換YYYY_MMで使用する型情報。 */
	private final TypeDescriptor tdYYYY_MM = createTypeDescriptor("fdYYYY_MM");

	/** 書式"yyyy/MM"の文字列を{@link java.time.YearMonth}に変換する。 */
	public java.time.YearMonth toYYYY_MM(String src) {
		return (java.time.YearMonth) conversionService.convert(src, tdString, tdYYYY_MM);
	}

	/** {@link java.time.YearMonth}を書式"yyyy/MM"の文字列に変換する。 */
	public String fmYYYY_MM(java.time.YearMonth src) {
		return (String) conversionService.convert(src, tdYYYY_MM, tdString);
	}

	/** 変換YYYYMMの定義；書式"yyyyMM"、対象型{@link java.time.YearMonth}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyyMM")
	private final java.time.YearMonth fdYYYYMM = null;
	/** 変換YYYYMMで使用する型情報。 */
	private final TypeDescriptor tdYYYYMM = createTypeDescriptor("fdYYYYMM");

	/** 書式"yyyyMM"の文字列を{@link java.time.YearMonth}に変換する。 */
	public java.time.YearMonth toYYYYMM(String src) {
		return (java.time.YearMonth) conversionService.convert(src, tdString, tdYYYYMM);
	}

	/** {@link java.time.YearMonth}を書式"yyyyMM"の文字列に変換する。 */
	public String fmYYYYMM(java.time.YearMonth src) {
		return (String) conversionService.convert(src, tdYYYYMM, tdString);
	}

	/** 変換MM_DDの定義；書式"MM/dd"、対象型{@link java.time.MonthDay}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "MM/dd")
	private final java.time.MonthDay fdMM_DD = null;
	/** 変換MM_DDで使用する型情報。 */
	private final TypeDescriptor tdMM_DD = createTypeDescriptor("fdMM_DD");

	/** 書式"MM/dd"の文字列を{@link java.time.MonthDay}に変換する。 */
	public java.time.MonthDay toMM_DD(String src) {
		return (java.time.MonthDay) conversionService.convert(src, tdString, tdMM_DD);
	}

	/** {@link java.time.MonthDay}を書式"MM/dd"の文字列に変換する。 */
	public String fmMM_DD(java.time.MonthDay src) {
		return (String) conversionService.convert(src, tdMM_DD, tdString);
	}

	/** 変換MMDDの定義；書式"MMdd"、対象型{@link java.time.MonthDay}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "MMdd")
	private final java.time.MonthDay fdMMDD = null;
	/** 変換MMDDで使用する型情報。 */
	private final TypeDescriptor tdMMDD = createTypeDescriptor("fdMMDD");

	/** 書式"MMdd"の文字列を{@link java.time.MonthDay}に変換する。 */
	public java.time.MonthDay toMMDD(String src) {
		return (java.time.MonthDay) conversionService.convert(src, tdString, tdMMDD);
	}

	/** {@link java.time.MonthDay}を書式"MMdd"の文字列に変換する。 */
	public String fmMMDD(java.time.MonthDay src) {
		return (String) conversionService.convert(src, tdMMDD, tdString);
	}

	/** 変換HH_MM_SSの定義；書式"HH:mm:ss"、対象型{@link java.time.LocalTime}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "HH:mm:ss")
	private final java.time.LocalTime fdHH_MM_SS = null;
	/** 変換HH_MM_SSで使用する型情報。 */
	private final TypeDescriptor tdHH_MM_SS = createTypeDescriptor("fdHH_MM_SS");

	/** 書式"HH:mm:ss"の文字列を{@link java.time.LocalTime}に変換する。 */
	public java.time.LocalTime toHH_MM_SS(String src) {
		return (java.time.LocalTime) conversionService.convert(src, tdString, tdHH_MM_SS);
	}

	/** {@link java.time.LocalTime}を書式"HH:mm:ss"の文字列に変換する。 */
	public String fmHH_MM_SS(java.time.LocalTime src) {
		return (String) conversionService.convert(src, tdHH_MM_SS, tdString);
	}

	/** 変換HHMMSSの定義；書式"HHmmss"、対象型{@link java.time.LocalTime}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "HHmmss")
	private final java.time.LocalTime fdHHMMSS = null;
	/** 変換HHMMSSで使用する型情報。 */
	private final TypeDescriptor tdHHMMSS = createTypeDescriptor("fdHHMMSS");

	/** 書式"HHmmss"の文字列を{@link java.time.LocalTime}に変換する。 */
	public java.time.LocalTime toHHMMSS(String src) {
		return (java.time.LocalTime) conversionService.convert(src, tdString, tdHHMMSS);
	}

	/** {@link java.time.LocalTime}を書式"HHmmss"の文字列に変換する。 */
	public String fmHHMMSS(java.time.LocalTime src) {
		return (String) conversionService.convert(src, tdHHMMSS, tdString);
	}

	/** 変換HH_MMの定義；書式"HH:mm"、対象型{@link java.time.LocalTime}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "HH:mm")
	private final java.time.LocalTime fdHH_MM = null;
	/** 変換HH_MMで使用する型情報。 */
	private final TypeDescriptor tdHH_MM = createTypeDescriptor("fdHH_MM");

	/** 書式"HH:mm"の文字列を{@link java.time.LocalTime}に変換する。 */
	public java.time.LocalTime toHH_MM(String src) {
		return (java.time.LocalTime) conversionService.convert(src, tdString, tdHH_MM);
	}

	/** {@link java.time.LocalTime}を書式"HH:mm"の文字列に変換する。 */
	public String fmHH_MM(java.time.LocalTime src) {
		return (String) conversionService.convert(src, tdHH_MM, tdString);
	}

	/** 変換HHMMの定義；書式"HHmm"、対象型{@link java.time.LocalTime}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "HHmm")
	private final java.time.LocalTime fdHHMM = null;
	/** 変換HHMMで使用する型情報。 */
	private final TypeDescriptor tdHHMM = createTypeDescriptor("fdHHMM");

	/** 書式"HHmm"の文字列を{@link java.time.LocalTime}に変換する。 */
	public java.time.LocalTime toHHMM(String src) {
		return (java.time.LocalTime) conversionService.convert(src, tdString, tdHHMM);
	}

	/** {@link java.time.LocalTime}を書式"HHmm"の文字列に変換する。 */
	public String fmHHMM(java.time.LocalTime src) {
		return (String) conversionService.convert(src, tdHHMM, tdString);
	}

	/** 変換YYYY_MM_DD_HH_MM_SSの定義；書式"yyyy/MM/dd HH:mm:ss"、対象型{@link java.time.LocalDateTime}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private final java.time.LocalDateTime fdYYYY_MM_DD_HH_MM_SS = null;
	/** 変換YYYY_MM_DD_HH_MM_SSで使用する型情報。 */
	private final TypeDescriptor tdYYYY_MM_DD_HH_MM_SS = createTypeDescriptor("fdYYYY_MM_DD_HH_MM_SS");

	/** 書式"yyyy/MM/dd HH:mm:ss"の文字列を{@link java.time.LocalDateTime}に変換する。 */
	public java.time.LocalDateTime toYYYY_MM_DD_HH_MM_SS(String src) {
		return (java.time.LocalDateTime) conversionService.convert(src, tdString, tdYYYY_MM_DD_HH_MM_SS);
	}

	/** {@link java.time.LocalDateTime}を書式"yyyy/MM/dd HH:mm:ss"の文字列に変換する。 */
	public String fmYYYY_MM_DD_HH_MM_SS(java.time.LocalDateTime src) {
		return (String) conversionService.convert(src, tdYYYY_MM_DD_HH_MM_SS, tdString);
	}

	/** 変換YYYYMMDDHHMMSSの定義；書式"yyyyMMddHHmmss"、対象型{@link java.time.LocalDateTime}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyyMMddHHmmss")
	private final java.time.LocalDateTime fdYYYYMMDDHHMMSS = null;
	/** 変換YYYYMMDDHHMMSSで使用する型情報。 */
	private final TypeDescriptor tdYYYYMMDDHHMMSS = createTypeDescriptor("fdYYYYMMDDHHMMSS");

	/** 書式"yyyyMMddHHmmss"の文字列を{@link java.time.LocalDateTime}に変換する。 */
	public java.time.LocalDateTime toYYYYMMDDHHMMSS(String src) {
		return (java.time.LocalDateTime) conversionService.convert(src, tdString, tdYYYYMMDDHHMMSS);
	}

	/** {@link java.time.LocalDateTime}を書式"yyyyMMddHHmmss"の文字列に変換する。 */
	public String fmYYYYMMDDHHMMSS(java.time.LocalDateTime src) {
		return (String) conversionService.convert(src, tdYYYYMMDDHHMMSS, tdString);
	}

	/** 変換YYYY_MM_DD_HH_MMの定義；書式"yyyy/MM/dd HH:mm"、対象型{@link java.time.LocalDateTime}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private final java.time.LocalDateTime fdYYYY_MM_DD_HH_MM = null;
	/** 変換YYYY_MM_DD_HH_MMで使用する型情報。 */
	private final TypeDescriptor tdYYYY_MM_DD_HH_MM = createTypeDescriptor("fdYYYY_MM_DD_HH_MM");

	/** 書式"yyyy/MM/dd HH:mm"の文字列を{@link java.time.LocalDateTime}に変換する。 */
	public java.time.LocalDateTime toYYYY_MM_DD_HH_MM(String src) {
		return (java.time.LocalDateTime) conversionService.convert(src, tdString, tdYYYY_MM_DD_HH_MM);
	}

	/** {@link java.time.LocalDateTime}を書式"yyyy/MM/dd HH:mm"の文字列に変換する。 */
	public String fmYYYY_MM_DD_HH_MM(java.time.LocalDateTime src) {
		return (String) conversionService.convert(src, tdYYYY_MM_DD_HH_MM, tdString);
	}

	/** 変換YYYYMMDDHHMMの定義；書式"yyyyMMddHHmm"、対象型{@link java.time.LocalDateTime}。 */
	@org.springframework.format.annotation.DateTimeFormat(pattern = "yyyyMMddHHmm")
	private final java.time.LocalDateTime fdYYYYMMDDHHMM = null;
	/** 変換YYYYMMDDHHMMで使用する型情報。 */
	private final TypeDescriptor tdYYYYMMDDHHMM = createTypeDescriptor("fdYYYYMMDDHHMM");

	/** 書式"yyyyMMddHHmm"の文字列を{@link java.time.LocalDateTime}に変換する。 */
	public java.time.LocalDateTime toYYYYMMDDHHMM(String src) {
		return (java.time.LocalDateTime) conversionService.convert(src, tdString, tdYYYYMMDDHHMM);
	}

	/** {@link java.time.LocalDateTime}を書式"yyyyMMddHHmm"の文字列に変換する。 */
	public String fmYYYYMMDDHHMM(java.time.LocalDateTime src) {
		return (String) conversionService.convert(src, tdYYYYMMDDHHMM, tdString);
	}
}
