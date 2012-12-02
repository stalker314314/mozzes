/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
      propertyCollection = new HashSet<PropertyType>(ReflectionUtils.getPropertyForCollection(propertyClass,
          collection, property));
    } else {
      propertyCollection = new HashSet<PropertyType>();
      for (CollectionType item : collection) {
        propertyCollection.add(invoker.invoke(item));
      }
    }

    return propertyCollection.contains(compareValue);
  }
}
