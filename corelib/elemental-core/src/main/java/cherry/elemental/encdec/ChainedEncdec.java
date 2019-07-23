/*
 * Copyright 2019 agwlvssainokuni
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

package cherry.elemental.encdec;

import java.util.List;
import java.util.ListIterator;

public class ChainedEncdec<R, T, E> implements Encdec<R, E> {

	private final Encdec<R, T> rawSide;

	private final List<Encdec<T, T>> middle;

	private final Encdec<T, E> encodedSide;

	public ChainedEncdec(Encdec<R, T> rawSide, List<Encdec<T, T>> middle, Encdec<T, E> encodedSide) {
		this.rawSide = rawSide;
		this.middle = middle;
		this.encodedSide = encodedSide;
	}

	@Override
	public E encode(R raw) {
		T t = rawSide.encode(raw);
		if (middle != null) {
			for (ListIterator<Encdec<T, T>> it = middle.listIterator(0); it.hasNext();) {
				t = it.next().encode(t);
			}
		}
		return encodedSide.encode(t);
	}

	@Override
	public R decode(E encoded) {
		T t = encodedSide.decode(encoded);
		if (middle != null) {
			for (ListIterator<Encdec<T, T>> it = middle.listIterator(middle.size()); it.hasPrevious();) {
				t = it.previous().decode(t);
			}
		}
		return rawSide.decode(t);
	}

}
