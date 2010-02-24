package org.mozzes.application.server.mockups;

import org.mozzes.application.common.transaction.TransactionIgnored;

@TransactionIgnored
public class MockUpIgnoredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}