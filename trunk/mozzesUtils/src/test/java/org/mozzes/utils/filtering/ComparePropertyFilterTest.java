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
package org.mozzes.utils.filtering;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.junit.Test;
import org.mozzes.utils.filtering.ComparePropertyFilter;

public class ComparePropertyFilterTest {

	@Test
	public void testComparePropertyFilterStringPropertyType() {
		// Valid propertyName (should result in successful creation)
		try {
			new CPFImpl<DummyClass, Integer>("testAttribute", null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		// Null propertyName (should result in IllegalArgumentException)
		try {
			new CPFImpl<DummyClass, Integer>(null, null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		// Empty propertyName (should result in IllegalArgumentException)
		try {
			new CPFImpl<DummyClass, Integer>("", null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}

		// Invalid propertyName (should result in successful creation, but will break later)
		try {
			new CPFImpl<DummyClass, Integer>("aa", null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testCompareProperty() {
		ComparePropertyFilter<DummyClass, Integer> pf;
		DummyClass object = new DummyClass();

		// Without comparator

		// Valid propertyName, null property, null compare value
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", null);
		assertEquals(0, pf.compareProperty(object));

		// Valid propertyName, not null property, null compare value
		object.setTestAttribute(13);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", null);
		assertEquals(1, pf.compareProperty(object));

		// Valid propertyName, not null property, not null compare value
		object.setTestAttribute(13);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", 14);
		assertEquals(-1, pf.compareProperty(object));

		// Valid propertyName, not null property, not null compare value
		object.setTestAttribute(13);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", 13);
		assertEquals(0, pf.compareProperty(object));

		// Valid propertyName, not null property, not null compare value
		object.setTestAttribute(13);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", 12);
		assertEquals(1, pf.compareProperty(object));

		// Valid propertyName, null property, not null compare value
		object.setTestAttribute(null);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", 15);
		assertEquals(-1, pf.compareProperty(object));

		// With comparator

		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return (int) Math.signum(o2 - o1);
			}
		};

		// Valid propertyName, null property, null compare value
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", null, comparator);
		assertEquals(0, pf.compareProperty(object));

		// Valid propertyName, not null property, null compare value
		object.setTestAttribute(13);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", null, comparator);
		assertEquals(1, pf.compareProperty(object));

		// Valid propertyName, not null property, not null compare value
		object.setTestAttribute(13);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", 14, comparator);
		assertEquals(1, pf.compareProperty(object));

		// Valid propertyName, not null property, not null compare value
		object.setTestAttribute(13);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", 13, comparator);
		assertEquals(0, pf.compareProperty(object));

		// Valid propertyName, not null property, not null compare value
		object.setTestAttribute(13);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", 12, comparator);
		assertEquals(-1, pf.compareProperty(object));

		// Valid propertyName, null property, not null compare value
		object.setTestAttribute(null);
		pf = new CPFImpl<DummyClass, Integer>("testAttribute", 15, comparator);
		assertEquals(-1, pf.compareProperty(object));

		// Invalid propertyName (Should result in RuntimeException)
		pf = new CPFImpl<DummyClass, Integer>("aa", null);
		try {
			pf.compareProperty(object);
			fail();
		} catch (RuntimeException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	private static class CPFImpl<ObjectType, PropertyType> extends ComparePropertyFilter<ObjectType, PropertyType> {

		public CPFImpl(String propertyName, PropertyType compareValue) {
			super(propertyName, compareValue);
		}

		public CPFImpl(String propertyName, PropertyType compareValue, Comparator<PropertyType> comparator) {
			super(propertyName, compareValue, comparator);
		}

		@Override
		public boolean isAcceptable(ObjectType object) {
			return false;
		}
	}
}
