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

package cherry.fundamental.mail.queue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.Yaml;

import cherry.fundamental.mail.Attachment;

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
	public void save(long messageId, Attachment... attachments) throws IOException {

		if (attachments.length <= 0) {
			return;
		}

		File destdir = getDestdir(messageId);
		destdir.mkdirs();

		List<Item> list = new ArrayList<>(attachments.length);
		for (int i = 0; i < attachments.length; i++) {
			Attachment a = attachments[i];
			String name = Integer.toString(i);
			File destfile = new File(destdir, name);
			try (InputStream in = getInputStream(a); OutputStream out = new FileOutputStream(destfile)) {
				byte[] buff = new byte[1024];
				int len;
				while ((len = in.read(buff)) > 0) {
					out.write(buff, 0, len);
				}
			}
			Item item = new Item();
			item.setFilename(a.getFilename());
			item.setContentType(a.getContentType());
			item.setName(name);
			list.add(item);
		}

		DumperOptions opts = new DumperOptions();
		opts.setDefaultFlowStyle(FlowStyle.BLOCK);
		opts.setLineBreak(LineBreak.WIN);
		Yaml yaml = new Yaml(opts);
		try (OutputStream out = new FileOutputStream(new File(destdir, listname));
				Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
			yaml.dump(list, writer);
		}
	}

	@Override
	public List<AttachedEntry> load(long messageId) throws IOException {

		File destdir = getDestdir(messageId);
		if (!destdir.exists()) {
			return Collections.emptyList();
		}

		Yaml yaml = new Yaml();
		try (InputStream in = new FileInputStream(new File(destdir, listname));
				Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
			@SuppressWarnings("unchecked")
			List<Item> list = (List<Item>) yaml.load(reader);
			return list.stream().map(item -> {
				return new AttachedEntry(item.getFilename(), item.getContentType(), new File(destdir, item.getName()));
			}).collect(Collectors.toList());
		}
	}

	@Override
	public void delete(long messageId) throws IOException {

		File destdir = getDestdir(messageId);
		if (!destdir.exists()) {
			return;
		}

		File[] files = destdir.listFiles();
		if (files != null) {
			for (File f : files) {
				Files.delete(f.toPath());
			}
		}
		Files.delete(destdir.toPath());
	}

	private File getDestdir(long messageId) {
		String destname = MessageFormat.format(pattern, messageId);
		return new File(basedir, destname);
	}

	private InputStream getInputStream(Attachment a) throws FileNotFoundException {
		if (a.getFile() != null) {
			return new FileInputStream(a.getFile());
		} else {
			return a.getStream();
		}
	}

	public static class Item {

		private String filename;

		private String contentType;

		private String name;

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
