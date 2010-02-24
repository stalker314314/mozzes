package org.mozzes.swing.mgf.datamodel;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.events.field.FieldEvent;
import org.mozzes.swing.mgf.datamodel.events.field.FieldEventListener;
import org.mozzes.swing.mgf.datamodel.events.field.ValueChangedEvent;


public class FieldTest {

	@Test
	public void testSetValue() {
		Field<DummyClass, Integer> field;
		field = new DummyField<DummyClass, Integer>(Integer.class);
		Hanlder listener = new Hanlder();
		field.addEventListener(listener);
		assertEquals(0, listener.valueChanged);
		field.setReadOnly(true);
		try {
			field.setValue(null, null);
			fail("UnsupportedOperationException should be thrown!");
		} catch (UnsupportedOperationException ok) {
		}
		field.setReadOnly(false);
		try {
			field.setValue(null, null);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {

		}
		assertEquals(0, listener.valueChanged);
	}

	@Test
	public void testFireEvent() {
		Field<DummyClass, Integer> field;

		field = new DummyField<DummyClass, Integer>(Integer.class);
		Hanlder listener = new Hanlder();
		field.addEventListener(listener);
		assertEquals(0, listener.totalEvents);
		field.fireEvent(new FieldEvent<DummyClass, Integer>() {
		});
		assertEquals(1, listener.totalEvents);
	}

	@Test
	public void testCompare() {
		Field<DummyClass, Integer> field = new IntField();
		assertEquals(true, field.isSortable());
		field.setSortable(false);
		assertEquals(false, field.isSortable());
		try {
			field.compare(null, null);
			fail("UnsupportedOperationException should be thrown because the field is not sortable!");
		} catch (UnsupportedOperationException ok) {
		}
		field.setSortable(true);
		assertEquals(true, field.isSortable());
		// Test null comparison
		assertEquals(0, field.compare(null, null));
		assertEquals(-1, field.compare(null, new DummyClass(1)));
		assertEquals(1, field.compare(new DummyClass(1), null));

		// Test Comparable
		assertEquals(0, field.compare(new DummyClass(1), new DummyClass(1)));
		assertEquals(-1, field.compare(new DummyClass(1), new DummyClass(2)));
		assertEquals(1, field.compare(new DummyClass(2), new DummyClass(1)));

		field.setComparator(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return (o1.equals(o2)) ? 0 : (o1 > o2 ? -1 : 1);
			}
		});

		// Test Comparator
		assertEquals(0, field.compare(new DummyClass(1), new DummyClass(1)));
		assertEquals(1, field.compare(new DummyClass(1), new DummyClass(2)));
		assertEquals(-1, field.compare(new DummyClass(2), new DummyClass(1)));

		// Test not comparable
		Field<DummyClass, DummyClass> field2 = new ClassField();
		field.setSortable(true);
		try {
			DummyClass dc1 = new DummyClass();
			DummyClass dc2 = new DummyClass();
			dc1.setDummyClassAttr(new DummyClass());
			dc2.setDummyClassAttr(new DummyClass());
			field2.compare(dc1, dc2);
			fail("UnsupportedOperationException should be thrown!");
		} catch (UnsupportedOperationException ok) {
		}
	}

	private static class ClassField extends Field<DummyClass, DummyClass> {
		public ClassField() {
			super(DummyClass.class);
		}

		@Override
		public DummyClass getValue(DummyClass object) {
			return object == null ? null : object.getDummyClassAttr();
		}

		@Override
		protected void valueSetter(DummyClass object, DummyClass value) {
			object.setDummyClassAttr(value);
		}
	}

	private static class IntField extends Field<DummyClass, Integer> {

		public IntField() {
			super(Integer.class);
		}

		@Override
		public Integer getValue(DummyClass object) {
			return object == null ? null : object.getIntAttr();
		}

		@Override
		protected void valueSetter(DummyClass object, Integer value) {
			object.setIntAttr(value);
		}
	}

	private static class DummyField<T, F> extends Field<T, F> {

		public DummyField(Class<F> fieldType) {
			super(fieldType);
		}

		@Override
		public F getValue(T object) {
			return null;
		}

		@Override
		protected void valueSetter(T object, F value) {
		}
	}

	private static class Hanlder implements FieldEventListener<DummyClass, Integer> {
		private int valueChanged = 0;
		private int totalEvents = 0;

		@Override
		public void handleModelFieldEvent(Field<DummyClass, Integer> field, FieldEvent<DummyClass, Integer> event) {
			totalEvents++;
			if (event instanceof ValueChangedEvent<?, ?>)
				valueChanged++;
		}
	}
}
