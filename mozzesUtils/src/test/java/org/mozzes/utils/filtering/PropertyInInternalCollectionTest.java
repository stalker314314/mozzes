package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mozzes.utils.filtering.PropertyInInternalCollection;
import org.mozzes.utils.reflection.Invoker;


public class PropertyInInternalCollectionTest {

	@Test
	public void testIsAcceptable() {
		PropertyInInternalCollection<DummyClass, Integer, Class<?>> filter = new PropertyInInternalCollection<DummyClass, Integer, Class<?>>(
				"testCollection", Integer.class, "class");

		// Object null
		DummyClass object = null;
		assertFalse(filter.isAcceptable(object));

		// Collection null
		object = new DummyClass();
		assertFalse(filter.isAcceptable(object));

		// Collection empty
		List<Integer> testCollection = new ArrayList<Integer>();
		object.setTestCollection(testCollection);
		assertFalse(filter.isAcceptable(object));

		// Collection contains valid entries
		testCollection.add(5);
		assertTrue(filter.isAcceptable(object));

	}

	@Test
	public void testPropertyInInternalCollectionStringPropertyTypeInvokerOfPropertyTypeCollectionType() {
		Invoker<Class<?>, Integer> invoker = null;
		// Invoker null (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", Integer.class, invoker);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		invoker = new Invoker<Class<?>, Integer>() {
			@Override
			public Class<?> invoke(Integer object) {
				return Integer.class;
			}
		};
		// Collection name null (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>(null, Integer.class, invoker);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Collection name empty (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("", Integer.class, invoker);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Compare value null (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", null, invoker);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Collection name invalid(pass, but will break latter)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("aaa", Integer.class, invoker);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testPropertyInInternalCollectionStringPropertyTypeString() {
		// PropertyName null (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", Integer.class,
					(String) null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// PropertyName empty (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", Integer.class, "");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// PropertyName invalid (pass, but will break latter)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", Integer.class, "aaa");
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Collection name null (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>(null, Integer.class, "class");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Collection name empty (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("", Integer.class, "class");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Compare value null (IllegalArgumentException)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", null, "class");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
		// Collection name invalid(pass, but will break latter)
		try {
			new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("aaa", Integer.class, "class");
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

}
