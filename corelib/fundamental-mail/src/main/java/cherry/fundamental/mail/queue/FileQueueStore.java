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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.Yaml;

public class FileQueueStore implements QueueStore {

	private final File basedir;

	private final String counterFile;

	private final String counterLock;

	private final String pattern;

	private final String messageFile;

	private final String scheduleFile;

	private final String sentFile;

	public FileQueueStore(File basedir, String counterFile, String counterLock, String pattern, String messageFile,
			String scheduleFile, String sentFile) {
		this.basedir = basedir;
		this.counterFile = counterFile;
		this.counterLock = counterLock;
		this.pattern = pattern;
		this.messageFile = messageFile;
		this.scheduleFile = scheduleFile;
		this.sentFile = sentFile;
	}

	@Override
	public long save(String loginId, String messageName, LocalDateTime scheduledAt, String from, List<String> to,
			List<String> cc, List<String> bcc, String replyTo, String subject, String text, String html) {
		try {

			long messageId = getNextId();
			File destdir = getDestdir(messageId);
			destdir.mkdirs();

			DumperOptions opts = new DumperOptions();
			opts.setDefaultFlowStyle(FlowStyle.BLOCK);
			opts.setLineBreak(LineBreak.WIN);
			Yaml yaml = new Yaml(opts);

			try (OutputStream out = new FileOutputStream(new File(destdir, messageFile));
					Writer w = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
				Item item = new Item();
				item.setLoginId(loginId);
				item.setMessageName(messageName);
				item.setFrom(from);
				item.setTo(to);
				item.setCc(cc);
				item.setBcc(bcc);
				item.setReplyTo(replyTo);
				item.setSubject(subject);
				item.setText(text);
				item.setHtml(html);
				yaml.dump(item, w);
			}

			try (OutputStream out = new FileOutputStream(new File(destdir, scheduleFile));
					Writer w = new OutputStreamWriter(out, StandardCharsets.UTF_8);
					PrintWriter pw = new PrintWriter(w)) {
				pw.println(scheduledAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			}

			return messageId;
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	@Override
	public List<Long> list(LocalDateTime dtm) {
		return doList(dtm, scheduleFile, true);
	}

	@Override
	public QueuedEntry get(long messageId) {

		File file = new File(getDestdir(messageId), messageFile);
		if (!file.isFile()) {
			return null;
		}

		Item item;
		try (InputStream in = new FileInputStream(file); Reader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
			Yaml yaml = new Yaml();
			item = yaml.load(r);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}

		return new QueuedEntry(item.getFrom(), item.getTo(), item.getCc(), item.getBcc(), item.getReplyTo(),
				item.getSubject(), item.getText(), item.getHtml());
	}

	@Override
	public void finish(long messageId, LocalDateTime sentAt) {

		File destdir = getDestdir(messageId);
		if (!destdir.isDirectory()) {
			return;
		}

		try (OutputStream out = new FileOutputStream(new File(destdir, sentFile));
				Writer w = new OutputStreamWriter(out, StandardCharsets.UTF_8);
				PrintWriter pw = new PrintWriter(w)) {
			pw.println(sentAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	@Override
	public List<Long> listSent(LocalDateTime dtm) {
		return doList(dtm, sentFile, false);
	}

	@Override
	public boolean delete(long messageId) {
		try {

			File destdir = getDestdir(messageId);
			if (!destdir.isDirectory()) {
				return false;
			}

			File msg = new File(destdir, messageFile);
			if (msg.isFile()) {
				Files.delete(msg.toPath());
			}

			File sched = new File(destdir, scheduleFile);
			if (sched.isFile()) {
				Files.delete(sched.toPath());
			}

			File sent = new File(destdir, sentFile);
			if (sent.isFile()) {
				Files.delete(sent.toPath());
			}

			String[] l = destdir.list();
			if (l == null || l.length == 0) {
				Files.delete(destdir.toPath());
			}

			return true;
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private synchronized long getNextId() throws IOException {
		try (FileOutputStream fout = new FileOutputStream(new File(basedir, counterLock))) {
			FileChannel fc = fout.getChannel();
			FileLock lock = fc.lock();
			try {

				File counter = new File(basedir, counterFile);
				long id;
				if (!counter.exists()) {
					id = -1L;
				} else {
					try (InputStream in = new FileInputStream(counter);
							Reader r = new InputStreamReader(in, StandardCharsets.UTF_8);
							BufferedReader br = new BufferedReader(r)) {
						String line = br.readLine();
						if (line == null) {
							id = -1L;
						} else {
							id = Long.parseLong(line);
						}
					}
				}

				try (OutputStream out = new FileOutputStream(counter);
						Writer w = new OutputStreamWriter(out, StandardCharsets.UTF_8);
						PrintWriter pw = new PrintWriter(w)) {
					pw.println(id + 1L);
				}

				return id + 1L;
			} finally {
				lock.release();
			}
		}
	}

	private NumberFormat getFormatter() {
		return new DecimalFormat(pattern);
	}

	private File getDestdir(long messageId) {
		String destname = getFormatter().format(messageId);
		return new File(basedir, destname);
	}

	private List<Long> doList(LocalDateTime dtm, String filename, boolean toSend) {

		if (!basedir.isDirectory()) {
			return Collections.emptyList();
		}

		String[] nms = basedir.list();
		if (nms == null) {
			return Collections.emptyList();
		}

		NumberFormat fmt = getFormatter();
		List<Long> idlist = new ArrayList<>(nms.length);
		for (String nm : nms) {
			File destdir = new File(basedir, nm);
			if (!destdir.isDirectory()) {
				continue;
			}
			File targetfile = new File(destdir, filename);
			if (!targetfile.isFile()) {
				continue;
			}
			if (toSend && new File(destdir, sentFile).isFile()) {
				continue;
			}
			long messageId;
			try {
				messageId = fmt.parse(nm).longValue();
			} catch (ParseException ex) {
				continue;
			}
			try (InputStream in = new FileInputStream(targetfile);
					Reader r = new InputStreamReader(in, StandardCharsets.UTF_8);
					BufferedReader br = new BufferedReader(r)) {
				String line = br.readLine();
				if (line == null) {
					idlist.add(messageId);
				}
				LocalDateTime at = LocalDateTime.parse(line, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				if (at.isBefore(dtm) || at.isEqual(dtm)) {
					idlist.add(messageId);
				}
			} catch (IOException ex) {
				continue;
			}
		}
		idlist.sort(null);
		return idlist;
	}

	public static class Item {

		private String loginId;
		private String messageName;
		private String from;
		private List<String> to;
		private List<String> cc;
		private List<String> bcc;
		private String replyTo;
		private String subject;
		private String text;
		private String html;

		public String getLoginId() {
			return loginId;
		}

		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}

		public String getMessageName() {
			return messageName;
		}

		public void setMessageName(String messageName) {
			this.messageName = messageName;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public List<String> getTo() {
			return to;
		}

		public void setTo(List<String> to) {
			this.to = to;
		}

		public List<String> getCc() {
			return cc;
		}

		public void setCc(List<String> cc) {
			this.cc = cc;
		}

		public List<String> getBcc() {
			return bcc;
		}

		public void setBcc(List<String> bcc) {
			this.bcc = bcc;
		}

		public String getReplyTo() {
			return replyTo;
		}

		public void setReplyTo(String replyTo) {
			this.replyTo = replyTo;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}
	}

}
