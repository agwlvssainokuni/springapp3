/*
 * Copyright 2014,2019 agwlvssainokuni
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

package cherry.elemental.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * データの版管理の機能を提供する。<br />
 * 素データはバイト列、版情報は整数とする。版管理の方式は、版情報 (4バイト) を素データの先頭に付加する形式をとる。
 */
public class DefaultVersionStrategy implements VersionStrategy<byte[], Integer> {

	@Override
	public byte[] encode(byte[] data, Integer version) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(baos)) {
			out.writeInt(version.intValue());
			out.write(data);
			out.flush();
			return baos.toByteArray();
		} catch (IOException ex) {
			// 発生しないはず。
			throw new UncheckedIOException(ex);
		}
	}

	@Override
	public VersionedData<byte[], Integer> decode(byte[] encoded) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
				DataInputStream in = new DataInputStream(bais)) {
			int version = in.readInt();
			byte[] data = new byte[encoded.length - 4];
			in.readFully(data);
			return new VersionedData<byte[], Integer>(data, version);
		} catch (IOException ex) {
			// 発生しないはず。
			throw new UncheckedIOException(ex);
		}
	}

}
