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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

public class AttachmentStoreImpl implements AttachmentStore {

	private final File basedir;

	private final String pattern;

	private final String listname;

	public AttachmentStoreImpl(File basedir, String pattern, String listname) {
		this.basedir = basedir;
		this.pattern = pattern;
		this.listname = listname;
	}

	@Override
	public boolean save(long messageId, Attachment... attachments) throws UncheckedIOException {

		if (attachments.length <= 0) {
			return false;
		}

		File destdir = getDestdir(messageId);
		destdir.mkdirs();

		List<Item> list = new ArrayList<>(attachments.length);
		for (Attachment a : attachments) {
			if (a.isStream()) {
				String name = UUID.randomUUID().toString();
				File destfile = new File(destdir, name);
				try (InputStream in = a.getStream(); OutputStream out = new FileOutputStream(destfile)) {
					byte[] buff = new byte[1024];
					int len;
					while ((len = in.read(buff)) > 0) {
						out.write(buff, 0, len);
					}
				} catch (IOException ex) {
					throw new UncheckedIOException(ex);
				}
				Item item = new Item();
				item.setFilename(a.getFilename());
				item.setFile(name);
				item.setContentType(a.getContentType());
				item.setStream(true);
				list.add(item);
			}
			if (a.isFile()) {
				Item item = new Item();
				item.setFilename(a.getFilename());
				item.setFile(a.getFile().getAbsolutePath());
				item.setContentType(a.getContentType());
				item.setStream(false);
				list.add(item);
			}
		}

		Yaml yaml = new Yaml();
		try (OutputStream out = new FileOutputStream(new File(destdir, listname));
				Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
			yaml.dump(list, writer);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}

		return true;
	}

	@Override
	public Optional<List<AttachedEntry>> load(long messageId) throws UncheckedIOException {

		File destdir = getDestdir(messageId);
		if (!destdir.exists()) {
			return Optional.empty();
		}

		Yaml yaml = new Yaml();
		try (InputStream in = new FileInputStream(new File(destdir, listname));
				Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
			@SuppressWarnings("unchecked")
			List<Item> list = (List<Item>) yaml.load(reader);
			return Optional.of(list.stream().map(item -> {
				if (item.isStream()) {
					return new AttachedEntry(item.getFilename(), new File(destdir, item.getFile()),
							item.getContentType(), item.isStream());
				} else {
					return new AttachedEntry(item.getFilename(), new File(item.getFile()), item.getContentType(),
							item.isStream());
				}
			}).collect(Collectors.toList()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	@Override
	public void delete(long messageId) throws UncheckedIOException {

		File destdir = getDestdir(messageId);
		if (!destdir.exists()) {
			return;
		}

		File[] files = destdir.listFiles();
		try {
			if (files != null) {
				for (File f : files) {
					Files.delete(f.toPath());
				}
			}
			Files.delete(destdir.toPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private File getDestdir(long messageId) {
		String destname = MessageFormat.format(pattern, messageId);
		return new File(basedir, destname);
	}

	public static class Item {

		private String filename;

		private String file;

		private String contentType;

		private boolean stream;

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public boolean isStream() {
			return stream;
		}

		public void setStream(boolean stream) {
			this.stream = stream;
		}
	}

}
