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

package cherry.gallery.login;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/login")
@Controller
public class LoginController {

	@RequestMapping(value = "start")
	public ModelAndView start(LoginForm form, BindingResult binding) {
		form.setPassword(null);
		return new ModelAndView();
	}

	@RequestMapping(value = "start", params = "loginFailed")
	public ModelAndView loginFailed(RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("loginFailed", true);
		return new ModelAndView(new RedirectView(redirectToStart(), true));
	}

	@RequestMapping(value = "start", params = "loggedOut")
	public ModelAndView loggedOut(RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("loggedOut", true);
		return new ModelAndView(new RedirectView(redirectToStart(), true));
	}

	private String redirectToStart() {
		return fromMethodCall(on(LoginController.class).start(null, null)).build().toUriString();
	}

}
