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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.mozzes.utils.misc.DateComparator;

public class DateComparatorTest {
	
	@Test
	public void testCompare() throws ParseException {
		DateComparator comparator = new DateComparator();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date31Dec2009_1200 = sdf.parse("31-12-2009 12:00:00");
		Date date22Jan2010_1200 = sdf.parse("22-01-2010 12:00:00");
		Date date22Jan2010_2300 = sdf.parse("22-01-2010 23:00:00");
		
		assertEquals(0, comparator.compare(date22Jan2010_1200, date22Jan2010_2300));
		assertEquals(1, comparator.compare(date22Jan2010_1200, date31Dec2009_1200));
		assertEquals(-1, comparator.compare(date31Dec2009_1200, date22Jan2010_1200));

		DateComparator comparatorWithTime = new DateComparator(false);
		assertEquals(0, comparatorWithTime.compare(date22Jan2010_1200, date22Jan2010_1200));
		assertEquals(-1, comparatorWithTime.compare(date22Jan2010_1200, date22Jan2010_2300));
		assertEquals(1, comparatorWithTime.compare(date22Jan2010_1200, date31Dec2009_1200));
		assertEquals(-1, comparatorWithTime.compare(date31Dec2009_1200, date22Jan2010_1200));
	}
}
