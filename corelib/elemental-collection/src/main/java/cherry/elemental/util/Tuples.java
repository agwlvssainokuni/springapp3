/*
 * Copyright 2016,2019 agwlvssainokuni
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

public class Tuples {

	public static <X1, X2> Tuple2<X1, X2> of(X1 x1, X2 x2) {
		Tuple2<X1, X2> t = new Tuple2<>();
		t.setX1(x1);
		t.setX2(x2);
		return t;
	}

	public static <X1, X2, X3> Tuple3<X1, X2, X3> of(X1 x1, X2 x2, X3 x3) {
		Tuple3<X1, X2, X3> t = new Tuple3<>();
		t.setX1(x1);
		t.setX2(x2);
		t.setX3(x3);
		return t;
	}

	public static <X1, X2, X3, X4> Tuple4<X1, X2, X3, X4> of(X1 x1, X2 x2, X3 x3, X4 x4) {
		Tuple4<X1, X2, X3, X4> t = new Tuple4<>();
		t.setX1(x1);
		t.setX2(x2);
		t.setX3(x3);
		t.setX4(x4);
		return t;
	}

	public static <X1, X2, X3, X4, X5> Tuple5<X1, X2, X3, X4, X5> of(X1 x1, X2 x2, X3 x3, X4 x4, X5 x5) {
		Tuple5<X1, X2, X3, X4, X5> t = new Tuple5<>();
		t.setX1(x1);
		t.setX2(x2);
		t.setX3(x3);
		t.setX4(x4);
		t.setX5(x5);
		return t;
	}

	public static <X1, X2, X3, X4, X5, X6> Tuple6<X1, X2, X3, X4, X5, X6> of(X1 x1, X2 x2, X3 x3, X4 x4, X5 x5, X6 x6) {
		Tuple6<X1, X2, X3, X4, X5, X6> t = new Tuple6<>();
		t.setX1(x1);
		t.setX2(x2);
		t.setX3(x3);
		t.setX4(x4);
		t.setX5(x5);
		t.setX6(x6);
		return t;
	}

	public static <X1, X2, X3, X4, X5, X6, X7> Tuple7<X1, X2, X3, X4, X5, X6, X7> of(X1 x1, X2 x2, X3 x3, X4 x4, X5 x5,
			X6 x6, X7 x7) {
		Tuple7<X1, X2, X3, X4, X5, X6, X7> t = new Tuple7<>();
		t.setX1(x1);
		t.setX2(x2);
		t.setX3(x3);
		t.setX4(x4);
		t.setX5(x5);
		t.setX6(x6);
		t.setX7(x7);
		return t;
	}

	public static <X1, X2, X3, X4, X5, X6, X7, X8> Tuple8<X1, X2, X3, X4, X5, X6, X7, X8> of(X1 x1, X2 x2, X3 x3,
			X4 x4, X5 x5, X6 x6, X7 x7, X8 x8) {
		Tuple8<X1, X2, X3, X4, X5, X6, X7, X8> t = new Tuple8<>();
		t.setX1(x1);
		t.setX2(x2);
		t.setX3(x3);
		t.setX4(x4);
		t.setX5(x5);
		t.setX6(x6);
		t.setX7(x7);
		t.setX8(x8);
		return t;
	}

	public static <X1, X2, X3, X4, X5, X6, X7, X8, X9> Tuple9<X1, X2, X3, X4, X5, X6, X7, X8, X9> of(X1 x1, X2 x2,
			X3 x3, X4 x4, X5 x5, X6 x6, X7 x7, X8 x8, X9 x9) {
		Tuple9<X1, X2, X3, X4, X5, X6, X7, X8, X9> t = new Tuple9<>();
		t.setX1(x1);
		t.setX2(x2);
		t.setX3(x3);
		t.setX4(x4);
		t.setX5(x5);
		t.setX6(x6);
		t.setX7(x7);
		t.setX8(x8);
		t.setX9(x9);
		return t;
	}

}
