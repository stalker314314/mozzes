package org.mozzes.swing.mgf.datamodel.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.events.DataModelEvent;
import org.mozzes.swing.mgf.datamodel.events.DataModelEventListener;
import org.mozzes.swing.mgf.datamodel.events.FieldAddedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldMovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldRemovedEvent;
import org.mozzes.swing.mgf.datamodel.events.FieldValueUpdatedEvent;
import org.mozzes.swing.mgf.datamodel.events.field.FieldEvent;
import org.mozzes.swing.mgf.datamodel.events.field.FieldEventListener;
import org.mozzes.swing.mgf.datamodel.events.field.ValueChangedEvent;
import org.mozzes.utils.reflection.Invoker;
import org.mozzes.utils.reflection.ReflectionUtils;


/**
 * {@link DataModel} implementation
 * 
 * @author milos
 * @param <T> Type of a bean for which this model is providing fields({@link Field})
 */
public class DefaultDataModel<T> implements DataModel<T>, Serializable {
	private static final long serialVersionUID = 15L;

	private final List<DataModelEventListener<T>> listeners = new LinkedList<DataModelEventListener<T>>();
	private final List<Field<T, ?>> fields = new LinkedList<Field<T, ?>>();
	private final InternalHandlers handlers = new InternalHandlers();

	@Override
	public void addEventListener(DataModelEventListener<T> listener) {
		if (listener == null)
			throw new IllegalArgumentException("Listener must not be null!");
		listeners.add(listener);
	}

	@Override
	public void removeEventListener(DataModelEventListener<T> listener) {
		listeners.remove(listener);
	}

	@Override
	public DataModel<T> addField(Field<T, ?> field) {
		return addField(getFieldsNumber(), field);
	}

	@Override
	public DataModel<T> addField(int index, Field<T, ?> field) {
		if (field == null)
			throw new IllegalArgumentException("Field cannot be null!");

		if (field.getName() != null && field.getName().isEmpty())
			throw new IllegalArgumentException(
					"Field cannot have empty name, name cann be null but not an empty string!");

		Invoker<String, Field<T, ?>> invoker = new Invoker<String, Field<T, ?>>() {
			@Override
			public String invoke(Field<T, ?> object) {
				return object.getName();
			}
		};

		List<String> fieldNames = ReflectionUtils.getPropertyForCollection(getFields(), invoker);
		if (field.getName() != null && fieldNames.contains(field.getName()))
			throw new IllegalArgumentException("Model already contains a field with specified name: " + field.getName());

		fields.add(index, field);
		registerFieldListener(field);
		fireEvent(new FieldAddedEvent<T>(index, field));
		return this;
	}

	@Override
	public Field<T, Object> getField(String name) {
		return getField(name, Object.class);
	}

	@Override
	public <F> Field<T, F> getField(String name, Class<F> clazz) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be null or empty!");
		for (int i = 0; i < getFieldsNumber(); i++) {
			if (getField(i).getName() != null && getField(i).getName().equals(name))
				return getField(i, clazz);
		}
		throw new IllegalArgumentException("Field '" + name + "' not found!");
	}

	@Override
	public Field<T, Object> getField(int index) {
		return getField(index, Object.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <F> Field<T, F> getField(int index, Class<F> clazz) {
		Field<T, ?> field = fields.get(index);
		if (!clazz.isAssignableFrom(field.getFieldType()))
			throw new ClassCastException("Invalid field type!");
		return (Field<T, F>) field;
	}

	@Override
	public Object getFieldValue(int index, T object) {
		return getFieldValue(index, object, Object.class);
	}

	@Override
	public <F> F getFieldValue(int index, T object, Class<F> clazz) {
		return getField(index, clazz).getValue(object);
	}

	@Override
	public List<Field<T, ?>> getFields() {
		return Collections.unmodifiableList(fields);
	}

	@Override
	public int getFieldsNumber() {
		return fields.size();
	}

	@Override
	public void moveField(int from, int to) {
		if (from == to)
			return;
		if (to < 0 || to >= fields.size())
			throw new IndexOutOfBoundsException();

		Field<T, ?> field = fields.remove(from);

		if (to == fields.size()) {
			fields.add(field);
		} else if (to > from || to < from) {
			fields.add(to, field);
		}
		fireEvent(new FieldMovedEvent<T>(from, to, field));
	}

	@Override
	public void moveField(Field<T, ?> field, int to) {
		if (!fields.contains(field))
			throw new IllegalArgumentException("Field you have provided is not contained by the model!");
		int i = 0;
		for (Field<T, ?> f : fields) {
			if (f.equals(field)) {
				break;
			}
			i++;
		}
		moveField(i, to);
	}

	@Override
	public Field<T, ?> removeField(int index) {
		Field<T, ?> field = fields.remove(index);
		unregisterFieldListener(field);
		fireEvent(new FieldRemovedEvent<T>(index, field));
		return field;
	}

	@Override
	public boolean removeField(Field<T, ?> field) {
		int i = 0;
		for (Field<T, ?> f : fields) {
			if (f.equals(field))
				break;
			i++;
		}

		boolean removed = fields.remove(field);
		if (removed) {
			unregisterFieldListener(field);
			fireEvent(new FieldRemovedEvent<T>(i, field));
		}
		return removed;
	}

	/**
	 * <em>For internal use ONLY, do not make this public  NEVER EVER.<br>
	 * Add a method which fires a specific event to the {@link DataModel} interface if you absolutely need it</em><br>
	 * Fires an event
	 * 
	 * @param event {@link DataModelEvent} to fire
	 */
	protected void fireEvent(DataModelEvent<T> event) {
		if (event == null)
			return;

		for (DataModelEventListener<T> listener : listeners) {
			listener.handleDataModelEvent(this, event);
		}
	}

	@SuppressWarnings("unchecked")
	private void registerFieldListener(Field<T, ?> field) {
		((Field<T, Object>) field).addEventListener(handlers);
	}

	@SuppressWarnings("unchecked")
	private void unregisterFieldListener(Field<T, ?> field) {
		((Field<T, Object>) field).removeEventListener(handlers);
	}

	@Override
	public int indexOf(String name) {
		for (int i = 0; i < fields.size(); i++) {
			Field<T, ?> field = fields.get(i);
			if (name.equals(field.getName()))
				return i;
		}
		return -1;
	}
	
	@Override
	public int indexOf(Field<T, ?> field) {
		if(field==null)
			return -1;
		for (int i = 0; i < fields.size(); i++) {
			Field<T, ?> f = fields.get(i);
			if (f.equals(field))
				return i;
		}
		return -1;
	}

	/**
	 * <em>Exists for ease of use and improved readability</em><br>
	 * Class that groups all the handlers which {@link DataModel} uses<br>
	 * 
	 * @author milos
	 */
	private class InternalHandlers implements FieldEventListener<T, Object> {

		@Override
		public void handleModelFieldEvent(Field<T, Object> field, FieldEvent<T, Object> event) {
			if (event instanceof ValueChangedEvent<?, ?>)
				propagateValueChanged(field, (ValueChangedEvent<T, Object>) event);
		}

		private void propagateValueChanged(Field<T, Object> field, ValueChangedEvent<T, Object> event) {
			int fieldIndex = 0;
			for (Field<T, ?> f : fields) {
				if (f.equals(field))
					break;
				fieldIndex++;
			}
			fireEvent(new FieldValueUpdatedEvent<T>(fieldIndex, field, event.getForObject(), event.getFrom(), event
					.getTo()));
		}
	}
}
