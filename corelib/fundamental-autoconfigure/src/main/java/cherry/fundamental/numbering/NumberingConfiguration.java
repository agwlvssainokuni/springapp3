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

package cherry.fundamental.numbering;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class NumberingConfiguration {

	@ConditionalOnClass({ Numbering.class })
	@ConditionalOnBean({ NumberingStore.class })
	public static class NumberingCfg {

		private final NumberingStore numberingStore;

		private final PlatformTransactionManager txMgr;

		public NumberingCfg(NumberingStore numberingStore, PlatformTransactionManager txMgr) {
			this.numberingStore = numberingStore;
			this.txMgr = txMgr;
		}

		@Bean
		@Primary
		public Numbering numbering(PlatformTransactionManager txMgr) {
			return createTxNumbering(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		}

		@Bean
		public Numbering numberingInTx(NumberingStore store, PlatformTransactionManager txMgr) {
			return createTxNumbering(TransactionDefinition.PROPAGATION_REQUIRED);
		}

		private Numbering createTxNumbering(int propagation) {
			TransactionTemplate txOps = new TransactionTemplate();
			txOps.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
			txOps.setTransactionManager(txMgr);
			txOps.setPropagationBehavior(propagation);
			txOps.afterPropertiesSet();
			return new NumberingImpl(numberingStore, txOps);
		}
	}

}
