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

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Tuple7<X1, X2, X3, X4, X5, X6, X7> implements Serializable {

	private static final long serialVersionUID = 1L;

	private X1 x1;

	private X2 x2;

	private X3 x3;

	private X4 x4;

	private X5 x5;

	private X6 x6;

	private X7 x7;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public X1 getX1() {
		return x1;
	}

	public void setX1(X1 x1) {
		this.x1 = x1;
	}

	public X2 getX2() {
		return x2;
	}

	public void setX2(X2 x2) {
		this.x2 = x2;
	}

	public X3 getX3() {
		return x3;
	}

	public void setX3(X3 x3) {
		this.x3 = x3;
	}

	public X4 getX4() {
		return x4;
	}

	public void setX4(X4 x4) {
		this.x4 = x4;
	}

	public X5 getX5() {
		return x5;
	}

	public void setX5(X5 x5) {
		this.x5 = x5;
	}

	public X6 getX6() {
		return x6;
	}

	public void setX6(X6 x6) {
		this.x6 = x6;
	}

	public X7 getX7() {
		return x7;
	}

	public void setX7(X7 x7) {
		this.x7 = x7;
	}

}