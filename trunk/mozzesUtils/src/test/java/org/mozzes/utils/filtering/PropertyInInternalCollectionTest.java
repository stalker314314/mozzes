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
import org.mozzes.utils.filtering.PropertyInInternalCollection;
import org.mozzes.utils.reflection.Invoker;

public class PropertyInInternalCollectionTest {

  @Test
  public void testIsAcceptable() {
    PropertyInInternalCollection<DummyClass, Integer, Class<?>> filter = new PropertyInInternalCollection<DummyClass, Integer, Class<?>>(
        "testCollection", Integer.class, "class");

    // Object null
    DummyClass object = null;
    assertFalse(filter.isAcceptable(object));

    // Collection null
    object = new DummyClass();
    assertFalse(filter.isAcceptable(object));

    // Collection empty
    List<Integer> testCollection = new ArrayList<Integer>();
    object.setTestCollection(testCollection);
    assertFalse(filter.isAcceptable(object));

    // Collection contains valid entries
    testCollection.add(5);
    assertTrue(filter.isAcceptable(object));

  }

  @Test
  public void testPropertyInInternalCollectionStringPropertyTypeInvokerOfPropertyTypeCollectionType() {
    Invoker<Class<?>, Integer> invoker = null;
    // Invoker null (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", Integer.class, invoker);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    invoker = new Invoker<Class<?>, Integer>() {
      @Override
      public Class<?> invoke(Integer object) {
        return Integer.class;
      }
    };
    // Collection name null (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>(null, Integer.class, invoker);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // Collection name empty (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("", Integer.class, invoker);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // Compare value null (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", null, invoker);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // Collection name invalid(pass, but will break latter)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("aaa", Integer.class, invoker);
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testPropertyInInternalCollectionStringPropertyTypeString() {
    // PropertyName null (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", Integer.class, (String) null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // PropertyName empty (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", Integer.class, "");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // PropertyName invalid (pass, but will break latter)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", Integer.class, "aaa");
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // Collection name null (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>(null, Integer.class, "class");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // Collection name empty (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("", Integer.class, "class");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // Compare value null (IllegalArgumentException)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("testCollection", null, "class");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
    // Collection name invalid(pass, but will break latter)
    try {
      new PropertyInInternalCollection<DummyClass, Integer, Class<?>>("aaa", Integer.class, "class");
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

}
