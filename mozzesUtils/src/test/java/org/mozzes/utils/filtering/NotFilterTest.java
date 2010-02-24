package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.utils.filtering.FakeFilter;
import org.mozzes.utils.filtering.Filter;
import org.mozzes.utils.filtering.NoResultsFilter;
import org.mozzes.utils.filtering.NotFilter;

public class NotFilterTest {

	@Test
	public void testIsAcceptable() {
		Filter<Integer> wrappedFilter = new NoResultsFilter<Integer>();
		NotFilter<Integer> notFilter = new NotFilter<Integer>(wrappedFilter);

		assertTrue(notFilter.isAcceptable(null));
		assertTrue(notFilter.isAcceptable(1));

		wrappedFilter = new FakeFilter<Integer>();
		notFilter.setWrappedFilter(wrappedFilter);
		assertFalse(notFilter.isAcceptable(null));
		assertFalse(notFilter.isAcceptable(1));

	}

	@Test
	public void testNotFilter() {
		Filter<Integer> wrappedFilter = null;
		try {
			new NotFilter<Integer>(wrappedFilter);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		wrappedFilter = new NoResultsFilter<Integer>();
		new NotFilter<Integer>(wrappedFilter);
	}

}
