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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mozzes.utils.filtering.FakeFilter;
import org.mozzes.utils.filtering.FilterUtils;

public class FilterUtilsTest {

  @Test
  public void testGetFilteredList() {
    FakeFilter<Integer> f = null;
    List<Integer> inputList = null;
    assertEquals(inputList, FilterUtils.getFilteredList(inputList, f));
    assertNull(FilterUtils.getFilteredList(inputList, f));

    f = new FakeFilter<Integer>();
    assertEquals(inputList, FilterUtils.getFilteredList(inputList, f));
    assertNull(FilterUtils.getFilteredList(inputList, f));

    inputList = new ArrayList<Integer>();
    assertEquals(inputList, FilterUtils.getFilteredList(inputList, f));
    assertTrue(FilterUtils.getFilteredList(inputList, f).isEmpty());

    inputList.add(1);
    inputList.add(3);
    assertEquals(inputList, FilterUtils.getFilteredList(inputList, f));
  }

}
