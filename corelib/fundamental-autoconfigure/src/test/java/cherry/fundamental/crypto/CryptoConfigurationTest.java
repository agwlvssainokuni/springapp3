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

package cherry.fundamental.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import cherry.elemental.crypto.Crypto;
import cherry.elemental.crypto.Signature;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CryptoConfigurationTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class CryptoConfigurationTest {

	@Autowired(required = false)
	private Crypto crypto;

	@Autowired(required = false)
	private Signature signature;

	@Autowired(required = false)
	private VersionInfo cryptoVersionInfo;

	@Autowired(required = false)
	private VersionInfo signatureVersionInfo;

	@Test
	public void testCrypto() {

		assertNotNull(crypto);
		assertNotNull(cryptoVersionInfo);
		assertEquals(2, cryptoVersionInfo.getVersion());

		cryptoVersionInfo.setVersion(0);
		byte[] e0 = crypto.encrypt("ABCD".getBytes());
		assertEquals(0, e0[3]);
		cryptoVersionInfo.setVersion(1);
		byte[] e1 = crypto.encrypt("ABCD".getBytes());
		assertEquals(1, e1[3]);
		cryptoVersionInfo.resetVersion();
		byte[] e2 = crypto.encrypt("ABCD".getBytes());
		assertEquals(2, e2[3]);

		assertEquals("ABCD", new String(crypto.decrypt(e0)));
		assertEquals("ABCD", new String(crypto.decrypt(e1)));
		assertEquals("ABCD", new String(crypto.decrypt(e2)));
	}

	@Test
	public void testSignature() {

		assertNotNull(signature);
		assertNotNull(signatureVersionInfo);
		assertEquals(2, signatureVersionInfo.getVersion());

		signatureVersionInfo.setVersion(0);
		byte[] s0 = signature.sign("ABCD".getBytes());
		assertEquals(0, s0[3]);
		signatureVersionInfo.setVersion(1);
		byte[] s1 = signature.sign("ABCD".getBytes());
		assertEquals(1, s1[3]);
		signatureVersionInfo.resetVersion();
		byte[] s2 = signature.sign("ABCD".getBytes());
		assertEquals(2, s2[3]);

		assertTrue(signature.verify("ABCD".getBytes(), s0));
		assertTrue(signature.verify("ABCD".getBytes(), s1));
		assertTrue(signature.verify("ABCD".getBytes(), s2));
	}

}
