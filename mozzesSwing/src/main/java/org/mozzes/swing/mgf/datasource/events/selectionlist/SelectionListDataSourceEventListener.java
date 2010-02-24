package org.mozzes.swing.mgf.datasource.events.selectionlist;

import org.mozzes.swing.mgf.datasource.SelectionListDataSource;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.list.ListDataSourceEventListener;

/**
 * Interface which SHOULD be implemented in order to listen to SelectionListDataSource events
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public interface SelectionListDataSourceEventListener<T> extends ListDataSourceEventListener<T> {
	/**
	 * Implement to handle any {@link BeanDataSourceEvent}
	 * 
	 * @param source {@link SelectionListDataSource Data source} which fired the event
	 * @param event {@link SelectionListDataSourceEvent Event} which was fired
	 */
	public void handleDataSourceEvent(SelectionListDataSource<T> source, SelectionListDataSourceEvent<T> event);
}
