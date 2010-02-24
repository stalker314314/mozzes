package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mozzes.utils.filtering.InPropertyFilter;

public class InPropertyFilterTest {

	@Test
	public void testInPropertyFilter() {
		// Valid propertyName (should result in successful creation)
		try {
			new InPropertyFilter<DummyClass, Integer>("testAttribute", null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		// Null propertyName (should result in IllegalArgumentException)
		try {
			new InPropertyFilter<DummyClass, Integer>(null, null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		// Empty propertyName (should result in IllegalArgumentException)
		try {
			new InPropertyFilter<DummyClass, Integer>("", null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		// Invalid propertyName (should result in successful creation, but will break later)
		try {
			new InPropertyFilter<DummyClass, Integer>("aa", null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testIsAcceptable() {
		Set<Integer> set = new HashSet<Integer>();
		InPropertyFilter<DummyClass, Integer> inPropertyFilter = new InPropertyFilter<DummyClass, Integer>(
				"testAttribute", set);

		// object null, set empty (result false)
		DummyClass object = null;
		assertFalse(inPropertyFilter.isAcceptable(object));

		// object not in set (result false)
		object = new DummyClass();
		object.setTestAttribute(1);
		assertFalse(inPropertyFilter.isAcceptable(object));

		// object added to set, changes are reflected (result true)
		set.add(1);
		assertTrue(inPropertyFilter.isAcceptable(object));

		// object added to the set used by filter (result true)
		inPropertyFilter.getInSet().add(1);
		assertTrue(inPropertyFilter.isAcceptable(object));

		// object was in inSet at constrution time (result true)
		inPropertyFilter = new InPropertyFilter<DummyClass, Integer>("testAttribute", set);
		assertTrue(inPropertyFilter.isAcceptable(object));
	}

}
