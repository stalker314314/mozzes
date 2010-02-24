package org.mozzes.swing.mgf.datamodel;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.events.DataModelEvent;
import org.mozzes.swing.mgf.datamodel.events.DataModelEventListener;
import org.mozzes.swing.mgf.datamodel.events.FieldAddedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldMovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldRemovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldValueUpdatedEvent;
import org.mozzes.swing.mgf.datamodel.fields.StaticDataField;
import org.mozzes.swing.mgf.datamodel.impl.DefaultDataModel;


public class DataModelTest {
	@Test
	public void testAddFieldFieldOfTQ() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		Handler listener = new Handler();
		model.addEventListener(listener);

		assertEquals(0, model.getFields().size());

		model.addField(new StaticDataField<DummyClass, String>(String.class, ""));
		assertEquals(1, model.getFields().size());

		model.addField(new StaticDataField<DummyClass, String>(String.class, ""));
		assertEquals(2, model.getFields().size());

		try {
			model.addField(null);
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		model.addField(new StaticDataField<DummyClass, String>("someName", String.class, ""));
		try {
			model.addField(new StaticDataField<DummyClass, String>("someName", String.class, ""));
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}
		assertEquals(3, listener.fieldAddedEvent);
	}

	@Test
	public void testAddFieldIntFieldOfTQ() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		Handler listener = new Handler();
		model.addEventListener(listener);

		assertEquals(0, model.getFields().size());

		try {
			model.addField(-1, new StaticDataField<DummyClass, String>(String.class, ""));
			fail("IndexOutOfBoundsException has to be thrown");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			model.addField(1, new StaticDataField<DummyClass, String>(String.class, ""));
			fail("IndexOutOfBoundsException has to be thrown");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}

		try {
			model.addField(0, null);
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		model.addField(0, new StaticDataField<DummyClass, String>(String.class, "0"));
		assertEquals(new StaticDataField<DummyClass, String>(String.class, "0"), model.getFields().get(0));

		model.addField(0, new StaticDataField<DummyClass, String>(String.class, "new 0"));
		assertEquals(new StaticDataField<DummyClass, String>(String.class, "new 0"), model.getFields().get(0));

		assertEquals(2, listener.fieldAddedEvent);
	}

