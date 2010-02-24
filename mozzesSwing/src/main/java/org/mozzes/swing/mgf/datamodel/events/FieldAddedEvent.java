package org.mozzes.swing.mgf.datamodel.events;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;

/**
 * Indicates that a field was added to the model
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 */
public class FieldAddedEvent<T> extends DataModelEvent<T> {
	private final int fieldIndex;
	private final Field<T, ?> field;

	/**
	 * Constructs the event
	 * 
	 * @param fieldIndex Index in the {@link DataModel} at which the field was added
	 * @param field {@link Field} which was added to the {@link DataModel}
	 */
	public FieldAddedEvent(int fieldIndex, Field<T, ?> field) {
		this.fieldIndex = fieldIndex;
		this.field = field;
	}

	/**
	 * @return Index in the {@link DataModel} at which the field was added
	 */
	public int getFieldIndex() {
		return fieldIndex;
	}

	/**
	 * @return {@link Field} which was added to the {@link DataModel}
	 */
	public Field<T, ?> getField() {
		return field;
	}
}
