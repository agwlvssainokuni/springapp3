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

package cherry.fundamental.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EnumTest {

	@Test
	public void testGetCode() {
		assertEquals(0, ExitStatus.NORMAL.getCode());
		assertEquals(1, ExitStatus.WARN.getCode());
		assertEquals(2, ExitStatus.ERROR.getCode());
		assertEquals(255, ExitStatus.FATAL.getCode());
	}

	@Test
	public void testMisc() {
		assertEquals(ExitStatus.NORMAL, ExitStatus.valueOf("NORMAL"));
	}

}
