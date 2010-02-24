package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.utils.filtering.Filter;
import org.mozzes.utils.filtering.LikePropertyFilter;

public class LikeContainsPropertyFilterTest {

	@Test
	public void testLikeContainsPropertyFilterStringString() {
		// PropertyName null (IllegalArgumentException)
		try {
			new LikePropertyFilter<DummyClass, Integer>(null, "adad", true);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// PropertyName empty (IllegalArgumentException)
		try {
			new LikePropertyFilter<DummyClass, Integer>("", "daas", true);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// CompareValue null (IllegalArgumentException)
		try {
			new LikePropertyFilter<DummyClass, Integer>("aa", null, true);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		// CompareValue empty (Pass, always true)
		new LikePropertyFilter<DummyClass, Integer>("aa", "");

		// PropertyName invalid (Pass, but will break latter)
		new LikePropertyFilter<DummyClass, Integer>("aa", "aaa", true);

		// All OK (Pass)
		new LikePropertyFilter<DummyClass, Integer>("testAttribute", "aaa", true);
	}

	@Test
	public void testIsAcceptable() {
		Filter<DummyClass> filter = new LikePropertyFilter<DummyClass, Integer>("testAttribute", "1", true);

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

		object.setTestAttribute(21);
		assertTrue(filter.isAcceptable(object));

		object.setTestAttribute(212);
		assertTrue(filter.isAcceptable(object));
	}
}
