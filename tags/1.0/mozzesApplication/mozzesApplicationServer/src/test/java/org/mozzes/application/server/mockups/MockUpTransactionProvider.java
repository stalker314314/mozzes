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
package org.mozzes.application.server.mockups;

import org.mozzes.application.plugin.transaction.TransactionManager;

import com.google.inject.Provider;

public class MockUpTransactionProvider implements Provider<TransactionManager> {

	private boolean failBegin = false;

	private boolean failCommit = false;

	private boolean failRollback = false;

	private boolean started = false;

	private boolean commited = false;

	private boolean rollbacked = false;

	public MockUpTransactionProvider(boolean failBegin, boolean failCommit, boolean failRollback) {
		this.failBegin = failBegin;
		this.failCommit = failCommit;
		this.failRollback = failRollback;
	}

	public MockUpTransactionProvider() {
		// empty
	}

	@Override
	public TransactionManager get() {

		return new TransactionManager() {

			@Override
			public void begin(boolean nested) {
				started = true;
				if (failBegin)
					throw new RuntimeException();
			}

			@Override
			public void commit() {
				commited = true;
				if (failCommit)
					throw new RuntimeException();
			}

			@Override
			public void rollback() {
				rollbacked = true;
				if (failRollback)
					throw new RuntimeException();
			}
			
			@Override
			public void finalizeTransaction(boolean successful) {
				// ignore
			}
		};
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isCommited() {
		return commited;
	}

	public boolean isRollbacked() {
		return rollbacked;
	}

	public void setFailBegin(boolean failBegin) {
		this.failBegin = failBegin;
	}

	public void setFailCommit(boolean failCommit) {
		this.failCommit = failCommit;
	}

	public void setFailRollback(boolean failRollback) {
		this.failRollback = failRollback;
	}
}