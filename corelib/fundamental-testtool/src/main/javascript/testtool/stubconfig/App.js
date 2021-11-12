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
import { Button, Container, Grid, InputLabel, MenuItem, Select, TextField, Typography } from "@mui/material";
import React, { useState } from "react";
import { alwaysReturn, getStubbedMethod, peekStub, resolveBeanName, resolveMethod } from "./api";
export { App };

const App = () => {
  let [className, setClassName] = useState("");
  let [beanName, setBeanName] = useState("(参考)");
  let [beanNameList, setBeanNameList] = useState(["(参考)"]);
  let [methodName, setMethodName] = useState("");
  let [methodNameList, setMethodNameList] = useState(["メソッドを引数のパターンで指定"]);
  let [methodIndex, setMethodIndex] = useState("0");
  let [value, setValue] = useState("");
  let [valueType, setValueType] = useState("");
  let [script, setScript] = useState("");
  let [engine, setEngine] = useState("");
  let [result, setResult] = useState("");

  const handleClassName = () => resolveBeanName(className).then(r => {
    setBeanNameList(r);
    setBeanName(r[0]);
  });

  const handleMethodName = () => resolveMethod(className, methodName).then(r => {
    setMethodNameList(r);
    setMethodIndex("0");
  });

  const handlePeekBtn = () => peekStub(className, methodName, methodIndex).then(r => {
    setValue(r[0]);
    setValueType(r[1]);
    setScript(r[2]);
    setEngine(r[3]);
  });

  const handleClearBtn = () => {
    setValue("");
    setValueType("");
    setScript("");
    setEngine("");
  };

  const handleRegisterBtn = () => alwaysReturn(className, methodName, methodIndex, value, valueType, script, engine).then(r => {
    setResult(r);
  });

  const handleListBtn = () => getStubbedMethod(className).then(r => {
    setResult(r);
  });

  return /*#__PURE__*/React.createElement(Container, null, /*#__PURE__*/React.createElement(Typography, {
    variant: "h4",
    marginTop: 1,
    marginBottom: 2
  }, "\u30B9\u30BF\u30D6\u8A2D\u5B9A\u30C4\u30FC\u30EB"), /*#__PURE__*/React.createElement(Grid, {
    container: true,
    spacing: 1
  }, /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 1
  }, /*#__PURE__*/React.createElement(InputLabel, null, "\u30AF\u30E9\u30B9")), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 7
  }, /*#__PURE__*/React.createElement(TextField, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    label: "Bean\u306EFQCN\u3092\u6307\u5B9A\u3057\u3066\u304F\u3060\u3055\u3044",
    value: className,
    onChange: e => setClassName(e.target.value),
    onBlur: handleClassName
  })), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 4
  }, /*#__PURE__*/React.createElement(Select, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    value: beanName,
    onChange: e => setBeanName(e.target.value)
  }, beanNameList.map(e => /*#__PURE__*/React.createElement(MenuItem, {
    value: e
  }, e)))), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 1
  }, /*#__PURE__*/React.createElement(InputLabel, null, "\u30E1\u30BD\u30C3\u30C9")), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 7
  }, /*#__PURE__*/React.createElement(TextField, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    label: "\u30E1\u30BD\u30C3\u30C9\u306E\u540D\u79F0\u3092\u6307\u5B9A\u3057\u3066\u304F\u3060\u3055\u3044",
    value: methodName,
    onChange: e => setMethodName(e.target.value),
    onBlur: handleMethodName
  })), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 4
  }, /*#__PURE__*/React.createElement(Select, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    value: methodIndex,
    onChange: e => setMethodIndex(e.target.value)
  }, methodNameList.map((e, i) => /*#__PURE__*/React.createElement(MenuItem, {
    value: i
  }, e)))), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 1
  }, /*#__PURE__*/React.createElement(InputLabel, null, "\u8FD4\u5374\u5024"), /*#__PURE__*/React.createElement(Button, {
    variant: "outlined",
    size: "small",
    onClick: handleClearBtn
  }, "\u30AF\u30EA\u30A2"), /*#__PURE__*/React.createElement(Button, {
    variant: "outlined",
    size: "small",
    onClick: handlePeekBtn
  }, "\u73FE\u5728\u5024")), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 7
  }, /*#__PURE__*/React.createElement(TextField, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    multiline: true,
    minRows: 3,
    label: "\u8FD4\u5374\u5024\u3092YAML\u5F62\u5F0F\u3067\u6307\u5B9A",
    value: value,
    onChange: e => setValue(e.target.value)
  })), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 4
  }, /*#__PURE__*/React.createElement(TextField, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    multiline: true,
    minRows: 3,
    label: "\u8FD4\u5374\u5024\u306E\u578B\u3092\u6307\u5B9A(\u975E\u5FC5\u9808)",
    value: valueType,
    onChange: e => setValueType(e.target.value)
  })), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 1
  }), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 11
  }, /*#__PURE__*/React.createElement(TextField, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    multiline: true,
    minRows: 3,
    label: "\u8FD4\u5374\u5024\u3092\u751F\u6210\u3059\u308B\u30B9\u30AF\u30EA\u30D7\u30C8\u3092\u8A18\u8FF0",
    value: script,
    onChange: e => setScript(e.target.value)
  })), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 1
  }, /*#__PURE__*/React.createElement(Button, {
    variant: "outlined",
    onClick: handleListBtn
  }, "\u4E00\u89A7")), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 11
  }, /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    variant: "contained",
    onClick: handleRegisterBtn
  }, "\u767B\u9332")), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 1
  }, /*#__PURE__*/React.createElement(InputLabel, null, "\u767B\u9332\u7D50\u679C")), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 11
  }, /*#__PURE__*/React.createElement(TextField, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    multiline: true,
    minRows: 5,
    value: result,
    onChange: e => setResult(e.target.value)
  }))), /*#__PURE__*/React.createElement(Typography, {
    align: "center",
    marginTop: 2
  }, "Copyright \xA9, 2015,2021, agwlvssainokuni"));
};