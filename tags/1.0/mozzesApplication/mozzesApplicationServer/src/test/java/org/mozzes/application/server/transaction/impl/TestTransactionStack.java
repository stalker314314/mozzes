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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mozzes.application.server.transaction.impl.TransactionContext;
import org.mozzes.application.server.transaction.impl.TransactionStack;

public class TestTransactionStack {

	private TransactionStack stack;
	
	@Before
	public void before(){
		stack = new TransactionStack();
	}
	
	@Test
	public void testAddingToStack() {
		TransactionContext c1 = new TransactionContext();
		stack.push(c1);
		assertTrue(stack.pop().equals(c1));
	}

	@Test
	public void testPopEmptyStack() {
		try {
			stack.pop();
			fail("should throw exception");
		} catch (RuntimeException e) {
			// ignore it's OK
		}
	}

	@Test
	public void testPeekEmptyStack() {
		try {
			stack.pop();
			fail("should throw exception");
		} catch (RuntimeException e) {
			// ignore it's OK
		}
	}

	@Test
	public void testAddingNull() {
		stack.push(null);
		assertNull(stack.pop());
	}

	@Test
	public void testNotEmpty() {
		stack.push(null);
		assertFalse(stack.isEmpty());
	}

	@Test
	public void testEmpty() {
		assertTrue(stack.isEmpty());
	}
}
