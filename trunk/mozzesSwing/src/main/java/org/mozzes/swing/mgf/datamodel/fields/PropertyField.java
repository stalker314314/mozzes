package org.mozzes.swing.mgf.datamodel.fields;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datarenderer.DataRenderModel;
import org.mozzes.utils.reflection.ReflectionException;
import org.mozzes.utils.reflection.ReflectionUtils;
import org.mozzes.utils.reflection.ReflectiveMethod;


/**
 * Field which returns/sets the value of a specified property(or nested property) of the bean
 * 
 * @author milos
 * 
 * @param <T> Type of the bean
 * @param <F> Type of the property
 */
public class PropertyField<T, F> extends Field<T, F> {
	private LinkedList<ReflectiveMethod<Object, Object>> getters = null;
	private ReflectiveMethod<Object, Void> setter = null;

	/**
	 * Specified in camel-case, use dot ('.') for nested properties.<br>
	 * Example:
	 * 
	 * <code>
	 * <pre>
	 * class User {
	 * 	private String username;
	 * 	private Role role;
	 * }
	 * 
	 * class Role {
	 * 	private String name;
	 * }
	 * </pre>
	 * </code><b><i>*Fields have to have get and set methods</i></b><br>
	 * <b>Some possible Property Fields are:</b> <code>
	 * <pre>
	 * new PropertyField&lt;User, String&gt;(String.class, &quot;username&quot;);
	 * new PropertyField&lt;User, String&gt;(String.class, &quot;role.name&quot;);
	 * new PropertyField&lt;User, Role&gt;(Role.class, &quot;role&quot;);
	 * </pre>
	 * </code><br>
	 * 
	 */
	private String property;

	/**
	 * Internal flag to indicate that the property cannot be set due to the missing setter in the declaring class
	 */
	private Boolean noSetter = null;

	/**
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @see PropertyField#property
	 */
	public PropertyField(Class<F> propertyType, String property) {
		super(property, propertyType);
		this.property = property;
	}

	/**
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 */
	public PropertyField(Class<F> propertyType, String property, String header) {
		super(property, propertyType, header);
		this.property = property;
	}

	/**
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 */
	public PropertyField(Class<F> propertyType, String property, boolean readOnly) {
		super(property, propertyType, readOnly);
		this.property = property;
	}

	/**
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 */
	public PropertyField(Class<F> propertyType, String property, String header, boolean readOnly) {
		super(property, propertyType, header, readOnly);
		this.property = property;
	}

