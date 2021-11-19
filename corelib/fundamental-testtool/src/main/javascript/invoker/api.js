/*
* Copyright 2021 agwlvssainokuni
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
import { csrfToken, uri } from "../resolver";
export { invoke, resolveBeanName, resolveMethod };

const invoke = (action => {
  let headers = new Headers();

  if (csrfToken.header != null) {
    headers.append(csrfToken.header, csrfToken.token);
  }

  return async (beanName, className, methodName, methodIndex, args, argTypes) => {
    let response = await fetch(action, {
      method: "POST",
      headers: headers,
      body: new URLSearchParams({
        beanName: beanName,
        className: className,
        methodName: methodName,
        methodIndex: methodIndex,
        args: args,
        argTypes: argTypes
      })
    });
    let result = await response.text();
    return result;
  };
})(uri("/testtool/invoker/invoke"));

const resolveBeanName = (action => {
  let headers = new Headers();

  if (csrfToken.header != null) {
    headers.append(csrfToken.header, csrfToken.token);
  }

  return async className => {
    let response = await fetch(action, {
      method: "POST",
      headers: headers,
      body: new URLSearchParams({
        className: className
      })
    });
    let result = await response.json();
    return result;
  };
})(uri("/testtool/invoker/bean"));

const resolveMethod = (action => {
  let headers = new Headers();

  if (csrfToken.header != null) {
    headers.append(csrfToken.header, csrfToken.token);
  }

  return async (className, methodName) => {
    let response = await fetch(action, {
      method: "POST",
      headers: headers,
      body: new URLSearchParams({
        className: className,
        methodName: methodName
      })
    });
    let result = await response.json();
    return result;
  };
})(uri("/testtool/invoker/method"));