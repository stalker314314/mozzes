package org.mozzes.swing.mgf.datasource.events.bean;

import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;

/**
 * Base class for all BeanDataSource events
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public abstract class BeanDataSourceEvent<T> extends DataSourceEvent<T> {
}
