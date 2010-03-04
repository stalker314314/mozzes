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
package org.mozzes.application.server.transaction.impl;

import org.mozzes.application.plugin.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class TransactionManagerImpl is the default implementation of the TransactionManager interface and adds support
 * for transaction management in the application.Default version don't do anything so MozzesServer framework users need
 * to specify their specific version of the TransactionManagment.
 */
public class TransactionManagerImpl implements TransactionManager {

	private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

	/*
	 * @see TransactionManager#begin()
	 */
	@Override
	public void begin(boolean nested) {
		logger.debug(nested ? "Nested transaction started" : "Transaction started");
	}

	/*
	 * @see TransactionManager#commit()
	 */
	@Override
	public void commit() {
		logger.debug("Transaction comitted");
	}

	/*
	 * @see TransactionManager#rollback()
	 */
	@Override
	public void rollback() {
		logger.debug("Transaction rollbacked");
	}
	
	/*
	 * @see TransactionManager#finalizeTransaction()
	 */
	@Override
	public void finalizeTransaction(boolean successful) {
		logger.debug("Transaction " + (successful ? "" : "un") + "successfully finalized" );
	}

}
