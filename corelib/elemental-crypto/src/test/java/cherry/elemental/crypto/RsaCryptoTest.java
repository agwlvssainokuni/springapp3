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
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.security.AlgorithmParameters;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class RsaCryptoTest {

	@Test
	public void testEncDec() throws Exception {
		RsaCrypto impl = create1();
		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(245);
			byte[] crypto = impl.encrypt(plain);
			assertFalse(Arrays.equals(crypto, plain));
			assertArrayEquals(impl.decrypt(crypto), plain);
		}
	}

	@Test
	public void testEncDecWithPbeKey() throws Exception {
		RsaCrypto impl = create2("password".toCharArray());
		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(245);
			byte[] crypto = impl.encrypt(plain);
			assertFalse(Arrays.equals(crypto, plain));
			assertArrayEquals(impl.decrypt(crypto), plain);
		}
	}

	private RsaCrypto create1() throws Exception {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(2048);
		KeyPair key = keygen.generateKeyPair();
		RsaCrypto impl = new RsaCrypto();
		impl.setAlgorithm("RSA/ECB/PKCS1Padding");
		impl.setPublicKeyBytes(key.getPublic().getEncoded());
		impl.setPrivateKeyBytes(key.getPrivate().getEncoded());
		return impl;
	}

	private RsaCrypto create2(char[] password) throws Exception {

		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(2048);
		KeyPair key = keygen.generateKeyPair();

		String pbeAlgName = "PBEWithMD5AndDES";
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(RandomUtils.nextBytes(8), 20);
		SecretKey pbeKey = SecretKeyFactory.getInstance(pbeAlgName).generateSecret(pbeKeySpec);
		AlgorithmParameters pbeParam = AlgorithmParameters.getInstance(pbeAlgName);
		pbeParam.init(pbeParamSpec);
		Cipher cipher = Cipher.getInstance(pbeAlgName);
		cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParam);
		EncryptedPrivateKeyInfo encryptedKeyInfo = new EncryptedPrivateKeyInfo(pbeParam,
				cipher.doFinal(key.getPrivate().getEncoded()));

		RsaCrypto impl = new RsaCrypto();
		impl.setAlgorithm("RSA/ECB/PKCS1Padding");
		impl.setPublicKeyBytes(key.getPublic().getEncoded());
		impl.setPrivateKeyBytes(encryptedKeyInfo.getEncoded(), password);
		return impl;
	}

}
