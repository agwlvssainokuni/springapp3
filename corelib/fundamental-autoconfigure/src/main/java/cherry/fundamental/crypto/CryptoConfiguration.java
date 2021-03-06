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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.crypto.NoSuchPaddingException;

import org.apache.commons.collections4.map.LazyMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import cherry.elemental.crypto.AesCrypto;
import cherry.elemental.crypto.Crypto;
import cherry.elemental.crypto.DefaultVersionStrategy;
import cherry.elemental.crypto.HmacSignature;
import cherry.elemental.crypto.NullCrypto;
import cherry.elemental.crypto.RsaCrypto;
import cherry.elemental.crypto.Signature;
import cherry.elemental.crypto.VersionedCrypto;
import cherry.elemental.crypto.VersionedSignature;

@Configuration
@ConditionalOnClass({ Crypto.class })
@PropertySource({ "classpath:cherry.properties" })
public class CryptoConfiguration {

	@ConditionalOnClass({ Crypto.class, VersionInfo.class })
	@ConfigurationProperties(prefix = "cherry.crypto")
	public static class CryptoCfg implements ApplicationContextAware {

		private ApplicationContext appctx;

		private String aeskeytop;

		private String pubkey;

		private String prvkey;

		private String aeskey;

		private String passwd;

		private String current;

		private String rsaAlgorithm;

		private String aesAlgorithm;

		private int encryptVersion;

		@Bean
		public Crypto crypto() {
			Map<Integer, Crypto> map = LazyMap.lazyMap(new LinkedHashMap<>(), v -> {
				AesCrypto impl = new AesCrypto();
				impl.setAlgorithm(aesAlgorithm);
				impl.setKeyCrypto(getKeyCrypto(appctx, aeskeytop, v.intValue(), prvkey, pubkey, passwd, rsaAlgorithm));
				impl.setSecretKeyBytes(getBytes(appctx, aeskeytop, v.intValue(), aeskey).get());
				return impl;
			});
			encryptVersion = getDefaultVersion(appctx, aeskeytop, aeskey, current).get().intValue();
			VersionedCrypto impl = new VersionedCrypto();
			impl.setCryptoMap(map);
			impl.setEncryptVersion(() -> encryptVersion);
			impl.setVersionStrategy(new DefaultVersionStrategy());
			return impl;
		}

		@Bean
		public VersionInfo cryptoVersionInfo() {
			return new VersionInfo() {

				@Override
				public int getVersion() {
					return encryptVersion;
				}

				@Override
				public void setVersion(int version) {
					encryptVersion = version;
				}

				@Override
				public void resetVersion() {
					encryptVersion = getDefaultVersion(appctx, aeskeytop, aeskey, current).get().intValue();
				}
			};
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) {
			this.appctx = applicationContext;
		}

		public void setAeskeytop(String aeskeytop) {
			this.aeskeytop = aeskeytop;
		}

		public void setPubkey(String pubkey) {
			this.pubkey = pubkey;
		}

		public void setPrvkey(String prvkey) {
			this.prvkey = prvkey;
		}

		public void setAeskey(String aeskey) {
			this.aeskey = aeskey;
		}

		public void setPasswd(String passwd) {
			this.passwd = passwd;
		}

		public void setCurrent(String current) {
			this.current = current;
		}

		public void setRsaAlgorithm(String rsaAlgorithm) {
			this.rsaAlgorithm = rsaAlgorithm;
		}

		public void setAesAlgorithm(String aesAlgorithm) {
			this.aesAlgorithm = aesAlgorithm;
		}
	}

	@ConditionalOnClass({ Signature.class, VersionInfo.class })
	@ConfigurationProperties(prefix = "cherry.crypto")
	public static class SignatureCfg implements ApplicationContextAware {

		private ApplicationContext appctx;

		private String hmackeytop;

		private String pubkey;

		private String prvkey;

		private String hmackey;

		private String passwd;

		private String current;

		private String rsaAlgorithm;

		private String hmacAlgorithm;

		private int signVersion;

