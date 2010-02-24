package org.mozzes.swing.mgf.datasource.events.list;

import java.util.List;

import org.mozzes.swing.mgf.datasource.events.DataSourceEvent;


/**
 * Base class for all ListDataSource events
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public abstract class ListDataSourceEvent<T> extends DataSourceEvent<List<T>> {

}
