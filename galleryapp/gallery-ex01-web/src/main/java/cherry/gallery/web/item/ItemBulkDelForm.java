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

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ItemBulkDelForm {

	@NotEmpty()
	@Valid()
	private List<SubForm> list;

	public List<SubForm> getList() {
		return list;
	}

	public void setList(List<SubForm> list) {
		this.list = list;
	}

	public static class SubForm {

		@NotNull
		private Long id;

		@NotNull
		private Long lockVer;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getLockVer() {
			return lockVer;
		}

		public void setLockVer(Long lockVer) {
			this.lockVer = lockVer;
		}
	}

}
