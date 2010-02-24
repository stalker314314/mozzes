package org.mozzes.swing.mgf.datasource.events.bean;

import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.events.DataSourceEventListener;

/**
 * Interface which SHOULD be implemented in order to listen to BeanDataSource events
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public interface BeanDataSourceEventListener<T> extends DataSourceEventListener<T> {
	/**
	 * Implement to handle any {@link BeanDataSourceEvent}
	 * 
	 * @param source {@link BeanDataSource Data source} which fired the event
	 * @param event {@link BeanDataSourceEvent Event} which was fired
	 */
	public void handleDataSourceEvent(BeanDataSource<T> source, BeanDataSourceEvent<T> event);
}
