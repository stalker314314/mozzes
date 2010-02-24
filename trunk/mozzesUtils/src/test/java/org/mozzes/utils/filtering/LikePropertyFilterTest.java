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
