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

package cherry.fundamental.mail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * メール送信機能。<br />
 * 添付ファイルを表す。
 */
public class Attachment {

	private final String filename;

	private final File file;

	private final InputStream stream;

	private final String contentType;

	public Attachment(String filename, File file, String contentType) {
		this(filename, file, null, contentType);
	}

	public Attachment(String filename, byte[] array, String contentType) {
		this(filename, null, new ByteArrayInputStream(array), contentType);
	}

	public Attachment(String filename, InputStream stream, String contentType) {
		this(filename, null, stream, contentType);
	}

	private Attachment(String filename, File file, InputStream stream, String contentType) {
		this.filename = filename;
		this.file = file;
		this.stream = stream;
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getFilename() {
		return filename;
	}

	public File getFile() {
		return file;
	}

	public InputStream getStream() {
		return stream;
	}

	public String getContentType() {
		return contentType;
	}

}
