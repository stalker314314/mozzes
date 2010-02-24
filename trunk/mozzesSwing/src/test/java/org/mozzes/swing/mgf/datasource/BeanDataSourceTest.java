package org.mozzes.swing.mgf.datasource;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;
import org.mozzes.swing.mgf.datasource.events.SourceChangedEvent;
import org.mozzes.swing.mgf.datasource.impl.DefaultBeanDataSource;


public class BeanDataSourceTest {
	private BeanDataSource<DummyClass> dataSource = null;
	private final DummyClass dataBean = new DummyClass();
	private int handleCalled;

	@Test
	public void testGetData() {
		dataSource = new DefaultBeanDataSource<DummyClass>(DummyClass.class, dataBean);
		assertEquals(dataBean, dataSource.getData());
	}

	@Test
	public void testSetData() {
		// Std. bean dataSource
		handleCalled = 0;
		dataSource = new DefaultBeanDataSource<DummyClass>(DummyClass.class, dataBean);
		dataSource.addEventListener(new Handler());
		dataSource.setData(new DummyClass(10));
		assertEquals(1, handleCalled);
		assertEquals(new DummyClass(10), dataSource.getData());
		// This test emphasises that the source object will not be changed, since we
		// didn't change its data, but have set a NEW SOURCE to the DataSource
		assertEquals(new DummyClass(), dataBean);

		handleCalled = 0;
		dataSource = new DefaultBeanDataSource<DummyClass>(DummyClass.class, dataBean);
		dataSource.addEventListener(new Handler());
		dataSource = new DefaultBeanDataSource<DummyClass>(dataSource);
		dataSource.addEventListener(new Handler());
		dataSource.setData(new DummyClass(11));
		assertEquals(1, handleCalled);
		assertEquals(new DummyClass(11), dataSource.getData());
		// This test emphasizes that the source object will not be changed, since we
		// didn't change its data, but have set a NEW SOURCE to the DataSource
		assertEquals(new DummyClass(), dataBean);

	}

	private class Handler implements DataSourceEventListener<DummyClass> {

		@Override
		public void handleDataSourceEvent(DataSource<DummyClass> source, DataSourceEvent<DummyClass> event) {
			if (event instanceof SourceChangedEvent<?>) {
				handleCalled++;
			}
		}
	}
}
