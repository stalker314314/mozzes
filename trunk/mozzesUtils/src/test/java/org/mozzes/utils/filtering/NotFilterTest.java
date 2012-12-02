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

import org.junit.Test;
import org.mozzes.utils.filtering.FakeFilter;
import org.mozzes.utils.filtering.Filter;
import org.mozzes.utils.filtering.NoResultsFilter;
import org.mozzes.utils.filtering.NotFilter;

public class NotFilterTest {

  @Test
  public void testIsAcceptable() {
    Filter<Integer> wrappedFilter = new NoResultsFilter<Integer>();
    NotFilter<Integer> notFilter = new NotFilter<Integer>(wrappedFilter);

    assertTrue(notFilter.isAcceptable(null));
    assertTrue(notFilter.isAcceptable(1));

    wrappedFilter = new FakeFilter<Integer>();
    notFilter.setWrappedFilter(wrappedFilter);
    assertFalse(notFilter.isAcceptable(null));
    assertFalse(notFilter.isAcceptable(1));

  }

  @Test
  public void testNotFilter() {
    Filter<Integer> wrappedFilter = null;
    try {
      new NotFilter<Integer>(wrappedFilter);
      fail();
    } catch (NullPointerException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    wrappedFilter = new NoResultsFilter<Integer>();
    new NotFilter<Integer>(wrappedFilter);
  }

}
