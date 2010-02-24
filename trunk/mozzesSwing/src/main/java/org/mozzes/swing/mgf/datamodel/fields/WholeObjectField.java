package org.mozzes.swing.mgf.datamodel.fields;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datarenderer.DataRenderModel;

/**
 * Returns a the passed object
 * 
 * @author milos
 */
public class WholeObjectField<T> extends Field<T, T> {

	/**
	 * Constructs the {@link WholeObjectField}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 */
	public WholeObjectField(Class<T> fieldType) {
		super(fieldType);
	}

	/**
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The string to be used by some {@link DataRenderModel} to display the field's name
	 */
	public WholeObjectField(Class<T> fieldType, String header) {
		super(fieldType, header);
	}

	/**
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public WholeObjectField(Class<T> fieldType, String header, boolean sortable) {
		super(fieldType, header, true, sortable);
	}

	/**
	 * Constructs the {@link WholeObjectField}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 */
	public WholeObjectField(String name, Class<T> fieldType) {
		super(name, fieldType);
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 */
	public WholeObjectField(String name, Class<T> fieldType, String header) {
		super(name, fieldType, header);
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public WholeObjectField(String name, Class<T> fieldType, String header, boolean sortable) {
		super(name, fieldType, header, true, sortable);
	}

	@Override
	public T getValue(T object) {
		return object;
	}

	@Override
	protected void valueSetter(T object, T value) {
		throw new UnsupportedOperationException("WholeObjectField is ALWAYS readonly!");
	}

	@Override
	public boolean isEditableFor(T object) {
		return false;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if (readOnly)
			super.setReadOnly(readOnly);
		else
			throw new UnsupportedOperationException("WholeObjectField must ALWAYS be readonly!");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getFieldType() == null) ? 0 : getFieldType().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WholeObjectField<?>))
			return false;
		WholeObjectField<?> other = (WholeObjectField<?>) obj;
		if (getFieldType() == null) {
			if (other.getFieldType() != null)
				return false;
		} else if (!getFieldType().equals(other.getFieldType()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WholeObjectField [getFieldType()=" + getFieldType() + ", getHeader()="
				+ getHeader() + ", getName()=" + getName() + ", isReadOnly()=" + isReadOnly() + ", isSortable()="
				+ isSortable() + "]";
	}
}
