package org.mozzes.swing.mgf.datamodel.fields;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datarenderer.DataRenderModel;

/**
 * Returns a fixed value for every object
 * 
 * @author milos
 * 
 * @param <T> The type of the bean that the field works with
 * @param <F> The type of the value that the field returns
 */
public class StaticDataField<T, F> extends Field<T, F> {
	private F value;

	/**
	 * Constructs the {@link StaticDataField}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param value Value that will be returned for every object
	 */
	public StaticDataField(Class<F> fieldType, F value) {
		super(fieldType);
		this.value = value;
	}

	/**
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 * @param value Value that will be returned for every object
	 */
	public StaticDataField(Class<F> fieldType, String header, F value) {
		super(fieldType, header);
		this.value = value;
	}

	/**
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param value Value that will be returned for every object
	 */
	public StaticDataField(Class<F> fieldType, boolean readOnly, F value) {
		super(fieldType, readOnly);
		this.value = value;
	}

	/**
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param value Value that will be returned for every object
	 */
	public StaticDataField(Class<F> fieldType, String header, boolean readOnly, F value) {
		super(fieldType, header, readOnly);
		this.value = value;
	}

	/**
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param value Value that will be returned for every object
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public StaticDataField(Class<F> fieldType, String header, boolean readOnly, boolean sortable, F value) {
		super(fieldType, header, readOnly, sortable);
		this.value = value;
	}

	/**
	 * Constructs the {@link StaticDataField}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param value Value that will be returned for every object
	 */
	public StaticDataField(String name, Class<F> fieldType, F value) {
		super(name, fieldType);
		this.value = value;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 * @param value Value that will be returned for every object
	 */
	public StaticDataField(String name, Class<F> fieldType, String header, F value) {
		super(name, fieldType, header);
		this.value = value;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param value Value that will be returned for every object
	 */
	public StaticDataField(String name, Class<F> fieldType, boolean readOnly, F value) {
		super(name, fieldType, readOnly);
		this.value = value;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param value Value that will be returned for every object
	 */
	public StaticDataField(String name, Class<F> fieldType, String header, boolean readOnly, F value) {
		super(name, fieldType, header, readOnly);
		this.value = value;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param value Value that will be returned for every object
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public StaticDataField(String name, Class<F> fieldType, String header, boolean readOnly, boolean sortable, F value) {
		super(name, fieldType, header, readOnly, sortable);
		this.value = value;
	}

	@Override
	public F getValue(T object) {
		return value;
	}

	@Override
	protected void valueSetter(T object, F value) {
		this.value = value;
	}

	@Override
	public boolean isEditableFor(T object) {
		return !isReadOnly();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (!(obj instanceof StaticDataField<?, ?>))
			return false;
		StaticDataField<?, ?> other = (StaticDataField<?, ?>) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StaticDataField [value=" + value + ", getFieldType()=" + getFieldType() + ", getHeader()="
				+ getHeader() + ", getName()=" + getName() + ", isReadOnly()=" + isReadOnly() + ", isSortable()="
				+ isSortable() + "]";
	}
}
