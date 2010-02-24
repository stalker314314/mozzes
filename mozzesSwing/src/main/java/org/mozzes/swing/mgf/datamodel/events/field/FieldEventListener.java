package org.mozzes.swing.mgf.datamodel.events.field;

import org.mozzes.swing.mgf.datamodel.Field;

/**
 * 
 * Interface which must be implemented in order to be able to react to {@link Field} updates ({@link FieldEvent})
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 * @param <F> The type of the field value
 */
public interface FieldEventListener<T, F> {
	/**
	 * @param field {@link Field} which triggered the event
	 * @param event Event that happened (concrete subclass of {@link FieldEvent})
	 */
	public void handleModelFieldEvent(Field<T, F> field, FieldEvent<T, F> event);
}
