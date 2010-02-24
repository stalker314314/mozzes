package org.mozzes.swing.mgf.datarenderer.table;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datamodel.impl.DefaultDataModel;
import org.mozzes.swing.mgf.datarenderer.table.CustomTableModel;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultListDataSource;


public class CustomTableModelTest {

	@Test
	public void testIsCellEditable() {
		CustomTableModel<DummyClass> atm = setupTableModel();
		assertTrue(atm.isCellEditable(0, 0));
		assertFalse(atm.isCellEditable(0, 1));
		assertFalse(atm.isCellEditable(0, 3)); // Default value false
	}

	private CustomTableModel<DummyClass> setupTableModel() {
		List<DummyClass> data = new ArrayList<DummyClass>();
		data.add(new DummyClass(1));
		data.add(new DummyClass(2));
		data.add(new DummyClass(3));
		data.add(new DummyClass(4, new DummyClass()));
		data.add(new DummyClass(5, new DummyClass(50)));
		data.add(new DummyClass(6, new DummyClass(61, new DummyClass(62))));
		ListDataSource<DummyClass> dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);

		DataModel<DummyClass> dataModel = new DefaultDataModel<DummyClass>();
		dataModel.addField(new PropertyField<DummyClass, Integer>(Integer.class, "intAttr"));
		dataModel.addField(new PropertyField<DummyClass, DummyClass>(DummyClass.class, "dummyClassAttr"));
		dataModel.addField(new PropertyField<DummyClass, Integer>(Integer.class, "dummyClassAttr.intAttr"));
		dataModel.addField(new PropertyField<DummyClass, Integer>(Integer.class,
				"dummyClassAttr.dummyClassAttr.intAttr"));

		dataModel.getField(0).setReadOnly(false);
		dataModel.getField(1).setReadOnly(true);
		dataModel.getField(2).setReadOnly(false);

		dataModel.getField(0).setHeader("");
		dataModel.getField(1).setHeader("some title");

		return new CustomTableModel<DummyClass>(dataModel, dataSource);
	}

	@Test
	public void testGetColumnNameInt() {
		CustomTableModel<DummyClass> atm = setupTableModel();
		assertEquals("", atm.getColumnName(0));
		assertEquals("some title", atm.getColumnName(1));
		assertEquals("C", atm.getColumnName(2));
		assertEquals("D", atm.getColumnName(3));
	}

	@Test
	public void testGetColumnCount() {
		CustomTableModel<DummyClass> atm = setupTableModel();
		assertEquals(4, atm.getColumnCount());
	}

	@Test
	public void testGetRowCount() {
		CustomTableModel<DummyClass> atm = setupTableModel();
		assertEquals(6, atm.getRowCount());
	}

	@Test
	public void testGetValueAt() {
		CustomTableModel<DummyClass> atm = setupTableModel();
		assertEquals(1, atm.getValueAt(0, 0));
		assertEquals(new DummyClass(), atm.getValueAt(3, 1));
	}

	@Test
	public void testSetValueAtObjectIntInt() {
		CustomTableModel<DummyClass> atm = setupTableModel();
		atm.setValueAt(10, 0, 0);
		assertEquals(10, atm.getValueAt(0, 0));

		try {
			atm.setValueAt(new DummyClass(), 0, 0);
			fail("ClassCastException should be thrown!");
		} catch (ClassCastException ok) {
		}

		try {
			atm.setValueAt(new DummyClass(), 0, 1);
			fail("UnsupportedOperationException should be thrown!");
		} catch (UnsupportedOperationException ok) {
		}

		try {
			atm.setValueAt(5, 0, 2);
			fail("UnsupportedOperationException should be thrown!");
		} catch (UnsupportedOperationException ok) {
		}
	}

	@Test
	public void testGetColumnClassInt() {
		CustomTableModel<DummyClass> atm = setupTableModel();
		assertEquals(Integer.class, atm.getColumnClass(0));
		assertEquals(DummyClass.class, atm.getColumnClass(1));
		assertEquals(Integer.class, atm.getColumnClass(2));
		assertEquals(Integer.class, atm.getColumnClass(3));
	}

}
