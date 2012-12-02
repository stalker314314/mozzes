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

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.utils.misc.BooleanComparator;

public class BooleanComparatorTest {

  @Test
  public void testCompare() {
    BooleanComparator comparator = new BooleanComparator();
    assertEquals(0, comparator.compare(true, true));
    assertEquals(0, comparator.compare(false, false));
    assertEquals(1, comparator.compare(true, false));
    assertEquals(-1, comparator.compare(false, true));
  }
}
