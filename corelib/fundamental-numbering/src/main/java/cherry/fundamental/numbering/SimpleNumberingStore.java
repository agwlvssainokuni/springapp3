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

import static java.text.MessageFormat.format;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections4.map.LazyMap;

public class SimpleNumberingStore implements NumberingStore {

	private final Map<String, Definition> definitionMap;

	private final Map<String, Long> currentValueMap;

	private final Map<String, Lock> lockMap;

	public SimpleNumberingStore(Map<String, Definition> definitionMap) {
		this.definitionMap = definitionMap;
		currentValueMap = LazyMap.lazyMap(new HashMap<String, Long>(), () -> Long.valueOf(0L));
		lockMap = LazyMap.lazyMap(new HashMap<String, Lock>(), () -> new ReentrantLock(true));
	}

	@Override
	public Definition getDefinition(String numberName) {
		if (!definitionMap.containsKey(numberName)) {
			throw new IllegalArgumentException(format("{0} must be defined", numberName));
		}
		return definitionMap.get(numberName);
	}

	@Override
	public synchronized long loadAndLock(String numberName) {
		lockMap.get(numberName).lock();
		return currentValueMap.get(numberName).longValue();
	}

	@Override
	public void saveAndUnlock(String numberName, long current) {
		currentValueMap.put(numberName, Long.valueOf(current));
		lockMap.get(numberName).unlock();
	}

}
