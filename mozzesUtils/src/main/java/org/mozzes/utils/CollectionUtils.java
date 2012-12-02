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
package org.mozzes.utils;

import java.lang.reflect.Array;
import java.util.Collection;

import org.mozzes.utils.reflection.ReflectionException;

/**
 * Static helper methods for use with Collections
 * 
 * @author milos
 */
public class CollectionUtils {

  /**
   * Returns the first object from a List or null if List is empty
   */
  public static <T> T getFirstOrNull(Collection<T> collection) {
    if (collection == null || collection.isEmpty())
      return null;
    return collection.iterator().next();
  }

  /**
   * Converts the collection to an array
   * 
   * @param <T>
   *          Class of the elements in the collection
   * @param clazz
   *          Class of the elements in the collection
   * @param collection
   *          Collection of elements
   * @return Array of type T containing all elemenents of the collection
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] asArray(Class<T> clazz, Collection<T> collection) {
    T[] array = (T[]) Array.newInstance(clazz, collection.size());
    return collection.toArray(array);
  }

  /**
   * @param collection
   *          Kolekcija koju treba kopirati
   * @return Kopiju kolekcije
   */
  @SuppressWarnings("unchecked")
  public static <T extends Collection<U>, U> T copy(T collection) throws ReflectionException {
    if (collection == null)
      throw new NullPointerException();
    try {
      return (T) collection.getClass().getConstructor(Collection.class).newInstance(collection);
    } catch (Exception e) {
      throw new ReflectionException(e);
    }
  }
}
