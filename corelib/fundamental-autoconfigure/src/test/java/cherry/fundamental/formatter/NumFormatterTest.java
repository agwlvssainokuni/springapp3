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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NumFormatterTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class NumFormatterTest {

	@Autowired
	private NumFormatter numFormatter;

	@Test
	public void testBigdec1() {
		Function<BigDecimal, String> format = numFormatter::fmBigdec1;
		Function<String, BigDecimal> parser = numFormatter::toBigdec1;
		assertEquals("1234", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1234.1", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdec(parser);
	}

	@Test
	public void testBigdec2() {
		Function<BigDecimal, String> format = numFormatter::fmBigdec2;
		Function<String, BigDecimal> parser = numFormatter::toBigdec2;
		assertEquals("1234", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1234.12", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdec(parser);
	}

	@Test
	public void testBigdec3() {
		Function<BigDecimal, String> format = numFormatter::fmBigdec3;
		Function<String, BigDecimal> parser = numFormatter::toBigdec3;
		assertEquals("1234", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1234.123", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdec(parser);
	}

	@Test
	public void testBigdec01() {
		Function<BigDecimal, String> format = numFormatter::fmBigdec01;
		Function<String, BigDecimal> parser = numFormatter::toBigdec01;
		assertEquals("1234.0", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1234.1", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdec(parser);
	}

	@Test
	public void testBigdec02() {
		Function<BigDecimal, String> format = numFormatter::fmBigdec02;
		Function<String, BigDecimal> parser = numFormatter::toBigdec02;
		assertEquals("1234.00", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1234.12", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdec(parser);
	}

	@Test
	public void testBigdec03() {
		Function<BigDecimal, String> format = numFormatter::fmBigdec03;
		Function<String, BigDecimal> parser = numFormatter::toBigdec03;
		assertEquals("1234.000", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1234.123", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdec(parser);
	}

	@Test
	public void testBigdecC1() {
		Function<BigDecimal, String> format = numFormatter::fmBigdecC1;
		Function<String, BigDecimal> parser = numFormatter::toBigdecC1;
		assertEquals("1,234", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1,234.1", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdecC(parser);
	}

	@Test
	public void testBigdecC2() {
		Function<BigDecimal, String> format = numFormatter::fmBigdecC2;
		Function<String, BigDecimal> parser = numFormatter::toBigdecC2;
		assertEquals("1,234", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1,234.12", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdecC(parser);
	}

	@Test
	public void testBigdecC3() {
		Function<BigDecimal, String> format = numFormatter::fmBigdecC3;
		Function<String, BigDecimal> parser = numFormatter::toBigdecC3;
		assertEquals("1,234", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1,234.123", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdecC(parser);
	}

	@Test
	public void testBigdecC01() {
		Function<BigDecimal, String> format = numFormatter::fmBigdecC01;
		Function<String, BigDecimal> parser = numFormatter::toBigdecC01;
		assertEquals("1,234.0", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1,234.1", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdecC(parser);
	}

	@Test
	public void testBigdecC02() {
		Function<BigDecimal, String> format = numFormatter::fmBigdecC02;
		Function<String, BigDecimal> parser = numFormatter::toBigdecC02;
		assertEquals("1,234.00", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1,234.12", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdecC(parser);
	}

	@Test
	public void testBigdecC03() {
		Function<BigDecimal, String> format = numFormatter::fmBigdecC03;
		Function<String, BigDecimal> parser = numFormatter::toBigdecC03;
		assertEquals("1,234.000", format.apply(BigDecimal.valueOf(1234L)));
		assertEquals("1,234.123", format.apply(BigDecimal.valueOf(1234.1234)));
		testBigdecC(parser);
	}

	private void testBigdec(Function<String, BigDecimal> parser) {
		assertEquals(BigDecimal.valueOf(1234L).setScale(0), parser.apply("1234"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(1), parser.apply("1234.0"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(2), parser.apply("1234.00"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(3), parser.apply("1234.000"));
		assertEquals(BigDecimal.valueOf(12341234L).movePointLeft(4), parser.apply("1234.1234"));
	}

	private void testBigdecC(Function<String, BigDecimal> parser) {
		assertEquals(BigDecimal.valueOf(1234L).setScale(0), parser.apply("1234"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(1), parser.apply("1234.0"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(2), parser.apply("1234.00"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(3), parser.apply("1234.000"));
		assertEquals(BigDecimal.valueOf(12341234L).movePointLeft(4), parser.apply("1234.1234"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(0), parser.apply("1,234"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(1), parser.apply("1,234.0"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(2), parser.apply("1,234.00"));
		assertEquals(BigDecimal.valueOf(1234L).setScale(3), parser.apply("1,234.000"));
		assertEquals(BigDecimal.valueOf(12341234L).movePointLeft(4), parser.apply("1,234.1234"));
	}

	@Test
	public void testInteger() {
		Function<Integer, String> format = numFormatter::fmInteger;
		Function<String, Integer> parser = numFormatter::toInteger;
		assertEquals("1234", format.apply(Integer.valueOf(1234)));
		assertEquals(Integer.valueOf(1234), parser.apply("1234"));
	}

	@Test
	public void testIntegerC() {
		Function<Integer, String> format = numFormatter::fmIntegerC;
		Function<String, Integer> parser = numFormatter::toIntegerC;
		assertEquals("1,234", format.apply(Integer.valueOf(1234)));
		assertEquals(Integer.valueOf(1234), parser.apply("1234"));
		assertEquals(Integer.valueOf(1234), parser.apply("1,234"));
	}

	@Test
	public void testLong() {
		Function<Long, String> format = numFormatter::fmLong;
		Function<String, Long> parser = numFormatter::toLong;
		assertEquals("1234", format.apply(Long.valueOf(1234L)));
		assertEquals(Long.valueOf(1234L), parser.apply("1234"));
	}

	@Test
	public void testLongC() {
		Function<Long, String> format = numFormatter::fmLongC;
		Function<String, Long> parser = numFormatter::toLongC;
		assertEquals("1,234", format.apply(Long.valueOf(1234L)));
		assertEquals(Long.valueOf(1234), parser.apply("1234"));
		assertEquals(Long.valueOf(1234), parser.apply("1,234"));
	}

	@Test
	public void testBigint() {
		Function<BigInteger, String> format = numFormatter::fmBigint;
		Function<String, BigInteger> parser = numFormatter::toBigint;
		assertEquals("1234", format.apply(BigInteger.valueOf(1234L)));
		assertEquals(BigInteger.valueOf(1234L), parser.apply("1234"));
	}

	@Test
	public void testBigintC() {
		Function<BigInteger, String> format = numFormatter::fmBigintC;
		Function<String, BigInteger> parser = numFormatter::toBigintC;
		assertEquals("1,234", format.apply(BigInteger.valueOf(1234L)));
		assertEquals(BigInteger.valueOf(1234), parser.apply("1234"));
		assertEquals(BigInteger.valueOf(1234), parser.apply("1,234"));
	}

}
