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

package cherry.gallery.web.item;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

public class ItemListForm {

	private String name;

	@NumberFormat(pattern = "#,##0")
	private BigDecimal priceFm;

	@NumberFormat(pattern = "#,##0")
	private BigDecimal priceTo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPriceFm() {
		return priceFm;
	}

	public void setPriceFm(BigDecimal priceFm) {
		this.priceFm = priceFm;
	}

	public BigDecimal getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(BigDecimal priceTo) {
		this.priceTo = priceTo;
	}

}
