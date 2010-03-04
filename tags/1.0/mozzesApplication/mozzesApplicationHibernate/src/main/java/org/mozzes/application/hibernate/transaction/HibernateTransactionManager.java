/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.application.hibernate.transaction;

import org.mozzes.application.plugin.transaction.TransactionManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class HibernateTransactionManager implements TransactionManager {
	
	@Inject
	private Provider<HibernateTransaction> transactionProvider;

	@Override
	public void begin(boolean nested) {
		transactionProvider.get().begin();
	}

	@Override
	public void commit() {
		transactionProvider.get().commit();
	}

	@Override
	public void rollback() {
		transactionProvider.get().rollback();
	}

	@Override
	public void finalizeTransaction(boolean successful) {
		transactionProvider.get().clear();
	}

}
