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
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

public class ItemBulkEditForm {

	@NotEmpty()
	@Valid()
	private List<SubForm> item;

	public List<SubForm> getItem() {
		return item;
	}

	public void setItem(List<SubForm> item) {
		this.item = item;
	}

	public static class SubForm {

		@NotNull
		private Long id;

		@NotEmpty
		private String name;

		@NotNull
		@NumberFormat(pattern = "#,##0")
		private BigDecimal price;

		@NotNull
		private Long lockVer;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public Long getLockVer() {
			return lockVer;
		}

		public void setLockVer(Long lockVer) {
			this.lockVer = lockVer;
		}
	}

}
