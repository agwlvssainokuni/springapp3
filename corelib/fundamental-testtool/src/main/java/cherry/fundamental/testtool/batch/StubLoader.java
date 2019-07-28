/*
 * Copyright 2015,2019 agwlvssainokuni
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

package cherry.fundamental.testtool.batch;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.FileSystemResource;

import cherry.fundamental.testtool.stub.StubConfigLoader;

public class StubLoader {

	private final StubConfigLoader jsonLoader;

	private final StubConfigLoader yamlLoader;

	private final File definitionDirectory;

	public StubLoader(StubConfigLoader jsonLoader, StubConfigLoader yamlLoader, File definitionDirectory) {
		this.jsonLoader = jsonLoader;
		this.yamlLoader = yamlLoader;
		this.definitionDirectory = definitionDirectory;
	}

	public void load() throws IOException {
		doLoad(jsonLoader, ".json");
		doLoad(yamlLoader, ".yaml");
	}

	private void doLoad(StubConfigLoader loader, String ext) throws IOException {
		File[] files = definitionDirectory.listFiles((d, n) -> n.endsWith(ext));
		if (files != null) {
			loader.load(Stream.of(files).map(FileSystemResource::new).collect(Collectors.toList()));
		}
	}

}
