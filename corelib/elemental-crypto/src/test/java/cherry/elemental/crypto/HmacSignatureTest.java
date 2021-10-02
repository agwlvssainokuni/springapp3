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

package cherry.elemental.crypto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class HmacSignatureTest {

	@Test
	public void testSignVerify() throws Exception {
		HmacSignature impl = create();
		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(245);
			byte[] signed = impl.sign(plain);
			assertFalse(Arrays.equals(plain, signed));
			assertTrue(impl.verify(plain, signed));
		}
	}

	private HmacSignature create() throws Exception {
		HmacSignature impl = new HmacSignature();
		impl.setAlgorithm("HmacSHA256");
		impl.setKeyCrypto(new NullCrypto());
		impl.setSecretKeyBytes(RandomUtils.nextBytes(16), "HmacSHA256");
		return impl;
	}

}
