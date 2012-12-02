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
import org.mozzes.utils.filtering.Filter;
import org.mozzes.utils.filtering.InInternalCollectionFilter;

public class InInternalCollectionFilterTest {

  @Test
  public void testInInternalCollectionFilter() {
    // PropertyName null (IllegalArgumentException)
    try {
      new InInternalCollectionFilter<DummyClass, Integer>(null, null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    // PropertyName empty (IllegalArgumentException)
    try {
      new InInternalCollectionFilter<DummyClass, Integer>("", null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    // Invalid PropertyName (pass but will break latter)
    new InInternalCollectionFilter<DummyClass, Integer>("aa", null);
    // All OK (pass)
    new InInternalCollectionFilter<DummyClass, Integer>("testCollection", null);
    // All OK (pass)
    new InInternalCollectionFilter<DummyClass, Integer>("testCollection", 1);
  }

  @Test
  public void testIsAcceptable() {
    Filter<DummyClass> filter = new InInternalCollectionFilter<DummyClass, Integer>("testCollection", null);

    // Object null (false)
    DummyClass object = null;
    assertFalse(filter.isAcceptable(object));

    // Collection null (false)
    object = new DummyClass();
    assertFalse(filter.isAcceptable(object));

    // Collection empty (false)
    object = new DummyClass();
    List<Integer> testCollection = new ArrayList<Integer>();
    object.setTestCollection(testCollection);
    assertFalse(filter.isAcceptable(object));

    // Collection does not contain filter element, filter element is null (false)
    testCollection.add(1);
    assertFalse(filter.isAcceptable(object));

    // Collection does not contains filter element, filter element is null (true)
    testCollection.add(null);
    assertTrue(filter.isAcceptable(object));

    filter = new InInternalCollectionFilter<DummyClass, Integer>("testCollection", 2);

    // Collection does not contain filter element, filter element is 2 (false)
    testCollection.add(1);
    assertFalse(filter.isAcceptable(object));

    // Collection does not contains filter element, filter element is 2 (true)
    testCollection.add(2);
    assertTrue(filter.isAcceptable(object));
  }

}
