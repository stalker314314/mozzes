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
package org.mozzes.swing.mgf.datamodel.fields;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.fields.StaticDataField;


public class StaticDataFieldTest {

	@Test
	public void testGetValue() {
		Field<Object, Integer> field = new StaticDataField<Object, Integer>(Integer.class, 10);
		assertEquals(Integer.valueOf(10), field.getValue(null));
		assertEquals(Integer.valueOf(10), field.getValue(new DummyClass()));
	}

	@Test
	public void testValueSetter() {
		Field<Object, Integer> field = new StaticDataField<Object, Integer>(Integer.class, 10);
		assertEquals(Integer.valueOf(10), field.getValue(null));
		assertEquals(Integer.valueOf(10), field.getValue(new DummyClass()));
		field.setReadOnly(false);
		field.setValue(null, 11);
		assertEquals(Integer.valueOf(11), field.getValue(null));
		assertEquals(Integer.valueOf(11), field.getValue(new DummyClass()));
	}
}
