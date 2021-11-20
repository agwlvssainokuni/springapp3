/*
 * Copyright 2015,2021 agwlvssainokuni
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import cherry.fundamental.testtool.stub.StubConfigLoader;

public class StubLoader {

	private final StubConfigLoader stubConfigLoader;

	public StubLoader(StubConfigLoader stubConfigLoader) {
		this.stubConfigLoader = stubConfigLoader;
	}

	public void load(File definitionDirectory) throws IOException {
		doLoad(definitionDirectory, ".yaml");
		doLoadScript(definitionDirectory, ".js");
	}

	private void doLoad(File definitionDirectory, String ext) throws IOException {
		List<Resource> resources = findFiles(definitionDirectory.toPath(), ext).map(FileSystemResource::new)
				.collect(Collectors.toList());
		stubConfigLoader.load(resources);
	}

	private void doLoadScript(File definitionDirectory, String ext) throws IOException {
		List<File> files = findFiles(definitionDirectory.toPath(), ext).map(Path::toFile).collect(Collectors.toList());
		stubConfigLoader.loadScript(files, ext);
	}

	private Stream<Path> findFiles(Path base, String ext) throws IOException {
		return Files.find(base, Integer.MAX_VALUE, (path, attr) -> {
			return attr.isRegularFile() && path.getFileName().toString().endsWith(ext);
		}).sorted();
	}

}
