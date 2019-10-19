#!/bin/bash
#
# Copyright 2019 agwlvssainokuni
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
basedir=$(cd $(dirname ${BASH_SOURCE[0]}); pwd)

######################################################################
# (1) コマンドライン引数を解析する。
usage_and_exit() {
	echo "Usage: $0 [options] deffile(.def)..." 1>&2
	echo "  Options" 1>&2
	echo "    -d DESTDIR" 1>&2
	echo "    -p PACKAGE" 1>&2
	exit $1
}

# 構成項目。
conf_dst=${basedir}/src/main/java
conf_pkg=cherry.fundamental.format

while getopts d:p:h OPT; do
	case $OPT in
	d) conf_dst=${OPTARG};;
	p) conf_pkg=${OPTARG};;
	h) usage_and_exit 0;;
	\?) usage_and_exit -1;;
	esac
done
shift $((OPTIND - 1))

######################################################################
# (2) 主処理の実体を定義する。
die() {
	echo "$1" 2>&1
	exit -1
}

# (a) 生成先ディレクトリ。
destdir=${conf_dst}/$(echo ${conf_pkg} | sed -e 's/\./\//g')
[[ -d "${destdir}" ]] || mkdir -p "${destdir}" || die "failed to mkdir ${destdir}"

# (b) 「定義ファイルに記述された変換処理」を生成する。
create_entries() {
	while read line; do
		# パラメタを読込む。
		eval ${line}
		# パラメタが揃わなければ生成しない。
		[[ -z "${annotation}" ]] && continue
		[[ -z "${type}" ]] && continue
		[[ -z "${name}" ]] && continue
		[[ -z "${pattern}" ]] && continue
		# 変換処理を形成する。
		cat <<__END_OF_TEMPLATE__

	/** 変換${name}の定義；書式"${pattern}"、対象型{@link ${type}}。 */
	@${annotation}(pattern = "${pattern}")
	private final ${type} fd${name} = null;
	/** 変換${name}で使用する型情報。 */
	private final TypeDescriptor td${name} = createTypeDescriptor("fd${name}");

	/** 書式"${pattern}"の文字列を{@link ${type}}に変換する。 */
	public ${type} to${name}(String src) {
		return (${type}) conversionService.convert(src, tdString, td${name});
	}

	/** {@link ${type}}を書式"${pattern}"の文字列に変換する。 */
	public String fm${name}(${type} src) {
		return (String) conversionService.convert(src, td${name}, tdString);
	}
__END_OF_TEMPLATE__
	done < "${deffile}"
}

# (c) 「変換処理クラス」を生成する。
for deffile in $@; do
	classname=$(basename "${deffile}" .def)
	cat <<__END_OF_TEMPLATE__ > ${destdir}/${classname}.java
/*
 * Copyright 2019 agwlvssainokuni
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

package ${conf_pkg};

import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

public class ${classname} {

	private final ConversionService conversionService;

	private final TypeDescriptor tdString = new TypeDescriptor(ResolvableType.forClass(String.class), null, null);

	public ${classname}(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	private TypeDescriptor createTypeDescriptor(String name) {
		try {
			return new TypeDescriptor(getClass().getDeclaredField(name));
		} catch (NoSuchFieldException ex) {
			throw new IllegalStateException(ex);
		}
	}
$(create_entries)
}
__END_OF_TEMPLATE__
done
