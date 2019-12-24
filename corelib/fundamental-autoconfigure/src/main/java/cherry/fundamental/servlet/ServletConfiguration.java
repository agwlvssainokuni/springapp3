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

package cherry.fundamental.servlet;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import cherry.fundamental.download.DownloadOperation;
import cherry.fundamental.download.DownloadTemplate;
import cherry.fundamental.download.Rfc5987Encoder;
import cherry.fundamental.onetimetoken.OneTimeTokenIssuer;
import cherry.fundamental.onetimetoken.OneTimeTokenIssuerImpl;
import cherry.fundamental.onetimetoken.OneTimeTokenValidator;
import cherry.fundamental.onetimetoken.OneTimeTokenValidatorImpl;

@Configuration
@PropertySource({ "classpath:cherry.properties" })
public class ServletConfiguration {

	@ConditionalOnClass({ DownloadOperation.class })
	@ConfigurationProperties(prefix = "cherry.download")
	public static class DownloadCfg {

		private String headerName;
		private String headerValue;
		private String datetimeFormat;
		private Charset filenameCharset;
		private String filenameLanguage;

		@Bean
		public DownloadOperation downloadOperation() {
			return new DownloadTemplate(headerName, headerValue, DateTimeFormatter.ofPattern(datetimeFormat),
					new Rfc5987Encoder(filenameCharset, filenameLanguage));
		}

		public void setHeaderName(String headerName) {
			this.headerName = headerName;
		}

		public void setHeaderValue(String headerValue) {
			this.headerValue = headerValue;
		}

		public void setDatetimeFormat(String datetimeFormat) {
			this.datetimeFormat = datetimeFormat;
		}

		public void setFilenameCharset(Charset filenameCharset) {
			this.filenameCharset = filenameCharset;
		}

		public void setFilenameLanguage(String filenameLanguage) {
			this.filenameLanguage = filenameLanguage;
		}
	}

	@ConditionalOnClass({ OneTimeTokenIssuer.class, OneTimeTokenValidator.class })
	@ConfigurationProperties(prefix = "cherry.onetimetoken")
	public static class OneTimeTokenConfiguration {

		private String name;

		@Bean
		public OneTimeTokenIssuer oneTimeTokenIssuer() {
			return new OneTimeTokenIssuerImpl(name);
		}

		@Bean
		public OneTimeTokenValidator oneTimeTokenValidator() {
			return new OneTimeTokenValidatorImpl(name);
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
