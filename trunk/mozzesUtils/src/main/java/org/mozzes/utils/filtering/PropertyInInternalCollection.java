package org.mozzes.utils.filtering;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mozzes.utils.reflection.Invoker;
import org.mozzes.utils.reflection.ReflectionException;
import org.mozzes.utils.reflection.ReflectionUtils;


/**
 * Filtrira po zadatom atributu nekog elementa u kolekciji koja je atribut objekta koji se filtrira<br>
 * (poredi sa compare value)
 * 
 * @author milos
 */
public class PropertyInInternalCollection<ObjectType, CollectionType, PropertyType> extends
		InInternalCollectionFilter<ObjectType, CollectionType> {

	Invoker<PropertyType, CollectionType> invoker;
	PropertyType compareValue;
	private String property;
	private Class<PropertyType> propertyClass;

	@SuppressWarnings("unchecked")
	public PropertyInInternalCollection(String collectionName, PropertyType compareValue,
			Invoker<PropertyType, CollectionType> invoker) {
		super(collectionName, null);
		this.compareValue = compareValue;

		if (invoker == null) {
			throw new IllegalArgumentException();
		}
		this.invoker = invoker;

		try {
			ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
			propertyClass = (Class<PropertyType>) pt.getActualTypeArguments()[3];
		} catch (Exception e) {
			if (compareValue == null) {
				throw new IllegalArgumentException();
			}
			propertyClass = (Class<PropertyType>) compareValue.getClass();
		}
	}

	@SuppressWarnings("unchecked")
	public PropertyInInternalCollection(String collectionName, PropertyType compareValue, String property) {
		super(collectionName, null);
		this.compareValue = compareValue;
		if (property == null || property.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.property = property;

		try {
			ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
			propertyClass = (Class<PropertyType>) pt.getActualTypeArguments()[3];
		} catch (Exception e) {
			if (compareValue == null) {
				throw new IllegalArgumentException();
			}
			propertyClass = (Class<PropertyType>) compareValue.getClass();
		}
	}

	@Override
	public boolean isAcceptable(ObjectType object) throws ReflectionException {
		if (object == null) {
			return false;
		}

		Collection<CollectionType> collection = getPropertyValueCollection(object);

		if (collection == null) {
			return false;
		}

		Set<PropertyType> propertyCollection = null;

		if (property != null && !property.isEmpty()) {
			propertyCollection = new HashSet<PropertyType>(ReflectionUtils
					.getPropertyForCollection(propertyClass, collection, property));
		} else {
			propertyCollection = new HashSet<PropertyType>();
			for (CollectionType item : collection) {
				propertyCollection.add(invoker.invoke(item));
			}
		}

		return propertyCollection.contains(compareValue);
	}
}
