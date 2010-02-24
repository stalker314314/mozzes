package org.mozzes.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mozzes.utils.StringUtils;

/**
 * Tests the StringUtils class
 * 
 * @author milos
 */
public class StringUtilsTest {

	/**
	 * Tests {@link StringUtils#joinParams(String, Object...)}
	 */
	@Test
	public void testJoinParams() {
		assertEquals("1", StringUtils.joinParams(null, 1));
		assertEquals("12", StringUtils.joinParams(null, 1, 2));
		assertEquals("1", StringUtils.joinParams(",", 1));
		assertEquals("1,2", StringUtils.joinParams(",", 1, 2));
		assertEquals("?, ?, ?", StringUtils.joinParams(", ", "?", "?", "?"));

		List<String> collection = new ArrayList<String>();
		collection.add("?");
		collection.add("?");
		collection.add("?");
		assertEquals("?, ?, ?", StringUtils.joinParams(", ", collection));

		String[] array = new String[] { "?", "?", "?" };
		assertEquals("?, ?, ?", StringUtils.joinParams(", ", (Object[]) array));

	}

	/**
	 * Tests {@link StringUtils#isEmpty(String)}
	 */
	@Test
	public void testIsEmpty() {
		assertTrue(StringUtils.isEmpty(null));
		assertTrue(StringUtils.isEmpty(""));
		assertTrue(StringUtils.isEmpty(" "));
		assertTrue(StringUtils.isEmpty("        "));
		assertFalse(StringUtils.isEmpty("a"));
		assertFalse(StringUtils.isEmpty("a a"));
		assertFalse(StringUtils.isEmpty("    a       "));
		assertFalse(StringUtils.isEmpty("a"));
	}

	/**
	 * Tests {@link StringUtils#defaultString(String)}
	 */
	@Test
	public void testDefaultString() {
		assertSame("", StringUtils.defaultString(null));
		assertSame("", StringUtils.defaultString(""));
		assertNotSame("", StringUtils.defaultString("a"));
	}

	/**
	 * Tests {@link StringUtils#isNotEmpty(String)}
	 */
	@Test
	public void testIsNotEmpty() {
		assertFalse(StringUtils.isNotEmpty(null));
		assertFalse(StringUtils.isNotEmpty(""));
		assertTrue(StringUtils.isNotEmpty("a"));
	}

	/**
	 * Tests {@link StringUtils#join(java.util.Iterator, String)}
	 */
	@Test
	public void testJoin() {
		List<String> list = new ArrayList<String>();
		assertNull(StringUtils.join(null, null));
		assertNull(StringUtils.join(null, ""));
		assertEquals("", StringUtils.join(list.iterator(), ""));
		list.add("a");
		assertEquals("a", StringUtils.join(list.iterator(), " "));
		list.add("a");
		assertEquals("a a", StringUtils.join(list.iterator(), " "));
		assertEquals("aa", StringUtils.join(list.iterator(), ""));
		assertEquals("aa", StringUtils.join(list.iterator(), null));
		assertEquals("a1a", StringUtils.join(list.iterator(), "1"));
	}

	/**
	 * Tests {@link StringUtils#split(String, char)}
	 */
	@Test
	public void testSplit() {
		assertNull(StringUtils.split(null, '-'));
		String[] s = StringUtils.split("", '-');
		assertEquals(0, s.length);

		s = StringUtils.split("-", '-');
		assertEquals(0, s.length);

		s = StringUtils.split("asd", '-');
		assertEquals(1, s.length);
		assertEquals("asd", s[0]);

		s = StringUtils.split("a-a", '-');
		assertEquals(2, s.length);
		assertEquals("a", s[0]);
		assertEquals("a", s[1]);

		s = StringUtils.split("ab-abc", '-');
		assertEquals(2, s.length);
		assertEquals("ab", s[0]);
		assertEquals("abc", s[1]);

		s = StringUtils.split("ab-abc-abcd-", '-');
		assertEquals(3, s.length);
		assertEquals("ab", s[0]);
		assertEquals("abc", s[1]);
		assertEquals("abcd", s[2]);

		s = StringUtils.split("aaa", 'a');
		assertEquals(0, s.length);
	}

