package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.utils.filtering.NoResultsFilter;

public class NoResultsFilterTest {

	@Test
	public void testIsAcceptable() {
		NoResultsFilter<Integer> noResultsFilter = new NoResultsFilter<Integer>();
		assertFalse(noResultsFilter.isAcceptable(null));
		assertFalse(noResultsFilter.isAcceptable(1));
	}

}
