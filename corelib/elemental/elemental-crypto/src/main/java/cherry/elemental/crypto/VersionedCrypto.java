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

import java.util.Map;
import java.util.function.Supplier;

import cherry.elemental.crypto.VersionStrategy.VersionedData;

public class VersionedCrypto implements Crypto {

	private Supplier<Integer> encryptVersion;

	private Map<Integer, Crypto> cryptoMap;

	private VersionStrategy<byte[], Integer> versionStrategy = new DefaultVersionStrategy();

	public void setEncryptVersion(Integer encryptVersion) {
		setEncryptVersion(() -> encryptVersion);
	}

	public void setEncryptVersion(Supplier<Integer> encryptVersion) {
		this.encryptVersion = encryptVersion;
	}

	public void setCryptoMap(Map<Integer, Crypto> cryptoMap) {
		this.cryptoMap = cryptoMap;
	}

	public void setVersionStrategy(VersionStrategy<byte[], Integer> versionStrategy) {
		this.versionStrategy = versionStrategy;
	}

	@Override
	public byte[] encrypt(byte[] in) {
		Integer v = encryptVersion.get();
		Crypto crypto = cryptoMap.get(v);
		if (crypto == null) {
			throw new IllegalStateException("No matching Crypto for version " + v);
		}
		byte[] b = crypto.encrypt(in);
		return versionStrategy.encode(b, v);
	}

	@Override
	public byte[] decrypt(byte[] in) {
		VersionedData<byte[], Integer> vd = versionStrategy.decode(in);
		Crypto crypto = cryptoMap.get(vd.getVersion());
		if (crypto == null) {
			throw new IllegalStateException("No matching Crypto for version " + vd.getVersion());
		}
		return crypto.decrypt(vd.getData());
	}

}
