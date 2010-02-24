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