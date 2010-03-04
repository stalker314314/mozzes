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
package org.mozzes.swing.mgf.datarenderer.combobox;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datamodel.fields.StaticDataField;
import org.mozzes.swing.mgf.datamodel.fields.WholeObjectField;
import org.mozzes.swing.mgf.datamodel.impl.DefaultDataModel;
import org.mozzes.swing.mgf.datarenderer.combobox.CustomComboBoxModel;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.SelectionMode;


public class CustomComboBoxModelTest {
	private final SelectionListDataSource<DummyClass> dataSource = new DefaultSelectionListDataSource<DummyClass>(
			DummyClass.class, new ArrayList<DummyClass>() {
private static final long serialVersionUID = 1L;

{
	add(new DummyClass(10));
	add(new DummyClass(11));
	add(new DummyClass(12));
}
});

	@Test
	public void testConstruction() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		try {
			new CustomComboBoxModel<DummyClass>(model);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException e) {
		}
		model.addField(new StaticDataField<DummyClass, String>(String.class, null));
		model.addField(new StaticDataField<DummyClass, String>(String.class, null));
		try {
			new CustomComboBoxModel<DummyClass>(model);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException e) {
		}

		model = new DefaultDataModel<DummyClass>();
		model.addField(new PropertyField<DummyClass, String>(String.class, "strAttr"));
		new CustomComboBoxModel<DummyClass>(model);
	}

	@Test
	public void testGetElementAt() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		model.addField(new PropertyField<DummyClass, String>(String.class, "strAttr"));

		CustomComboBoxModel<DummyClass> clm = new CustomComboBoxModel<DummyClass>(model);
		try {
			clm.getElementAt(0);
			fail("IllegalStateException should be thrown!");
		} catch (IllegalStateException ok) {
		}
		clm.setDataSource(dataSource);
		try {
			clm.getElementAt(100);
			fail("IndexOutOfBoundsException should be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			clm.getElementAt(-1);
			fail("IndexOutOfBoundsException should be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}
		assertEquals(model.getField(0).getValue(dataSource.get(0)), clm.getElementAt(0));
	}

	@Test
	public void testGetSize() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		model.addField(new PropertyField<DummyClass, String>(String.class, "strAttr"));

		CustomComboBoxModel<DummyClass> clm = new CustomComboBoxModel<DummyClass>(model);
		assertEquals(0, clm.getSize());
		clm.setDataSource(dataSource);
		assertEquals(dataSource.getSize(), clm.getSize());
	}

	@Test
	public void testSetDataSource() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		model.addField(new PropertyField<DummyClass, String>(String.class, "strAttr"));

		CustomComboBoxModel<DummyClass> clm = new CustomComboBoxModel<DummyClass>(model);
		dataSource.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		try {
			clm.setDataSource(dataSource);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		dataSource.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		clm.setDataSource(dataSource);
	}

	@Test
	public void testSelection() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		model.addField(new PropertyField<DummyClass, Integer>(Integer.class, "intAttr"));

		CustomComboBoxModel<DummyClass> clm = new CustomComboBoxModel<DummyClass>(model);
		assertEquals(0, clm.getSize());

		clm.setDataSource(dataSource);
		assertEquals(dataSource.getSize(), clm.getSize());

		dataSource.setSelectedIndices(0);
		assertEquals(model.getFieldValue(0, dataSource.getSelectedValue()), clm.getSelectedItem());

		dataSource.clearSelection();
		assertEquals(null, clm.getSelectedItem());

		clm.setSelectedItem(model.getFieldValue(0, dataSource.get(0)));
		assertEquals(model.getFieldValue(0, dataSource.getSelectedValue()), clm.getSelectedItem());

		clm.setSelectedItem(null);
		assertEquals(null, clm.getSelectedItem());
		assertEquals(null, dataSource.getSelectedValue());

		try {
			clm.setSelectedItem("adasd");
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		try {
			clm.setSelectedItem(new DummyClass());
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
	}

	@Test
	public void testObjectSelection() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		// model.addField(new PropertyField<DummyClass, Integer>(Integer.class, "intAttr"));
		model.addField(new WholeObjectField<DummyClass>(DummyClass.class));

		CustomComboBoxModel<DummyClass> clm = new CustomComboBoxModel<DummyClass>(model);
		assertEquals(0, clm.getSize());

		clm.setDataSource(dataSource);
		assertEquals(dataSource.getSize(), clm.getSize());

		dataSource.setSelectedIndices(0);
		assertEquals(dataSource.getSelectedValue(), clm.getSelectedItem());

		dataSource.clearSelection();
		assertEquals(null, clm.getSelectedItem());

		clm.setSelectedItem(dataSource.get(1));
		assertEquals(dataSource.getSelectedValue(), clm.getSelectedItem());

		clm.setSelectedItem(null);
		assertEquals(null, clm.getSelectedItem());
		assertEquals(null, dataSource.getSelectedValue());

		clm.setSelectedItem(model.getFieldValue(0, dataSource.get(0)));
		assertEquals(dataSource.getSelectedValue(), clm.getSelectedItem());

		// try {
		// clm.setSelectedItem(model.getFieldValue(0, dataSource.get(0)));
		// fail("IllegalArgumentException should be thrown!");
		// } catch (IllegalArgumentException ok) {
		// }

		try {
			clm.setSelectedItem(Integer.valueOf(111));
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		try {
			clm.setSelectedItem(new DummyClass());
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
	}
}
