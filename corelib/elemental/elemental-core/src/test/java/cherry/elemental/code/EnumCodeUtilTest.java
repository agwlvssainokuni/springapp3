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

package cherry.elemental.code;

import static cherry.elemental.code.EnumCodeUtil.getCodeList;
import static cherry.elemental.code.EnumCodeUtil.getCodeMap;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class EnumCodeUtilTest {

	public enum FlagCode implements ICodeType<Integer> {
		FALSE(11), TRUE(22);

		private Integer c;

		private FlagCode(Integer c) {
			this.c = c;
		}

		@Override
		public Integer getCodeValue() {
			return c;
		}
	}

	public static class NotEnumCode implements ICodeType<Integer> {

		private Integer c;

		public NotEnumCode(Integer c) {
			this.c = c;
		}

		@Override
		public Integer getCodeValue() {
			return c;
		}
	}

	@Test
	public void testGetCodeList() {
		List<FlagCode> list = getCodeList(FlagCode.class);
		assertThat(list.size(), is(2));
		assertThat(list.get(0), is(FlagCode.FALSE));
		assertThat(list.get(1), is(FlagCode.TRUE));
	}

	@Test
	public void testGetCodeMap() {
		Map<Integer, FlagCode> map = getCodeMap(FlagCode.class);
		assertThat(map.size(), is(2));
		assertThat(map.get(11), is(FlagCode.FALSE));
		assertThat(map.get(22), is(FlagCode.TRUE));
	}

	@Test
	public void testGetCodeList_NotEnum() {
		try {
			getCodeList(NotEnumCode.class);
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			// OK
		}
	}

	@Test
	public void testGetCodeMap_NotEnum() {
		try {
			getCodeMap(NotEnumCode.class);
			fail("Exception must be thrown");
		} catch (IllegalArgumentException ex) {
			// OK
		}
	}

	@Test
	public void testInstantiate() {
		try {
			new EnumCodeUtil();
		} catch (Exception ex) {
			fail("Exception must not be thrown");
		}
	}

}
