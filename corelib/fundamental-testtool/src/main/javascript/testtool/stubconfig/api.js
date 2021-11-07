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
export { alwaysReturn, peekStub, resolveBeanName, resolveMethod, getStubbedMethod };

const alwaysReturn = (action => {
  let headers = new Headers();

  if (csrfToken.header != null) {
    headers.append(csrfToken.header, csrfToken.token);
  }

  return async (className, methodName, methodIndex, value, valueType) => {
    let response = await fetch(action, {
      method: "POST",
      headers: headers,
      body: new URLSearchParams({
        className: className,
        methodName: methodName,
        methodIndex: methodIndex,
        value: value,
        valueType: valueType
      })
    });
    let result = await response.text();
    return result;
  };
})(uri("/testtool/stubconfig/put"));

const peekStub = (action => {
  let headers = new Headers();

  if (csrfToken.header != null) {
    headers.append(csrfToken.header, csrfToken.token);
  }

  return async (className, methodName, methodIndex) => {
    let response = await fetch(action, {
      method: "POST",
      headers: headers,
      body: new URLSearchParams({
        className: className,
        methodName: methodName,
        methodIndex: methodIndex
      })
    });
    let result = await response.json();
    return result;
  };
})(uri("/testtool/stubconfig/peek"));

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
})(uri("/testtool/stubconfig/bean"));

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
})(uri("/testtool/stubconfig/method"));

const getStubbedMethod = (action => {
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
})(uri("/testtool/stubconfig/list"));