	@Test
	public void testRemoveFieldFieldOfTQ() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		Handler listener = new Handler();
		model.addEventListener(listener);
		assertEquals(0, model.getFields().size());

		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 1));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 2));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 3));

		assertEquals(3, model.getFields().size());
		assertTrue(model.removeField(new StaticDataField<DummyClass, Integer>(Integer.class, 1)));
		assertFalse(model.removeField(new StaticDataField<DummyClass, Integer>(Integer.class, 1)));
		assertFalse(model.removeField(null));
		assertEquals(2, model.getFields().size());

		assertEquals(1, listener.fieldRemovedEvent);
	}

	@Test
	public void testRemoveFieldInt() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		Handler listener = new Handler();
		model.addEventListener(listener);
		assertEquals(0, model.getFields().size());

		try {
			model.removeField(-1);
			fail("IndexOutOfBoundsException has to be thrown");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			model.removeField(0);
			fail("IndexOutOfBoundsException has to be thrown");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}

		model.addField(new StaticDataField<DummyClass, String>(String.class, "0"));
		assertEquals(new StaticDataField<DummyClass, String>(String.class, "0"), model.removeField(0));

		assertEquals(0, model.getFields().size());

		model.addField(new StaticDataField<DummyClass, String>(String.class, "0"));
		model.addField(new StaticDataField<DummyClass, String>(String.class, "new 0"));
		assertEquals(new StaticDataField<DummyClass, String>(String.class, "new 0"), model.removeField(1));

		assertEquals(1, model.getFields().size());
		assertEquals(2, listener.fieldRemovedEvent);
	}

	@Test
	public void testMoveFieldIntInt() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		Handler listener = new Handler();
		model.addEventListener(listener);
		assertEquals(0, model.getFields().size());

		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 0));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 1));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 2));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 3));

		try {
			model.moveField(-1, 1);
			fail("IndexOutOfBoundsException has to be thrown");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			model.moveField(4, 1);
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			model.moveField(0, -1);
			fail("IndexOutOfBoundsException has to be thrown");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		try {
			model.moveField(0, 4);
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}

		// 0 1 2 3 => 1 2 3 0
		model.moveField(0, 3);
		assertEquals(1, model.getFieldValue(0, null));
		assertEquals(2, model.getFieldValue(1, null));
		assertEquals(3, model.getFieldValue(2, null));
		assertEquals(0, model.getFieldValue(3, null));

		// 1 2 3 0 => 0 1 2 3
		model.moveField(3, 0);
		assertEquals(0, model.getFieldValue(0, null));
		assertEquals(1, model.getFieldValue(1, null));
		assertEquals(2, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		// 0 1 2 3 => 1 2 0 3
		model.moveField(0, 2);
		assertEquals(1, model.getFieldValue(0, null));
		assertEquals(2, model.getFieldValue(1, null));
		assertEquals(0, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		// 1 2 0 3 => 0 1 2 3
		model.moveField(2, 0);
		assertEquals(0, model.getFieldValue(0, null));
		assertEquals(1, model.getFieldValue(1, null));
		assertEquals(2, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		// 0 1 2 3 => 0 2 1 3
		model.moveField(1, 2);
		assertEquals(0, model.getFieldValue(0, null));
		assertEquals(2, model.getFieldValue(1, null));
		assertEquals(1, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		// 0 2 1 3 => 0 1 2 3
		model.moveField(1, 2);
		assertEquals(0, model.getFieldValue(0, null));
		assertEquals(1, model.getFieldValue(1, null));
		assertEquals(2, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		assertEquals(6, listener.fieldMovedEvent);
	}

	@Test
	public void testMoveFieldFieldOfTQInt() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		Handler listener = new Handler();
		model.addEventListener(listener);
		assertEquals(0, model.getFields().size());

		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 0));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 1));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 2));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 3));

		try {
			model.moveField(null, -1);
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		try {
			model.moveField(null, -1);
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		try {
			model.moveField(new StaticDataField<DummyClass, Integer>(Integer.class, 11), -1);
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		try {
			model.moveField(new StaticDataField<DummyClass, Integer>(Integer.class, 0), -1);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		// 0 1 2 3 => 1 2 3 0
		model.moveField(new StaticDataField<DummyClass, Integer>(Integer.class, 0), 3);
		assertEquals(1, model.getFieldValue(0, null));
		assertEquals(2, model.getFieldValue(1, null));
		assertEquals(3, model.getFieldValue(2, null));
		assertEquals(0, model.getFieldValue(3, null));

		// 1 2 3 0 => 0 1 2 3
		model.moveField(new StaticDataField<DummyClass, Integer>(Integer.class, 0), 0);
		assertEquals(0, model.getFieldValue(0, null));
		assertEquals(1, model.getFieldValue(1, null));
		assertEquals(2, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		// 0 1 2 3 => 1 2 0 3
		model.moveField(new StaticDataField<DummyClass, Integer>(Integer.class, 0), 2);
		assertEquals(1, model.getFieldValue(0, null));
		assertEquals(2, model.getFieldValue(1, null));
		assertEquals(0, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		// 1 2 0 3 => 0 1 2 3
		model.moveField(new StaticDataField<DummyClass, Integer>(Integer.class, 0), 0);
		assertEquals(0, model.getFieldValue(0, null));
		assertEquals(1, model.getFieldValue(1, null));
		assertEquals(2, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		// 0 1 2 3 => 0 2 1 3
		model.moveField(new StaticDataField<DummyClass, Integer>(Integer.class, 1), 2);
		assertEquals(0, model.getFieldValue(0, null));
		assertEquals(2, model.getFieldValue(1, null));
		assertEquals(1, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		// 0 2 1 3 => 0 1 2 3
		model.moveField(new StaticDataField<DummyClass, Integer>(Integer.class, 2), 2);
		assertEquals(0, model.getFieldValue(0, null));
		assertEquals(1, model.getFieldValue(1, null));
		assertEquals(2, model.getFieldValue(2, null));
		assertEquals(3, model.getFieldValue(3, null));

		assertEquals(6, listener.fieldMovedEvent);
	}

	@Test
	public void testGetFieldInt() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		assertEquals(0, model.getFields().size());

		try {
			model.getField(-1);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		try {
			model.getField(0);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 0));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 1));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 2));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 3));

		assertEquals(new StaticDataField<DummyClass, Integer>(Integer.class, 0), model.getField(0));
		assertEquals(new StaticDataField<DummyClass, Integer>(Integer.class, 1), model.getField(1));
		assertEquals(new StaticDataField<DummyClass, Integer>(Integer.class, 2), model.getField(2));
		assertEquals(new StaticDataField<DummyClass, Integer>(Integer.class, 3), model.getField(3));

	}

	@Test
	public void testGetFieldIntClassOfF() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		assertEquals(0, model.getFields().size());

		try {
			model.getField(-1, Object.class);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		try {
			model.getField(0, Object.class);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 0));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 1));

		// Wrong class test
		try {
			model.getField(0, DummyClass.class);
			fail("ClassCastException should be thrown!");
		} catch (ClassCastException ok) {
		}
		// All OK test
		assertEquals(new StaticDataField<DummyClass, Integer>(Integer.class, 1), model.getField(1, Integer.class));
	}

	@Test
	public void testGetFieldValueIntT() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		assertEquals(0, model.getFields().size());

		try {
			model.getFieldValue(-1, null);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		try {
			model.getFieldValue(0, null);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 0));

		assertFalse(model.getFieldValue(0, null).equals(new DummyClass()));
		// All OK test
		assertEquals(0, model.getFieldValue(0, null));
	}

	@Test
	public void testGetFieldValueIntTClassOfF() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		assertEquals(0, model.getFields().size());

		try {
			model.getFieldValue(-1, null, Object.class);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		try {
			model.getFieldValue(0, null, Object.class);
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}

		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 0));
		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 1));

		// Wrong class test
		try {
			model.getFieldValue(0, null, DummyClass.class);
			fail("ClassCastException should be thrown!");
		} catch (ClassCastException ok) {
		}

		// All OK test
		assertEquals(Integer.valueOf(1), model.getFieldValue(1, null, Integer.class));
	}

	@Test
	public void testGetFields() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();

		assertNotNull(model.getFields());

		ArrayList<Field<DummyClass, ?>> fields = new ArrayList<Field<DummyClass, ?>>();
		assertEquals(fields, model.getFields());

		Field<DummyClass, ?> field = new StaticDataField<DummyClass, Integer>(Integer.class, 0);
		fields.add(field);
		model.addField(field);
		assertEquals(fields, model.getFields());

		try {
			model.getFields().add(field);
			fail("UnsupportedOperationException has to be thrown!");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			model.getFields().remove(field);
			fail("UnsupportedOperationException has to be thrown!");
		} catch (UnsupportedOperationException ok) {
		}
	}

	@Test
	public void testGetFieldsNumber() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		assertEquals(model.getFields().size(), model.getFieldsNumber());
		Field<DummyClass, ?> field = new StaticDataField<DummyClass, Integer>(Integer.class, 0);
		model.addField(field);
		assertEquals(model.getFields().size(), model.getFieldsNumber());
	}

	@Test
	public void testEventPropagation() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		Handler listener = new Handler();
		model.addEventListener(listener);

		model.addField(new StaticDataField<DummyClass, Integer>(Integer.class, 10));
		model.getField(0).setReadOnly(false);
		model.getField(0).setValue(null, null);
		model.getField(0).setValue(null, null);
		assertEquals(2, listener.fieldValueUpdatedEvent);
	}

	public static class Handler implements DataModelEventListener<DummyClass> {
		private int fieldAddedEvent = 0;
		private int fieldRemovedEvent = 0;
		private int fieldMovedEvent = 0;
		private int fieldValueUpdatedEvent = 0;

		@Override
		public void handleDataModelEvent(DataModel<DummyClass> model, DataModelEvent<DummyClass> event) {
			if (event instanceof FieldAddedEvent<?>)
				fieldAddedEvent++;
			if (event instanceof FieldRemovedEvent<?>)
				fieldRemovedEvent++;
			if (event instanceof FieldMovedEvent<?>)
				fieldMovedEvent++;
			if (event instanceof FieldValueUpdatedEvent<?>)
				fieldValueUpdatedEvent++;
		}
	}

	@Test
	public void testGetFieldString() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		assertEquals(0, model.getFields().size());

		try {
			model.getField(null);
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		try {
			model.getField("");
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		model.addField(new StaticDataField<DummyClass, Integer>("a", Integer.class, 0));
		model.addField(new StaticDataField<DummyClass, Integer>("b", Integer.class, 1));
		model.addField(new StaticDataField<DummyClass, Integer>("c", Integer.class, 2));
		model.addField(new StaticDataField<DummyClass, Integer>("d", Integer.class, 3));

		assertEquals(new StaticDataField<DummyClass, Integer>("a", Integer.class, 0), model.getField("a"));
		assertEquals(new StaticDataField<DummyClass, Integer>("b", Integer.class, 1), model.getField("b"));
		assertEquals(new StaticDataField<DummyClass, Integer>("c", Integer.class, 2), model.getField("c"));
		assertEquals(new StaticDataField<DummyClass, Integer>("d", Integer.class, 3), model.getField("d"));

		try {
			model.getField("zz");
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}
	}

	@Test
	public void testGetFieldStringClassOfF() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		assertEquals(0, model.getFields().size());

		try {
			model.getField(null);
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		try {
			model.getField("");
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		model.addField(new StaticDataField<DummyClass, Integer>("a", Integer.class, 0));
		model.addField(new StaticDataField<DummyClass, Integer>("b", Integer.class, 1));
		model.addField(new StaticDataField<DummyClass, Integer>("c", Integer.class, 2));
		model.addField(new StaticDataField<DummyClass, Integer>("d", Integer.class, 3));

		assertEquals(new StaticDataField<DummyClass, Integer>("a", Integer.class, 0),
				model.getField("a", Integer.class));
		assertEquals(new StaticDataField<DummyClass, Integer>("b", Integer.class, 1),
				model.getField("b", Integer.class));
		assertEquals(new StaticDataField<DummyClass, Integer>("c", Integer.class, 2),
				model.getField("c", Integer.class));
		assertEquals(new StaticDataField<DummyClass, Integer>("d", Integer.class, 3),
				model.getField("d", Integer.class));

		try {
			model.getField("zz");
			fail("IllegalArgumentException has to be thrown!");
		} catch (IllegalArgumentException ok) {
		}
	}
}
