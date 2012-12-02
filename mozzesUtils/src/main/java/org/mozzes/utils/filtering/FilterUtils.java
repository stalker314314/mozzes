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

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {

  /**
   * Primenjuje se filter na prosledjenu listu i vraca rezultujuca lista.
   * 
   * @param inputList
   *          - lista koja se filtrira
   * @param filter
   *          - ako je null vraca se originalna lista
   * @return filtrirana lista
   */
  public static <T> List<T> getFilteredList(List<T> inputList, Filter<T> filter) {
    List<T> filteredList = inputList;

    if (filter != null && inputList != null) {
      filteredList = new ArrayList<T>();

      for (T objectFromList : inputList) {
        if (filter.isAcceptable(objectFromList))
          filteredList.add(objectFromList);
      }
    }

    return filteredList;
  }

}
