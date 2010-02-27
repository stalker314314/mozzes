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
package org.mozzes.application.demo.mockups;

import org.mozzes.application.plugin.transaction.TransactionManager;

/**
 * Mockup implementation of the server transaction manager.<br>
 * In the testing we're only interested for calls of the {@link TransactionManager} methods so we need some boolean
 * variables to store that information
 * 
 * @author vita
 */
public class MTransactionManager implements TransactionManager {

	public static boolean commited = false;

	public static boolean rollbacked = false;

	public static boolean started = false;

	/**
	 * if we're starting new transaction commited and rollbacked are false again
	 */
	@Override
	public void begin(boolean nested) {
		started = true;
		rollbacked = false;
		commited = false;
	}

	/**
	 * @see TransactionManager#commit()
	 */
	@Override
	public void commit() {
		commited = true;
	}

	/**
	 * @see TransactionManager#rollback()
	 */
	@Override
	public void rollback() {
		rollbacked = true;
	}
	
	@Override
	public void finalizeTransaction(boolean successful) {
		
	}
}
