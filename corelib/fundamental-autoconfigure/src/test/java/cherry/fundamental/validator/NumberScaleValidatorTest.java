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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NumberScaleValidatorTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class NumberScaleValidatorTest {

	@Autowired
	private DataBinderHelper dataBinderHelper;

	@Test
	public void testOK() {

		Map<String, String> val = new HashMap<>();
		val.put("number", null);
		val.put("decimal0", "123");
		val.put("decimal1", "123.4");
		val.put("decimal3", "123.456");
		val.put("double0", "123");
		val.put("double1", "123.4");
		val.put("double3", "123.456");
		val.put("float0", "123");
		val.put("float1", "123.4");
		val.put("float3", "123.456");
		val.put("int0", "123");
		val.put("int1", "123.4");
		val.put("int3", "123.456");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(0, result.getErrorCount());
		assertNull(dto.getNumber());
		assertEquals(BigDecimal.valueOf(123L, 0), dto.getDecimal0());
		assertEquals(BigDecimal.valueOf(1234L, 1), dto.getDecimal1());
		assertEquals(BigDecimal.valueOf(123456L, 3), dto.getDecimal3());
		assertEquals(Double.valueOf(123.0), dto.getDouble0());
		assertEquals(Double.valueOf(123.4), dto.getDouble1());
		assertEquals(Double.valueOf(123.456), dto.getDouble3());
		assertEquals(Float.valueOf(123.0f), dto.getFloat0());
		assertEquals(Float.valueOf(123.4f), dto.getFloat1());
		assertEquals(Float.valueOf(123.456f), dto.getFloat3());
		assertEquals(Integer.valueOf(123), dto.getInt0());
		assertEquals(Integer.valueOf(123), dto.getInt1());
		assertEquals(Integer.valueOf(123), dto.getInt3());
	}

	@Test
	public void testNG() {

		Map<String, String> val = new HashMap<>();
		val.put("decimal0", "123.4");
		val.put("decimal1", "123.45");
		val.put("decimal3", "123.4567");
		val.put("double0", "123.4");
		val.put("double1", "123.45");
		val.put("double3", "123.4567");
		val.put("float0", "123.4");
		val.put("float1", "123.45");
		val.put("float3", "123.4567");

		TestDto dto = new TestDto();

		BindingResult result = dataBinderHelper.bindAndValidate(dto, new MutablePropertyValues(val));
		assertEquals(9, result.getErrorCount());
		assertEquals(BigDecimal.valueOf(1234L, 1), dto.getDecimal0());
		assertEquals(BigDecimal.valueOf(12345L, 2), dto.getDecimal1());
		assertEquals(BigDecimal.valueOf(1234567L, 4), dto.getDecimal3());
		assertEquals(Double.valueOf(123.4), dto.getDouble0());
		assertEquals(Double.valueOf(123.45), dto.getDouble1());
		assertEquals(Double.valueOf(123.4567), dto.getDouble3());
		assertEquals(Float.valueOf(123.4f), dto.getFloat0());
		assertEquals(Float.valueOf(123.45f), dto.getFloat1());
		assertEquals(Float.valueOf(123.4567f), dto.getFloat3());
	}

	public static class TestDto {

		@NumberFormat(pattern = "###0.#########")
		@NumberScale()
		private BigDecimal number;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(0)
		private BigDecimal decimal0;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(1)
		private BigDecimal decimal1;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(3)
		private BigDecimal decimal3;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(0)
		private Double double0;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(1)
		private Double double1;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(3)
		private Double double3;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(0)
		private Float float0;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(1)
		private Float float1;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(3)
		private Float float3;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(0)
		private Integer int0;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(1)
		private Integer int1;

		@NumberFormat(pattern = "###0.#########")
		@NumberScale(3)
		private Integer int3;

		public BigDecimal getNumber() {
			return number;
		}

		public void setNumber(BigDecimal number) {
			this.number = number;
		}

		public BigDecimal getDecimal0() {
			return decimal0;
		}

		public void setDecimal0(BigDecimal decimal0) {
			this.decimal0 = decimal0;
		}

		public BigDecimal getDecimal1() {
			return decimal1;
		}

		public void setDecimal1(BigDecimal decimal1) {
			this.decimal1 = decimal1;
		}

		public BigDecimal getDecimal3() {
			return decimal3;
		}

		public void setDecimal3(BigDecimal decimal3) {
			this.decimal3 = decimal3;
		}

		public Double getDouble0() {
			return double0;
		}

		public void setDouble0(Double double0) {
			this.double0 = double0;
		}

		public Double getDouble1() {
			return double1;
		}

		public void setDouble1(Double double1) {
			this.double1 = double1;
		}

		public Double getDouble3() {
			return double3;
		}

		public void setDouble3(Double double3) {
			this.double3 = double3;
		}

		public Float getFloat0() {
			return float0;
		}

		public void setFloat0(Float float0) {
			this.float0 = float0;
		}

		public Float getFloat1() {
			return float1;
		}

		public void setFloat1(Float float1) {
			this.float1 = float1;
		}

		public Float getFloat3() {
			return float3;
		}

		public void setFloat3(Float float3) {
			this.float3 = float3;
		}

		public Integer getInt0() {
			return int0;
		}

		public void setInt0(Integer int0) {
			this.int0 = int0;
		}

		public Integer getInt1() {
			return int1;
		}

		public void setInt1(Integer int1) {
			this.int1 = int1;
		}

		public Integer getInt3() {
			return int3;
		}

		public void setInt3(Integer int3) {
			this.int3 = int3;
		}
	}

}
