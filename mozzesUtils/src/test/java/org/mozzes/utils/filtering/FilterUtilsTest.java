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
