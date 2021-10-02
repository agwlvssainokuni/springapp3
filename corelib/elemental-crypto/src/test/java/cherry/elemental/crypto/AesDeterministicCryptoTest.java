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

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class AesDeterministicCryptoTest {

	@Test
	public void testDefault() throws Exception {

		AesDeterministicCrypto crypto = new AesDeterministicCrypto();
		crypto.setSecretKeyBytes(RandomUtils.nextBytes(16));
		crypto.setInitVectorBytes(RandomUtils.nextBytes(16));

		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] enc = crypto.encrypt(plain);
			byte[] dec = crypto.decrypt(enc);
			assertArrayEquals(dec, plain);
		}
	}

	@Test
	public void testCBC() throws Exception {

		AesDeterministicCrypto crypto = new AesDeterministicCrypto();
		crypto.setAlgorithm("AES/CBC/PKCS5Padding");
		crypto.setSecretKeyBytes(RandomUtils.nextBytes(16));
		crypto.setInitVectorBytes(RandomUtils.nextBytes(16));

		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] enc = crypto.encrypt(plain);
			byte[] dec = crypto.decrypt(enc);
			assertArrayEquals(dec, plain);
		}
	}

	@Test
	public void testECB() throws Exception {

		AesDeterministicCrypto crypto = new AesDeterministicCrypto();
		crypto.setAlgorithm("AES/ECB/PKCS5Padding");
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
		byte[] iv = RandomUtils.nextBytes(16);

		AesDeterministicCrypto crypto0 = new AesDeterministicCrypto();
		crypto0.setSecretKeyBytes(key);
		crypto0.setInitVectorBytes(iv);

		AesDeterministicCrypto keyCrypto = new AesDeterministicCrypto();
		keyCrypto.setSecretKeyBytes(RandomUtils.nextBytes(16));
		keyCrypto.setInitVectorBytes(RandomUtils.nextBytes(16));

		AesDeterministicCrypto crypto1 = new AesDeterministicCrypto();
		crypto1.setKeyCrypto(keyCrypto);
		crypto1.setSecretKeyBytes(keyCrypto.encrypt(key));
		crypto1.setInitVectorBytes(keyCrypto.encrypt(iv));

		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] enc0 = crypto0.encrypt(plain);
			byte[] enc1 = crypto1.encrypt(plain);
			assertArrayEquals(enc1, enc0);
			byte[] dec0 = crypto0.decrypt(enc0);
			byte[] dec1 = crypto1.decrypt(enc1);
			assertArrayEquals(dec0, plain);
			assertArrayEquals(dec1, plain);
		}
	}

}
