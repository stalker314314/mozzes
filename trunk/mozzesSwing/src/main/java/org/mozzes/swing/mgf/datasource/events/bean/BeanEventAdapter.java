package org.mozzes.swing.mgf.datasource.events.bean;

import org.mozzes.swing.mgf.datasource.DataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;

public abstract class BeanEventAdapter<T> implements BeanDataSourceEventListener<T> {

	@Override
	public void handleDataSourceEvent(DataSource<T> source, DataSourceEvent<T> event) {
	}

}
