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
import static org.mockito.ArgumentMatchers.anyInt;
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

public class FinishBatchTest {

	private Supplier<LocalDateTime> currentDateTime;

	private BatchStatusStore batchStatusStore;

	@BeforeEach
	@SuppressWarnings("unchecked")
	public void before() {
		currentDateTime = mock(Supplier.class);
		batchStatusStore = mock(BatchStatusStore.class);
	}

	@Test
	public void testExecute_NORMAL_0() {
		// 準備
		LocalDateTime dtm = LocalDateTime.now();
		when(currentDateTime.get()).thenReturn(dtm);
		when(batchStatusStore.isBatchRunning("batchId")).thenReturn(true);
		FinishBatch impl = create();
		// 実行
		assertEquals(ExitStatus.NORMAL, impl.execute(args("batchId", "NORMAL")));
		// 検証
		verify(batchStatusStore, times(1)).isBatchRunning(eq("batchId"));
		verify(batchStatusStore, times(1)).updateToFinished(eq("batchId"), eq(dtm), eq(ExitStatus.NORMAL), eq(0));
		verify(currentDateTime, times(1)).get();
	}

	@Test
	public void testExecute_NORMAL_1() {
		// 準備
		LocalDateTime dtm = LocalDateTime.now();
		when(currentDateTime.get()).thenReturn(dtm);
		when(batchStatusStore.isBatchRunning("batchId")).thenReturn(true);
		FinishBatch impl = create();
		// 実行
		assertEquals(ExitStatus.NORMAL, impl.execute(args("batchId", "NORMAL", "0")));
		// 検証
		verify(batchStatusStore, times(1)).isBatchRunning(eq("batchId"));
		verify(batchStatusStore, times(1)).updateToFinished(eq("batchId"), eq(dtm), eq(ExitStatus.NORMAL), eq(0));
		verify(currentDateTime, times(1)).get();
	}

	@Test
	public void testExecute_WARN() {
		// 準備
		when(batchStatusStore.isBatchRunning("batchId")).thenReturn(false);
		FinishBatch impl = create();
		// 実行
		assertEquals(ExitStatus.WARN, impl.execute(args("batchId", "NORMAL")));
		// 検証
		verify(batchStatusStore, times(1)).isBatchRunning(eq("batchId"));
		verify(batchStatusStore, never()).updateToFinished(anyString(), any(LocalDateTime.class), any(ExitStatus.class),
				anyInt());
		verify(currentDateTime, never()).get();
	}

	@Test
	public void testExecute_ERROR_0() {
		// 準備
		FinishBatch impl = create();
		// 実行
		assertEquals(ExitStatus.ERROR, impl.execute(args()));
		// 検証
		verify(batchStatusStore, never()).isBatchRunning(anyString());
		verify(batchStatusStore, never()).updateToFinished(anyString(), any(LocalDateTime.class), any(ExitStatus.class),
				anyInt());
		verify(currentDateTime, never()).get();
	}

	@Test
	public void testExecute_ERROR_1() {
		// 準備
		FinishBatch impl = create();
		// 実行
		assertEquals(ExitStatus.ERROR, impl.execute(args("batchId")));
		// 検証
		verify(batchStatusStore, never()).isBatchRunning(anyString());
		verify(batchStatusStore, never()).updateToFinished(anyString(), any(LocalDateTime.class), any(ExitStatus.class),
				anyInt());
		verify(currentDateTime, never()).get();
	}

	private ApplicationArguments args(String... args) {
		return new DefaultApplicationArguments(args);
	}

	private FinishBatch create() {
		BatchStatusServiceImpl impl = new BatchStatusServiceImpl(batchStatusStore, currentDateTime);
		return new FinishBatch(impl);
	}

}
