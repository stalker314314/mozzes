package org.mozzes.utils;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;

import org.junit.Test;
import org.mozzes.utils.JavaUtils;

/**
 * Tests the StringUtils class
 * 
 * @author milos
 */
public class JavaUtilsTest {

	/**
	 * Tests {@link JavaUtils#getArrayForVarArgs(Object[])}
	 */
	@Test
	public void testGetArrayForVarArgs() {
		// Primitive type Array
		assertEquals(1, JavaUtils.getArrayForVarArgs(new Object[] { 1 }).length);
		assertEquals(2, JavaUtils.getArrayForVarArgs(new Object[] { 1, 2 }).length);

		// Array with Collection
		ArrayList<Integer> collection = new ArrayList<Integer>();
		collection.add(1);
		assertEquals(1, JavaUtils.getArrayForVarArgs(new Object[] { collection }).length);
		collection.add(1);
		assertEquals(2, JavaUtils.getArrayForVarArgs(new Object[] { collection }).length);
	}

	@Test
	public void testGetDeepCopyTest() {
		DummyClass d;
		DummyClass dCopy;

		d = new DummyClass();
		dCopy = JavaUtils.getDeepCopy(d);
		assertEquals(d, dCopy);
		assertFalse(d == dCopy);

		d = new DummyClass();
		d.setInnerDummyClass(new DummyClass());
		dCopy = JavaUtils.getDeepCopy(d);
		assertEquals(d, dCopy);
		assertFalse(d == dCopy);
		assertFalse(d.getInnerDummyClass() == dCopy.getInnerDummyClass());

		d = new DummyClass();
		d.setInnerDummyClass(new DummyClass());
		d.innerDummyClass.setInnerDummyClass(new DummyClass());
		dCopy = JavaUtils.getDeepCopy(d);
		assertEquals(d, dCopy);
		assertFalse(d == dCopy);
		assertFalse(d.getInnerDummyClass() == dCopy.getInnerDummyClass());
		assertFalse(d.getInnerDummyClass().getInnerDummyClass() == dCopy.getInnerDummyClass().getInnerDummyClass());
}

	private static class DummyClass implements Serializable {
		private static final long serialVersionUID = 1L;

		private DummyClass innerDummyClass;
		private Integer someAttr;

		public void setInnerDummyClass(DummyClass innerDummyClass) {
			this.innerDummyClass = innerDummyClass;
		}

		public DummyClass getInnerDummyClass() {
			return innerDummyClass;
		}

		@SuppressWarnings("unused")
		public void setSomeAttr(Integer someAttr) {
			this.someAttr = someAttr;
		}

		@SuppressWarnings("unused")
		public Integer getSomeAttr() {
			return someAttr;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((innerDummyClass == null) ? 0 : innerDummyClass.hashCode());
			result = prime * result + ((someAttr == null) ? 0 : someAttr.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof DummyClass))
				return false;
			DummyClass other = (DummyClass) obj;
			if (innerDummyClass == null) {
				if (other.innerDummyClass != null)
					return false;
			} else if (!innerDummyClass.equals(other.innerDummyClass))
				return false;
			if (someAttr == null) {
				if (other.someAttr != null)
					return false;
			} else if (!someAttr.equals(other.someAttr))
				return false;
			return true;
		}
	}
}
