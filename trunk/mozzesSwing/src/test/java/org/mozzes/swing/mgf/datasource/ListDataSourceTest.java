package org.mozzes.swing.mgf.datasource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.DataUpdatedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.list.ObjectReplacedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsAddedEvent;
import org.mozzes.swing.mgf.datasource.events.list.ObjectsRemovedEvent;
import org.mozzes.swing.mgf.datasource.impl.DefaultBeanDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultListDataSource;


public class ListDataSourceTest {
	private List<DummyClass> data;
	private ListDataSource<DummyClass> dataSource;
	private BeanDataSource<List<DummyClass>> beanDataSource;

	private int removedEvt;
	private int addedEvt;
	private int replacedEvt;
	private int sourceChanged;
	private int dataUpdated;

	@Test
	public void testAddT() {
		// Std. List source
		addedEvt = 0;
		data = new ArrayList<DummyClass>();
		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);
		dataSource.addEventListener(new Handler());
		dataSource.add(new DummyClass());
		assertEquals(1, data.size());
		assertEquals(new DummyClass(), data.get(0));
		assertEquals(1, addedEvt);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testChangingDataSourceAndEventPropagation() {
		// Std. List source
		sourceChanged = 0;
		data = new ArrayList<DummyClass>();
		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);
		dataSource.addEventListener(new Handler());
		dataSource.setData(new ArrayList<DummyClass>());
		assertEquals(1, sourceChanged);

