package org.mozzes.swing.mgf.datasource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.bean.DataUpdatedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsAddedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsRemovedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsUpdatedEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionChangedEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionListDataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.selectionlist.SelectionModeChangedEvent;
import org.mozzes.swing.mgf.datasource.impl.DefaultListDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultSelectionListDataSource;
import org.mozzes.swing.mgf.datasource.impl.SelectionMode;


public class SelectionListDataSourceTest {
	private static final int VALUE_DELTA = 100;
	private final SelectionListHandlers selListHandlers = new SelectionListHandlers();
	private final ListHandlers listHandlers = new ListHandlers();
	private final BeanHandlers beanHandlers = new BeanHandlers();
	private final List<Integer> data = new ArrayList<Integer>() {
		private static final long serialVersionUID = 15L;
		{
			for (int i = 0; i < 10; i++) {
				add(i + VALUE_DELTA);
			}
		}
	};

	@Test
	public void testClearBug() {

	}

	@Test
	public void testObjectsUpdatedOnSelectionEventBug() {
		SelectionListDataSource<Integer> src = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		src.addEventListener(selListHandlers);
		src.addEventListener(listHandlers);

		selListHandlers.selChangeCount = 0;
		listHandlers.objectsAdded = 0;
		listHandlers.dataUpdated = 0;
		listHandlers.objectsUpdated = 0;
		listHandlers.objectsRemoved = 0;
		listHandlers.sourceChanged = 0;

		src.setSelectedIndices(0);
		src.add(0, 123);

		assertEquals(2, selListHandlers.selChangeCount);
		assertEquals(1, listHandlers.objectsAdded);
		assertEquals(0, listHandlers.objectsRemoved);
		assertEquals(0, listHandlers.objectsUpdated);
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(0, listHandlers.sourceChanged);
		assertEquals(1, src.getSelectedIndex());
	}

	@Test
	public void testFromObjectSourceEventPropagation() {
		SelectionListDataSource<Integer> src = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		src.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		src.addEventListener(selListHandlers);
		src.addEventListener(listHandlers);

		src.getSelectedValuesDataSource().addEventListener(listHandlers);
		src.getSelectedIndicesDataSource().addEventListener(listHandlers);

		src.getSelectedIndexDataSource().addEventListener(beanHandlers);
		src.getSelectedValueDataSource().addEventListener(beanHandlers);

		// From ObjectsSource propagation
		selListHandlers.selChangeCount = 0;
		listHandlers.objectsUpdated = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.getSelectedValueDataSource().fireDataUpdatedEvent();
		assertEquals(0, selListHandlers.selChangeCount);
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(2, listHandlers.objectsUpdated);
		assertEquals(1, beanHandlers.dataUpdated);
	}

	@Test
	public void testFromObjectsSourceEventPropagation() {
		SelectionListDataSource<Integer> src = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		src.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		src.addEventListener(selListHandlers);
		src.addEventListener(listHandlers);

		src.getSelectedValuesDataSource().addEventListener(listHandlers);
		src.getSelectedIndicesDataSource().addEventListener(listHandlers);

		src.getSelectedIndexDataSource().addEventListener(beanHandlers);
		src.getSelectedValueDataSource().addEventListener(beanHandlers);

		// From ObjectsSource propagation
		selListHandlers.selChangeCount = 0;
		listHandlers.objectsUpdated = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.getSelectedValuesDataSource().fireDataUpdatedEvent();
		assertEquals(0, selListHandlers.selChangeCount);
		assertEquals(1, listHandlers.dataUpdated);
		assertEquals(1, listHandlers.objectsUpdated);
		assertEquals(1, beanHandlers.dataUpdated);

		// From ObjectsSource propagation
		selListHandlers.selChangeCount = 0;
		listHandlers.objectsUpdated = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.getSelectedValuesDataSource().fireObjectsUpdatedEvent();
		assertEquals(0, selListHandlers.selChangeCount);
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(2, listHandlers.objectsUpdated);
		assertEquals(1, beanHandlers.dataUpdated);
	}

