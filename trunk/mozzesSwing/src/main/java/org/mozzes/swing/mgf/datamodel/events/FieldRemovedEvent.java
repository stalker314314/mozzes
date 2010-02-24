package org.mozzes.swing.mgf.datamodel.events;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;

/**
 * Indicates that a field was removed from the model
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 */
public class FieldRemovedEvent<T> extends DataModelEvent<T> {
	private final int fieldIndex;
	private final Field<T, ?> field;

	/**
	 * Constructs the event
	 * 
	 * @param fieldIndex Index in the {@link DataModel} at which the field was removed
	 * @param field {@link Field} which was removed from the {@link DataModel}
	 */
	public FieldRemovedEvent(int fieldIndex, Field<T, ?> field) {
		this.fieldIndex = fieldIndex;
		this.field = field;
	}

	/**
	 * @return Index in the {@link DataModel} at which the field was removed
	 */
	public int getFieldIndex() {
		return fieldIndex;
	}

	/**
	 * @return {@link Field} which was removed from the {@link DataModel}
	 */
	public Field<T, ?> getField() {
		return field;
	}
}
