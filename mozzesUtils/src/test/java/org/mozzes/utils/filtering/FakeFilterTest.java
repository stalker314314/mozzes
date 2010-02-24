package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.utils.filtering.FakeFilter;

public class FakeFilterTest {

	@Test
	public void testIsAcceptable() {
		FakeFilter<Integer> fakeFilter = new FakeFilter<Integer>();
		assertTrue(fakeFilter.isAcceptable(null));
		assertTrue(fakeFilter.isAcceptable(1));
	}

}
