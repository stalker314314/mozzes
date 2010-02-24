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
		assertEquals(
				new NoResultsFilter<DummyClass>().isAcceptable(object),
				new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>()).isAcceptable(object));

		object = new DummyClass();
		assertEquals(
				new NoResultsFilter<DummyClass>().isAcceptable(object),
				new ExpressionFilter<DummyClass>(new NoResultsFilter<DummyClass>()).isAcceptable(object));
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
