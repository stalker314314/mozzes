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
package org.mozzes.swing.mgf.datarenderer.list;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datamodel.fields.StaticDataField;
import org.mozzes.swing.mgf.datamodel.impl.DefaultDataModel;
import org.mozzes.swing.mgf.datarenderer.list.CustomListModel;
import org.mozzes.swing.mgf.datarenderer.list.ListRenderModel;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedAdapter;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedEvent;
import org.mozzes.swing.mgf.datasource.impl.DefaultListDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.SelectionMode;


public class CustomListModelTest {
	private final ListDataSource<DummyClass> dataSource = new DefaultListDataSource<DummyClass>(
			DummyClass.class, new ArrayList<DummyClass>() {
		private static final long serialVersionUID = 1L;

		{
			add(new DummyClass());
		}
	});

	@Test
	public void testConstruction() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		try {
			new CustomListModel<DummyClass>(model);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException e) {
		}
		model.addField(new StaticDataField<DummyClass, String>(String.class, null));
		model.addField(new StaticDataField<DummyClass, String>(String.class, null));
		try {
			new CustomListModel<DummyClass>(model);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException e) {
		}

		model = new DefaultDataModel<DummyClass>();
		model.addField(new PropertyField<DummyClass, String>(String.class, "strAttr"));
		new CustomListModel<DummyClass>(model);
	}

	@Test
	public void testGetElementAt() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		model.addField(new PropertyField<DummyClass, String>(String.class, "strAttr"));

		CustomListModel<DummyClass> clm = new CustomListModel<DummyClass>(model);
		try {
			clm.getElementAt(0);
			fail("IllegalStateException should be thrown!");
		} catch (IllegalStateException ok) {
		}

		clm.setDataSource(dataSource);
		try {
			clm.getElementAt(1);
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

		CustomListModel<DummyClass> clm = new CustomListModel<DummyClass>(model);
		assertEquals(0, clm.getSize());

		clm.setDataSource(dataSource);
		assertEquals(dataSource.getSize(), clm.getSize());
	}

	@Test
	public void testSelection() {
		DataModel<DummyClass> model = new DefaultDataModel<DummyClass>();
		model.addField(new PropertyField<DummyClass, String>(String.class, "strAttr"));
		DefaultSelectionListDataSource<DummyClass> source = new DefaultSelectionListDataSource<DummyClass>(
				DummyClass.class);
		for (int i = 0; i < 5; i++) {
			source.add(new DummyClass());
		}
		source.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		SelectionCounter counter = new SelectionCounter();
		source.addEventListener(counter);
		ListRenderModel<DummyClass> lrm = new ListRenderModel<DummyClass>(source);

		assertEquals(0, counter.getSelectionsCount());
		assertEquals(lrm.getList().getSelectedIndices().length, source.getSelectedIndices().size());
		for (int i = 0; i < source.getSelectedIndices().size(); i++) {
			assertEquals(source.getSelectedIndices().get(i),
					Integer.valueOf(lrm.getList().getSelectedIndices()[i]));
		}

		lrm.getList().setSelectedIndex(0);
		assertEquals(0, lrm.getList().getSelectedIndex());
		assertEquals(1, counter.getSelectionsCount());
		assertEquals(lrm.getList().getSelectedIndices().length, source.getSelectedIndices().size());
		for (int i = 0; i < source.getSelectedIndices().size(); i++) {
			assertEquals(source.getSelectedIndices().get(i),
					Integer.valueOf(lrm.getList().getSelectedIndices()[i]));
		}

		lrm.getList().setSelectedIndex(1);
		assertEquals(lrm.getList().getSelectedIndices().length, source.getSelectedIndices().size());
		assertEquals(2, counter.getSelectionsCount());
		for (int i = 0; i < source.getSelectedIndices().size(); i++) {
			assertEquals(source.getSelectedIndices().get(i),
					Integer.valueOf(lrm.getList().getSelectedIndices()[i]));
		}

		lrm.getList().setSelectedIndex(0);
		assertEquals(lrm.getList().getSelectedIndices().length, source.getSelectedIndices().size());
		assertEquals(3, counter.getSelectionsCount());
		for (int i = 0; i < source.getSelectedIndices().size(); i++) {
			assertEquals(source.getSelectedIndices().get(i),
					Integer.valueOf(lrm.getList().getSelectedIndices()[i]));
		}
	}

	private static class SelectionCounter extends SelectionChangedAdapter<DummyClass> {
		private int selectionsCount = 0;

		@Override
		public void selectionChanged(SelectionListDataSource<DummyClass> source, SelectionChangedEvent<DummyClass> event) {
			selectionsCount++;
		}

		public int getSelectionsCount() {
			return selectionsCount;
		}

	}
}
