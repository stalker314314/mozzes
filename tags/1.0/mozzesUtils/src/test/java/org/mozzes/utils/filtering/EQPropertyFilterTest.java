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
package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.utils.filtering.EQPropertyFilter;

public class EQPropertyFilterTest {

	@Test
	public void testEQPropertyFilter() {
		// Property name null (should result in IllegalArgumentException)
		try {
			new EQPropertyFilter<DummyClass, Integer>(null, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Property name empty (should result in IllegalArgumentException)
		try {
			new EQPropertyFilter<DummyClass, Integer>("", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Property name invalid (should instantiate, but will break later)
		try {
			new EQPropertyFilter<DummyClass, Integer>("aa", null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Property name valid (all OK)
		try {
			new EQPropertyFilter<DummyClass, Integer>("testAttribute", null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testIsAcceptable() {
		DummyClass object = null;
		EQPropertyFilter<DummyClass, Integer> eq;
		// Property name invalid, object null (Should return false)
		eq = new EQPropertyFilter<DummyClass, Integer>("aa", null);
		assertFalse(eq.isAcceptable(object));

		// Property name invalid, object not null (Should result in RuntimeException)
		object = new DummyClass();
		eq = new EQPropertyFilter<DummyClass, Integer>("aa", null);
		try {
			eq.isAcceptable(object);
			fail();
		} catch (RuntimeException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		// Property name valid, object null(Should result in false)
		object = null;
		eq = new EQPropertyFilter<DummyClass, Integer>("testAttribute", null);
		assertFalse(eq.isAcceptable(object));

		// Property name valid, object not null, property null, compare value null(Should result in true)
		object = new DummyClass();
		eq = new EQPropertyFilter<DummyClass, Integer>("testAttribute", null);
		assertTrue(eq.isAcceptable(object));

		// Property name valid, object not null, property not null, compare value null(Should result in false)
		object = new DummyClass();
		object.setTestAttribute(10);
		eq = new EQPropertyFilter<DummyClass, Integer>("testAttribute", null);
		assertFalse(eq.isAcceptable(object));

		// Property name valid, object not null, property null, compare value not null(Should result in false)
		object = new DummyClass();
		eq = new EQPropertyFilter<DummyClass, Integer>("testAttribute", 10);
		assertFalse(eq.isAcceptable(object));

		// Property name valid, property=compare value(Should result in true)
		object = new DummyClass();
		object.setTestAttribute(11);
		eq = new EQPropertyFilter<DummyClass, Integer>("testAttribute", 11);
		assertTrue(eq.isAcceptable(object));

		// Property name valid, property!=compare value(Should result in false)
		object = new DummyClass();
		object.setTestAttribute(12);
		eq = new EQPropertyFilter<DummyClass, Integer>("testAttribute", 13);
		assertFalse(eq.isAcceptable(object));
	}
}
