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
import org.mozzes.utils.filtering.ExpressionFilter;
import org.mozzes.utils.filtering.FakeFilter;
import org.mozzes.utils.filtering.Filter;
import org.mozzes.utils.filtering.FilterWrapper;

public class FilterWrapperTest {

	@Test
	public void testFilterWrapper() {
		Filter<Integer> wrappedFilter;
		// Wrapped filter=null (Should result in NullPointerException);
		wrappedFilter = null;
		try {
			new FilterWrapper<Integer>(wrappedFilter) {};
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Wrapped filter!=null (All OK);
		wrappedFilter = new FakeFilter<Integer>();
		try {
			new FilterWrapper<Integer>(wrappedFilter) {};
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testSetWrappedFilter() {
		Filter<Integer> wrappedFilter = new FakeFilter<Integer>();
		FilterWrapper<Integer> filterWrapper = new FilterWrapper<Integer>(wrappedFilter) {};
		// Wrapped filter=null (Should result in NullPointerException);
		wrappedFilter = null;
		try {
			filterWrapper.setWrappedFilter(wrappedFilter);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Wrapped filter!=null (All OK);
		wrappedFilter = new FakeFilter<Integer>();
		try {
			filterWrapper.setWrappedFilter(wrappedFilter);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testIsAcceptable() {
		Filter<Integer> wrappedFilter = new FakeFilter<Integer>();
		FilterWrapper<Integer> filterWrapper = new FilterWrapper<Integer>(wrappedFilter) {};
		assertEquals(wrappedFilter.isAcceptable(null), filterWrapper.isAcceptable(null));
		Integer obj = 1;
		assertEquals(wrappedFilter.isAcceptable(obj), filterWrapper.isAcceptable(obj));
	}

	@Test
	public void testReplaceFilter() {

	}

	@Test
	public void testRemoveFilter() {
		Filter<Integer> wrappedFilter = new FakeFilter<Integer>();
		FilterWrapper<Integer> filterWrapper = new FilterWrapper<Integer>(wrappedFilter) {};

		assertFalse(filterWrapper.removeFilter(null));
		assertFalse(filterWrapper.removeFilter(new ExpressionFilter<Integer>()));
		assertTrue(filterWrapper.removeFilter(new FakeFilter<Integer>()));
	}

}
