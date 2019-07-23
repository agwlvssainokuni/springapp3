#!/bin/bash
#
# Copyright 2016,2019 agwlvssainokuni
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

######################################################################
# [機能概要]
# ・環境依存の設定項目を定義するファイルを形成する。
######################################################################
# [処理詳細]
# (1) コマンドライン引数を解析する。
# (2) 主処理の実体を定義する。
#     (a) 環境依存設定ファイル形成
# (3) 引数に指定されたディレクトリに対して主処理を実行する。
######################################################################

# 処理対象のディレクトリを解決する起点。
instdir=$(cd $(dirname ${BASH_SOURCE[0]})/..; pwd)

# 環境依存設定ファイル形成で使用するテンプレートエンジン(Bash版Mustache)。
. $(cd $(dirname ${BASH_SOURCE[0]}); pwd)/mo/mo

######################################################################
# (1) コマンドライン引数を解析する。
usage_and_exit() {
	echo "Usage: $0 [options] subdir..." 1>&2
	echo "  Options" 1>&2
	echo "   [to create config file]" 1>&2
	echo "    -c          " 1>&2
	echo "    -C CONF     " 1>&2
	echo "    -P PFILE    " 1>&2
	echo "    -f          " 1>&2
	exit $1
}

# 環境依存設定ファイル形成
conf_enabled=false
conf_dir=conf
pfile="${instdir}/param.txt"
force=false

while getopts cC:P:flL:S:h OPT; do
	case $OPT in
	c) conf_enabled=true;;
	C) conf_dir=${OPTARG};;
	P) pfile="${instdir}/${OPTARG}";;
	f) force=true;;
	h) usage_and_exit 0;;
	\?) usage_and_exit -1;;
	esac
done
shift $((OPTIND - 1))

######################################################################
# (2) 主処理の実体を定義する。
exit_code=0

die() {
	echo "$1" 2>&1
	exit -1
}

# (2)-(a) 環境依存設定ファイル形成
create_conf_file() {

	pushd "$1" > /dev/null || die "Failed to pushd $1"
	echo "  $1, create environmental configuration"

	for f in *.template; do

		echo "    $f"
		dest=$(basename "$f" .template)
		if [[ ! -f "${pfile}" ]]; then
			cp -p "$f" "${dest}" || die "Failed to cp -p $f $dest"
			continue
		fi

		mo --source="${pfile}" "$f" > "${dest}.tmp"
		diff -q "$f" "${dest}.tmp" > /dev/null
		if [[ $? -eq 0 ]]; then
			rm -f "${dest}.tmp" || die "Failed to rm -f ${dest}.tmp"
			cp -p "$f" "${dest}" || die "Failed to cp -p $f $dest"
			continue
		fi

		if [[ -f "${dest}" ]]; then
			diff -q "${dest}" "${dest}.tmp" > /dev/null
			if [[ $? -ne 0 ]]; then
				if [[ "${force}" == "true" ]]; then
					echo "    [OVERWRITING]"
				else
					echo "    [DIFFERENCE]"
					echo "    ================================================"
					diff -u "${dest}" "${dest}.tmp"
					echo "    ================================================"
					exit_code=1
					continue
				fi
			fi
		fi

		mv -f "${dest}.tmp" "${dest}" || die "Failed to mv -f ${dest}.tmp $dest"
	done

	popd > /dev/null
}

######################################################################
# (3) 引数に指定されたディレクトリに対して主処理を実行する。
for subdir in "$@"; do

	targetdir="${instdir}/${subdir}"
	[[ -d "${targetdir}" ]] || die "$targetdir should exist"

	pushd "${targetdir}" > /dev/null || die "Failed to pushd $targetdir"
	echo "Processing in ${subdir}"

	# 環境依存設定ファイル形成
	if [[ "${conf_enabled}" == "true" ]]; then
		[[ -d "${conf_dir}" ]] || continue
		create_conf_file "${conf_dir}"
	fi

	popd > /dev/null
done

exit ${exit_code}
