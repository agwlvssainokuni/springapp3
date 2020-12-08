/*
 * Copyright 2015,2019 agwlvssainokuni
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

package cherry.fundamental.numbering;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefinitionTest {

	@Test
	public void test() {
		Definition dto = new Definition();
		dto.setTemplate("template");
		dto.setMinValue(100000);
		dto.setMaxValue(999999);
		assertEquals("template", dto.getTemplate());
		assertEquals(100000, dto.getMinValue());
		assertEquals(999999, dto.getMaxValue());
		assertEquals("Definition[maxValue=999999,minValue=100000,template=template]", dto.toString());
	}

}
