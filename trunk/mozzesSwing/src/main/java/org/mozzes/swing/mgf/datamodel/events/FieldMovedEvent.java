package org.mozzes.swing.mgf.datamodel.events;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;

/**
 * Indicates that a field was moved in the model
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the model operates
 */
public class FieldMovedEvent<T> extends DataModelEvent<T> {
	private final int from;
	private final int to;
	private final Field<T, ?> field;

	/**
	 * Constructs the event
	 * 
	 * @param from Index in the {@link DataModel} from which the field was moved
	 * @param to Index in the {@link DataModel} to which the field was moved
	 * @param field {@link Field} which was moved
	 */
	public FieldMovedEvent(int from, int to, Field<T, ?> field) {
		this.from = from;
		this.to = to;
		this.field = field;
	}

	/**
	 * @return Index in the {@link DataModel} from which the field was moved
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * @return Index in the {@link DataModel} to which the field was moved
	 */
	public int getTo() {
		return to;
	}

	/**
	 * @return {@link Field} which was moved
	 */
	public Field<T, ?> getField() {
		return field;
	}
}
