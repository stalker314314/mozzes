package org.mozzes.swing.mgf.datasource.events.selectionlist;

import java.util.List;

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEvent;


public abstract class SelectionListEventAdapter<T> implements SelectionListDataSourceEventListener<T> {

	@Override
	public void handleDataSourceEvent(ListDataSource<T> source, ListDataSourceEvent<T> event) {
	}

	@Override
	public void handleDataSourceEvent(DataSource<List<T>> source, DataSourceEvent<List<T>> event) {
	}

	@Override
	public void handleDataSourceEvent(BeanDataSource<List<T>> source, BeanDataSourceEvent<List<T>> event) {
	}

}
