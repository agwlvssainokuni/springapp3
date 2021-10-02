/*
 * Copyright 2016,2021 agwlvssainokuni
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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class ChainedEncdecTest {

	@Test
	public void testEncodeDecodeForStringWithMiddle() {
		Encdec<String, String> impl = createForStringWithMiddle();
		for (int i = 0; i < 1000; i++) {
			String raw = RandomStringUtils.randomAlphanumeric(1024);
			assertEquals(raw, impl.decode(impl.encode(raw)));
		}
	}

	@Test
	public void testEncodeDecodeForStringWithoutMiddle() {
		Encdec<String, String> impl = createForStringWithoutMiddle();
		for (int i = 0; i < 1000; i++) {
			String raw = RandomStringUtils.randomAlphanumeric(1024);
			assertEquals(raw, impl.decode(impl.encode(raw)));
		}
	}

	protected Encdec<String, String> createForStringWithMiddle() {
		return new ChainedEncdec<>(string(), asList(gzip()), base64(true));
	}

	protected Encdec<String, String> createForStringWithoutMiddle() {
		return new ChainedEncdec<>(string(), null, base64(true));
	}

	private Encdec<String, byte[]> string() {
		StringEncdec impl = new StringEncdec();
		impl.setCharset(StandardCharsets.UTF_8);
		return impl;
	}

	private Encdec<byte[], byte[]> gzip() {
		return new GzipEncdec();
	}

	private Encdec<byte[], String> base64(boolean base64url) {
		Base64Encdec impl = new Base64Encdec();
		impl.setBase64url(base64url);
		return impl;
	}

}
