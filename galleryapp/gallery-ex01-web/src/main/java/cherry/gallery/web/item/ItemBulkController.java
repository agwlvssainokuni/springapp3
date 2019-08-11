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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;

@RequestMapping("/item/bulk")
@SessionAttributes(types = { ItemBulkForm.class })
@Controller
public class ItemBulkController {

	@RequestMapping(params = "edit")
	public ModelAndView bulkedit(ItemBulkForm idform) {
		UriComponents redirTo = fromMethodCall(on(ItemBulkEditController.class).start(null, null, null, null, null))
				.build();
		return new ModelAndView(new RedirectView(redirTo.toUriString(), true));
	}

	@RequestMapping(params = "del")
	public ModelAndView bulkdel(ItemBulkForm idform) {
		UriComponents redirTo = fromMethodCall(on(ItemBulkDelController.class).start(null, null, null, null, null))
				.build();
		return new ModelAndView(new RedirectView(redirTo.toUriString(), true));
	}

}
