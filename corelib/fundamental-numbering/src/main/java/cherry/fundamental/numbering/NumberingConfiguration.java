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

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

@Configuration
@ConditionalOnBean(NumberingStore.class)
public class NumberingConfiguration {

	@Autowired
	private NumberingStore numberingStore;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Bean
	@Primary
	public TransactionProxyFactoryBean numbering() {
		return createNumberingFactoryBean(Propagation.REQUIRES_NEW);
	}

	@Bean
	public TransactionProxyFactoryBean numberingInTx() {
		return createNumberingFactoryBean(Propagation.REQUIRED);
	}

	private TransactionProxyFactoryBean createNumberingFactoryBean(Propagation propagation) {
		Properties attr = new Properties();
		attr.setProperty("*", DefaultTransactionAttribute.PREFIX_PROPAGATION + propagation);
		TransactionProxyFactoryBean bean = new TransactionProxyFactoryBean();
		bean.setTarget(new NumberingImpl(numberingStore));
		bean.setProxyInterfaces(new Class[] { Numbering.class });
		bean.setTransactionManager(transactionManager);
		bean.setTransactionAttributes(attr);
		return bean;
	}

}
