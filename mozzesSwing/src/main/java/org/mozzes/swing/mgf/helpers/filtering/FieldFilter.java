package org.mozzes.swing.mgf.helpers.filtering;

import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.utils.filtering.Filter;


/**
 * Base class for all filters which use a {@link Field} in the process of filtering
 * 
 * @author milos
 * 
 * @param <T> Type of the bean
 * @param <Q> Type of the field value
 */
abstract class FieldFilter<T, Q> implements Filter<T> {

	private Field<T, Q> field;

	protected FieldFilter() {
	}

	protected FieldFilter(Field<T, Q> field) {
		this.field = field;
	}

	public void setField(Field<T, Q> field) {
		this.field = field;
	}

	public Field<T, Q> getField() {
		return field;
	}
}
