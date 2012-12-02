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
import org.mozzes.utils.filtering.PropertyFilter;

public class PropertyFilterTest {

  @Test
  @SuppressWarnings("unused")
  public void testPropertyFilter() {
    // Valid propertyName (should result in successful creation)
    try {
      PropertyFilter<TestClass, Integer> pf = new PropertyFilter<TestClass, Integer>("testAttribute", null) {
        @Override
        public boolean isAcceptable(TestClass object) {
          return false;
        }
      };
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    // Null propertyName (should result in IllegalArgumentException)
    try {
      PropertyFilter<TestClass, Integer> pf = new PropertyFilter<TestClass, Integer>(null, null) {
        @Override
        public boolean isAcceptable(TestClass object) {
          return false;
        }
      };
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    // Empty propertyName (should result in IllegalArgumentException)
    try {
      PropertyFilter<TestClass, Integer> pf = new PropertyFilter<TestClass, Integer>("", null) {
        @Override
        public boolean isAcceptable(TestClass object) {
          return false;
        }
      };
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    // Invalid propertyName (should result in successful creation, but will break later)
    try {
      PropertyFilter<TestClass, Integer> pf = new PropertyFilter<TestClass, Integer>("aa", null) {
        @Override
        public boolean isAcceptable(TestClass object) {
          return false;
        }
      };
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetPropertyValue() {
    TestClass object = new TestClass();
    object.setTestAttribute(12);
    PropertyFilter<TestClass, Integer> pf;

    // Valid propertyName
    pf = new PropertyFilter<TestClass, Integer>("testAttribute", null) {
      @Override
      public boolean isAcceptable(TestClass object) {
        return false;
      }
    };
    try {
      assertEquals(Integer.valueOf(12), pf.getPropertyValue(object));
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    // Invalid propertyName (Should result in a RuntimeException, caused by NoSuchFieldException)
    pf = new PropertyFilter<TestClass, Integer>("aa", null) {
      @Override
      public boolean isAcceptable(TestClass object) {
        return false;
      }
    };
    try {
      pf.getPropertyValue(object);
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testGetPropertyValueCollection() {
    TestClass object = new TestClass();
    object.setTestAttribute(12);
    List<Integer> testCollection = new ArrayList<Integer>();
    testCollection.add(1);
    testCollection.add(11);
    object.setTestCollection(testCollection);
    PropertyFilter<TestClass, Integer> pf;

    // Valid propertyName, but property not a collection (should result in ClassCastException)
    pf = new PropertyFilter<TestClass, Integer>("testAttribute", null) {
      @Override
      public boolean isAcceptable(TestClass object) {
        return false;
      }
    };
    try {
      pf.getPropertyValueCollection(object);
      fail();
    } catch (ClassCastException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    // Invalid propertyName (Should result in a RuntimeException, caused by NoSuchFieldException)
    pf = new PropertyFilter<TestClass, Integer>("aa", null) {
      @Override
      public boolean isAcceptable(TestClass object) {
        return false;
      }
    };
    try {
      pf.getPropertyValueCollection(object);
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

    // Valid propertyName, and property is a collection
    pf = new PropertyFilter<TestClass, Integer>("testCollection", null) {
      @Override
      public boolean isAcceptable(TestClass object) {
        return false;
      }
    };
    try {
      assertEquals(testCollection, pf.getPropertyValueCollection(object));
    } catch (Exception e) {
      fail();
    }
  }

  @SuppressWarnings("unused")
  private static class TestClass {
    private Integer testAttribute;
    private List<Integer> testCollection;

    public void setTestAttribute(Integer testAttribute) {
      this.testAttribute = testAttribute;
    }

    public Integer getTestAttribute() {
      return testAttribute;
    }

    public void setTestCollection(List<Integer> testCollection) {
      this.testCollection = testCollection;
    }

    public List<Integer> getTestCollection() {
      return testCollection;
    }
  }
}
