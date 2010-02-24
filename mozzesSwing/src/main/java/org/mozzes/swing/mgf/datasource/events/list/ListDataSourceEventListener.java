package org.mozzes.swing.mgf.datasource.events.list;

import java.util.List;

import org.mozzes.swing.mgf.datasource.ListDataSource;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEvent;
import org.mozzes.swing.mgf.datasource.events.bean.BeanDataSourceEventListener;


/**
 * Interface which SHOULD be implemented in order to listen to ListDataSource events
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public interface ListDataSourceEventListener<T> extends BeanDataSourceEventListener<List<T>> {
	/**
	 * Implement to handle any {@link BeanDataSourceEvent}
	 * 
	 * @param source {@link ListDataSource Data source} which fired the event
	 * @param event {@link ListDataSourceEvent Event} which was fired
	 */
	public void handleDataSourceEvent(ListDataSource<T> source, ListDataSourceEvent<T> event);
}
