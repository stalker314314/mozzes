package org.mozzes.swing.mgf.datamodel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.mozzes.swing.mgf.datamodel.events.field.FieldEvent;
import org.mozzes.swing.mgf.datamodel.events.field.FieldEventListener;
import org.mozzes.swing.mgf.datamodel.events.field.ReadOnlyPropertyChangedEvent;
import org.mozzes.swing.mgf.datamodel.events.field.ValueChangedEvent;
import org.mozzes.swing.mgf.datarenderer.DataRenderModel;
import org.mozzes.utils.reflection.ReflectionUtils;


/**
 * Represents a field on the model.<br>
 * The purpose of the field is to return a value based on the data provided by some bean, or provide a generic way to
 * set the same value to some bean<br>
 * This is base class for all fields (PropertyField, CalculatedField, StaticDataField,...)
 * 
 * @author milos
 * 
 * @param <T> The type of the bean that the field works with
 * @param <F> The type of the value that the field returns
 */
public abstract class Field<T, F> implements Cloneable {
	private String header;
	private String name;
	private boolean readOnly;
	private boolean sortable;
	private Class<F> fieldType;
	private final List<FieldEventListener<T, F>> listeners = new LinkedList<FieldEventListener<T, F>>();
	/**
	 * The {@link Comparator} that is used by {@link Field#compare(Object, Object)} for purposes of sorting<br>
	 * <i>null</i> if the value is used as a {@link Comparable}
	 */
	private Comparator<F> comparator;

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 */
	public Field(Class<F> fieldType) {
		this(null, fieldType, null, true, true);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 */
	public Field(Class<F> fieldType, String header) {
		this(null, fieldType, header, true, true);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 */
	public Field(Class<F> fieldType, boolean readOnly) {
		this(null, fieldType, null, readOnly, true);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public Field(Class<F> fieldType, boolean readOnly, boolean sortable) {
		this(null, fieldType, null, readOnly, sortable);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 */
	public Field(Class<F> fieldType, String header, boolean readOnly) {
		this(null, fieldType, header, readOnly, true);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public Field(Class<F> fieldType, String header, boolean readOnly, boolean sortable) {
		this(null, fieldType, header, readOnly, sortable);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 */
	public Field(String name, Class<F> fieldType) {
		this(name, fieldType, null, true, true);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 */
	public Field(String name, Class<F> fieldType, String header) {
		this(name, fieldType, header, true, true);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 */
	public Field(String name, Class<F> fieldType, boolean readOnly) {
		this(name, fieldType, null, readOnly, true);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public Field(String name, Class<F> fieldType, boolean readOnly, boolean sortable) {
		this(name, fieldType, null, readOnly, sortable);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 */
	public Field(String name, Class<F> fieldType, String header, boolean readOnly) {
		this(name, fieldType, header, readOnly, true);
	}

	/**
	 * Constructs a {@link Field}
	 * 
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param fieldType The Class of the value that the field returns or sets
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public Field(String name, Class<F> fieldType, String header, boolean readOnly, boolean sortable) {
		this.name = name;
		this.fieldType = ReflectionUtils.resolvePrimitiveType(fieldType);
		// this.fieldType = fieldType;
		this.setHeader(header);
		this.setReadOnly(readOnly);
		this.setSortable(sortable);
	}

	/**
	 * Adds a listener to the list that's notified each time a change to the field is made.
	 * 
	 * @param listener The listener to be notified about field events
	 */
	public void addEventListener(FieldEventListener<T, F> listener) {
		if (listener == null)
			throw new IllegalArgumentException("Listener must not be null!");
		listeners.add(listener);
	}

	/**
	 * Removes a listener from the list that's notified each time a change to the field is made.
	 * 
	 * @param listener The listener to be removed
	 */
	public void removeEventListener(FieldEventListener<T, F> listener) {
		listeners.remove(listener);
	}

	/**
	 * <b>Implemented by a subclass</b><br>
	 * Returns the value of the {@link Field} for the specified <i>object</i>
	 * 
	 * @param object Bean for which the value should be returned
	 * @return Value of the {@link Field} for the specified <i>object</i>
	 */
	abstract public F getValue(T object);

	/**
	 * Sets the value of the field for an object<br>
	 * <b>Does not do any actual setting, only does necessary checks, calls the
	 * {@link Field#valueSetter(Object, Object)} and fires {@link ValueChangedEvent} </b>
	 * 
	 * @param object Object for which the value should be set
	 * @param value The value which should be set
	 * @throws UnsupportedOperationException If the field is Read Only
	 */
	public final void setValue(T object, F value) {
		if (readOnly)
			throw new UnsupportedOperationException("The field is read only!");
		if (!isEditableFor(object)) {
			String reason = getReasonForNotEditable(object);
			String text = reason == null ? "" : " Reason: " + reason;
			throw new IllegalArgumentException("The field cannot be edited for specified object!" + text);
		}
		if (value != null && !getFieldType().isAssignableFrom(value.getClass()))
			throw new ClassCastException(String.format(
					"Invalid value type for this field! Field type: %s, Value type: %s",
					getFieldType().getSimpleName(), value.getClass().getSimpleName()));
		F from = getValue(object);
		valueSetter(object, value);
		F to = getValue(object);
		fireEvent(new ValueChangedEvent<T, F>(object, from, to));
	}

	/**
	 * Default implementation returns null, subclasses that override {@link Field#isEditableFor(Object)} should override
	 * this also.
	 * 
	 * @param object Object for which to get the reason
	 * @return Explanation why the specified object cannot be edited (null if the object is editable or if the reason is
	 *         not specified)
	 */
	protected String getReasonForNotEditable(T object) {
		return null;
	}

	/**
	 * <b>Implemented by a subclasses</b><br />
	 * Actual value setting is done by this method
	 * 
	 * @param object Object for which the value should be set
	 * @param value The value which should be set
	 */
	protected abstract void valueSetter(T object, F value);

	/**
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return The localized resource to be used by some {@link DataRenderModel} to display the field
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param readOnly true if you want to set the field be read-only, false otherwise
	 */
	public void setReadOnly(boolean readOnly) {
		if (readOnly != this.readOnly) {
		this.readOnly = readOnly;
			fireReadOnlyPropertyChangedEvent();
	}
	}

	/**
	 * @return true if the field is read-only, false otherwise
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param sortable true if you want to enable some {@link DataRenderModel} to sort data using this field, false
	 *            otherwise
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * @return true if beans can be sorted using the value of this field, false otherwise
	 */
	public boolean isSortable() {
		return sortable;
	}

	/**
	 * @param comparator The {@link Comparator} that will be used by {@link Field#compare(Object, Object)} for purposes
	 *            of sorting<br>
	 *            <b>pass <i>null</i> if you want to use the value as {@link Comparable} for sorting</b>
	 */
	public void setComparator(Comparator<F> comparator) {
		this.comparator = comparator;
	}

	/**
	 * @return The {@link Comparator} that is used by {@link Field#compare(Object, Object)} for purposes of sorting<br>
	 *         <i>null</i> if the value is used as a {@link Comparable}
	 */
	public Comparator<F> getComparator() {
		return comparator;
	}

	/**
	 * @return The {@link Class} of the value returned by this field
	 */
	public Class<F> getFieldType() {
		return fieldType;
	}

	/**
	 * Compares two beans by the value returned by {@link Field#getValue(Object)}<br>
	 * (Uses {@link Field#comparator} if provided, else tries to use the field value as {@link Comparable})
	 * 
	 * @param object1 First object for comparison
	 * @param object2 Second object for comparison
	 * 
	 * @return 1 if <i>object1</i>><i>object2</i><br>
	 *         0 if <i>object1</i>=<i>object2</i><br>
	 *         -1 if <i>object1</i><<i>object2</i>
	 * @throws UnsupportedOperationException if the field is not sortable ({@link Field#isSortable()}<br>
	 *             Or if the {@link Field#comparator} is not provided and {@link Field#getValue(Object)} does not
	 *             implement {@link Comparable}
	 */
	@SuppressWarnings("unchecked")
	public int compare(T object1, T object2) {
		if (!sortable)
			throw new UnsupportedOperationException("Field cannot be used for sorting and comparing.");

		F value1 = getValue(object1);
		F value2 = getValue(object2);

		if (comparator != null)
			return comparator.compare(value1, value2);

		if (value1 == null && value2 == null)
			return 0;
		if (value1 != null && value2 == null)
			return 1;
		if (value1 == null && value2 != null)
			return -1;

		if (value1 instanceof Comparable<?>)
			return ((Comparable<F>) value1).compareTo(value2);

		throw new UnsupportedOperationException("Unable to compare values!");
	}

	/**
	 * Fires any field event (notifies listeners that the event has been fired)
	 * 
	 * @param event {@link FieldEvent} to be fired
	 */
	protected void fireEvent(FieldEvent<T, F> event) {
		if (event == null)
			return;

		for (FieldEventListener<T, F> listener : listeners) {
			listener.handleModelFieldEvent(this, event);
		}
	}

	public void fireValueChanged(T object, F from) {
		fireEvent(new ValueChangedEvent<T, F>(object, from, this.getValue(object)));
	}

	public void fireValueChanged(T object) {
		fireEvent(new ValueChangedEvent<T, F>(object, null, this.getValue(object)));
	}

	private void fireReadOnlyPropertyChangedEvent() {
		fireEvent(new ReadOnlyPropertyChangedEvent<T, F>());
	}

	/**
	 * @return Unique name assigned to {@link Field field}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks if the field can be edited for the specified object<br>
	 * (Default implementation based only on {@link Field#readOnly read-only} attribute, subclasses should add more
	 * restrictions if necessary)<br>
	 * <b>If your class overrides this method, it should override {@link Field#getReasonForNotEditable(Object)}</b>
	 * 
	 * @param object Object for which to check edit-ability
	 * @return True if the field can be edited for specified object, false otherwise
	 */
	public boolean isEditableFor(T object) {
		if (object == null)
			return false;
		return !isReadOnly();
	}

	/**
	 * @param objects Objects for which to get the value of the field
	 * @return List of the field value for each object in the list
	 */
	public List<F> getValueList(List<T> objects) {
		if (objects == null)
			throw new IllegalArgumentException("Objects list cannot be null!");
		List<F> result = new ArrayList<F>(objects.size());
		for (T object : objects) {
			result.add(getValue(object));
		}
		return result;
	}

	/**
	 * @return Copy of this field
	 */
	@SuppressWarnings("unchecked")
	public Field<T, F> getCopy() {
		try {
			Field<T, F> clone = (Field<T, F>) this.clone();
			listeners.clear();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(
					"An exception occured while copying! All subclasses of Field must have PROTECTED EMPTY CONSTRUCTOR!",
					e);
		}
	}

	/**
	 * @param readOnly See {@link Field#setReadOnly(boolean) readOnly} attribute 
	 * @return Copy of this field with readOnly attribute set to <i>readOnly</i>
	 */
	public Field<T, F> getCopy(boolean readOnly) {
		Field<T, F> copy = getCopy();
		copy.setReadOnly(readOnly);
		return copy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comparator == null) ? 0 : comparator.hashCode());
		result = prime * result + ((fieldType == null) ? 0 : fieldType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Field<?, ?>))
			return false;
		Field<?, ?> other = (Field<?, ?>) obj;
		if (comparator == null) {
			if (other.comparator != null)
				return false;
		} else if (!comparator.equals(other.comparator))
			return false;
		if (fieldType == null) {
			if (other.fieldType != null)
				return false;
		} else if (!fieldType.equals(other.fieldType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
