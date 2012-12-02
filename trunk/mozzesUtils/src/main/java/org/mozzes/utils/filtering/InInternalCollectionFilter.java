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

import java.util.Collection;

/**
 * [LC-IMPL] [TLB-2810] [nenadl] Filter koji proverava da li se odredjena vrednost nalazi u kolekciji vrednosti u okviru
 * objekta koji se filtrira.
 * 
 * @author nenadl
 */
public class InInternalCollectionFilter<ObjectType, PropertyType> extends PropertyFilter<ObjectType, PropertyType> {

  /**
   * Konstruktor.
   * 
   * @param collectionName
   *          - naziv kolekcije vrednosti u okviru objekta.
   * @param compareValue
   *          - vrednost za koju se proverava da li postoji u kolekciji.
   */
  public InInternalCollectionFilter(String collectionName, PropertyType compareValue) {
    super(collectionName, compareValue);
  }

  /**
   * Proverava da li odredjeni objekat prolazi kroz filter.
   * 
   * @param object
   *          - objekat koji treba proveriti.
   * @return - da li objekat prolazi kroz filter.
   */
  public boolean isAcceptable(ObjectType object) {

    if (object == null) {
      return false;
    }

    Collection<PropertyType> propertyCollection = getPropertyValueCollection(object);

    boolean filterPassed = false;
    if (propertyCollection != null) {
      filterPassed = propertyCollection.contains(getCompareValue());
    }

    return filterPassed;
  }
}
