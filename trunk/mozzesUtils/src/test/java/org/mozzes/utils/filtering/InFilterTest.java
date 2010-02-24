package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mozzes.utils.filtering.InFilter;

public class InFilterTest {

	@Test
	public void testInFilter() {
		try {
			new InFilter<Integer>(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		new InFilter<Integer>(new ArrayList<Integer>());
		assertTrue(true);
	}

	@Test
	public void testIsAcceptable() {
		Set<Integer> set = new HashSet<Integer>();
		InFilter<Integer> inFilter = new InFilter<Integer>(set);

		// object null, set empty (result false)
		Integer object = null;
		assertFalse(inFilter.isAcceptable(object));

		// object not in set (result false)
		object = 1;
		assertFalse(inFilter.isAcceptable(object));

		// object added to set, but the changes are not reflected since the inSet
		// is copied at construction time (result false)
		set.add(1);
		assertFalse(inFilter.isAcceptable(object));

		// object added to the set used by filter (result true)
		inFilter.getInSet().add(1);
		assertTrue(inFilter.isAcceptable(object));

		// object was in inSet at constrution time (result true)
		inFilter = new InFilter<Integer>(set);
		assertTrue(inFilter.isAcceptable(object));
	}

}
