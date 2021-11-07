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
    const handleInvoke = () => invoke(beanName, className, methodName, methodIndex, args, argTypes)
        .then(r => {
            setResult(r);
        });

    return (
        <Container>
            <Typography variant="h4" marginTop={1} marginBottom={2}>
                呼出しツール
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
                    <InputLabel>引数</InputLabel>
                </Grid>
                <Grid item lg={7}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        size="small"
                        multiline
                        minRows={3}
                        label="引数のリストをYAML形式で指定"
                        value={args}
                        onChange={(e) => setArgs(e.target.value)}>
                    </TextField>
                </Grid>
                <Grid item lg={4}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        size="small"
                        multiline
                        minRows={3}
                        label="引数の型のリストをYAML形式で指定(非必須)"
                        value={argTypes}
                        onChange={(e) => setArgTypes(e.target.value)}>
                    </TextField>
                </Grid>

                <Grid item lg={1}></Grid>
                <Grid item lg={11}>
                    <Button
                        fullWidth
                        variant="contained"
                        onClick={handleInvoke}>
                        実行
                    </Button>
                </Grid>

                <Grid item lg={1}>
                    <InputLabel>実行結果</InputLabel>
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
