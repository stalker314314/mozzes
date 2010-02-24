package org.mozzes.swing.mgf.datasource.events.selectionlist;

import org.mozzes.swing.mgf.datasource.SelectionListDataSource;

public abstract class SelectionChangedAdapter<T> extends SelectionListEventAdapter<T> {

	@Override
	public void handleDataSourceEvent(SelectionListDataSource<T> source, SelectionListDataSourceEvent<T> event) {
		if (event instanceof SelectionChangedEvent<?>) {
			selectionChanged(source, (SelectionChangedEvent<T>) event);
		}
	}

	public abstract void selectionChanged(SelectionListDataSource<T> source, SelectionChangedEvent<T> event);

}