	@Test
	public void testFromIndicesSourceEventPropagation() {
		SelectionListDataSource<Integer> src = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		src.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		src.addEventListener(selListHandlers);

		src.getSelectedValuesDataSource().addEventListener(listHandlers);
		src.getSelectedIndicesDataSource().addEventListener(listHandlers);

		src.getSelectedIndexDataSource().addEventListener(beanHandlers);
		src.getSelectedValueDataSource().addEventListener(beanHandlers);

		// From IndicesSource propagation
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;

		src.getSelectedIndicesDataSource().add(0);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(2, listHandlers.dataUpdated);
		assertEquals(2, beanHandlers.dataUpdated);

		// From IndicesSource propagation -- No change
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.getSelectedIndicesDataSource().add(0);
		assertEquals(0, selListHandlers.selChangeCount);
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(0, beanHandlers.dataUpdated);

		// From IndicesSource propagation -- No change in first one
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.getSelectedIndicesDataSource().add(1);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(2, listHandlers.dataUpdated);
		assertEquals(0, beanHandlers.dataUpdated);

		src.clearSelection();

		// From IndicesSource propagation 2
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.getSelectedIndicesDataSource().getData().add(0);
		src.getSelectedIndicesDataSource().fireDataUpdatedEvent();
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(2, listHandlers.dataUpdated);
		assertEquals(2, beanHandlers.dataUpdated);

		// From IndicesSource propagation 2 -- No change
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.getSelectedIndicesDataSource().getData().add(0);
		src.getSelectedIndicesDataSource().fireDataUpdatedEvent();
		assertEquals(0, selListHandlers.selChangeCount);
		assertEquals(1, listHandlers.dataUpdated);
		assertEquals(0, beanHandlers.dataUpdated);

		// From IndicesSource propagation 2 -- No change in first one
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.getSelectedIndicesDataSource().getData().add(1);
		src.getSelectedIndicesDataSource().fireDataUpdatedEvent();
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(2, listHandlers.dataUpdated);
		assertEquals(0, beanHandlers.dataUpdated);
	}

	@Test
	public void testToSelectionSourcesEventPropagation() {
		SelectionListDataSource<Integer> src = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		src.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		src.addEventListener(selListHandlers);

		src.getSelectedValuesDataSource().addEventListener(listHandlers);
		src.getSelectedIndicesDataSource().addEventListener(listHandlers);

		src.getSelectedIndexDataSource().addEventListener(beanHandlers);
		src.getSelectedValueDataSource().addEventListener(beanHandlers);

		// To sources propagation
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.addSelectedIndices(0);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(2, listHandlers.dataUpdated);
		assertEquals(2, beanHandlers.dataUpdated);

		// To sources propagation -- No change
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.addSelectedIndices(0);
		assertEquals(0, selListHandlers.selChangeCount);
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(0, beanHandlers.dataUpdated);

		// To sources propagation -- No change in first one
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		beanHandlers.dataUpdated = 0;
		src.addSelectedIndices(1);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(2, listHandlers.dataUpdated);
		assertEquals(0, beanHandlers.dataUpdated);
	}

	@Test
	public void testSelectionPropagation() {
		SelectionListDataSource<Integer> src1 = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		SelectionListDataSource<Integer> src2 = new DefaultSelectionListDataSource<Integer>(Integer.class, src1);
		src2.getSelectedIndicesDataSource().bindTo(src1.getSelectedIndicesDataSource());

		src1.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		src2.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);

		src1.addEventListener(selListHandlers);
		src2.addEventListener(selListHandlers);
		src1.getSelectedIndicesDataSource().addEventListener(listHandlers);
		src2.getSelectedIndicesDataSource().addEventListener(listHandlers);

		selListHandlers.selChangeCount = 0;
		listHandlers.objectsAdded = 0;
		listHandlers.objectsRemoved = 0;
		listHandlers.dataUpdated = 0;
		listHandlers.sourceChanged = 0;

