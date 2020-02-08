/*
 * Copyright 2019,2020 agwlvssainokuni
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

import { uri } from "./uri";

export function common() {

	(function (token) {
		$.ajaxSetup({
			headers: { "X-CSRF-TOKEN": token }
		});
	})($("meta[name='csrf']").attr("content"));

	$("#className").blur(function (event) {
		let old = $(this).data("old");
		if (old == $(this).val()) {
			return;
		}
		$(this).data("old", $(this).val());
		if ("" == $(this).val()) {
			return;
		}
		$.ajax(uri() + "?bean", {
			method: "POST",
			data: {
				className: $("#className").val()
			},
			success: function (data, textStatus, jqXHR) {
				$("#beanName option.withValue").remove();
				for (let i = 0; i < data.length; i++) {
					let opt = $("<option/>").addClass("withValue");
					opt.attr("value", data[i]).attr("label", data[i]).text(data[i]);
					$("#beanName").append(opt);
				}
				$("#beanName option.withValue:first").attr("selected", "selected");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
	});

	$("#methodName").blur(function (event) {
		let old = $(this).data("old");
		if (old == $(this).val()) {
			return;
		}
		$(this).data("old", $(this).val());
		if ("" == $(this).val() || "" == $("#className").val()) {
			return;
		}
		$.ajax(uri() + "?method", {
			method: "POST",
			data: {
				className: $("#className").val(),
				methodName: $("#methodName").val()
			},
			success: function (data, textStatus, jqXHR) {
				$("#methodIndex option.withValue").remove();
				for (let i = 0; i < data.length; i++) {
					let opt = $("<option/>").addClass("withValue");
					opt.attr("value", i).attr("label", data[i]).text(data[i]);
					$("#methodIndex").append(opt);
				}
				$("#methodIndex option.withValue:first").attr("selected", "selected");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
	});

}
