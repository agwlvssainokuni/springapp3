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
import { invoke, resolveBeanName, resolveMethod } from "./api";
export { App };

const App = () => {
  let [className, setClassName] = useState("");
  let [beanName, setBeanName] = useState("Bean名称(非必須)");
  let [beanNameList, setBeanNameList] = useState(["Bean名称(非必須)"]);
  let [methodName, setMethodName] = useState("");
  let [methodNameList, setMethodNameList] = useState(["メソッドを引数のパターンで指定"]);
  let [methodIndex, setMethodIndex] = useState("0");
  let [args, setArgs] = useState("");
  let [argTypes, setArgTypes] = useState("");
  let [result, setResult] = useState("");

  const handleClassName = () => resolveBeanName(className).then(r => {
    setBeanNameList(r);
    setBeanName(r[0]);
  });

  const handleMethodName = () => resolveMethod(className, methodName).then(r => {
    setMethodNameList(r);
    setMethodIndex("0");
  });

  const handleInvoke = () => invoke(beanName, className, methodName, methodIndex, args, argTypes).then(r => {
    setResult(r);
  });

  return /*#__PURE__*/React.createElement(Container, null, /*#__PURE__*/React.createElement(Typography, {
    variant: "h4",
    marginTop: 1,
    marginBottom: 2
  }, "\u547C\u51FA\u3057\u30C4\u30FC\u30EB"), /*#__PURE__*/React.createElement(Grid, {
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
  }, /*#__PURE__*/React.createElement(InputLabel, null, "\u5F15\u6570")), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 7
  }, /*#__PURE__*/React.createElement(TextField, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    multiline: true,
    minRows: 3,
    label: "\u5F15\u6570\u306E\u30EA\u30B9\u30C8\u3092YAML\u5F62\u5F0F\u3067\u6307\u5B9A",
    value: args,
    onChange: e => setArgs(e.target.value)
  })), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 4
  }, /*#__PURE__*/React.createElement(TextField, {
    fullWidth: true,
    variant: "outlined",
    size: "small",
    multiline: true,
    minRows: 3,
    label: "\u5F15\u6570\u306E\u578B\u306E\u30EA\u30B9\u30C8\u3092YAML\u5F62\u5F0F\u3067\u6307\u5B9A(\u975E\u5FC5\u9808)",
    value: argTypes,
    onChange: e => setArgTypes(e.target.value)
  })), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 1
  }), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 11
  }, /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    variant: "contained",
    onClick: handleInvoke
  }, "\u5B9F\u884C")), /*#__PURE__*/React.createElement(Grid, {
    item: true,
    lg: 1
  }, /*#__PURE__*/React.createElement(InputLabel, null, "\u5B9F\u884C\u7D50\u679C")), /*#__PURE__*/React.createElement(Grid, {
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