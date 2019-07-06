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

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Encdec implements Encdec<byte[], String> {

	private boolean base64url = false;

	public void setBase64url(boolean base64url) {
		this.base64url = base64url;
	}

	@Override
	public String encode(byte[] raw) {
		Encoder encoder;
		if (base64url) {
			encoder = Base64.getUrlEncoder().withoutPadding();
		} else {
			encoder = Base64.getEncoder();
		}
		return encoder.encodeToString(raw);
	}

	@Override
	public byte[] decode(String encoded) {
		Decoder decoder;
		if (base64url) {
			decoder = Base64.getUrlDecoder();
		} else {
			decoder = Base64.getDecoder();
		}
		return decoder.decode(encoded);
	}

}