	/**
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public PropertyField(Class<F> propertyType, String property, boolean readOnly, boolean sortable) {
		super(property, propertyType, readOnly, sortable);
		this.property = property;
	}

	/**
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public PropertyField(Class<F> propertyType, String property, String header, boolean readOnly, boolean sortable) {
		super(property, propertyType, header, readOnly, sortable);
		this.property = property;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @see PropertyField#property
	 */
	public PropertyField(String name, Class<F> propertyType, String property) {
		super(name, propertyType);
		this.property = property;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 */
	public PropertyField(String name, Class<F> propertyType, String property, String header) {
		super(name, propertyType, header);
		this.property = property;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 */
	public PropertyField(String name, Class<F> propertyType, String property, boolean readOnly) {
		super(name, propertyType, readOnly);
		this.property = property;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 */
	public PropertyField(String name, Class<F> propertyType, String property, String header, boolean readOnly) {
		super(name, propertyType, header, readOnly);
		this.property = property;
	}

	/**
	 * @param name The name of this field (used for easier referencing to the field using
	 *            {@link DataModel#getField(String)} or {@link DataModel#getField(String, Class)}
	 * @param propertyType {@link Class} of the property
	 * @param property Name of the property specified in camel-case, use dot ('.') for nested properties<br>
	 *            <b>For an example {@link PropertyField#property}</b>
	 * @param header The localized resource to be used by some {@link DataRenderModel} to display the field's name
	 * @param readOnly Specifies is the field read-only or not (if yes calling setter will throw
	 *            {@link UnsupportedOperationException})
	 * @param sortable Specifies is the field can be used for sorting objects<br>
	 *            <b>(if not, calling {@link Field#compare(Object, Object)} will throw
	 *            {@link UnsupportedOperationException})</b>
	 */
	public PropertyField(String name, Class<F> propertyType, String property, String header, boolean readOnly,
			boolean sortable) {
		super(name, propertyType, header, readOnly, sortable);
		this.property = property;
	}

	/**
	 * <em>*Used internally</em><br>
	 * Returns the value of a nested property at the specified depth
	 * 
	 * @param object Object for which to return value
	 * @param depth Depth to which to go (Specify -1 to go to the last property in chain)<br>
	 *            <b>Example:</b> for "employee.user.role.name", depth of 2 would return
	 *            <code>object.getEmployee().getUser().getRole();</code>
	 * @return Value of a nested property at the specified depth(null if any property in chain is null)
	 * @throws ReflectionException if the passed object does not contain such property chain, or some property in the
	 *             chain has inaccessible(non-public) getter
	 */
	private Object getPropertyValue(T object, int depth) throws ReflectionException {
		if (getters == null)
			createMethodCache(object.getClass());
		int goTo = depth;
		if (goTo == -1)
			goTo = getters.size() - 1;

		Object o = object;

		for (int i = 0; i <= goTo; i++) {
			ReflectiveMethod<Object, Object> getter = getters.get(i);
				if (o == null)
					return null;
				o = getter.invoke(o);
			}

		return o;
	}

	@Override
	@SuppressWarnings("unchecked")
	public F getValue(T object) throws ReflectionException {
		if (object == null)
			return null;

		F result = (F) getPropertyValue(object, -1);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void valueSetter(T object, F value) throws ReflectionException, ClassCastException {
		// the check is already done by superclass using isEditableFor (every check must be implemented there )
		// if (!isEditableFor(object))
		// throw new IllegalArgumentException(getReasonForNotEditable(object));

		Object last = getPreviousToLastObjectInChain(object);

		if (getters == null)
			createMethodCache(object.getClass());

		Class<?> parameterType = setter.getParameterTypes()[0];
		F realValue = value;
		if (parameterType.isPrimitive() && value == null)
			realValue = (F) ReflectionUtils.getDefaultValueOf(parameterType);
		// throw new IllegalArgumentException("Cannot set null for primitive type property: " + property);

		Class<?> nonPrimitiveType = ReflectionUtils.resolvePrimitiveType(parameterType);
		if (realValue != null && !nonPrimitiveType.isAssignableFrom(realValue.getClass()))
			throw new ClassCastException("Ivalid proprerty type");

			setter.invoke(last, realValue);
		}

	private Object getPreviousToLastObjectInChain(T object) {
		if (getters == null)
			createMethodCache(object.getClass());
		return getters.size() == 1 ? object : getPropertyValue(object, getters.size() - 2);
	}

	@Override
	public boolean isEditableFor(T object) {
		boolean result;
		result = object != null;
		result = result && getPreviousToLastObjectInChain(object) != null;
		result = result && (noSetter == null || !noSetter);
		return super.isEditableFor(object) && result;
	}

	@Override
	protected String getReasonForNotEditable(T object) {
		if (object == null)
			return "Object must not be null!";
		if (getPreviousToLastObjectInChain(object) == null)
			return "Chain broken! (Some property in chain is null)";
		if (noSetter)
			return "Object does not have a setter for the specified property!";
		return null;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if (noSetter != null && noSetter && !readOnly)
			throw new IllegalArgumentException("Property cannot be editable due to missing setter!");
		super.setReadOnly(readOnly);
	}

	private void setNoSetter(Boolean noSetter) {
		this.noSetter = noSetter;
		// if (noSetter == true)
		// setReadOnly(true);
		if (noSetter == true && !isReadOnly())
			throw new IllegalArgumentException(String.format(
					"Property \"%s\" cannot be edited due to missing setter!", property));
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return Property chain string for this field
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * This method is called only once per field instance, the first time a {@link PropertyField#getValue(Object)} or
	 * {@link PropertyField#setValue(Object, Object)} is called<br>
	 * Constructs the reflection {@link Method methods} cache
	 * 
	 * @param clazz {@link Class} of the bean
	 * @throws ReflectionException if some of the {@link PropertyField#property properties} in the chain do not exist or
	 *             are inaccessible
	 */
	@SuppressWarnings("unchecked")
	private void createMethodCache(Class<?> clazz) throws ReflectionException {
		getters = new LinkedList<ReflectiveMethod<Object, Object>>();
		setter = null;
		String[] properties = getPropertyList(property);

		Class<?> cClazz = clazz;
		for (int i = 0; i < properties.length; i++) {
			String prop = properties[i];
			ReflectiveMethod<Object, Object> getter = null;
			try {
				getter = ReflectionUtils.getGetter((Class<Object>) cClazz, prop);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException(String.format("Ivalid property chain: \"%s\", property: \"%s\"",
						property, prop));
			} catch (ReflectionException e) {
				throw new IllegalArgumentException(String.format("Ivalid property chain: \"%s\", property: \"%s\"",
						property, prop));
			}

			if (getter == null)
				throw new IllegalStateException("This should never happen!");

			getters.add(getter);
			if (i == properties.length - 1) {
				Class<Object> realType = ReflectionUtils.resolvePrimitiveType(getter.getReturnType());
				if (!getFieldType().isAssignableFrom(realType))
					throw new ClassCastException(
							String.format("Invalid property type! " +
							"Defined type: \"%s\", Real Type: \"%s\", Property: \"%s\"",
							getFieldType().getSimpleName(), realType.getSimpleName(), property));
				try {
					setter = ReflectionUtils.getSetter((Class<Object>) cClazz, getFieldType(), prop);
					setNoSetter(false);
				} catch (ReflectionException e) {
					setNoSetter(true);
				}
			}
			cClazz = getter.getReturnType();
		}

	}

	/**
	 * Splits the string on dots and returns the resulting array
	 * 
	 * @param propertyName The string to split
	 * @return {@link Array} of strings representing property names in the chain
	 */
	private String[] getPropertyList(String propertyName) {
		if (propertyName == null || propertyName.length() == 0)
			throw new IllegalArgumentException("PropertyName must not be null or empty!");

		StringTokenizer tokenizer = new StringTokenizer(propertyName, ".", false);
		String[] properties = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			properties[i] = tokenizer.nextToken();
			i++;
		}
		return properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof PropertyField<?, ?>))
			return false;
		PropertyField<?, ?> other = (PropertyField<?, ?>) obj;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertyField [property=" + property + ", getFieldType()=" + getFieldType() + ", getHeader()="
				+ getHeader() + ", getName()=" + getName() + ", isReadOnly()=" + isReadOnly() + ", isSortable()="
				+ isSortable() + "]";
	}

}
