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

package cherry.elemental.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * コマンドを実行機能。<br />
 * コマンドを実行し、実行結果 (終了コード、標準出力、標準エラー出力) を取得する。
 */
public class CommandLauncherImpl implements CommandLauncher {

	/** 標準出力、標準エラー出力を収集する際の文字符号化方式。既定値はUTF-8。 */
	private Charset charset = StandardCharsets.UTF_8;

	/** 表樹エラー出力を標準出力にリダイレクトするか。既定値はtrue (リダイレクトする)。 */
	private boolean redirectErrorStream = true;

	/** 標準出力、標準エラー出力をリダイレクトする先の一時ファイルを配置するディレクトリ。 */
	private File redirectTempdir = null;

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public void setRedirectErrorStream(boolean redirectErrorStream) {
		this.redirectErrorStream = redirectErrorStream;
	}

	public void setRedirectTempdir(File redirectTempdir) {
		this.redirectTempdir = redirectTempdir;
	}

	/**
	 * コマンドを実行し、実行結果 (終了コード、標準出力、標準エラー出力) を取得する。
	 * 
	 * @param command 実行するコマンド (コマンドライン)。
	 * @return　実行結果 (終了コード、標準出力、標準エラー出力)。
	 * @throws IOException コマンド不正 (存在しない)、または、標準出力、標準エラー出力の読取り不正。
	 * @throws InterruptedException コマンドの終了待機時に割込みが発生したことを表す。
	 */
	@Override
	public CommandResult launch(String... command) throws IOException, InterruptedException {
		final File stdoutFile;
		final File stderrFile;
		ProcessBuilder builder = new ProcessBuilder(command);
		if (redirectErrorStream) {
			stdoutFile = File.createTempFile("CommandLauncher", ".stdout", redirectTempdir);
			stdoutFile.deleteOnExit();
			stderrFile = null;
			builder.redirectOutput(stdoutFile).redirectErrorStream(redirectErrorStream);
		} else {
			stdoutFile = File.createTempFile("CommandLauncher", ".stdout", redirectTempdir);
			stdoutFile.deleteOnExit();
			stderrFile = File.createTempFile("CommandLauncher", ".stderr", redirectTempdir);
			stderrFile.deleteOnExit();
			builder.redirectOutput(stdoutFile).redirectError(stderrFile);
		}
		Process proc = builder.start();
		CommandResult result = new CommandResult();
		int exitValue = proc.waitFor();
		result.setExitValue(exitValue);
		try (InputStream in = new FileInputStream(stdoutFile); Reader r = new InputStreamReader(in, charset)) {
			result.setStdout(readerToString(r));
		} finally {
			stdoutFile.delete();
		}
		if (stderrFile != null) {
			try (InputStream in = new FileInputStream(stderrFile); Reader r = new InputStreamReader(in, charset)) {
				result.setStderr(readerToString(r));
			} finally {
				stderrFile.delete();
			}
		}
		return result;
	}

	private String readerToString(Reader r) throws IOException {
		StringBuilder builder = new StringBuilder();
		char[] buf = new char[2048];
		int len;
		while ((len = r.read(buf, 0, buf.length)) >= 0) {
			builder.append(buf, 0, len);
		}
		return builder.toString();
	}
}