	/**
	 * Tests {@link StringUtils#split(String, String)}
	 */
	@Test
	public void testSplitString() {
		assertNull(StringUtils.split(null, null));
		assertNull(StringUtils.split(null, ""));
		String s[] = StringUtils.split("", null);
		assertEquals(0, s.length);

		s = StringUtils.split("-", "-");
		assertEquals(0, s.length);

		s = StringUtils.split("asd", "-");
		assertEquals(1, s.length);
		assertEquals("asd", s[0]);

		s = StringUtils.split("a-a", "-");
		assertEquals(2, s.length);
		assertEquals("a", s[0]);
		assertEquals("a", s[1]);

		s = StringUtils.split("ab-abc", "-");
		assertEquals(2, s.length);
		assertEquals("ab", s[0]);
		assertEquals("abc", s[1]);

		s = StringUtils.split("ab-abc-abcd-", "-");
		assertEquals(3, s.length);
		assertEquals("ab", s[0]);
		assertEquals("abc", s[1]);
		assertEquals("abcd", s[2]);

		s = StringUtils.split("aaa", "a");
		assertEquals(0, s.length);

		s = StringUtils.split("a11ab11abc", "11");
		assertEquals(3, s.length);
		assertEquals("a", s[0]);
		assertEquals("ab", s[1]);
		assertEquals("abc", s[2]);

		s = StringUtils.split("11a11ab11", "11");
		assertEquals(2, s.length);
		assertEquals("a", s[0]);
		assertEquals("ab", s[1]);
	}

	/**
	 * Tests for {@link StringUtils#replace(String, String, String)}
	 */
	@Test
	public void testReplace() {
		assertNull(StringUtils.replace(null, null, null));
		assertEquals("asd", StringUtils.replace("asd", null, null));
		assertEquals("asd", StringUtils.replace("asd", "", null));
		assertEquals("asd", StringUtils.replace("asd", "a", null));
		assertEquals("asd", StringUtils.replace("asd", "a", "a"));
		assertEquals("sd", StringUtils.replace("asd", "a", ""));
		assertEquals("sd", StringUtils.replace("asada", "a", ""));
		assertEquals("ab11ab11", StringUtils.replace("ab1ab1", "ab", "ab1"));
	}

	/**
	 * Tests for {@link StringUtils#contains(String, String)}
	 */
	@Test
	public void testContains() {
		assertFalse(StringUtils.contains(null, null));
		assertFalse(StringUtils.contains(null, ""));
		assertFalse(StringUtils.contains(null, " a "));
		assertFalse(StringUtils.contains("", null));
		assertFalse(StringUtils.contains(" a ", null));
		assertTrue(StringUtils.contains("a", "a"));
		assertTrue(StringUtils.contains("ab", "a"));
		assertTrue(StringUtils.contains("ba", "a"));
		assertTrue(StringUtils.contains("bcacb", "a"));
		assertFalse(StringUtils.contains("ba", "ba1"));
	}

	/**
	 * Tests for {@link StringUtils#capitalize(String)}
	 */
	@Test
	public void testCapitalize() {
		assertEquals("Aaaa", StringUtils.capitalize("aaaa"));
		assertEquals("Aaaa", StringUtils.capitalize("Aaaa"));
		assertEquals("AaaA", StringUtils.capitalize("aaaA"));
		assertEquals("AAaA", StringUtils.capitalize("aAaA"));
		assertEquals("AAaA", StringUtils.capitalize("AAaA"));

		assertEquals("", StringUtils.capitalize(""));

		try {
			StringUtils.capitalize(null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Tests for {@link StringUtils#fillWithChar(char, int)}
	 */
	@Test
	public void testFillWithChars() {
		assertEquals("a", StringUtils.fillWithChar('a', 1));
		assertEquals("bbb", StringUtils.fillWithChar('b', 3));
		assertEquals("1111111111", StringUtils.fillWithChar('1', 10));
		assertEquals("", StringUtils.fillWithChar('c', 0));
		try {
			assertEquals("", StringUtils.fillWithChar('d', -1));
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Tests for {@link StringUtils#capitalizeName(String)}
	 */
	@Test
	public void testCapitalizeName() {
		// All names in lowercase.
		assertEquals("Marko Markovic", StringUtils.capitalizeName("marko markovic"));
		assertEquals("Dejan Petkovic Rambo", StringUtils.capitalizeName("dejan petkovic rambo"));
		assertEquals("Ivana Brlic-Mazuranic", StringUtils.capitalizeName("ivana brlic-mazuranic"));
		// Mixed uppercase and lowercase.
		assertEquals("Marko Markovic", StringUtils.capitalizeName("maRkO mArKOviC"));
		assertEquals("Ivana Brlic-Mazuranic", StringUtils.capitalizeName("IVAna brLIc-MAzuRANic"));
		assertEquals("Tim O'reilly", StringUtils.capitalizeName("tim o'Reilly"));
		// Names capitalization are already correct.
		assertEquals("Marko Markovic", StringUtils.capitalizeName("Marko Markovic"));
		assertEquals("Ivana Brlic-Mazuranic", StringUtils.capitalizeName("Ivana Brlic-Mazuranic"));
		//
		assertEquals("", StringUtils.capitalizeName(""));
		assertEquals("", StringUtils.capitalizeName("\n"));
		assertEquals("", StringUtils.capitalizeName("   "));
		assertEquals("", StringUtils.capitalizeName(" \n\t  "));
		assertEquals("Marko Markovic", StringUtils.capitalizeName(" marko markovic  "));

		try {
			StringUtils.capitalizeName(null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}
}