		@Bean
		public Signature signature() {
			Map<Integer, Signature> map = LazyMap.lazyMap(new LinkedHashMap<>(), v -> {
				HmacSignature impl = new HmacSignature();
				impl.setAlgorithm(hmacAlgorithm);
				impl.setKeyCrypto(getKeyCrypto(appctx, hmackeytop, v.intValue(), prvkey, pubkey, passwd, rsaAlgorithm));
				impl.setSecretKeyBytes(getBytes(appctx, hmackeytop, v.intValue(), hmackey).get(), hmacAlgorithm);
				return impl;
			});
			signVersion = getDefaultVersion(appctx, hmackeytop, hmackey, current).get().intValue();
			VersionedSignature impl = new VersionedSignature();
			impl.setSignatureMap(map);
			impl.setSignVersion(() -> signVersion);
			impl.setVersionStrategy(new DefaultVersionStrategy());
			return impl;
		}

		@Bean
		public VersionInfo signatureVersionInfo() {
			return new VersionInfo() {

				@Override
				public int getVersion() {
					return signVersion;
				}

				@Override
				public void setVersion(int version) {
					signVersion = version;
				}

				@Override
				public void resetVersion() {
					signVersion = getDefaultVersion(appctx, hmackeytop, hmackey, current).get().intValue();
				}
			};
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) {
			this.appctx = applicationContext;
		}

		public void setHmackeytop(String hmackeytop) {
			this.hmackeytop = hmackeytop;
		}

		public void setPubkey(String pubkey) {
			this.pubkey = pubkey;
		}

		public void setPrvkey(String prvkey) {
			this.prvkey = prvkey;
		}

		public void setHmackey(String hmackey) {
			this.hmackey = hmackey;
		}

		public void setPasswd(String passwd) {
			this.passwd = passwd;
		}

		public void setCurrent(String current) {
			this.current = current;
		}

		public void setRsaAlgorithm(String rsaAlgorithm) {
			this.rsaAlgorithm = rsaAlgorithm;
		}

		public void setHmacAlgorithm(String hmacAlgorithm) {
			this.hmacAlgorithm = hmacAlgorithm;
		}
	}

	private static Optional<Integer> getDefaultVersion(ApplicationContext appctx, String topdir, String keyfile,
			String curfile) {

		List<Integer> vlist = new ArrayList<>();
		try {
			for (Resource r : appctx.getResources(topdir + "/*/" + keyfile)) {
				String s = r.getURI().toString();
				int end = s.length() - (keyfile.length() + 1);
				int bgn = s.lastIndexOf("/", end - 1);
				String v = s.substring(bgn + 1, end);
				vlist.add(Integer.valueOf(v));
			}
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
		vlist.sort((a, b) -> b.compareTo(a));

		Optional<Integer> vopt = vlist.stream()
				.filter(v -> appctx.getResource(topdir + "/" + v + "/" + curfile).exists()).findFirst();
		if (vopt.isPresent()) {
			return vopt;
		} else {
			return vlist.stream().findFirst();
		}
	}

	private static Crypto getKeyCrypto(ApplicationContext appctx, String topdir, int version, String prvfile,
			String pubfile, String pwdfile, String algorithm) {

		Optional<byte[]> prvOpt = getBytes(appctx, topdir, version, prvfile);
		if (!prvOpt.isPresent()) {
			return new NullCrypto();
		}

		RsaCrypto crypto = new RsaCrypto();
		crypto.setAlgorithm(algorithm);

		try {

			Optional<byte[]> pubOpt = getBytes(appctx, topdir, version, pubfile);
			if (pubOpt.isPresent()) {
				crypto.setPublicKeyBytes(pubOpt.get());
			}

			Optional<char[]> pwdOpt = getPasswd(appctx, topdir, version, pwdfile);
			if (pwdOpt.isPresent()) {
				crypto.setPrivateKeyBytes(prvOpt.get(), pwdOpt.get());
			} else {
				crypto.setPrivateKeyBytes(prvOpt.get());
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | InvalidKeySpecException ex) {
			throw new IllegalStateException(ex);
		}

		return crypto;
	}

	private static Optional<byte[]> getBytes(ApplicationContext appctx, String topdir, int version, String filename) {

		Resource res = appctx.getResource(topdir + "/" + version + "/" + filename);
		if (!res.exists()) {
			return Optional.empty();
		}

		try (InputStream in = res.getInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.flush();
			return Optional.of(out.toByteArray());
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	private static Optional<char[]> getPasswd(ApplicationContext appctx, String topdir, int version, String filename) {

		Resource res = appctx.getResource(topdir + "/" + filename);
		if (!res.exists()) {
			return Optional.empty();
		}

		try (InputStream in = res.getInputStream()) {
			Properties props = new Properties();
			props.load(in);
			return Optional.ofNullable(props.getProperty(String.valueOf(version))).map(String::toCharArray);
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