		src1.addSelectedIndices(0);
		assertEquals(2, selListHandlers.selChangeCount);
		assertEquals(2, listHandlers.dataUpdated);
		assertEquals(src1.getSelectedIndicesDataSource().getData(), src2.getSelectedIndicesDataSource().getData());
		assertEquals(src1.getSelectedIndices(), src2.getSelectedIndices());
		assertEquals(src1.getSelectedIndicesDataSource().getData(), src1.getSelectedIndices());
	}

	@Test
	public void testAddSelectedIndices() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		assertEquals(-1, selList.getSelectedIndex());
		assertEquals(0, selList.getSelectedIndices().size());
		try {
			selList.addSelectedIndices(-1);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selList.addSelectedIndices(10);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selList.addSelectedIndices(0);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}

		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		assertEquals(0, selList.getSelectedIndices().size());
		try {
			selList.addSelectedIndices(-1, 1, 0);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.addSelectedIndices(1, 0, 10);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		assertEquals(0, selList.getSelectedIndices().size());
		selList.addSelectedIndices(0);
		assertEquals(1, selList.getSelectedIndices().size());
		selList.addSelectedIndices(1, 2);
		assertEquals(3, selList.getSelectedIndices().size());
		selList.addSelectedIndices(1, 2);
		assertEquals(3, selList.getSelectedIndices().size());
		assertEquals(2, selListHandlers.selChangeCount);
	}

	@Test
	public void testAddSelectionInterval() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		// Single selection
		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		assertEquals(-1, selList.getSelectedIndex());
		assertEquals(0, selList.getSelectedIndices().size());
		try {
			selList.addSelectionInterval(-1, 0);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selList.addSelectionInterval(10, 15);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selList.addSelectionInterval(0, -1);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}

		// Multiple selection
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		try {
			selList.addSelectionInterval(-1, 0);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}

		try {
			selList.addSelectionInterval(10, 15);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.addSelectionInterval(0, -1);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		selListHandlers.selChangeCount = 0;
		assertEquals(0, selList.getSelectedIndices().size());
		selList.addSelectionInterval(0, 3);
		assertEquals(4, selList.getSelectedIndices().size());
		assertEquals(1, selListHandlers.selChangeCount);
		selList.addSelectionInterval(2, 5);
		assertEquals(6, selList.getSelectedIndices().size());
		assertEquals(2, selListHandlers.selChangeCount);
		selList.addSelectionInterval(0, 5);
		assertEquals(6, selList.getSelectedIndices().size());
		assertEquals(2, selListHandlers.selChangeCount);
		selList.addSelectionInterval(5, 0);
		assertEquals(6, selList.getSelectedIndices().size());
		assertEquals(2, selListHandlers.selChangeCount);
		selList.addSelectionInterval(7, 5);
		assertEquals(8, selList.getSelectedIndices().size());
		assertEquals(3, selListHandlers.selChangeCount);
	}

	@Test
	public void testClearSelection() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		selListHandlers.selChangeCount = 0;
		selList.setSelectedIndices(0);
		assertEquals(0, selList.getSelectedIndex());
		selList.clearSelection();
		assertEquals(-1, selList.getSelectedIndex());
		assertEquals(2, selListHandlers.selChangeCount);
	}

	@Test
	public void testGetSelectedIndex() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selList.setSelectedIndices(0);
		assertEquals(0, selList.getSelectedIndex());
		selList.clearSelection();
		assertEquals(-1, selList.getSelectedIndex());
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.setSelectedIndices(3, 1, 2);
		// try {
		// selList.getSelectedIndex();
		// fail("UnsupportedOperationException should be thrown!");
		// } catch (UnsupportedOperationException ok) {
		// }
		assertEquals(1, selList.getSelectedIndex());
	}

	@Test
	public void testGetSelectedIndices() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);

		List<Integer> expected = new ArrayList<Integer>();

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selList.setSelectedIndices(0);
		expected.add(0);
		assertEquals(expected, selList.getSelectedIndices());
		selList.clearSelection();
		expected.clear();
		assertEquals(expected, selList.getSelectedIndices());
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.setSelectedIndices(3, 1, 2);
		expected.clear();
		expected.add(1);
		expected.add(2);
		expected.add(3);
		assertEquals(expected, selList.getSelectedIndices());
	}

	@Test
	public void testGetSelectedIndexDataSource() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);
		selList.getSelectedIndicesDataSource().addEventListener(listHandlers);

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		selList.setSelectedIndices(1);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(1, listHandlers.dataUpdated);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		selList.addSelectedIndices(4);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(1, listHandlers.dataUpdated);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		selList.getSelectedIndicesDataSource().fireDataUpdatedEvent();
		assertEquals(0, selListHandlers.selChangeCount); // because change did not actually happen
		assertEquals(1, listHandlers.dataUpdated);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		selList.getSelectedIndicesDataSource().getData().add(0);
		selList.getSelectedIndicesDataSource().fireDataUpdatedEvent();
		assertEquals(1, selListHandlers.selChangeCount); // now change did happen, so an event is fired
		assertEquals(1, listHandlers.dataUpdated);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());
		// ^ Permutovano equals ne radi!
		selList.remove(0);

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		selList.getSelectedIndicesDataSource().add(8);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(1, listHandlers.objectsAdded);
		assertEquals(1, listHandlers.dataUpdated);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		List<Integer> list = new ArrayList<Integer>();
		selList.clearSelection();

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		listHandlers.sourceChanged = 0;
		selList.getSelectedIndicesDataSource().setData(list);
		assertEquals(0, selListHandlers.selChangeCount); // there was no change in selection
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(1, listHandlers.sourceChanged);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		list = new ArrayList<Integer>();
		list.add(1);
		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		listHandlers.sourceChanged = 0;
		selList.getSelectedIndicesDataSource().setData(list);
		assertEquals(1, selListHandlers.selChangeCount); // there was change in selection
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(1, listHandlers.sourceChanged);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		list.add(8);
		selList.getSelectedIndicesDataSource().fireDataUpdatedEvent();
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(1, listHandlers.dataUpdated);

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		listHandlers.sourceChanged = 0;
		ListDataSource<Integer> otherSource = new DefaultListDataSource<Integer>(Integer.class, list);
		selList.getSelectedIndicesDataSource().bindTo(otherSource);
		assertEquals(0, selListHandlers.selChangeCount); // there was no change in selection
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(1, listHandlers.sourceChanged);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		listHandlers.sourceChanged = 0;
		otherSource = new DefaultListDataSource<Integer>(Integer.class, list);
		otherSource.add(2);
		selList.getSelectedIndicesDataSource().bindTo(otherSource);
		assertEquals(1, selListHandlers.selChangeCount); // there was a change in selection
		assertEquals(0, listHandlers.dataUpdated);
		assertEquals(1, listHandlers.sourceChanged);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		selListHandlers.selChangeCount = 0;
		listHandlers.dataUpdated = 0;
		listHandlers.objectsRemoved = 0;
		otherSource.remove((Integer) 8);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(1, listHandlers.dataUpdated);
		assertEquals(1, listHandlers.objectsRemoved);
		assertEquals(selList.getSelectedIndices(), selList.getSelectedIndicesDataSource().getData());

		selList.clearSelection();
		SelectionListDataSource<Integer> otherSelList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		otherSelList.getSelectedIndicesDataSource().bindTo(selList.getSelectedIndicesDataSource());

		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		otherSelList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);

		selListHandlers.selChangeCount = 0;
		selList.addSelectedIndices(1, 2, 3);
		assertEquals(selList.getSelectedIndices(), otherSelList.getSelectedIndices());
		otherSelList.addSelectedIndices(1, 2, 3);
		assertEquals(selList.getSelectedIndices(), otherSelList.getSelectedIndices());
		assertEquals(1, selListHandlers.selChangeCount);
	}

	@Test
	public void testGetSelectedObjectsDataSource() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);
		ListDataSource<Integer> selValuesDS = selList.getSelectedValuesDataSource();

		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		assertEquals(selList.getSelectedValues(), selValuesDS.getData());

		try {
			selValuesDS.getData().add(1);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selValuesDS.getData().remove(2);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selValuesDS.add(2);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selValuesDS.remove(1);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selValuesDS.setData(new ArrayList<Integer>());
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selValuesDS.bindTo(new DefaultListDataSource<Integer>(Integer.class, new ArrayList<Integer>()));
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}

		selValuesDS.addEventListener(listHandlers);
		listHandlers.dataUpdated = 0;
		selList.addSelectedIndices(1, 2, 3);
		assertEquals(1, listHandlers.dataUpdated);
		selListHandlers.selChangeCount = 0;
		selList.getSelectedIndicesDataSource().remove(1, 3);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(2, listHandlers.dataUpdated);
		assertEquals(selList.getSelectedValues(), selValuesDS.getData());
	}

	@Test
	public void testGetSelectedValue() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selList.setSelectedIndices(0);
		assertEquals(Integer.valueOf(100), selList.getSelectedValue());
		selList.clearSelection();
		assertEquals(null, selList.getSelectedValue());
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.setSelectedIndices(3, 1, 2);
		// try {
		// selList.getSelectedValue();
		// fail("UnsupportedOperationException should be thrown!");
		// } catch (UnsupportedOperationException ok) {
		// }
		assertSame(selList.get(1), selList.getSelectedValue());
	}

	@Test
	public void testGetSelectedValues() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		List<Integer> expected = new ArrayList<Integer>();

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selList.setSelectedIndices(0);
		expected.add(100);
		assertEquals(expected, selList.getSelectedValues());

		selList.clearSelection();
		expected.clear();
		assertEquals(expected, selList.getSelectedValues());

		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.setSelectedIndices(3, 1, 2);
		expected.add(101);
		expected.add(102);
		expected.add(103);
		assertEquals(expected, selList.getSelectedValues());
	}

	@Test
	public void testInvertSelection() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);
		List<Integer> expected = new ArrayList<Integer>();

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selList.clearSelection();
		selListHandlers.selChangeCount = 0;
		try {
			selList.invertSelection();
		} catch (IllegalArgumentException ok) {
		}
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.selectAll();
		selList.removeSelectedIndices(4);
		selListHandlers.selChangeCount = 0;
		selList.invertSelection();
		expected.add(4);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(expected, selList.getSelectedIndices());

		selList.clearSelection();
		selListHandlers.selChangeCount = 0;
		selList.invertSelection();
		expected.clear();
		for (int i = 0; i < selList.getSize(); i++) {
			expected.add(i);
		}
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(expected, selList.getSelectedIndices());
		selListHandlers.selChangeCount = 0;
		selList.invertSelection();
		assertEquals(1, selListHandlers.selChangeCount);
		expected.clear();
		assertEquals(expected, selList.getSelectedIndices());
	}

	@Test
	public void testRemoveSelectedIndices() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);
		List<Integer> expected = new ArrayList<Integer>();

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		selList.removeSelectedIndices(0, 1, 2);
		assertEquals(0, selListHandlers.selChangeCount);
		try {
			selList.removeSelectedIndices(-1);
			fail("IndexOutOfBoundsException should be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}
		selList.setSelectedIndices(0);
		selListHandlers.selChangeCount = 0;
		selList.removeSelectedIndices(0);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(expected, selList.getSelectedIndices());
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.setSelectedIndices(3, 1, 2);
		selListHandlers.selChangeCount = 0;
		selList.removeSelectedIndices(3, 2, 5);
		assertEquals(1, selListHandlers.selChangeCount);
		expected.add(1);
		assertEquals(expected, selList.getSelectedIndices());
	}

	@Test
	public void testRemoveSelectionInterval() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		List<Integer> expected = new ArrayList<Integer>();

		selListHandlers.selChangeCount = 0;
		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		selList.removeSelectionInterval(0, 2);
		assertEquals(0, selListHandlers.selChangeCount);
		try {
			selList.removeSelectionInterval(-1, 2);
			fail("IndexOutOfBoundsException should be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.removeSelectionInterval(1, -1);
			fail("IndexOutOfBoundsException should be thrown!");
		} catch (IndexOutOfBoundsException ok) {
		}
		selList.setSelectedIndices(0);
		selListHandlers.selChangeCount = 0;
		selList.removeSelectionInterval(0, 0);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(expected, selList.getSelectedIndices());
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.setSelectedIndices(3, 1, 2);
		selListHandlers.selChangeCount = 0;
		selList.removeSelectionInterval(2, 5);
		assertEquals(1, selListHandlers.selChangeCount);
		expected.add(1);
		assertEquals(expected, selList.getSelectedIndices());
	}

	@Test
	public void testSelectAll() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		try {
			selList.selectAll();
			fail("UnsupportedOperationException should be thrown!");
		} catch (UnsupportedOperationException ok) {
		}
		assertEquals(0, selListHandlers.selChangeCount);
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		assertEquals(1, selListHandlers.selChangeCount);
		selListHandlers.selChangeCount = 0;
		selList.selectAll();
		List<Integer> expected = new ArrayList<Integer>();
		for (int i = 0; i < selList.getSize(); i++) {
			expected.add(i);
		}
		assertEquals(expected, selList.getSelectedIndices());
	}

	@Test
	public void testSetSelectedIndices() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		assertEquals(-1, selList.getSelectedIndex());
		assertEquals(0, selList.getSelectedIndices().size());
		try {
			selList.setSelectedIndices(-1);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.setSelectedIndices(10);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.setSelectedIndices(0, 1);
			fail("IllegalArgumentException should be thrown");
		} catch (IllegalArgumentException ok) {
		}

		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		assertEquals(0, selList.getSelectedIndices().size());
		try {
			selList.setSelectedIndices(-1, 1, 0);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.setSelectedIndices(1, 0, 10);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		assertEquals(0, selList.getSelectedIndices().size());
		selList.setSelectedIndices(0);
		assertEquals(1, selList.getSelectedIndices().size());
		assertEquals(1, selListHandlers.selChangeCount);
		selList.setSelectedIndices(1, 2);
		assertEquals(2, selList.getSelectedIndices().size());
		assertEquals(2, selListHandlers.selChangeCount);
		selList.setSelectedIndices(1, 2);
		assertEquals(2, selList.getSelectedIndices().size());
		assertEquals(2, selListHandlers.selChangeCount);
	}

	@Test
	public void testSetSelectionInterval() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		// Single selection
		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		assertEquals(-1, selList.getSelectedIndex());
		assertEquals(0, selList.getSelectedIndices().size());
		try {
			selList.setSelectionInterval(-1, 0);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selList.setSelectionInterval(10, 15);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}
		try {
			selList.setSelectionInterval(0, -1);
			fail("UnsupportedOperationException should be thrown");
		} catch (UnsupportedOperationException ok) {
		}

		// Multiple selection
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		try {
			selList.setSelectionInterval(-1, 0);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}

		try {
			selList.setSelectionInterval(10, 15);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.setSelectionInterval(0, -1);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		selListHandlers.selChangeCount = 0;
		assertEquals(0, selList.getSelectedIndices().size());
		selList.setSelectionInterval(0, 3);
		assertEquals(4, selList.getSelectedIndices().size());
		selList.setSelectionInterval(2, 5);
		assertEquals(4, selList.getSelectedIndices().size());
		selList.setSelectionInterval(0, 5);
		assertEquals(6, selList.getSelectedIndices().size());
		selList.setSelectionInterval(5, 0);
		assertEquals(6, selList.getSelectedIndices().size());
		selList.setSelectionInterval(7, 5);
		assertEquals(3, selList.getSelectedIndices().size());
		assertEquals(5, selListHandlers.selChangeCount);
	}

	@Test
	public void testSetSelectionMode() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		selListHandlers.selChangeCount = 0;
		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(1, selListHandlers.selModeChangeCount);
		selList.setSelectedIndices(0);

		selListHandlers.selChangeCount = selListHandlers.selModeChangeCount = 0;
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(1, selListHandlers.selModeChangeCount);
		assertEquals(0, selList.getSelectedIndices().size());
		selList.setSelectedIndices(0);
	}

	@Test
	public void testSelectionModification() {
		List<Integer> newData = new LinkedList<Integer>(data);
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, newData);
		selList.addEventListener(selListHandlers);
		List<Integer> expectedSel = new ArrayList<Integer>();

		// DataUpdated
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.addSelectedIndices(1, 2, 3);
		selListHandlers.selChangeCount = 0;
		selList.fireDataUpdatedEvent();
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(new ArrayList<Integer>(), selList.getSelectedIndices());

		// ObjectsAdded
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.addSelectedIndices(1, 2, 3);
		selListHandlers.selChangeCount = 0;
		selList.add(0, 11);
		selList.add(0, 11);
		expectedSel.clear();
		expectedSel.add(3);
		expectedSel.add(4);
		expectedSel.add(5);
		assertEquals(2, selListHandlers.selChangeCount);
		assertEquals(expectedSel, selList.getSelectedIndices());

		// ObjectsAdded in the middle
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.addSelectedIndices(1, 2, 3);
		selListHandlers.selChangeCount = 0;
		selList.add(0, 11); // 2,3,4
		selList.add(3, 11); // 2,4,5
		expectedSel.clear();
		expectedSel.add(2);
		expectedSel.add(4);
		expectedSel.add(5);
		assertEquals(2, selListHandlers.selChangeCount);
		assertEquals(expectedSel, selList.getSelectedIndices());

		// ObjectsRemoved
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.addSelectedIndices(1, 2, 3);
		selListHandlers.selChangeCount = 0;
		selList.remove(5);
		expectedSel.clear();
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(expectedSel, selList.getSelectedIndices());

		// SourceChanged
		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selList.addSelectedIndices(1, 2, 3);
		selListHandlers.selChangeCount = 0;
		selList.setData(new ArrayList<Integer>());
		assertEquals(1, selListHandlers.selChangeCount);
		assertEquals(new ArrayList<Integer>(), selList.getSelectedIndices());

	}

	@Test
	public void testToggleSelectedIndices() {
		SelectionListDataSource<Integer> selList = new DefaultSelectionListDataSource<Integer>(Integer.class, data);
		selList.addEventListener(selListHandlers);

		List<Integer> expected = new ArrayList<Integer>();

		selList.setSelectionMode(SelectionMode.SINGLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		assertEquals(-1, selList.getSelectedIndex());
		assertEquals(0, selList.getSelectedIndices().size());
		try {
			selList.toggleSelectedIndices(-1);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.toggleSelectedIndices(10);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.toggleSelectedIndices(0, 1);
			fail("IllegalArgumentException should be thrown");
		} catch (IllegalArgumentException ok) {
		}

		selList.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
		selListHandlers.selChangeCount = 0;
		assertEquals(0, selList.getSelectedIndices().size());
		try {
			selList.toggleSelectedIndices(-1, 1, 0);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		try {
			selList.toggleSelectedIndices(1, 0, 10);
			fail("IndexOutOfBoundsException should be thrown");
		} catch (IndexOutOfBoundsException ok) {
		}
		assertEquals(0, selList.getSelectedIndices().size());
		selList.toggleSelectedIndices(0);
		expected.add(0);
		assertEquals(expected, selList.getSelectedIndices());
		selList.toggleSelectedIndices(0);
		expected.clear();
		assertEquals(expected, selList.getSelectedIndices());
		selList.toggleSelectedIndices(2, 1);
		expected.add(1);
		expected.add(2);
		assertEquals(expected, selList.getSelectedIndices());
		selList.toggleSelectedIndices(1, 3);
		expected.clear();
		expected.add(2);
		expected.add(3);
		assertEquals(expected, selList.getSelectedIndices());
		assertEquals(4, selListHandlers.selChangeCount);
	}

	private static class SelectionListHandlers implements SelectionListDataSourceEventListener<Integer> {
		public int selModeChangeCount;
		private int selChangeCount;

		@Override
		public void handleDataSourceEvent(SelectionListDataSource<Integer> source,
				SelectionListDataSourceEvent<Integer> event) {
			if (event instanceof SelectionChangedEvent<?>)
				selChangeCount++;
			if (event instanceof SelectionModeChangedEvent<?>)
				selModeChangeCount++;
		}

		@Override
		public void handleDataSourceEvent(ListDataSource<Integer> source, ListDataSourceEvent<Integer> event) {
			// ignore
		}

		@Override
		public void handleDataSourceEvent(DataSource<List<Integer>> source, DataSourceEvent<List<Integer>> event) {
			// ignore
		}

		@Override
		public void handleDataSourceEvent(BeanDataSource<List<Integer>> source, BeanDataSourceEvent<List<Integer>> event) {
			// ignore
	}
	}

	private static class ListHandlers implements ListDataSourceEventListener<Integer> {
		private int sourceChanged;
		private int objectsAdded;
		private int objectsRemoved;
		private int dataUpdated;
		private int objectsUpdated;

		@Override
		public void handleDataSourceEvent(ListDataSource<Integer> source, ListDataSourceEvent<Integer> event) {
			if (event instanceof ObjectsAddedEvent<?>)
				objectsAdded++;
			if (event instanceof ObjectsRemovedEvent<?>)
				objectsRemoved++;
			if (event instanceof ObjectsUpdatedEvent<?>)
				objectsUpdated++;
		}

		@Override
		public void handleDataSourceEvent(DataSource<List<Integer>> source, DataSourceEvent<List<Integer>> event) {
			if (event instanceof SourceChangedEvent<?>)
				sourceChanged++;
		}

		@Override
		public void handleDataSourceEvent(BeanDataSource<List<Integer>> source, BeanDataSourceEvent<List<Integer>> event) {
			if (event instanceof DataUpdatedEvent<?>)
				dataUpdated++;
		}
	}

	private static class BeanHandlers implements BeanDataSourceEventListener<Integer> {
		private int sourceChanged;
		private int dataUpdated;

		@Override
		public void handleDataSourceEvent(BeanDataSource<Integer> source, BeanDataSourceEvent<Integer> event) {
			if (event instanceof DataUpdatedEvent<?>)
				dataUpdated++;
		}

		@Override
		public void handleDataSourceEvent(DataSource<Integer> source, DataSourceEvent<Integer> event) {
			if (event instanceof SourceChangedEvent<?>)
				sourceChanged++;
		}
	}
}
