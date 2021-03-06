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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class CollectionUtilsTest {

  @Test
  public void testGetFirstOrNull() {

    Collection<String> testData = null;
    assertEquals(null, CollectionUtils.getFirstOrNull(testData));

    testData = new ArrayList<String>();
    assertEquals(null, CollectionUtils.getFirstOrNull(testData));
    testData.add("aaa");
    assertEquals("aaa", CollectionUtils.getFirstOrNull(testData));

    testData.add("bbb");
    assertEquals("aaa", CollectionUtils.getFirstOrNull(testData));

    testData = new HashSet<String>();
    assertEquals(null, CollectionUtils.getFirstOrNull(testData));

    testData.add("aaa");
    assertEquals("aaa", CollectionUtils.getFirstOrNull(testData));

    testData.add("aaa");
    assertEquals("aaa", CollectionUtils.getFirstOrNull(testData));

    testData.add("bbb");
    assertEquals("aaa", CollectionUtils.getFirstOrNull(testData));
  }

  @Test
  public void testAsArray() {
    Collection<String> s = null;
    try {
      CollectionUtils.asArray(String.class, s);
      fail();
    } catch (NullPointerException e) {
      assertTrue(true);
    }

    s = new ArrayList<String>();
    assertArrayEquals(new String[] {}, CollectionUtils.asArray(String.class, s));
    s.add("aaa");
    assertArrayEquals(new String[] { "aaa" }, CollectionUtils.asArray(String.class, s));
    s.add("bbb");
    assertArrayEquals(new String[] { "aaa", "bbb" }, CollectionUtils.asArray(String.class, s));
    s.add("aaa");
    assertArrayEquals(new String[] { "aaa", "bbb", "aaa" }, CollectionUtils.asArray(String.class, s));

    s = new HashSet<String>();
    assertArrayEquals(new String[] {}, CollectionUtils.asArray(String.class, s));
    s.add("aaa");
    assertArrayEquals(new String[] { "aaa" }, CollectionUtils.asArray(String.class, s));
    s.add("bbb");
    assertArrayEquals(new String[] { "aaa", "bbb" }, CollectionUtils.asArray(String.class, s));
    s.add("aaa");
    assertArrayEquals(new String[] { "aaa", "bbb" }, CollectionUtils.asArray(String.class, s));

    s = new HashSet<String>();
    assertArrayEquals(new String[] {}, CollectionUtils.asArray(String.class, s));
    s.add("aaa");
    assertArrayEquals(new String[] { "aaa" }, CollectionUtils.asArray(String.class, s));
    s.add("bbb");

    Matcher<String[]> matcher = new BaseMatcher<String[]>() {

      @Override
      public boolean matches(Object result) {
        String[] resultArray = (String[]) result;
        if (Arrays.equals(new String[] { "aaa", "bbb" }, resultArray)
            || Arrays.equals(new String[] { "aaa", "bbb" }, resultArray)) {
          return true;
        }
        return false;
      }

      @Override
      public void describeTo(Description description) {
      }
    };
    assertThat(CollectionUtils.asArray(String.class, s), matcher);
  }

  @Test
  public void testCopy() {
    Collection<String> c = null;

    try {
      CollectionUtils.copy(c);
      fail();
    } catch (NullPointerException e) {
      assertTrue(true);
    }

    c = new ArrayList<String>();
    assertEquals(c, CollectionUtils.copy(c));
    assertNotSame(c, CollectionUtils.copy(c));

    c.add("aaa");
    assertEquals(c, CollectionUtils.copy(c));
    assertNotSame(c, CollectionUtils.copy(c));

    c = new HashSet<String>();
    assertEquals(c, CollectionUtils.copy(c));
    assertNotSame(c, CollectionUtils.copy(c));

    c.add("aaa");
    assertEquals(c, CollectionUtils.copy(c));
    assertNotSame(c, CollectionUtils.copy(c));
  }
}
