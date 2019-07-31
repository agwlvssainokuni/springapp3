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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DownloadTemplateTest.class)
@SpringBootApplication
@ImportResource(locations = "classpath:spring/appctx-trace.xml")
public class DownloadTemplateTest {

	@Autowired
	private DownloadOperation downloadOperation;

	private DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyyMMddHHmmss")
			.createDateTimeFormatter();

	@Test
	public void testDownload00() throws IOException {
		HttpServletResponse response = createMock();
		LocalDateTime timestamp = LocalDateTime.now();
		String ts = dateTimeFormatter.format(timestamp);

		downloadOperation.download(response, "text/csv", "test_{0}.csv", timestamp, out -> {
			return 123L;
		});
		verify(response).setContentType("text/csv");
		verify(response, never()).setCharacterEncoding("UTF-8");
		verify(response).setHeader("Content-Disposition",
				"attachment; filename=\"test_" + ts + ".csv\"; filename*=UTF-8''test_" + ts + ".csv");
	}

	@Test
	public void testDownload01() throws IOException {
		HttpServletResponse response = createMock();
		LocalDateTime timestamp = LocalDateTime.now();
		String ts = dateTimeFormatter.format(timestamp);

		IOException ioex = new IOException();

		try {
			downloadOperation.download(response, "text/csv", "test_{0}.csv", timestamp, out -> {
				throw new UncheckedIOException(ioex);
			});
			fail("Excption must be thrown");
		} catch (UncheckedIOException ex) {
			assertSame(ex.getCause(), ioex);
		}
		verify(response).setContentType("text/csv");
		verify(response, never()).setCharacterEncoding("UTF-8");
		verify(response).setHeader("Content-Disposition",
				"attachment; filename=\"test_" + ts + ".csv\"; filename*=UTF-8''test_" + ts + ".csv");
	}

	@Test
	public void testDownload02() throws IOException {
		HttpServletResponse response = createMock();
		LocalDateTime timestamp = LocalDateTime.now();
		String ts = dateTimeFormatter.format(timestamp);

		downloadOperation.download(response, "text/csv", "test_{0}.csv", timestamp, out -> {
			return 123L;
		});
		verify(response).setContentType("text/csv");
		verify(response, never()).setCharacterEncoding("UTF-8");
		verify(response).setHeader("Content-Disposition",
				"attachment; filename=\"test_" + ts + ".csv\"; filename*=UTF-8''test_" + ts + ".csv");
	}

	@Test
	public void testDownload10() throws IOException {
		HttpServletResponse response = createMock();
		LocalDateTime timestamp = LocalDateTime.now();
		String ts = dateTimeFormatter.format(timestamp);

		downloadOperation.download(response, "text/csv", StandardCharsets.UTF_8, "test_{0}.csv", timestamp, out -> {
			return 123L;
		});
		verify(response).setContentType("text/csv");
		verify(response).setCharacterEncoding("UTF-8");
		verify(response).setHeader("Content-Disposition",
				"attachment; filename=\"test_" + ts + ".csv\"; filename*=UTF-8''test_" + ts + ".csv");
	}

	@Test
	public void testDownload11() throws IOException {
		HttpServletResponse response = createMock();
		LocalDateTime timestamp = LocalDateTime.now();
		String ts = dateTimeFormatter.format(timestamp);

		IOException ioex = new IOException();

		try {
			downloadOperation.download(response, "text/csv", StandardCharsets.UTF_8, "test_{0}.csv", timestamp,
					out -> {
						throw new UncheckedIOException(ioex);
					});
			fail("Excption must be thrown");
		} catch (UncheckedIOException ex) {
			assertSame(ex.getCause(), ioex);
		}
		verify(response).setContentType("text/csv");
		verify(response).setCharacterEncoding("UTF-8");
		verify(response).setHeader("Content-Disposition",
				"attachment; filename=\"test_" + ts + ".csv\"; filename*=UTF-8''test_" + ts + ".csv");
	}

	@Test
	public void testDownload12() throws IOException {
		HttpServletResponse response = createMock();
		LocalDateTime timestamp = LocalDateTime.now();
		String ts = dateTimeFormatter.format(timestamp);

		downloadOperation.download(response, "text/csv", StandardCharsets.UTF_8, "test_{0}.csv", timestamp, out -> {
			return 123L;
		});
		verify(response).setContentType("text/csv");
		verify(response).setCharacterEncoding("UTF-8");
		verify(response).setHeader("Content-Disposition",
				"attachment; filename=\"test_" + ts + ".csv\"; filename*=UTF-8''test_" + ts + ".csv");
	}

	private HttpServletResponse createMock() throws IOException {
		HttpServletResponse response = mock(HttpServletResponse.class);
		ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		return response;
	}

}
