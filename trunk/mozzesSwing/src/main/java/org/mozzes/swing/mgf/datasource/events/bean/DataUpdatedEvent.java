package org.mozzes.swing.mgf.datasource.events.bean;

import org.mozzes.swing.mgf.datasource.BeanDataSource;

/**
 * Indicates that some data contained by the bean which source is providing is changed
 * 
 * @author milos
 * 
 * @param <T> The type of the object which {@link BeanDataSource} provides
 */
public class DataUpdatedEvent<T> extends BeanDataSourceEvent<T> {

}
