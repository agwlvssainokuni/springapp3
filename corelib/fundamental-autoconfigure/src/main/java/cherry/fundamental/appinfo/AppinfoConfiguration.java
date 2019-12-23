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

package cherry.fundamental.appinfo;

import java.io.File;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:cherry.properties" })
public class AppinfoConfiguration {

	@ConditionalOnClass({ InstanceInformation.class })
	@ConfigurationProperties(prefix = "cherry.appinfo")
	public static class InstanceInformationCfg {

		private String id;
		private String environmentName;
		private File baseDirectory;

		@Bean
		@ConditionalOnMissingBean
		public InstanceInformation instanceInformation() {
			return new InstanceInformationImpl(id, environmentName, baseDirectory);
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setEnvironmentName(String environmentName) {
			this.environmentName = environmentName;
		}

		public void setBaseDirectory(File baseDirectory) {
			this.baseDirectory = baseDirectory;
		}
	}

}
