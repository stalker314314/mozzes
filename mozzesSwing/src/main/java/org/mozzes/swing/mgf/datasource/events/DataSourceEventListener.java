package org.mozzes.swing.mgf.datasource.events;

import org.mozzes.swing.mgf.datasource.DataSource;

/**
 * Interface which has be implemented in order to listen all DataSource events
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public interface DataSourceEventListener<T> {
	/**
	 * Implement to handle any {@link DataSourceEvent}
	 * 
	 * @param source {@link DataSource Data source} which fired the event
	 * @param event {@link DataSourceEvent Event} which was fired
	 */
	public void handleDataSourceEvent(DataSource<T> source, DataSourceEvent<T> event);
}
