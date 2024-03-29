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
import static org.junit.jupiter.api.Assertions.fail;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class VersionedSignatureTest {

	@Test
	public void simpleSignAndVerify() throws Exception {

		Map<Integer, Signature> map = createSignatureMap(1);

		VersionedSignature impl = new VersionedSignature();
		impl.setSignVersion(0);
		impl.setSignatureMap(map);
		impl.setVersionStrategy(new DefaultVersionStrategy());

		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] signed = impl.sign(plain);
			assertFalse(Arrays.equals(plain, signed));
			assertTrue(impl.verify(plain, signed));
		}
	}

	@Test
	public void versionedSignAndVerify() throws Exception {

		Map<Integer, Signature> map = createSignatureMap(2);

		VersionedSignature impl0 = new VersionedSignature();
		impl0.setSignVersion(0);
		impl0.setSignatureMap(map);

		VersionedSignature impl1 = new VersionedSignature();
		impl1.setSignVersion(1);
		impl1.setSignatureMap(map);

		for (int i = 0; i < 100; i++) {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] signed = impl0.sign(plain);
			assertFalse(Arrays.equals(plain, signed));
			assertFalse(Arrays.equals(impl1.sign(plain), signed));
			assertTrue(impl0.verify(plain, signed));
			assertTrue(impl1.verify(plain, signed));
		}
	}

	@Test
	public void errorNoMatchingSignatureToSign() throws Exception {
		Map<Integer, Signature> map = createSignatureMap(1);
		VersionedSignature impl0 = new VersionedSignature();
		impl0.setSignVersion(1);
		impl0.setSignatureMap(map);
		try {
			byte[] plain = RandomUtils.nextBytes(1024);
			impl0.sign(plain);
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			// OK
		}
	}

	@Test
	public void errorNoMatchingSignatureToVerify() throws Exception {

		Map<Integer, Signature> map1 = createSignatureMap(2);
		Map<Integer, Signature> map0 = new HashMap<>();
		map0.put(0, map1.get(0));

		VersionedSignature impl0 = new VersionedSignature();
		impl0.setSignVersion(0);
		impl0.setSignatureMap(map0);

		VersionedSignature impl1 = new VersionedSignature();
		impl1.setSignVersion(1);
		impl1.setSignatureMap(map1);

		try {
			byte[] plain = RandomUtils.nextBytes(1024);
			byte[] signed = impl1.sign(plain);
			impl0.verify(plain, signed);
			fail("Exception must be thrown");
		} catch (IllegalStateException ex) {
			// OK
		}
	}

	private Map<Integer, Signature> createSignatureMap(int num) throws Exception {
		Map<Integer, Signature> map = new HashMap<>();
		for (int i = 0; i < num; i++) {
			map.put(i, createRSASignature());
		}
		return map;
	}

	private RsaSignature createRSASignature() throws Exception {
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(2048);
		KeyPair key = keygen.generateKeyPair();
		RsaSignature impl = new RsaSignature();
		impl.setAlgorithm("SHA256withRSA");
		impl.setPublicKeyBytes(key.getPublic().getEncoded());
		impl.setPrivateKeyBytes(key.getPrivate().getEncoded());
		return impl;
	}

}
