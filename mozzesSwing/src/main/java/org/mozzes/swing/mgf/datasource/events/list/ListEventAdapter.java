package org.mozzes.swing.mgf.datasource.events.list;

import java.util.List;

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;


public abstract class ListEventAdapter<T> implements ListDataSourceEventListener<T> {

	@Override
	public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
	}

	@Override
	public void handleDataSourceEvent(BeanDataSource<List<T>> source, BeanDataSourceEvent<List<T>> event) {
	}

}
