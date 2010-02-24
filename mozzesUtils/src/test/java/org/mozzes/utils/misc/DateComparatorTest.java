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
