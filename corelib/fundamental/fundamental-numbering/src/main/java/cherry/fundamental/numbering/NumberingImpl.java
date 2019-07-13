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

import java.text.MessageFormat;

public class NumberingImpl implements Numbering {

	private final NumberingStore numberingStore;

	public NumberingImpl(NumberingStore numberingStore) {
		this.numberingStore = numberingStore;
	}

	@Override
	public String issueAsString(String numberName) {

		if (numberName == null) {
			throw new IllegalArgumentException("numberName must not be null");
		}

		Definition def = numberingStore.getDefinition(numberName);
		MessageFormat fmt = new MessageFormat(def.getTemplate());
		long current = numberingStore.loadAndLock(numberName);
		int offset = 0;
		try {

			long v = current + 1;
			if (v < def.getMinValue()) {
				throw new IllegalStateException(format("{0} must not be < {1}", numberName, def.getMinValue()));
			}
			if (v > def.getMaxValue()) {
				throw new IllegalStateException(format("{0} must not be > {1}", numberName, def.getMaxValue()));
			}
			String result = fmt.format(new Object[] { Long.valueOf(v) });

			offset = 1;
			return result;
		} finally {
			numberingStore.saveAndUnlock(numberName, current + offset);
		}
	}

	@Override
	public String[] issueAsString(String numberName, int count) {

		if (numberName == null) {
			throw new IllegalArgumentException("numberName must not be null");
		}
		if (count <= 0) {
			throw new IllegalArgumentException("count must not be <= 0");
		}

		Definition def = numberingStore.getDefinition(numberName);
		MessageFormat fmt = new MessageFormat(def.getTemplate());
		long current = numberingStore.loadAndLock(numberName);
		int offset = 0;
		try {

			String[] result = new String[count];
			for (int i = 1; i <= count; i++) {
				long v = current + i;
				if (v < def.getMinValue()) {
					throw new IllegalStateException(format("{0} must not be < {1}", numberName, def.getMinValue()));
				}
				if (v > def.getMaxValue()) {
					throw new IllegalStateException(format("{0} must not be > {1}", numberName, def.getMaxValue()));
				}
				result[i - 1] = fmt.format(new Object[] { Long.valueOf(v) });
			}

			offset = count;
			return result;
		} finally {
			numberingStore.saveAndUnlock(numberName, current + offset);
		}
	}

	@Override
	public long issueAsLong(String numberName) {

		if (numberName == null) {
			throw new IllegalArgumentException("numberName must not be null");
		}

		Definition def = numberingStore.getDefinition(numberName);
		long current = numberingStore.loadAndLock(numberName);
		int offset = 0;
		try {

			long v = current + 1;
			if (v < def.getMinValue()) {
				throw new IllegalStateException(format("{0} must not be < {1}", numberName, def.getMinValue()));
			}
			if (v > def.getMaxValue()) {
				throw new IllegalStateException(format("{0} must not be > {1}", numberName, def.getMaxValue()));
			}

			offset = 1;
			return v;
		} finally {
			numberingStore.saveAndUnlock(numberName, current + offset);
		}
	}

	@Override
	public long[] issueAsLong(String numberName, int count) {

		if (numberName == null) {
			throw new IllegalArgumentException("numberName must not be null");
		}
		if (count <= 0) {
			throw new IllegalArgumentException("count must not be <= 0");
		}

		Definition def = numberingStore.getDefinition(numberName);
		long current = numberingStore.loadAndLock(numberName);
		int offset = 0;
		try {

			long[] result = new long[count];
			for (int i = 1; i <= count; i++) {
				long v = current + i;
				if (v < def.getMinValue()) {
					throw new IllegalStateException(format("{0} must not be < {1}", numberName, def.getMinValue()));
				}
				if (v > def.getMaxValue()) {
					throw new IllegalStateException(format("{0} must not be > {1}", numberName, def.getMaxValue()));
				}
				result[i - 1] = v;
			}

			offset = count;
			return result;
		} finally {
			numberingStore.saveAndUnlock(numberName, current + offset);
		}
	}

}
