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
package org.mozzes.utils.misc;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This is a comparator for Boolean values which treats TRUE as a greater value than FALSE.<br>
 * Thus:<br>
 * <b> compare(true,true) = compare(false,false) = 0<br>
 * compare(true,false) = 1<br>
 * compare(false,true) = -1<br>
 * </b>
 * 
 * @author milos
 */
public class BooleanComparator implements Comparator<Boolean>, Serializable {
  private static final long serialVersionUID = 1L;

  @Override
  public int compare(Boolean o1, Boolean o2) {
    int b1 = o1.booleanValue() ? 0 : 1;
    int b2 = o2.booleanValue() ? 0 : 1;

    return b2 - b1;
  }

}
