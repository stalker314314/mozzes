package org.mozzes.swing.mgf.translators;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.swing.mgf.translation.TranslationException;
import org.mozzes.swing.mgf.translation.translators.IntegerToString;


public class StringToIntegerTest {
	IntegerToString integerToString = new IntegerToString();

	@Test
	public void testTranslateTo() {
		try {
			assertEquals("10", integerToString.translateTo(10));
			assertEquals("-10", integerToString.translateTo(-10));
			assertEquals(null, integerToString.translateTo(null));
		} catch (TranslationException ignore) {
			fail("Should never happen!");
	}
	}

	@Test
	public void testTranslateFrom() throws TranslationException {
		assertEquals(Integer.valueOf(0), integerToString.translateFrom("0"));
		assertEquals(Integer.valueOf(0), integerToString.translateFrom("-0"));
		assertEquals(Integer.valueOf(10), integerToString.translateFrom("10"));
		assertEquals(Integer.valueOf(-10), integerToString.translateFrom("-10"));
		assertEquals(Integer.valueOf(0), integerToString.translateFrom("0000"));
		assertEquals(Integer.valueOf(0), integerToString.translateFrom("-0000"));
		assertEquals(Integer.valueOf(10), integerToString.translateFrom("000010"));
		assertEquals(Integer.valueOf(-10), integerToString.translateFrom("-00010"));
		assertEquals(null, integerToString.translateFrom(""));

		try {
			integerToString.translateFrom("+0");
			fail("TranslationException should be thrown!");
		} catch (TranslationException e) {
		}
		try {
			integerToString.translateFrom("+10");
			fail("TranslationException should be thrown!");
		} catch (TranslationException e) {
		}
		try {
			integerToString.translateFrom("+10");
			fail("TranslationException should be thrown!");
		} catch (TranslationException e) {
		}
		try {
			integerToString.translateFrom("a");
			fail("TranslationException should be thrown!");
		} catch (TranslationException e) {
		}
		try {
			integerToString.translateFrom("1.0");
			fail("TranslationException should be thrown!");
		} catch (TranslationException e) {
		}
		try {
			integerToString.translateFrom("1,0");
			fail("TranslationException should be thrown!");
		} catch (TranslationException e) {
		}
	}

	@Test
	public void testGetFromClass() {
		assertEquals(Integer.class, integerToString.getFromClass());
	}

	@Test
	public void testGetToClass() {
		assertEquals(String.class, integerToString.getToClass());
	}

}
