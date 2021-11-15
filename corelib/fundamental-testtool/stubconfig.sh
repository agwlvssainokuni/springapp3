#!/bin/bash -e
#
# Copyright 2021 agwlvssainokuni
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# ディレクトリ解決の起点(このスクリプトを配置したディレクトリ)。
basedir=$(cd $(dirname ${BASH_SOURCE[0]}) && pwd)

######################################################################
# (1) コマンドライン引数を解析する。
usage_and_exit() {
    echo "Usage: $0 [options]" 1>&2
    echo "  Options" 1>&2
    echo "    -u BASEURL" 1>&2
    echo "    -s SRCDIR" 1>&2
    exit $1
}

# 構成項目。
conf_url=http://localhost:8080
conf_src=./src

while getopts u:s:h OPT
do
    case $OPT in
    u) conf_url=${OPTARG%/};;
    s) conf_src=${OPTARG%/};;
    h) usage_and_exit 0;;
    \?) usage_and_exit -1;;
    esac
done
shift $((OPTIND - 1))

######################################################################
# (2) 主処理の実体を定義する。

# (a) スタブ設定ファイルをリストアップする。
find ${conf_src} -type f -name '*.js' | sort | while read file
do
    # (b) スタブ設定対象のクラス名、メソッド名、メソッドインデックスを抽出する。
    className=$(basename $(dirname ${file}))
    basename ${file} .js | while IFS=. read name index
    do
        methodName=${name}
        methodIndex=${index:-0}
        # (c) 抽出したクラス名、メソッド名、メソッドインデックスを画面に表示する。
        echo ${file}
        echo "  ${className}"
        echo "  ${methodName} ${methodIndex}"
        # (d) スタブ設定対象APIを呼び出す。
        curl \
            --data-urlencode "className=${className}" \
            --data-urlencode "methodName=${methodName}" \
            --data-urlencode "methodIndex=${methodIndex}" \
            --data-urlencode "value=" \
            --data-urlencode "valueType=" \
            --data-urlencode "script@${file}" \
            --data-urlencode "engine=" \
            "${conf_url}/testtool/stubconfig/put"
    done
done
