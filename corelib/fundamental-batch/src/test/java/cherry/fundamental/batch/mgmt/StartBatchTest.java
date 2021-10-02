/*
 * Copyright 2014,2021 agwlvssainokuni
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

package cherry.fundamental.batch.mgmt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import cherry.fundamental.batch.ExitStatus;

public class StartBatchTest {

	private Supplier<LocalDateTime> currentDateTime;

	private BatchStatusStore batchStatusStore;

	@BeforeEach
	@SuppressWarnings("unchecked")
	public void before() {
		currentDateTime = mock(Supplier.class);
		batchStatusStore = mock(BatchStatusStore.class);
	}

	@Test
	public void testExecute_NORMAL() {
		// 準備
		LocalDateTime dtm = LocalDateTime.now();
		when(currentDateTime.get()).thenReturn(dtm);
		when(batchStatusStore.isBatchRunning("batchId")).thenReturn(false);
		StartBatch impl = create();
		// 実行
		assertEquals(ExitStatus.NORMAL, impl.execute(args("batchId")));
		// 検証
		verify(batchStatusStore, times(1)).isBatchRunning(eq("batchId"));
		verify(batchStatusStore, times(1)).updateToRunning(eq("batchId"), eq(dtm));
		verify(currentDateTime, times(1)).get();
	}

	@Test
	public void testExecute_WARN() {
		// 準備
		when(batchStatusStore.isBatchRunning("batchId")).thenReturn(true);
		StartBatch impl = create();
		// 実行
		assertEquals(ExitStatus.WARN, impl.execute(args("batchId")));
		// 検証
		verify(batchStatusStore, times(1)).isBatchRunning(eq("batchId"));
		verify(batchStatusStore, never()).updateToRunning(anyString(), any(LocalDateTime.class));
		verify(currentDateTime, never()).get();
	}

	@Test
	public void testExecute_ERROR() {
		// 準備
		StartBatch impl = create();
		// 実行
		assertEquals(ExitStatus.ERROR, impl.execute(args()));
		// 検証
		verify(batchStatusStore, never()).isBatchRunning(anyString());
		verify(batchStatusStore, never()).updateToRunning(anyString(), any(LocalDateTime.class));
		verify(currentDateTime, never()).get();
	}

	private ApplicationArguments args(String... args) {
		return new DefaultApplicationArguments(args);
	}

	private StartBatch create() {
		BatchStatusServiceImpl impl = new BatchStatusServiceImpl(batchStatusStore, currentDateTime);
		return new StartBatch(impl);
	}

}
