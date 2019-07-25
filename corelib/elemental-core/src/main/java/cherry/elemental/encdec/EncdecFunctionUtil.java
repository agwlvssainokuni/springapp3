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

import java.util.function.Function;

public class EncdecFunctionUtil {

	public static <R, E> Function<R, E> createEncodeFunction(Encdec<R, E> encdec) {
		return encdec::encode;
	}

	public static <R, E> Function<E, R> createDecodeFunction(Encdec<R, E> encdec) {
		return encdec::decode;
	}

}