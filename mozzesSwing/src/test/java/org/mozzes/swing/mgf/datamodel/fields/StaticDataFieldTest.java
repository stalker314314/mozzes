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
