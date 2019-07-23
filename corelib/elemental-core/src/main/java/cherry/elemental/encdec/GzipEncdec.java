/*
 * Copyright 2016,2019 agwlvssainokuni
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

package cherry.elemental.encdec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipEncdec implements Encdec<byte[], byte[]> {

	@Override
	public byte[] encode(byte[] raw) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			try (GZIPOutputStream gzip = new GZIPOutputStream(out, true)) {
				gzip.write(raw);
				gzip.flush();
			}
			return out.toByteArray();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public byte[] decode(byte[] encoded) {
		try (ByteArrayInputStream in = new ByteArrayInputStream(encoded);
				GZIPInputStream gzip = new GZIPInputStream(in);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] buff = new byte[8 * 1024];
			int len;
			while ((len = gzip.read(buff)) >= 0) {
				out.write(buff, 0, len);
			}
			out.flush();
			return out.toByteArray();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
