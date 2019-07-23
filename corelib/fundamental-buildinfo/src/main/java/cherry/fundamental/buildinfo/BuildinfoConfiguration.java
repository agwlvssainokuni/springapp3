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

package cherry.fundamental.buildinfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

@Configuration
@PropertySource({ "classpath:cherry/fundamental/buildinfo/Buildinfo.properties" })
@ConfigurationProperties(prefix = "fundamental.buildinfo")
public class BuildinfoConfiguration {

	private Resource resource;

	private Charset charset;

	@Bean
	public Buildinfo buildinfo() {
		if (!resource.exists()) {
			return new BuildinfoImpl("");
		}
		try (InputStream in = resource.getInputStream(); InputStreamReader r = new InputStreamReader(in, charset)) {
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			int len;
			while ((len = r.read(buf)) > 0) {
				sb.append(buf, 0, len);
			}
			return new BuildinfoImpl(sb.toString());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

}
