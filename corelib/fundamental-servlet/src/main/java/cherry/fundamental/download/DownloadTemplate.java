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

package cherry.fundamental.download;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadTemplate implements DownloadOperation {

	public static final String OPER_DOWNLOAD = "operation.DOWNLOAD";

	private final Logger loggerOper = LoggerFactory.getLogger(OPER_DOWNLOAD);

	private final String headerName;

	private final String headerValue;

	private final DateTimeFormatter formatter;

	private final Function<String, String> filenameEncoder;

	public DownloadTemplate(String headerName, String headerValue, DateTimeFormatter formatter,
			Function<String, String> filenameEncoder) {
		this.headerName = headerName;
		this.headerValue = headerValue;
		this.formatter = formatter;
		this.filenameEncoder = filenameEncoder;
	}

	@Override
	public void download(HttpServletResponse response, String contentType, String filename, LocalDateTime timestamp,
			Function<OutputStream, Long> action) {

		String fname = MessageFormat.format(filename, formatter.format(timestamp));

		response.setContentType(contentType);
		String value = MessageFormat.format(headerValue, fname, filenameEncoder.apply(fname));
		response.setHeader(headerName, value);

		loggerOper.info("STARTING: Content-Type={}, {}={}", contentType, headerName, value);

		try (OutputStream out = response.getOutputStream()) {
			Long count = action.apply(out);
			loggerOper.info("COMPLETED: {} items", count);
		} catch (UncheckedIOException ex) {
			loggerOper.warn("FAILED WITH I/O EXCEPTION", ex.getCause());
			throw ex;
		} catch (IOException ex) {
			loggerOper.warn("FAILED WITH I/O EXCEPTION", ex);
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public void download(HttpServletResponse response, String contentType, Charset charset, String filename,
			LocalDateTime timestamp, Function<Writer, Long> action) {

		String fname = MessageFormat.format(filename, formatter.format(timestamp));

		response.setContentType(contentType);
		response.setCharacterEncoding(charset.name());
		String value = MessageFormat.format(headerValue, fname, filenameEncoder.apply(fname));
		response.setHeader(headerName, value);

		loggerOper.info("STARTING: Content-Type={}, charset={}, {}={}", contentType, charset.name(), headerName, value);

		try (OutputStream out = response.getOutputStream(); Writer writer = new OutputStreamWriter(out, charset)) {
			Long count = action.apply(writer);
			loggerOper.info("COMPLETED: {} items", count);
		} catch (UncheckedIOException ex) {
			loggerOper.warn("FAILED WITH I/O EXCEPTION", ex.getCause());
			throw ex;
		} catch (IOException ex) {
			loggerOper.warn("FAILED WITH I/O EXCEPTION", ex);
			throw new IllegalStateException(ex);
		}
	}

}
