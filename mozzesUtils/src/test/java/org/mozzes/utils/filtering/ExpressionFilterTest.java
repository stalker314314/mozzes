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
import org.mozzes.utils.filtering.ExpressionFilter;
import org.mozzes.utils.filtering.FakeFilter;
import org.mozzes.utils.filtering.NoResultsFilter;

public class ExpressionFilterTest {

  @Test
  public void testExpressionFilter() {
    assertTrue(new ExpressionFilter<DummyClass>().isAcceptable(null));
    assertTrue(new ExpressionFilter<DummyClass>().isAcceptable(new DummyClass()));
  }

  @Test
  public void testExpressionFilterFilterOfT() {

    DummyClass object = null;
    assertEquals(new NoResultsFilter<DummyClass>().isAcceptable(object), new ExpressionFilter<DummyClass>(
        new NoResultsFilter<DummyClass>()).isAcceptable(object));

    object = new DummyClass();
    assertEquals(new NoResultsFilter<DummyClass>().isAcceptable(object), new ExpressionFilter<DummyClass>(
        new NoResultsFilter<DummyClass>()).isAcceptable(object));
  }

  @Test
  public void testAnd() {

    DummyClass object = null;
    ExpressionFilter<DummyClass> operand1 = new ExpressionFilter<DummyClass>();
    ExpressionFilter<DummyClass> operand2 = new ExpressionFilter<DummyClass>();

    // true && true = true
    assertTrue(operand1.and(operand2).isAcceptable(object));

    // false && false = false
    operand1 = new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>());
    operand2 = new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>());
    assertFalse(operand1.and(operand2).isAcceptable(object));

    // false && true = false
    operand1 = new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>());
    operand2 = new ExpressionFilter<DummyClass>(new FakeFilter<DummyClass>());
    assertFalse(operand1.and(operand2).isAcceptable(object));

    // true && false = false
    operand1 = new ExpressionFilter<DummyClass>(new FakeFilter<DummyClass>());
    operand2 = new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>());
    assertFalse(operand1.and(operand2).isAcceptable(object));
  }

  @Test
  public void testOr() {
    DummyClass object = null;
    ExpressionFilter<DummyClass> operand1 = new ExpressionFilter<DummyClass>();
    ExpressionFilter<DummyClass> operand2 = new ExpressionFilter<DummyClass>();

    // true || true = true
    assertTrue(operand1.or(operand2).isAcceptable(object));

    // false || false = false
    operand1 = new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>());
    operand2 = new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>());
    assertFalse(operand1.or(operand2).isAcceptable(object));

    // false || true = true
    operand1 = new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>());
    operand2 = new ExpressionFilter<DummyClass>(new FakeFilter<DummyClass>());
    assertTrue(operand1.or(operand2).isAcceptable(object));

    // true || false = true
    operand1 = new ExpressionFilter<DummyClass>(new FakeFilter<DummyClass>());
    operand2 = new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>());
    assertTrue(operand1.or(operand2).isAcceptable(object));
  }

}
