/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.utils.reflection;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mozzes.utils.reflection.Invoker;
import org.mozzes.utils.reflection.ReflectionException;
import org.mozzes.utils.reflection.ReflectionUtils;
import org.mozzes.utils.reflection.ReflectiveMethod;

public class ReflectionUtilsTest {

	@Test
	public void testGetPropertyForCollectionCollectionOfObjectTypeString() {
		List<ClassForTesting> c = null;
		List<Integer> expected = new ArrayList<Integer>();

		// Test null collection (should result in NullPointerException)
		try {
			ReflectionUtils.getPropertyForCollection(Integer.class, c, (String) null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}

		c = new ArrayList<ClassForTesting>();
		// Test empty collection (should result in empty collection)
		assertEquals(Collections.emptyList(), ReflectionUtils.getPropertyForCollection(Integer.class, c, (String) null));

		ClassForTesting cft = null;
		c.add(cft);
		// Test null property string(should result in NullPointerException)
		try {
			ReflectionUtils.getPropertyForCollection(Integer.class, c, (String) null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}

		cft = new ClassForTesting();
		c.add(0, cft);
		// Test null object in collection (should result in null for that object)
		expected.add(null);
		expected.add(null);
		assertEquals(expected, ReflectionUtils.getPropertyForCollection(Integer.class, c, "readWrite"));

		// Test normal all OK
		expected.clear();
		c.clear();
		cft = new ClassForTesting();
		cft.setReadWrite(10);
		c.add(cft);
		cft = new ClassForTesting();
		cft.setReadWrite(11);
		c.add(cft);
		expected.add(10);
		expected.add(11);
		assertEquals(expected, ReflectionUtils.getPropertyForCollection(Integer.class, c, "readWrite"));
	}

	@Test
	public void testGetPropertyForCollectionObjectTypeArrayString() {
		ClassForTesting[] c = null;
		List<Integer> expected = new ArrayList<Integer>();

		// Test null collection (should result in NullPointerException)
		try {
			ReflectionUtils.getPropertyForCollection(Integer.class, c, (String) null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}

		c = new ClassForTesting[0];
		// Test empty collection (should result in empty collection)
		assertEquals(Collections.emptyList(), ReflectionUtils.getPropertyForCollection(Integer.class, c, (String) null));

		c = new ClassForTesting[1];
		ClassForTesting cft = null;
		c[0] = cft;
		// Test null property string(should result in NullPointerException)
		try {
			ReflectionUtils.getPropertyForCollection(Integer.class, c, (String) null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}

		c = new ClassForTesting[2];
		cft = new ClassForTesting();
		c[0] = cft;
		c[1] = null;
		// Test null object in collection (should result in null for that object)
		expected.add(null);
		expected.add(null);
		assertEquals(expected, ReflectionUtils.getPropertyForCollection(Integer.class, c, "readWrite"));

		// Test normal all OK
		expected.clear();
		cft = new ClassForTesting();
		cft.setReadWrite(10);
		c[0] = cft;
		cft = new ClassForTesting();
		cft.setReadWrite(11);
		c[1] = cft;
		expected.add(10);
		expected.add(11);
		assertEquals(expected, ReflectionUtils.getPropertyForCollection(Integer.class, c, "readWrite"));
	}

	@Test
	public void testGetPropertyForCollectionCollectionOfQextendsObjectTypeInvokerOfReturnTypeObjectType() {
		List<ClassForTesting> c = null;
		List<Integer> exp = null;
		Invoker<Integer, ClassForTesting> invoker = null;

		// Test for both null
		try {
			ReflectionUtils.getPropertyForCollection(c, invoker);
			fail("Should throw IllegalArgumentException!");
		} catch (IllegalArgumentException ok) {
		}

		// Test for invoker null
		c = new ArrayList<ClassForTesting>();
		try {
			ReflectionUtils.getPropertyForCollection(c, invoker);
			fail("Should throw IllegalArgumentException!");
		} catch (IllegalArgumentException ok) {
		}

		// Test for collection null
		c = null;
		invoker = new Invoker<Integer, ClassForTesting>() {

			@Override
			public Integer invoke(ClassForTesting object) {
				return object.getReadWrite();
			}
		};
		try {
			ReflectionUtils.getPropertyForCollection(c, invoker);
			fail("Should throw IllegalArgumentException!");
		} catch (IllegalArgumentException ok) {
		}

		// Test for empty collection(should result in empty collection)
		c = new ArrayList<ClassForTesting>();
		assertEquals(Collections.emptyList(), ReflectionUtils.getPropertyForCollection(c, invoker));

		// Test for collection with null elements
		// (should result in a collection with nulls at the same position)
		c = new ArrayList<ClassForTesting>();
		c.add(null);
		exp = new ArrayList<Integer>();
		exp.add(null);
		assertEquals(exp, ReflectionUtils.getPropertyForCollection(c, invoker));

		// Test fot not null object with null invoker result
		// (should result in a collection with the results of invoker/nulls at apropriate positions)
		c = new ArrayList<ClassForTesting>();
		ClassForTesting cft = new ClassForTesting();
		cft.setReadWrite(null);
		c.add(cft);
		cft = new ClassForTesting();
		cft.setReadWrite(11);
		c.add(cft);
		exp = new ArrayList<Integer>();
		exp.add(null);
		exp.add(11);
		assertEquals(exp, ReflectionUtils.getPropertyForCollection(c, invoker));

		// Test normal case
		// (should result in a collection with the results of invoker)
		c = new ArrayList<ClassForTesting>();
		cft = new ClassForTesting();
		cft.setReadWrite(10);
		c.add(cft);
		cft = new ClassForTesting();
		cft.setReadWrite(11);
		c.add(cft);
		exp = new ArrayList<Integer>();
		exp.add(10);
		exp.add(11);
		assertEquals(exp, ReflectionUtils.getPropertyForCollection(c, invoker));
	}

	@Test
	public void testGetSetter() {
		// Test null
		try {
			ReflectionUtils.getSetter(ClassForTesting.class, ClassForTesting.class, null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException ok) {
		}

		// Test empty
		try {
			ReflectionUtils.getSetter(ClassForTesting.class, ClassForTesting.class, "");
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException ok) {
		}

		// Test no setter
		try {
			ReflectionUtils.getSetter(ClassForTesting.class, Integer.class, "noAccess");
			fail("Should throw ReflectionException with IllegalArgumentException as a cause.");
		} catch (ReflectionException re) {
			try {
				throw (re.getCause());
			} catch (NoSuchMethodException e) {
				assertTrue(true);
			} catch (Throwable t) {
				fail(t.getMessage());
			}
		}

		// Test has setter
		assertNotNull(ReflectionUtils.getSetter(
				ClassForTesting.class, Integer.class, "write"));

		// Test has setter boolean (napomena: boolean!=Boolean)
		assertNotNull(ReflectionUtils.getSetter(
				ClassForTesting.class, boolean.class, "readWriteB"));

		// Test has setter boolean (napomena: boolean!=Boolean)
		assertNotNull(ReflectionUtils.getSetter(
				ClassForTesting.class, Boolean.class, "readWriteB"));

		// Test private setter (will pass but invocation will throw an exception
		// unless setter.setAccessible(true) is called)
		assertNotNull(ReflectionUtils.getSetter(
				ClassForTesting.class, Integer.class, "readWriteP"));

		// Test protected setter (will pass but invocation will throw an exception
		// unless setter.setAccessible(true) is called)
		assertNotNull(ReflectionUtils.getSetter(ClassForTesting.class, Integer.class, "readWriteProtected"));
	}

	@Test
	public void testGetPropertyValue() {
		ClassForTesting t = null;

		assertNull(ReflectionUtils.getPropertyValue(t, Integer.class, "readWrite"));
		t = new ClassForTesting();
		assertNull(ReflectionUtils.getPropertyValue(t, Integer.class, "readWrite"));
		t.setReadWrite(10);
		assertEquals(Integer.valueOf(10), ReflectionUtils.getPropertyValue(t, Integer.class, "readWrite"));

		// null property
		try {
			assertEquals(Integer.valueOf(10), ReflectionUtils.getPropertyValue(t, Integer.class, null));
			fail("Should throw IllegalArgumentException!");
		} catch (IllegalArgumentException ok) {
		}

		// empty string for property
		try {
			assertEquals(Integer.valueOf(10), ReflectionUtils.getPropertyValue(t, Integer.class, ""));
			fail("Should throw IllegalArgumentException!");
		} catch (IllegalArgumentException ok) {
			}

		// Test private getter
		try {
			ReflectionUtils.getPropertyValue(t, Integer.class, "readWriteP");
			fail("Should throw ReflectionException with IllegalAccessException as cause!");
		} catch (ReflectionException re) {
			try {
				throw (re.getCause());
			} catch (IllegalAccessException ok) {
			} catch (Throwable th) {
				fail(Arrays.toString(th.getStackTrace()));
			}
		}

		// Test protected getter (since we are in the same package, it works,
		ReflectionUtils.getPropertyValue(t, Integer.class, "readWriteProtected");
			}

	@Test
	public void testResolvePrimitiveType() {
		assertEquals(Boolean.class, ReflectionUtils.resolvePrimitiveType(boolean.class));
		assertEquals(Integer.class, ReflectionUtils.resolvePrimitiveType(int.class));
		assertEquals(Float.class, ReflectionUtils.resolvePrimitiveType(float.class));
		assertEquals(Byte.class, ReflectionUtils.resolvePrimitiveType(byte.class));
		assertEquals(Character.class, ReflectionUtils.resolvePrimitiveType(char.class));
		assertEquals(Double.class, ReflectionUtils.resolvePrimitiveType(double.class));

		assertEquals(Boolean.class, ReflectionUtils.resolvePrimitiveType(Boolean.class));
		assertEquals(Integer.class, ReflectionUtils.resolvePrimitiveType(Integer.class));
		assertEquals(Float.class, ReflectionUtils.resolvePrimitiveType(Float.class));
		assertEquals(Byte.class, ReflectionUtils.resolvePrimitiveType(Byte.class));
		assertEquals(Character.class, ReflectionUtils.resolvePrimitiveType(Character.class));
		assertEquals(Double.class, ReflectionUtils.resolvePrimitiveType(Double.class));

		assertEquals(ClassForTesting.class, ReflectionUtils.resolvePrimitiveType(ClassForTesting.class));
	}

	@Test
	public void testGetDefaultValueOf() {
		assertEquals(null, ReflectionUtils.getDefaultValueOf(Integer.class));
		assertEquals(null, ReflectionUtils.getDefaultValueOf(Double.class));
		assertEquals(null, ReflectionUtils.getDefaultValueOf(Long.class));
		assertEquals(null, ReflectionUtils.getDefaultValueOf(Boolean.class));
		assertEquals(null, ReflectionUtils.getDefaultValueOf(Character.class));
		assertEquals(null, ReflectionUtils.getDefaultValueOf(Byte.class));

		assertEquals(Integer.valueOf(0), ReflectionUtils.getDefaultValueOf(int.class));
		assertEquals(Double.valueOf(0), ReflectionUtils.getDefaultValueOf(double.class));
		assertEquals(Long.valueOf(0), ReflectionUtils.getDefaultValueOf(long.class));
		assertEquals(false, ReflectionUtils.getDefaultValueOf(boolean.class));
		assertEquals((Character) '\u0000', ReflectionUtils.getDefaultValueOf(char.class));
		assertEquals(Byte.valueOf((byte) 0), ReflectionUtils.getDefaultValueOf(byte.class));
	}

	@Test
	public void resolvePrimitiveNull() {
		assertEquals(null, ReflectionUtils.resolvePrimitiveNull(Integer.class, null));
		assertEquals(null, ReflectionUtils.resolvePrimitiveNull(Double.class, null));
		assertEquals(null, ReflectionUtils.resolvePrimitiveNull(Long.class, null));
		assertEquals(null, ReflectionUtils.resolvePrimitiveNull(Boolean.class, null));
		assertEquals(null, ReflectionUtils.resolvePrimitiveNull(Character.class, null));
		assertEquals(null, ReflectionUtils.resolvePrimitiveNull(Byte.class, null));

		assertEquals(Integer.valueOf(10), ReflectionUtils.resolvePrimitiveNull(Integer.class, 10));
		assertEquals(Double.valueOf(10), ReflectionUtils.resolvePrimitiveNull(Double.class, 10.0));
		assertEquals(Long.valueOf(10), ReflectionUtils.resolvePrimitiveNull(Long.class, 10L));
		assertEquals(true, ReflectionUtils.resolvePrimitiveNull(Boolean.class, true));
		assertEquals((Character) 'a', ReflectionUtils.resolvePrimitiveNull(Character.class, 'a'));
		assertEquals(Byte.valueOf((byte) 10), ReflectionUtils.resolvePrimitiveNull(Byte.class, (byte) 10));

		assertEquals(Integer.valueOf(0), ReflectionUtils.resolvePrimitiveNull(int.class, null));
		assertEquals(Double.valueOf(0), ReflectionUtils.resolvePrimitiveNull(double.class, null));
		assertEquals(Long.valueOf(0), ReflectionUtils.resolvePrimitiveNull(long.class, null));
		assertEquals(false, ReflectionUtils.resolvePrimitiveNull(boolean.class, null));
		assertEquals((Character) '\u0000', ReflectionUtils.resolvePrimitiveNull(char.class, null));
		assertEquals(Byte.valueOf((byte) 0), ReflectionUtils.resolvePrimitiveNull(byte.class, null));

		assertEquals(Integer.valueOf(10), ReflectionUtils.resolvePrimitiveNull(int.class, 10));
		assertEquals(Double.valueOf(10), ReflectionUtils.resolvePrimitiveNull(double.class, 10.0));
		assertEquals(Long.valueOf(10), ReflectionUtils.resolvePrimitiveNull(long.class, 10L));
		assertEquals(true, ReflectionUtils.resolvePrimitiveNull(boolean.class, true));
		assertEquals((Character) 'a', ReflectionUtils.resolvePrimitiveNull(char.class, 'a'));
		assertEquals(Byte.valueOf((byte) 10), ReflectionUtils.resolvePrimitiveNull(byte.class, (byte) 10));
	}

	@Test
	public void convertNumber() {
		assertEquals(Double.class, ReflectionUtils.convertNumber(10, Double.class).getClass());
		assertEquals(Double.class, ReflectionUtils.convertNumber(10, double.class).getClass());
		assertEquals(Integer.class, ReflectionUtils.convertNumber(10, int.class).getClass());
		assertEquals(Integer.class, ReflectionUtils.convertNumber(10, Integer.class).getClass());

		assertEquals(Integer.valueOf(10), ReflectionUtils.convertNumber(10.3245, Integer.class));
		assertEquals(Integer.valueOf(10), ReflectionUtils.convertNumber(10.5981, Integer.class));

		assertEquals(Double.valueOf(10.3245), ReflectionUtils.convertNumber(10.3245, Double.class));
		assertEquals(Double.valueOf(10.5981), ReflectionUtils.convertNumber(10.5981, Double.class));
		assertEquals(Double.valueOf(10), ReflectionUtils.convertNumber(10, Double.class));

		assertEquals(Double.valueOf(123), ReflectionUtils.convertNumber(Byte.valueOf((byte) 123), Double.class));

		assertEquals(Byte.valueOf((byte) 123), ReflectionUtils.convertNumber(Double.valueOf(123), Byte.class));
		assertEquals(Byte.valueOf((byte) 123), ReflectionUtils.convertNumber(Double.valueOf(123.654), Byte.class));
		assertEquals(Byte.valueOf((byte) 0), ReflectionUtils.convertNumber(Double.valueOf(256), Byte.class));
		assertEquals(Byte.valueOf((byte) 1), ReflectionUtils.convertNumber(Double.valueOf(257), Byte.class));
		assertEquals(Byte.valueOf((byte) 1), ReflectionUtils.convertNumber(Double.valueOf(257.9999), Byte.class));

		assertEquals(null, ReflectionUtils.convertNumber(null, Byte.class));
	}

	@Test
	public void testGetMethod() {
		try {
			assertNotNull(ReflectionUtils.getMethod(Object.class, String.class, "toString"));
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		try {
			assertNotNull(ReflectionUtils.getMethod(Object.class, Object.class, "toString"));
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		try {
			assertNotNull(ReflectionUtils.getMethod(Object.class, "toString"));
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		try {
			assertNotNull(ReflectionUtils.getMethod(Object.class, Void.class, "toString"));
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		try {
			ReflectionUtils.getMethod(Object.class, Integer.class, "toString");
			// fail("Should throw IllegalArgumentException!");
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}
		// catch (IllegalArgumentException ok) {
		// }

		try {
			ReflectionUtils.getMethod(Object.class, Integer.class, "toString");
			// fail("Should throw IllegalArgumentException!");
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}
		// catch (IllegalArgumentException ok) {
		// }

		try {
			ReflectionUtils.getMethod(Object.class, "toString", int.class, Object.class, Integer.class);
			fail("Should throw NoSuchMethodException!");
		} catch (NoSuchMethodException ok) {
		}

		try {
			ReflectionUtils.getMethod(Object.class, "adsasdad");
			fail("Should throw NoSuchMethodException!");
		} catch (NoSuchMethodException ok) {
		}
	}

	@Test
	public void testGetGetter() {
		// Test when property is null
		try {
			ReflectionUtils.getGetter(ClassForTesting.class, Object.class, null);
			fail("Should throw IllegalArgumentException!");
		} catch (IllegalArgumentException ok) {
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		// Test when property is empty
		try {
			ReflectionUtils.getGetter(ClassForTesting.class, Object.class, "");
			fail("Should throw IllegalArgumentException!");
		} catch (IllegalArgumentException ok) {
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		// Test when field has no getter
		try {
			ReflectionUtils.getGetter(ClassForTesting.class, Object.class, "noAccess");
			fail("Should throw NoSuchMethodException!");
		} catch (NoSuchMethodException ok) {
		}

		// Test when there is no field at all
		try {
			ReflectionUtils.getGetter(ClassForTesting.class, Object.class, "zzzz");
			fail("Should throw NoSuchMethodException!");
		} catch (NoSuchMethodException ok) {
		}

		// Test has getter, but wrong return type specified
		try {
			ReflectionUtils.getGetter(ClassForTesting.class, String.class, "read");
			// fail("Should throw IllegalArgumentException!");
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}
		// catch (IllegalArgumentException ok) {
		// }

		// Test has getter, subclass of real return type specified
		try {
			assertNotNull(ReflectionUtils.getGetter(ClassForTesting.class, Object.class, "read"));
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		// Test has getter, return type OK
		try {
			ReflectiveMethod<ClassForTesting, Integer> getter =
					ReflectionUtils.getGetter(ClassForTesting.class, Integer.class, "read");
			assertNotNull(getter);
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		// Test has getter, return type OK, but it is "Boolean" and the method is starting with "is"
		try {
			ReflectiveMethod<ClassForTesting, Boolean> getter =
					ReflectionUtils.getGetter(ClassForTesting.class, Boolean.class, "readWriteB");
			assertNotNull(getter);
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		// Test has getter, return type OK, but it is "boolean" and the method is starting with "is"
		try {
			ReflectiveMethod<ClassForTesting, Boolean> getter =
					ReflectionUtils.getGetter(ClassForTesting.class, boolean.class, "readWriteB");
			assertNotNull(getter);
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		// Test private getter: will pass, but you will not be able to invoke the method
		// unless you call method.setAccessible(true)
		try {
			ReflectiveMethod<ClassForTesting, Integer> getter =
					ReflectionUtils.getGetter(ClassForTesting.class, int.class, "readWriteP");
			assertNotNull(getter);
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}

		// Test protected getter: will pass, but you will not be able to invoke the method
		// unless you call method.setAccessible(true)
		try {
			ReflectiveMethod<ClassForTesting, Integer> getter =
					ReflectionUtils.getGetter(ClassForTesting.class, int.class, "readWriteProtected");
			assertNotNull(getter);
		} catch (NoSuchMethodException e) {
			fail("Should not happen!");
		}
	}

	@SuppressWarnings("unused")
	private class ClassForTesting {
		private Integer noAccess;
		private Integer read;
		private Integer write;
		private Integer readWrite;

		private boolean readWriteB;

		private Integer readWriteP;
		private Integer readWriteProtected;

		public Integer getRead() {
			return read;
		}

		public void setWrite(Integer write) {
			this.write = write;
		}

		public void setReadWrite(Integer readWrite) {
			this.readWrite = readWrite;
		}

		public Integer getReadWrite() {
			return readWrite;
		}

		public void setReadWriteB(boolean readWriteB) {
			this.readWriteB = readWriteB;
		}

		public boolean isReadWriteB() {
			return readWriteB;
		}

		private void setReadWriteP(Integer readWriteP) {
			this.readWriteP = readWriteP;
		}

		private Integer getReadWriteP() {
			return readWriteP;
		}

		protected void setReadWriteProtected(Integer readWriteProtected) {
			this.readWriteProtected = readWriteProtected;
		}

		protected Integer getReadWriteProtected() {
			return readWriteProtected;
		}
	}
}
