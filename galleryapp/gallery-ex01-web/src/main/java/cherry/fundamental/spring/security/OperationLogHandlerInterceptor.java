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

package cherry.fundamental.spring.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class OperationLogHandlerInterceptor implements HandlerInterceptor {

	public static final String LOGIN_ID = "loginId";

	public static final String PARAM = "param";

	public static final String OPER_ENTER = "operation.ENTER";

	public static final String OPER_MIDDLE = "operation.MIDDLE";

	public static final String OPER_EXIT = "operation.EXIT";

	private final Logger loggerEnter = LoggerFactory.getLogger(OPER_ENTER);

	private final Logger loggerMiddle = LoggerFactory.getLogger(OPER_MIDDLE);

	private final Logger loggerExit = LoggerFactory.getLogger(OPER_EXIT);

	private final List<Pair<Pattern, String>> paramPattern;

	public OperationLogHandlerInterceptor(Pattern... pattern) {
		if (pattern == null) {
			paramPattern = new ArrayList<>(0);
		} else {
			paramPattern = new ArrayList<>(pattern.length);
			int i = 0;
			for (Pattern p : pattern) {
				paramPattern.add(Pair.of(p, PARAM + i));
				i += 1;
			}
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			SecurityContext context = SecurityContextHolder.getContext();
			if (context != null) {
				principal = context.getAuthentication();
			}
		}
		if (principal != null) {
			MDC.put(LOGIN_ID, principal.getName());
		}

		StringBuilder builder = createBasicInfo(request);

		builder.append(" {");
		boolean first = true;
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {

			String key = entry.getKey();
			String lkey = key.toLowerCase();
			String[] val = entry.getValue();

			if (!first) {
				builder.append(", ");
			}
			first = false;
			builder.append(key).append(": ");
			if (lkey.contains("password")) {
				builder.append("<MASKED>");
			} else {
				builder.append(ToStringBuilder.reflectionToString(val, ToStringStyle.SIMPLE_STYLE));
			}

			for (Pair<Pattern, String> p : paramPattern) {
				if (p.getLeft().matcher(lkey).matches()) {
					if (val != null && val.length == 1) {
						MDC.put(p.getRight(), val[0]);
					} else {
						MDC.put(p.getRight(), ToStringBuilder.reflectionToString(val, ToStringStyle.SIMPLE_STYLE));
					}
				}
			}
		}
		builder.append("}");

		loggerEnter.info(builder.toString());

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {

		StringBuilder builder = createBasicInfo(request);

		if (modelAndView == null) {
			builder.append(" ModelAndView=null");
		} else if (modelAndView.hasView()) {
			builder.append(" viewName=").append(modelAndView.getViewName());
		} else {
			builder.append(" noView");
		}

		loggerMiddle.info(builder.toString());
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		StringBuilder builder = createBasicInfo(request);
		if (ex == null) {
			loggerExit.info(builder.toString());
		} else {
			loggerExit.error(builder.toString(), ex);
		}

		MDC.remove(LOGIN_ID);
		for (Pair<Pattern, String> p : paramPattern) {
			MDC.remove(p.getRight());
		}
	}

	private StringBuilder createBasicInfo(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		return builder.append(request.getRemoteAddr()).append(" ").append(request.getMethod()).append(" ")
				.append(request.getRequestURI());
	}

}
