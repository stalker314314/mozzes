package org.mozzes.swing.mgf.datamodel.events.field;

import org.mozzes.swing.mgf.datamodel.Field;

/**
 * Indicates that fields {@link Field#isReadOnly() read-only} property has changed
 * 
 * @author milos
 * 
 * @param <T> The type of object on which the field operates
 * @param <F> The type of the field value
 */
public class ReadOnlyPropertyChangedEvent<T, F> extends FieldEvent<T, F> {

	public ReadOnlyPropertyChangedEvent() {
	}
}
