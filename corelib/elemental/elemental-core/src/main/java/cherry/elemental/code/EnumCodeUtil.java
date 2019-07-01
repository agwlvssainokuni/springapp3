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

import static java.text.MessageFormat.format;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumCodeUtil {

	public static <C, E extends ICodeType<C>> List<E> getCodeList(Class<E> type) {
		checkArgument(type);
		return Stream.of(type.getEnumConstants()).collect(Collectors.toList());
	}

	public static <C, E extends ICodeType<C>> Map<C, E> getCodeMap(Class<E> type) {
		checkArgument(type);
		return Stream.of(type.getEnumConstants()).collect(
				Collectors.toMap(ICodeType::getCodeValue, Function.identity(), (a, b) -> b, LinkedHashMap::new));
	}

	@SafeVarargs
	public static <C, E extends ICodeType<C>> Map<C, E> getCodeMap(E... es) {
		return Stream.of(es).collect(
				Collectors.toMap(ICodeType::getCodeValue, Function.identity(), (a, b) -> b, LinkedHashMap::new));
	}

	private static void checkArgument(Class<?> type) {
		if (type.getEnumConstants() == null) {
			throw new IllegalArgumentException(format("{0} does not represent an enum type.", type.getSimpleName()));
		}
	}

}
