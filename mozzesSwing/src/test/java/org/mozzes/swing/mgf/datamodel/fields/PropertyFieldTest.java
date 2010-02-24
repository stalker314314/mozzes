package org.mozzes.swing.mgf.datamodel.fields;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;


public class PropertyFieldTest {

	@Test
	public void testGetValue() {
		// Field test
		Field<DummyClass, Integer> field1 = new PropertyField<DummyClass, Integer>(Integer.class, "intAttr");
		assertEquals(null, field1.getValue(null));
		assertEquals(null, field1.getValue(new DummyClass()));
		assertEquals(Integer.valueOf(10), field1.getValue(new DummyClass(10)));

		// Nesting test1
		field1 = new PropertyField<DummyClass, Integer>(Integer.class, "dummyClassAttr.intAttr");
		assertEquals(null, field1.getValue(null));
		assertEquals(null, field1.getValue(new DummyClass()));
		assertEquals(Integer.valueOf(10), field1.getValue(new DummyClass(new DummyClass(10))));

		// Nesting test2
		field1 = new PropertyField<DummyClass, Integer>(Integer.class, "dummyClassAttr.dummyClassAttr.intAttr");
		assertEquals(null, field1.getValue(null));
		assertEquals(null, field1.getValue(new DummyClass(new DummyClass())));
		assertEquals(Integer.valueOf(10), field1.getValue(new DummyClass(new DummyClass(new DummyClass(10)))));

		// Wrong property type test
		field1 = new PropertyField<DummyClass, Integer>(Integer.class, "dummyClassAttr");
		try {
			field1.getValue(new DummyClass(new DummyClass()));
			fail("ClassCastException should be thrown!");
		} catch (ClassCastException ok) {
		}

		// No such property test
		field1 = new PropertyField<DummyClass, Integer>(Integer.class, "test");
		try {
			field1.getValue(new DummyClass(new DummyClass()));
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		Field<DummyClass, DummyClass> field2 = new PropertyField<DummyClass, DummyClass>(DummyClass.class,
				"dummyClassAttr");
		assertEquals(null, field2.getValue(null));
		assertEquals(null, field2.getValue(new DummyClass()));
		assertEquals(new DummyClass(10), field2.getValue(new DummyClass(new DummyClass(10))));
	}

	@Test
	public void testValueSetter() {
		// Field test
		Field<DummyClass, Integer> field = new PropertyField<DummyClass, Integer>(Integer.class, "intAttr");
		DummyClass object = new DummyClass();
		field.setReadOnly(false);
		field.setValue(object, 10);
		assertEquals(Integer.valueOf(10), object.getIntAttr());

		// Nesting test
		field = new PropertyField<DummyClass, Integer>(Integer.class, "dummyClassAttr.intAttr");
		object = new DummyClass(new DummyClass());
		field.setReadOnly(false);
		field.setValue(object, 10);
		assertEquals(Integer.valueOf(10), object.getDummyClassAttr().getIntAttr());

		// Nesting test (nested object null)
		field = new PropertyField<DummyClass, Integer>(Integer.class, "dummyClassAttr.intAttr");
		object = new DummyClass();
		field.setReadOnly(false);
		try {
			field.setValue(object, 10);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		// No such property test
		field = new PropertyField<DummyClass, Integer>(Integer.class, "test");
		object = new DummyClass();
		field.setReadOnly(false);
		try {
			field.setValue(object, 10);
			fail("IllegalArgumentException should be thorwn!");
		} catch (IllegalArgumentException ok) {
		}

		// Wrong property type test
		field = new PropertyField<DummyClass, Integer>(Integer.class, "dummyClassAttr");
		object = new DummyClass();
		field.setReadOnly(false);
		try {
			field.setValue(object, 10);
			fail("ClassCastException has to be thrown!");
		} catch (ClassCastException ok) {
		}

		// No setter method test
		field = new PropertyField<DummyClass, Integer>(Integer.class, "noSetter");
		object = new DummyClass();
		field.setReadOnly(false);
		try {
			field.setValue(object, 10);
			fail("IllegalArgumentException should be thorwn!");
		} catch (IllegalArgumentException ok) {
		}
	}

	@Test
	public void testSetReadOnly() {
		// No setter method test
		PropertyField<DummyClass, Integer> field = new PropertyField<DummyClass, Integer>(Integer.class, "noSetter");
		field.isEditableFor(new DummyClass());
		try {
			field.setReadOnly(false);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
	}

	@Test
	public void testValueSetterPrimitiveTypes() {
		// Field test
		Field<DummyClass, Boolean> field = new PropertyField<DummyClass, Boolean>(Boolean.class, "primitiveBool");
		DummyClass object = new DummyClass();
		field.setReadOnly(false);

		field.setValue(object, true);
		assertTrue(object.isPrimitiveBool());

		field.setValue(object, false);
		assertFalse(object.isPrimitiveBool());

		field.setValue(object, null);
		assertFalse(object.isPrimitiveBool());
	}
}