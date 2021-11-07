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
    let [result, setResult] = useState("");

    const handleClassName = () => resolveBeanName(className)
        .then(r => {
            setBeanNameList(r);
            setBeanName(r[0]);
        });
    const handleMethodName = () => resolveMethod(className, methodName)
        .then(r => {
            setMethodNameList(r);
            setMethodIndex("0");
        });
    const handlePeekBtn = () => peekStub(className, methodName, methodIndex)
        .then(r => {
            setValue(r[0]);
            setValueType(r[1]);
        });
    const handleClearBtn = () => {
        setValue("");
        setValueType("");
    };
    const handleRegisterBtn = () => alwaysReturn(className, methodName, methodIndex, value, valueType)
        .then(r => {
            setResult(r);
        });
    const handleListBtn = () => getStubbedMethod(className)
        .then(r => {
            setResult(r);
        });

    return (
        <Container>
            <Typography variant="h4" marginTop={1} marginBottom={2}>
                スタブ設定ツール
            </Typography>

            <Grid container spacing={1}>

                <Grid item lg={1}>
                    <InputLabel>クラス</InputLabel>
                </Grid>
                <Grid item lg={7}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        size="small"
                        label="BeanのFQCNを指定してください"
                        value={className}
                        onChange={(e) => setClassName(e.target.value)}
                        onBlur={handleClassName}>
                    </TextField>
                </Grid>
                <Grid item lg={4}>
                    <Select
                        fullWidth
                        variant="outlined"
                        size="small"
                        value={beanName}
                        onChange={(e) => setBeanName(e.target.value)}>
                        {
                            beanNameList.map((e) =>
                                <MenuItem value={e}>
                                    {e}
                                </MenuItem>
                            )
                        }
                    </Select>
                </Grid>

                <Grid item lg={1}>
                    <InputLabel>メソッド</InputLabel>
                </Grid>
                <Grid item lg={7}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        size="small"
                        label="メソッドの名称を指定してください"
                        value={methodName}
                        onChange={(e) => setMethodName(e.target.value)}
                        onBlur={handleMethodName}>
                    </TextField>
                </Grid>
                <Grid item lg={4}>
                    <Select
                        fullWidth
                        variant="outlined"
                        size="small"
                        value={methodIndex}
                        onChange={(e) => setMethodIndex(e.target.value)}>
                        {
                            methodNameList.map((e, i) =>
                                <MenuItem value={i}>
                                    {e}
                                </MenuItem>
                            )
                        }
                    </Select>
                </Grid>

                <Grid item lg={1}>
                    <InputLabel>返却値</InputLabel>
                    <Button
                        variant="outlined"
                        size="small"
                        onClick={handleClearBtn}>
                        クリア
                    </Button>
                    <Button
                        variant="outlined"
                        size="small"
                        onClick={handlePeekBtn}>
                        現在値
                    </Button>
                </Grid>
                <Grid item lg={7}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        size="small"
                        multiline
                        minRows={3}
                        label="返却値をYAML形式で指定"
                        value={value}
                        onChange={(e) => setValue(e.target.value)}>
                    </TextField>
                </Grid>
                <Grid item lg={4}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        size="small"
                        multiline
                        minRows={3}
                        label="返却値の型を指定(非必須)"
                        value={valueType}
                        onChange={(e) => setValueType(e.target.value)}>
                    </TextField>
                </Grid>

                <Grid item lg={1}>
                    <Button
                        variant="outlined"
                        onClick={handleListBtn}>
                        一覧
                    </Button>
                </Grid>
                <Grid item lg={11}>
                    <Button
                        fullWidth
                        variant="contained"
                        onClick={handleRegisterBtn}>
                        登録
                    </Button>
                </Grid>

                <Grid item lg={1}>
                    <InputLabel>登録結果</InputLabel>
                </Grid>
                <Grid item lg={11}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        size="small"
                        multiline
                        minRows={5}
                        value={result}
                        onChange={(e) => setResult(e.target.value)}>
                    </TextField>
                </Grid>
            </Grid>

            <Typography align="center" marginTop={2}>
                Copyright &copy;, 2015,2021, agwlvssainokuni
            </Typography>
        </Container>
    );
}
