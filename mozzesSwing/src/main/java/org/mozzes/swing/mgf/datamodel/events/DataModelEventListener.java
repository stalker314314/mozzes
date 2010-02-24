package org.mozzes.swing.mgf.datamodel.events;

import org.mozzes.swing.mgf.datamodel.DataModel;

/**
 * Interface which must be implemented in order to be able to react to {@link DataModel} changes({@link DataModelEvent})
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 */
public interface DataModelEventListener<T> {
	/**
	 * @param model {@link DataModel} which triggered the event
	 * @param event Event that happened (concrete subclass of {@link DataModelEvent})
	 */
	public void handleDataModelEvent(DataModel<T> model, DataModelEvent<T> event);
}
