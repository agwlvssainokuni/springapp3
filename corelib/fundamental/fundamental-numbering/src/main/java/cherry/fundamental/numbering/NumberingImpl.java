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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class NumberingImpl implements Numbering {

	private final NumberingStore numberingStore;

	public NumberingImpl(NumberingStore numberingStore) {
		this.numberingStore = numberingStore;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String issueAsString(String numberName) {
		return doIssueAsT(numberName, 1, def -> {
			MessageFormat fmt = new MessageFormat(def.getTemplate());
			return v -> fmt.format(new Object[] { v });
		}).get(0);
	}

	@Transactional()
	@Override
	public String issueAsStringInTx(String numberName) {
		return doIssueAsT(numberName, 1, def -> {
			MessageFormat fmt = new MessageFormat(def.getTemplate());
			return v -> fmt.format(new Object[] { v });
		}).get(0);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public List<String> issueAsString(String numberName, int count) {
		return doIssueAsT(numberName, count, def -> {
			MessageFormat fmt = new MessageFormat(def.getTemplate());
			return v -> fmt.format(new Object[] { v });
		});
	}

	@Transactional()
	@Override
	public List<String> issueAsStringInTx(String numberName, int count) {
		return doIssueAsT(numberName, count, def -> {
			MessageFormat fmt = new MessageFormat(def.getTemplate());
			return v -> fmt.format(new Object[] { v });
		});
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Long issueAsLong(String numberName) {
		return doIssueAsT(numberName, 1, def -> Function.identity()).get(0);
	}

	@Transactional()
	@Override
	public Long issueAsLongInTx(String numberName) {
		return doIssueAsT(numberName, 1, def -> Function.identity()).get(0);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public List<Long> issueAsLong(String numberName, int count) {
		return doIssueAsT(numberName, count, def -> Function.identity());
	}

	@Transactional()
	@Override
	public List<Long> issueAsLongInTx(String numberName, int count) {
		return doIssueAsT(numberName, count, def -> Function.identity());
	}

	private <T> List<T> doIssueAsT(String numberName, int count, Function<Definition, Function<Long, T>> genfmt) {

		if (numberName == null) {
			throw new IllegalArgumentException("numberName must not be null");
		}
		if (count <= 0) {
			throw new IllegalArgumentException("count must not be <= 0");
		}

		Definition def = numberingStore.getDefinition(numberName);
		Function<Long, T> fmt = genfmt.apply(def);
		long current = numberingStore.loadAndLock(numberName);
		int offset = 0;
		try {

			List<T> result = new ArrayList<>(count);
			for (int i = 1; i <= count; i++) {
				long v = current + i;
				if (v < def.getMinValue()) {
					throw new IllegalStateException(format("{0} must not be < {1}", numberName, def.getMinValue()));
				}
				if (v > def.getMaxValue()) {
					throw new IllegalStateException(format("{0} must not be > {1}", numberName, def.getMaxValue()));
				}
				result.add(fmt.apply(v));
			}

			offset = count;
			return result;
		} finally {
			numberingStore.saveAndUnlock(numberName, current + offset);
		}
	}

}
