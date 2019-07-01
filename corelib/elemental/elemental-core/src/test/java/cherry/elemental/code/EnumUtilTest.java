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

import static cherry.elemental.code.EnumUtil.getEnumList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class EnumUtilTest {

	public enum FlagCode {
		FALSE, TRUE
	}

	@Test
	public void testGetEnumList() {
		List<FlagCode> list = getEnumList(FlagCode.class);
		assertThat(list.size(), is(2));
		assertThat(list.get(0), is(FlagCode.FALSE));
		assertThat(list.get(1), is(FlagCode.TRUE));
	}

	@Test
	public void testInstantiate() {
		try {
			new EnumUtil();
		} catch (Exception ex) {
			fail("Exception must not be thrown");
		}
	}

}
