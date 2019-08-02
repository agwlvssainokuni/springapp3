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

package cherry.fundamental.mail;

import java.util.Map;

public class SimpleTemplateStore implements TemplateStore {

	private Map<String, Template> templateMap;

	public SimpleTemplateStore(Map<String, Template> templateMap) {
		this.templateMap = templateMap;
	}

	@Override
	public Template get(String templateName) {
		return templateMap.get(templateName);
	}

	@Override
	public void put(String templateName, Template template) {
		templateMap.put(templateName, template);
	}

}
