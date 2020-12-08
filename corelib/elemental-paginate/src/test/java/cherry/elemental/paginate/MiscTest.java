/*
 * Copyright 2004,2019 agwlvssainokuni
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

package cherry.elemental.paginate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 通常の機能の試験でカバレッジが到達しない部位を試験する。
 */
public class MiscTest {

	@Test
	public void testPageToString() {
		Page page = new Page();
		page.setNo(1);
		page.setCount(2);
		page.setFrom(3);
		page.setTo(4);
		assertEquals("Page[count=2,from=3,no=1,to=4]", page.toString());
	}

	@Test
	public void testPageSetToString() {
		PageSet pageSet = new PageSet();
		assertEquals(
				"PageSet[current=<null>,first=<null>,last=<null>,next=<null>,pageSz=0,prev=<null>,range=<null>,totalCount=0]",
				pageSet.toString());
	}

}
