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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/item/edit")
@Controller
public class ItemEditController {

	private static final String VIEW_START = "item/edit/start";

	@Autowired
	private ItemService itemService;

	@RequestMapping()
	public ModelAndView init(@RequestParam(defaultValue = "") String to, @RequestParam() long id) {
		UriComponents redirTo;
		if (StringUtils.isNotEmpty(to)) {
			redirTo = UriComponentsBuilder.fromPath(to).build();
		} else {
			redirTo = fromMethodCall(on(ItemEditController.class).start(id, null, null)).build();
		}
		return new ModelAndView(new RedirectView(redirTo.toUriString(), true));
	}

	@RequestMapping("start")
	public ModelAndView start(@RequestParam() long id, ItemEditForm form, BindingResult binding) {

		// 照会してFORMにセット。
		Item item = itemService.findById(id).get();
		form.setName(item.getName());
		form.setPrice(item.getPrice());
		form.setLockVer(item.getLockVer());

		return new ModelAndView(VIEW_START);
	}

	@RequestMapping("confirm")
	public ModelAndView confirm(@RequestParam() long id, @Validated() ItemEditForm form, BindingResult binding) {
		if (hasErrors(id, form, binding)) {
			return new ModelAndView(VIEW_START);
		}
		return new ModelAndView();
	}

	@RequestMapping(value = "execute", params = "back")
	public ModelAndView back(@RequestParam() long id, @Validated() ItemEditForm form, BindingResult binding) {
		return new ModelAndView(VIEW_START);
	}

	@RequestMapping("execute")
	public ModelAndView execute(@RequestParam() long id, @Validated() ItemEditForm form, BindingResult binding) {
		if (hasErrors(id, form, binding)) {
			return new ModelAndView(VIEW_START);
		}

		// 更新処理。
		Item item = new Item();
		item.setId(id);
		item.setName(form.getName());
		item.setPrice(form.getPrice());
		item.setLockVer(form.getLockVer());
		itemService.update(item);

		UriComponents redirTo = fromMethodCall(on(ItemEditController.class).completed(id)).build();
		return new ModelAndView(new RedirectView(redirTo.toUriString(), true));
	}

	@RequestMapping("completed")
	public ModelAndView completed(@RequestParam() long id) {

		// 照会してセット。
		Item item = itemService.findById(id).get();

		ModelAndView mav = new ModelAndView();
		mav.addObject(item);
		return mav;
	}

	private boolean hasErrors(long id, ItemEditForm form, BindingResult binding) {

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