		// BeanDataSource as the source provider
		dataUpdated = sourceChanged = 0;
		data = new ArrayList<DummyClass>();
		beanDataSource = new DefaultBeanDataSource<List<DummyClass>>((Class<List<DummyClass>>) (Class<?>) List.class,
				data);
		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, beanDataSource);
		dataSource.addEventListener(new Handler());
		beanDataSource.addEventListener(new Handler());
		beanDataSource.setData(new ArrayList<DummyClass>());
		assertEquals(1, sourceChanged);
		assertEquals(1, dataUpdated);
		dataSource.setData(data);
		assertEquals(2, sourceChanged);
		assertEquals(1, dataUpdated);

		// ListDataSource as the source provider
		sourceChanged = 0;
		data = new ArrayList<DummyClass>();

		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);
		dataSource.addEventListener(new Handler());

		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, dataSource);
		dataSource.addEventListener(new Handler());

		data = new ArrayList<DummyClass>();
		dataSource.setData(data);
		assertEquals(1, sourceChanged);

		// Different sources propagation
		sourceChanged = addedEvt = removedEvt = dataUpdated = replacedEvt = 0;
		data = new ArrayList<DummyClass>();
		BeanDataSource<List<DummyClass>> ds0 = new DefaultBeanDataSource<List<DummyClass>>(
				(Class<List<DummyClass>>) (Class<?>) List.class,
				data);
		ds0.addEventListener(new Handler());
		ListDataSource<DummyClass> ds2 = new DefaultListDataSource<DummyClass>(DummyClass.class, ds0);
		ds2.addEventListener(new Handler());
		// Bottom-up propagation
		ds0.fireDataUpdatedEvent();
		assertEquals(2, dataUpdated);
		// Top-down propagation
		dataUpdated = 0;
		ds2.fireDataUpdatedEvent();
		assertEquals(2, dataUpdated);
		// Both
		dataUpdated = 0;
		ds2.fireDataUpdatedEvent();
		ds0.fireDataUpdatedEvent();
		ds2.fireDataUpdatedEvent();
		ds0.fireDataUpdatedEvent();
		assertEquals(8, dataUpdated);

		// Specific event propagation, conversion to DataUpdated
		sourceChanged = addedEvt = removedEvt = dataUpdated = replacedEvt = 0;
		ds2.add(new DummyClass());
		assertEquals(1, addedEvt);
		assertEquals(1, dataUpdated);
		ds2.add(new DummyClass());
		assertEquals(2, addedEvt);
		assertEquals(2, dataUpdated);

		// Specific event propagation, no conversion, other DataSource understands it
		sourceChanged = addedEvt = removedEvt = dataUpdated = replacedEvt = 0;
		data = new ArrayList<DummyClass>();
		ListDataSource<DummyClass> ds1 = new DefaultListDataSource<DummyClass>(DummyClass.class, data);
		ds1.addEventListener(new Handler());
		ds2 = new DefaultListDataSource<DummyClass>(DummyClass.class, ds1);
		ds2.addEventListener(new Handler());
		// Bottom-up propagation
		ds1.add(new DummyClass());
		assertEquals(2, addedEvt);
		assertEquals(0, dataUpdated);
		// Top-down propagation
		addedEvt = 0;
		ds2.add(new DummyClass());
		assertEquals(2, addedEvt);
		assertEquals(0, dataUpdated);
		// Both
		addedEvt = dataUpdated = 0;
		ds1.add(new DummyClass());
		assertEquals(2, addedEvt);

		ds2.add(new DummyClass());
		assertEquals(4, addedEvt);

		ds1.add(new DummyClass());
		assertEquals(6, addedEvt);

		ds2.add(new DummyClass());
		assertEquals(8, addedEvt);
		assertEquals(0, dataUpdated);
	}

	@Test
	public void testAddIntT() {
		// Std. List source
		addedEvt = 0;
		data = new ArrayList<DummyClass>();
		data.add(new DummyClass());
		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);

		dataSource.addEventListener(new Handler());
		assertEquals(new DummyClass(), dataSource.get(0));
		dataSource.add(0, new DummyClass(1));

		assertEquals(2, data.size());
		assertEquals(new DummyClass(1), dataSource.get(0));
		assertEquals(1, addedEvt);

		try {
			dataSource.add(-1, new DummyClass(1));
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ignore) {
		}

		try {
			dataSource.add(3, new DummyClass(1));
			fail("IndexOutOfBoundsException has to be thrown!");
		} catch (IndexOutOfBoundsException ignore) {
		}
	}

	@Test
	public void testSet() {
		// Std. List source
		replacedEvt = 0;
		data = new ArrayList<DummyClass>();
		data.add(new DummyClass());
		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);
		dataSource.addEventListener(new Handler());
		assertEquals(new DummyClass(), dataSource.set(0, new DummyClass(0)));
		try {
			assertEquals(null, dataSource.set(1, new DummyClass(1)));
			fail("Array is empty, thus IndexOutOfBoundsException has to be trown.");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		assertEquals(1, data.size());
		assertEquals(1, replacedEvt);
		assertEquals(new DummyClass(0), dataSource.get(0));
	}

	@Test
	public void testRemoveT() {
		// Std. List source
		replacedEvt = 0;
		data = new ArrayList<DummyClass>();
		data.add(new DummyClass());
		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);
		dataSource.addEventListener(new Handler());
		assertEquals(true, dataSource.remove(new DummyClass()));
		assertEquals(false, dataSource.remove(new DummyClass()));
		assertEquals(0, data.size());
		assertEquals(1, removedEvt);
	}

	@Test
	public void testRemoveInt() {
		// Std. List source
		replacedEvt = 0;
		data = new ArrayList<DummyClass>();
		data.add(new DummyClass());
		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);
		dataSource.addEventListener(new Handler());
		assertEquals(new DummyClass(), dataSource.remove(0));
		try {
			assertEquals(null, dataSource.remove(0));
			fail("Array is empty, thus IndexOutOfBoundsException has to be trown.");
		} catch (IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		assertEquals(0, data.size());
		assertEquals(1, removedEvt);
	}

	@Test
	public void testGetSize() {
		data = new ArrayList<DummyClass>();
		dataSource = new DefaultListDataSource<DummyClass>(DummyClass.class, data);
		assertEquals(data.size(), dataSource.getSize());
		data.add(null);
		assertEquals(data.size(), dataSource.getSize());
		data.add(new DummyClass());
		assertEquals(data.size(), dataSource.getSize());
		data.add(null);
		assertEquals(data.size(), dataSource.getSize());
		assertEquals(3, data.size());
	}

	private class Handler implements ListDataSourceEventListener<DummyClass> {

		@Override
		public void handleDataSourceEvent(DataSource<List<DummyClass>> source, DataSourceEvent<List<DummyClass>> event) {
			if (event instanceof SourceChangedEvent<?>)
				sourceChanged++;
		}

		@Override
		public void handleDataSourceEvent(ListDataSource<DummyClass> source, ListDataSourceEvent<DummyClass> event) {

			if (event instanceof ObjectsRemovedEvent<?>)
				removedEvt++;
			if (event instanceof ObjectReplacedEvent<?>)
				replacedEvt++;
			if (event instanceof ObjectsAddedEvent<?>)
				addedEvt++;
		}

		@Override
		public void handleDataSourceEvent(BeanDataSource<List<DummyClass>> source,
				BeanDataSourceEvent<List<DummyClass>> event) {
			if (event instanceof DataUpdatedEvent<?>)
				dataUpdated++;
	}
	}
}
