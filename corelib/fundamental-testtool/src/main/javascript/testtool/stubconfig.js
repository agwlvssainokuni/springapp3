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
// ENTRY

import { uri } from "./uri";
import { common } from "./common";

$(common);

$(function () {

	$("#registerBtn").click(function (event) {
		$.ajax(uri(), {
			method: "POST",
			data: {
				className: $("#className").val(),
				methodName: $("#methodName").val(),
				methodIndex: $("#methodIndex").val(),
				value: $("#value").val(),
				valueType: $("#valueType").val()
			},
			success: function (data, textStatus, jqXHR) {
				$("#result").val(data);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
	});

	$("#peekBtn").click(function (event) {
		$.ajax(uri() + "?peek", {
			method: "POST",
			data: {
				className: $("#className").val(),
				methodName: $("#methodName").val(),
				methodIndex: $("#methodIndex").val()
			},
			success: function (data, textStatus, jqXHR) {
				if (data == null) {
					return;
				}
				$("#value").val(data[0]);
				$("#valueType").val(data[1]);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
	});

	$("#clearBtn").click(function (event) {
		$("#value").val("");
		$("#valueType").val("");
	});

	$("#listBtn").click(function (event) {
		$.ajax(uri() + "?list", {
			method: "POST",
			data: {
				className: $("#className").val()
			},
			success: function (data, textStatus, jqXHR) {
				$("#result").val(data);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
	});

});
