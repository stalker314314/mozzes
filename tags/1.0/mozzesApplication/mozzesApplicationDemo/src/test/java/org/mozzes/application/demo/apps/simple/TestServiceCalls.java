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
package org.mozzes.application.demo.apps.simple;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.mozzes.application.common.transaction.TransactionIgnored;
import org.mozzes.application.demo.apps.TestBase;
import org.mozzes.application.demo.mockups.MTransactionManager;
import org.mozzes.application.demo.mockups.services.basic.BasicService;
import org.mozzes.application.demo.mockups.services.basic.ServiceThatThrowsExceptions;
import org.mozzes.application.plugin.transaction.TransactionManager;


/**
 * This test case shows how client is calling server's service methods.
 * 
 * @author vita
 */
public class TestServiceCalls extends TestBase {

	/**
	 * Client is calling service method that simply is returning 123 value.
	 */
	@Test
	public void testServiceMethodCall() {
		Integer result = getClient().getService(BasicService.class).getIntegerFromServer();
		assertEquals(BasicService.returnedValue, result);
		testNormalFinish();
	}

	/**
	 * Client is calling the service method that is throwing the exception but the exception type is annotated with the
	 * {@link TransactionIgnored} annotation and that basically means that if the exception does occur, the exception is
	 * propagated to the caller but the corresponding transaction is not rollbacked and commit is executed.
	 */
	@Test
	public void testServiceMethodCallThrowsIgnoredException() {
		try {
			getClient().getService(ServiceThatThrowsExceptions.class).serviceWhichThrowsIgnoredException();
			fail("exception is not thrown");
		} catch (Exception ignoreItsOK) {
		}
		/* transaction IS commited because this exception is ignored by the transaction manager */
		testNormalFinish();
	}

	/**
	 * Client that is calling service method that simply is throwing the checked exception and because the exception is
	 * checked and specified in the service interface client must handle the exception
	 */
	@Test
	public void testServiceMethodCallThrowsException() {
		try {
			getClient().getService(ServiceThatThrowsExceptions.class).serviceWhichThrowsException();
			fail("exception is not thrown");
		} catch (IOException ignoreItsOK) {
		}
		/* transaction is not commited */
		testRollbacked();
	}

	/**
	 * Client that is calling service method that simply is throwing the runtime exception.<br>
	 * Exception is runtime and client doesn't need to handle that exception.<br>
	 * (but this test case does because of the JUnit framework will complain)
	 */
	@Test
	public void testServiceMethodCallThrowsRuntimeException() {
		try {
			getClient().getService(ServiceThatThrowsExceptions.class).serviceWhichThrowsRuntimeException();
			fail("exception is not thrown");
		} catch (NumberFormatException ignoreItsOK) {
		}
		/* transaction is not commited */
		testRollbacked();
	}

	/**
	 * This is utility method that checks if the {@link TransactionManager#begin(boolean)},{@link TransactionManager#commit()},
	 * {@link TransactionManager#rollback()} are called properly. If the transaction is finished normally begin() and
	 * commit() are called and rollback() is not called
	 */
	private void testNormalFinish() {
		/* started and committed */
		assertTrue(MTransactionManager.started);
		assertTrue(MTransactionManager.commited);
		assertFalse(MTransactionManager.rollbacked);
	}

	/**
	 * This is utility method that checks if the {@link TransactionManager#begin(boolean)},{@link TransactionManager#commit()},
	 * {@link TransactionManager#rollback()} are called properly. If the transaction not finished normally begin() and
	 * rollback() are called and commit() is not called
	 */
	private void testRollbacked() {
		/* started and rollbacked */
		assertTrue(MTransactionManager.started);
		assertTrue(MTransactionManager.rollbacked);
		assertFalse(MTransactionManager.commited);
	}
}
