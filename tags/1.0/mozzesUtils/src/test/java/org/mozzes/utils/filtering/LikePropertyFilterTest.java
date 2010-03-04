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
import org.mozzes.utils.filtering.Filter;
import org.mozzes.utils.filtering.LikePropertyFilter;

public class LikePropertyFilterTest {

	@Test
	public void testLikePropertyFilterStringString() {
		// PropertyName null (IllegalArgumentException)
		try {
			new LikePropertyFilter<DummyClass, Integer>(null, "adad");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// PropertyName empty (IllegalArgumentException)
		try {
			new LikePropertyFilter<DummyClass, Integer>("", "daas");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// CompareValue null (IllegalArgumentException)
		try {
			new LikePropertyFilter<DummyClass, Integer>("aa", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// CompareValue empty (Pass, always true)
		new LikePropertyFilter<DummyClass, Integer>("aa", "");

		// PropertyName invalid (Pass, but will break latter)
		new LikePropertyFilter<DummyClass, Integer>("aa", "aaa");

		// All OK (Pass)
		new LikePropertyFilter<DummyClass, Integer>("testAttribute", "aaa");
	}

	@Test
	public void testIsAcceptable() {
		Filter<DummyClass> filter = new LikePropertyFilter<DummyClass, Integer>("testAttribute", "1");

		DummyClass object = null;
		assertFalse(filter.isAcceptable(object));

		object = new DummyClass();
		assertFalse(filter.isAcceptable(object));

		object.setTestAttribute(2);
		assertFalse(filter.isAcceptable(object));

		object.setTestAttribute(1);
		assertTrue(filter.isAcceptable(object));

		object.setTestAttribute(11);
		assertTrue(filter.isAcceptable(object));

		object.setTestAttribute(12);
		assertTrue(filter.isAcceptable(object));
	}
}
