/*
 * Copyright 2016,2021 agwlvssainokuni
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

package cherry.elemental.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class MdMapsTest {

	@Test
	public void test1d() {
		Map<Integer, Boolean> map1 = MdMaps.defaulted1dMap(MdMaps.HASHMAP, Boolean.FALSE);
		Map<Integer, Boolean> map2 = MdMaps.defaulted1dMap(MdMaps.HASHMAP, () -> Boolean.TRUE);
		Map<Integer, Boolean> map3 = MdMaps.lazy1dMap(MdMaps.HASHMAP, () -> Boolean.FALSE);
		for (int x1 = 0; x1 < 1000000; x1++) {
			assertFalse(MdMaps.getOpt(map1, rand()).get());
			assertTrue(MdMaps.getOpt(map2, rand()).get());
			assertFalse(MdMaps.getOpt(map3, rand()).get());
		}
	}

	@Test
	public void test2d() {
		Map<Integer, Map<Integer, Boolean>> map1 = MdMaps.defaulted2dMap(MdMaps.TREEMAP, Boolean.FALSE);
		Map<Integer, Map<Integer, Boolean>> map2 = MdMaps.defaulted2dMap(MdMaps.TREEMAP, () -> Boolean.TRUE);
		Map<Integer, Map<Integer, Boolean>> map3 = MdMaps.lazy2dMap(MdMaps.TREEMAP, () -> Boolean.FALSE);
		for (int x1 = 0; x1 < 1000; x1++) {
			for (int x2 = 0; x2 < 1000; x2++) {
				assertFalse(MdMaps.getOpt(map1, rand(), rand()).get());
				assertTrue(MdMaps.getOpt(map2, rand(), rand()).get());
				assertFalse(MdMaps.getOpt(map3, rand(), rand()).get());
			}
		}
	}

	@Test
	public void test3d() {
		Map<Integer, Map<Integer, Map<Integer, Boolean>>> map1 = MdMaps.defaulted3dMap(MdMaps.LINKEDHASHMAP,
				Boolean.FALSE);
		Map<Integer, Map<Integer, Map<Integer, Boolean>>> map2 = MdMaps.defaulted3dMap(MdMaps.LINKEDHASHMAP,
				() -> Boolean.TRUE);
		Map<Integer, Map<Integer, Map<Integer, Boolean>>> map3 = MdMaps.lazy3dMap(MdMaps.LINKEDHASHMAP,
				() -> Boolean.FALSE);
		for (int x1 = 0; x1 < 100; x1++) {
			for (int x2 = 0; x2 < 100; x2++) {
				for (int x3 = 0; x3 < 100; x3++) {
					assertFalse(MdMaps.getOpt(map1, rand(), rand(), rand()).get());
					assertTrue(MdMaps.getOpt(map2, rand(), rand(), rand()).get());
					assertFalse(MdMaps.getOpt(map3, rand(), rand(), rand()).get());
				}
			}
		}
	}

	// @Test
	public void test4d() {
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>> map1 = MdMaps.defaulted4dMap(MdMaps.HASHMAP,
				Boolean.FALSE);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>> map2 = MdMaps.defaulted4dMap(MdMaps.HASHMAP,
				() -> Boolean.TRUE);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>> map3 = MdMaps.lazy4dMap(MdMaps.HASHMAP,
				() -> Boolean.FALSE);
		for (int x1 = 0; x1 < 100; x1++) {
			for (int x2 = 0; x2 < 100; x2++) {
				for (int x3 = 0; x3 < 10; x3++) {
					for (int x4 = 0; x4 < 10; x4++) {
						assertFalse(MdMaps.getOpt(map1, rand(), rand(), rand(), rand()).get());
						assertTrue(MdMaps.getOpt(map2, rand(), rand(), rand(), rand()).get());
						assertFalse(MdMaps.getOpt(map3, rand(), rand(), rand(), rand()).get());
					}
				}
			}
		}
	}

	// @Test
	public void test5d() {
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>>> map1 = MdMaps
				.defaulted5dMap(MdMaps.TREEMAP, Boolean.FALSE);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>>> map2 = MdMaps
				.defaulted5dMap(MdMaps.TREEMAP, () -> Boolean.TRUE);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>>> map3 = MdMaps
				.lazy5dMap(MdMaps.TREEMAP, () -> Boolean.FALSE);
		for (int x1 = 0; x1 < 100; x1++) {
			for (int x2 = 0; x2 < 10; x2++) {
				for (int x3 = 0; x3 < 10; x3++) {
					for (int x4 = 0; x4 < 10; x4++) {
						for (int x5 = 0; x5 < 10; x5++) {
							assertFalse(MdMaps.getOpt(map1, rand(), rand(), rand(), rand(), rand()).get());
							assertTrue(MdMaps.getOpt(map2, rand(), rand(), rand(), rand(), rand()).get());
							assertFalse(MdMaps.getOpt(map3, rand(), rand(), rand(), rand(), rand()).get());
						}
					}
				}
			}
		}
	}

	// @Test
	public void test6d() {
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>>>> map1 = MdMaps
				.defaulted6dMap(MdMaps.LINKEDHASHMAP, Boolean.FALSE);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>>>> map2 = MdMaps
				.defaulted6dMap(MdMaps.LINKEDHASHMAP, () -> Boolean.TRUE);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>>>> map3 = MdMaps
				.lazy6dMap(MdMaps.LINKEDHASHMAP, () -> Boolean.FALSE);
		for (int x1 = 0; x1 < 10; x1++) {
			for (int x2 = 0; x2 < 10; x2++) {
				for (int x3 = 0; x3 < 10; x3++) {
					for (int x4 = 0; x4 < 10; x4++) {
						for (int x5 = 0; x5 < 10; x5++) {
							for (int x6 = 0; x6 < 10; x6++) {
								assertFalse(MdMaps.getOpt(map1, rand(), rand(), rand(), rand(), rand(), rand()).get());
								assertTrue(MdMaps.getOpt(map2, rand(), rand(), rand(), rand(), rand(), rand()).get());
								assertFalse(MdMaps.getOpt(map3, rand(), rand(), rand(), rand(), rand(), rand()).get());
							}
						}
					}
				}
			}
		}
	}

	@Test
	public void testGetOpt() {
		Map<Integer, Boolean> map1d = Collections.emptyMap();
		Map<Integer, Map<Integer, Boolean>> map2d = Collections.singletonMap(1, map1d);
		Map<Integer, Map<Integer, Map<Integer, Boolean>>> map3d = Collections.singletonMap(1, map2d);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>> map4d = Collections.singletonMap(1, map3d);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>>> map5d = Collections
				.singletonMap(1, map4d);
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>>>> map6d = Collections
				.singletonMap(1, map5d);
		assertFalse(MdMaps.getOpt(map1d, 1).isPresent());
		assertFalse(MdMaps.getOpt(map2d, 1, 1).isPresent());
		assertFalse(MdMaps.getOpt(map3d, 1, 1, 1).isPresent());
		assertFalse(MdMaps.getOpt(map4d, 1, 1, 1, 1).isPresent());
		assertFalse(MdMaps.getOpt(map5d, 1, 1, 1, 1, 1).isPresent());
		assertFalse(MdMaps.getOpt(map6d, 1, 1, 1, 1, 1, 1).isPresent());
	}

	private int rand() {
		return RandomUtils.nextInt(0, 10000);
	}

}
