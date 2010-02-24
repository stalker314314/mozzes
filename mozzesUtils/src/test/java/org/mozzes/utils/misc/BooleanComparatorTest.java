package org.mozzes.utils.misc;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.utils.misc.BooleanComparator;


public class BooleanComparatorTest {

	@Test
	public void testCompare() {
		BooleanComparator comparator = new BooleanComparator();
		assertEquals(0, comparator.compare(true, true));
		assertEquals(0, comparator.compare(false, false));
		assertEquals(1, comparator.compare(true, false));
		assertEquals(-1, comparator.compare(false, true));
	}
}
