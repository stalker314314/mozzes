package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.utils.filtering.GEPropertyFilter;

public class GEPropertyFilterTest {

	@Test
	public void testGEPropertyFilterStringPropertyType() {
		// Property name null (should result in IllegalArgumentException)
		try {
			new GEPropertyFilter<DummyClass, Integer>(null, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Property name empty (should result in IllegalArgumentException)
		try {
			new GEPropertyFilter<DummyClass, Integer>("", null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Property name invalid (should instantiate, but will break later)
		try {
			new GEPropertyFilter<DummyClass, Integer>("aa", null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Property name valid (all OK)
		try {
			new GEPropertyFilter<DummyClass, Integer>("testAttribute", null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testIsAcceptable() {
		DummyClass object = null;
		GEPropertyFilter<DummyClass, Integer> eq;
		// Property name invalid, object null (Should return false)
		eq = new GEPropertyFilter<DummyClass, Integer>("aa", null);
		assertFalse(eq.isAcceptable(object));

		// Property name invalid, object not null (Should result in RuntimeException)
		object = new DummyClass();
		eq = new GEPropertyFilter<DummyClass, Integer>("aa", null);
		try {
			eq.isAcceptable(object);
			fail();
		} catch (RuntimeException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		// Property name valid, object null(Should result in false)
		object = null;
		eq = new GEPropertyFilter<DummyClass, Integer>("testAttribute", null);
		assertFalse(eq.isAcceptable(object));

		// Property name valid, object not null, property null, compare value null(Should result in true)
		object = new DummyClass();
		eq = new GEPropertyFilter<DummyClass, Integer>("testAttribute", null);
		assertTrue(eq.isAcceptable(object));

		// Property name valid, object not null, property not null, compare value null(Should result in true)
		object = new DummyClass();
		object.setTestAttribute(10);
		eq = new GEPropertyFilter<DummyClass, Integer>("testAttribute", null);
		assertTrue(eq.isAcceptable(object));

		// Property name valid, object not null, property null, compare value not null(Should result in false)
		object = new DummyClass();
		eq = new GEPropertyFilter<DummyClass, Integer>("testAttribute", 10);
		assertFalse(eq.isAcceptable(object));

		// Property name valid, property=compare value(Should result in true)
		object = new DummyClass();
		object.setTestAttribute(11);
		eq = new GEPropertyFilter<DummyClass, Integer>("testAttribute", 11);
		assertTrue(eq.isAcceptable(object));

		// Property name valid, property<compare value(Should result in false)
		object = new DummyClass();
		object.setTestAttribute(12);
		eq = new GEPropertyFilter<DummyClass, Integer>("testAttribute", 13);
		assertFalse(eq.isAcceptable(object));
		
		// Property name valid, property>compare value(Should result in true)
		object = new DummyClass();
		object.setTestAttribute(15);
		eq = new GEPropertyFilter<DummyClass, Integer>("testAttribute", 13);
		assertTrue(eq.isAcceptable(object));
	}
}
