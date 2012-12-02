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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mozzes.application.server.MozzesTestBase;
import org.mozzes.application.server.mockups.MockUpInvocation;
import org.mozzes.application.server.mockups.MockUpTransactionProvider;
import org.mozzes.application.server.mockups.MockuUpInvocationHandler;
import org.mozzes.application.server.mockups.MockupTransactionStackProvider;
import org.mozzes.application.server.mockups.ServerService1;
import org.mozzes.application.server.transaction.impl.TransactionContext;
import org.mozzes.application.server.transaction.impl.TransactionInterceptor;

public class TestTransactionInterceptor extends MozzesTestBase {

  private MockUpInvocation<ServerService1> invocation;
  private TransactionInterceptor interceptor;
  private MockUpTransactionProvider transactionProvider;

  @Before
  public void before() {
    invocation = new MockUpInvocation<ServerService1>(ServerService1.class, "service1Method1", null, null);
    transactionProvider = new MockUpTransactionProvider();
    interceptor = new TransactionInterceptor(new MockupTransactionStackProvider(), transactionProvider);
  }

  @Test
  public void testTransactionInterceptorInvocationSuccessful() {

    Object returnVal = null;
    try {
      returnVal = interceptor.invoke(invocation, new MockuUpInvocationHandler());
    } catch (Throwable e) {
      fail("should not throw exception");
    }
    assertNotNull(returnVal);
    assertTrue(invocation.isInvoked());
    assertTrue(transactionProvider.isStarted());
    assertTrue(transactionProvider.isCommited());
  }

  @Test
  public void testTransactionInterceptorInvocationFailing() {

    Object returnVal = null;
    try {
      returnVal = interceptor.invoke(invocation, new MockuUpInvocationHandler(true));
      fail("should throw exception");
    } catch (Throwable e) {
      // ignore
    }
    assertNull(returnVal);
    assertTrue(invocation.isInvoked());
    assertTrue(transactionProvider.isStarted());
    assertTrue(transactionProvider.isRollbacked());
  }

  @Test
  public void testTransactionInterceptorBeginFailing() {

    transactionProvider.setFailBegin(true);

    Object returnVal = null;
    try {
      returnVal = interceptor.invoke(invocation, new MockuUpInvocationHandler());
      fail("should throw exception");
    } catch (Throwable e) {
      // ignore
    }
    assertNull(returnVal);
    assertFalse(invocation.isInvoked());
    assertTrue(transactionProvider.isStarted());
    assertTrue(transactionProvider.isRollbacked());
  }

  @Test
  public void testTransactionInterceptorRollbackFailing() {

    transactionProvider.setFailRollback(true);

    Object returnVal = null;
    try {
      returnVal = interceptor.invoke(invocation, new MockuUpInvocationHandler(true));
      fail("should throw exception");
    } catch (Throwable e) {
      // ignore
    }

    assertNull(returnVal);
    assertTrue(invocation.isInvoked());
    assertTrue(transactionProvider.isStarted());
    assertTrue(transactionProvider.isRollbacked());
  }

  @Test
  public void testTransactionInterceptorCommitFailing() {
    transactionProvider.setFailCommit(true);

    Object returnVal = null;
    try {
      returnVal = interceptor.invoke(invocation, new MockuUpInvocationHandler());
      fail("should throw exception");
    } catch (Throwable e) {
      // ignore
    }

    assertNull(returnVal);
    assertTrue(invocation.isInvoked());
    assertTrue(transactionProvider.isStarted());
    assertTrue(transactionProvider.isCommited());
  }

  @Test
  public void testTransactionAlreadyStartedAndDontNeedNewSuccess() {

    MockupTransactionStackProvider stack = new MockupTransactionStackProvider();
    stack.get().push(new TransactionContext());

    interceptor = new TransactionInterceptor(stack, transactionProvider);

    Object returnVal = null;
    try {
      returnVal = interceptor.invoke(invocation, new MockuUpInvocationHandler());
    } catch (Throwable e) {
      fail("should not throw exception");
    }

    assertNotNull(returnVal);
    assertTrue(invocation.isInvoked());
    assertFalse(transactionProvider.isStarted());
  }

  @Test
  public void testTransactionAlreadyStartedAndDontNeedNewFail() {

    MockupTransactionStackProvider stack = new MockupTransactionStackProvider();
    stack.get().push(new TransactionContext());
    interceptor = new TransactionInterceptor(stack, transactionProvider);

    Object returnVal = null;
    try {
      returnVal = interceptor.invoke(invocation, new MockuUpInvocationHandler(true));
      fail("should throw exception");
    } catch (Throwable e) {
      // ignore
    }
    assertNull(returnVal);
    assertTrue(invocation.isInvoked());
    assertFalse(transactionProvider.isStarted());
  }

  @Test
  public void testTransactionInvocationFailedExecptionIgnored() {

    Object returnVal = null;
    try {
      returnVal = interceptor.invoke(invocation, new MockuUpInvocationHandler(true, true));
      fail("should throw exception");
    } catch (Throwable e) {
      // ignore
    }

    assertNull(returnVal);
    assertTrue(invocation.isInvoked());
    assertTrue(transactionProvider.isStarted());
    assertTrue(transactionProvider.isCommited());
  }
}