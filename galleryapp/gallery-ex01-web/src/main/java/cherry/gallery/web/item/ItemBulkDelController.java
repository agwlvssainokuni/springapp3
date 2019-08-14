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

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import cherry.gallery.web.item.ItemBulkDelForm.SubForm;

@RequestMapping("/item/bulkdel")
@SessionAttributes(names = { "itemBulkForm" }, types = { ItemBulkDelForm.class })
@Controller
public class ItemBulkDelController {

	private static final String VIEW_START = "item/bulkdel/start";

	@Autowired
	private ItemService itemService;

	@RequestMapping()
	public ModelAndView init(@RequestParam(defaultValue = "") String to, SessionStatus status) {
		status.setComplete();
		UriComponents redirTo;
		if (StringUtils.isNotEmpty(to)) {
			redirTo = UriComponentsBuilder.fromPath(to).build();
		} else {
			redirTo = fromMethodCall(on(ItemListController.class).execute(null, null)).build();
		}
		return new ModelAndView(new RedirectView(redirTo.toUriString(), true));
	}

	@RequestMapping("start")
	public ModelAndView start(@Validated() ItemBulkForm idform, BindingResult idbinding, ItemBulkDelForm form,
			BindingResult binding, SessionStatus status) {
		if (idbinding.hasErrors()) {
			status.setComplete();
			UriComponents redirTo = fromMethodCall(on(ItemListController.class).execute(null, null)).build();
			return new ModelAndView(new RedirectView(redirTo.toUriString(), true));
		}

		List<Long> idlist = idform.getCheckedId();
		if (idlist.isEmpty()) {
			status.setComplete();
			UriComponents redirTo = fromMethodCall(on(ItemListController.class).execute(null, null)).build();
			return new ModelAndView(new RedirectView(redirTo.toUriString(), true));
		}

		// 照会してFORMにセット。
		List<Item> list = itemService.findById(idlist);
		form.setList(list.stream().map(item -> {
			SubForm f = new SubForm();
			f.setId(item.getId());
			f.setLockVer(item.getLockVer());
			return f;
		}).collect(Collectors.toList()));

		ModelAndView mav = new ModelAndView(VIEW_START);
		mav.addObject(new Items(list));
		return mav;
	}

	@RequestMapping("execute")
	public ModelAndView execute(@Validated() ItemBulkDelForm form, BindingResult binding) {
		if (hasErrors(form, binding)) {
			return new ModelAndView(VIEW_START);
		}

		// 削除処理。
		List<Item> list = form.getList().stream().map(f -> {
			Item item = new Item();
			item.setId(f.getId());
			item.setLockVer(f.getLockVer());
			return item;
		}).collect(Collectors.toList());
		itemService.delete(list);

		UriComponents redirTo = fromMethodCall(on(ItemBulkDelController.class).completed(null, null)).build();
		return new ModelAndView(new RedirectView(redirTo.toUriString(), true));
	}

	@RequestMapping("completed")
	public ModelAndView completed(ItemBulkDelForm form, BindingResult binding) {
		return new ModelAndView();
	}

	private boolean hasErrors(ItemBulkDelForm form, BindingResult binding) {

		// 単項目チェック
		if (binding.hasErrors()) {
			return true;
		}

		// 項目間チェック
		// TODO 項目間チェック。

		if (binding.hasErrors()) {
			return true;
		}

		// 整合性チェック
		// TODO 整合性チェック。

		if (binding.hasErrors()) {
			return true;
		}

		return false;
	}

}
