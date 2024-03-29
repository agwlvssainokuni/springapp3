/*
 * Copyright 2014,2021 agwlvssainokuni
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class AesCryptoTest {

	@Test
	public void testDefault() throws Exception {

		AesCrypto crypto = new AesCrypto();
		crypto.setSecretKeyBytes(RandomUtils.nextBytes(16));

		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] enc = crypto.encrypt(plain);
			byte[] dec = crypto.decrypt(enc);
			assertArrayEquals(dec, plain);
		}
	}

	@Test
	public void testCBC() throws Exception {

		AesCrypto crypto = new AesCrypto();
		crypto.setAlgorithm("AES/CBC/PKCS5Padding");
		crypto.setSecretKeyBytes(RandomUtils.nextBytes(16));

		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] enc = crypto.encrypt(plain);
			byte[] dec = crypto.decrypt(enc);
			assertArrayEquals(dec, plain);
		}
	}

	@Test
	public void testUsingKeyCrypto() throws Exception {

		byte[] key = RandomUtils.nextBytes(16);

		AesCrypto crypto0 = new AesCrypto();
		crypto0.setSecretKeyBytes(key);

		AesCrypto keyCrypto = new AesCrypto();
		keyCrypto.setSecretKeyBytes(RandomUtils.nextBytes(16));

		AesCrypto crypto1 = new AesCrypto();
		crypto1.setKeyCrypto(keyCrypto);
		crypto1.setSecretKeyBytes(keyCrypto.encrypt(key));

		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] enc0 = crypto0.encrypt(plain);
			byte[] enc1 = crypto1.encrypt(plain);
			assertFalse(Arrays.equals(enc1, enc0));
			byte[] dec0 = crypto0.decrypt(enc0);
			byte[] dec1 = crypto1.decrypt(enc1);
			assertArrayEquals(dec0, plain);
			assertArrayEquals(dec1, plain);
		}
	}

	@Test
	public void testRandomizing() {
		int size = 10000;
		for (int i = 0; i < 10; i++) {

			byte[] key = RandomUtils.nextBytes(16);
			byte[] plain = RandomUtils.nextBytes(1024);
			AesCrypto crypto = new AesCrypto();
			crypto.setSecretKeyBytes(key);

			Set<String> set = new HashSet<>();
			for (int j = 0; j < size; j++) {
				byte[] enc = crypto.encrypt(plain);
				set.add(Hex.encodeHexString(enc));
			}
			assertEquals(set.size(), size);
		}
	}

}
