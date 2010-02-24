package org.mozzes.swing.mgf.translators;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.swing.mgf.translation.translators.NeutralTranslator;


public class NeutralTranslatorTest {
	private final NeutralTranslator<String> translator = new NeutralTranslator<String>(String.class);

	@Test
	public void testTranslateFrom() {
		assertEquals(translator.translateTo(""), translator.translateFrom(""));
		assertEquals(translator.translateTo(null), translator.translateFrom(null));
	}

	@Test
	public void testTranslateTo() {
		assertEquals(translator.translateFrom(""), translator.translateTo(""));
		assertEquals(translator.translateFrom(null), translator.translateTo(null));
	}

	@Test
	public void testGetFromClass() {
		assertEquals(translator.getToClass(), translator.getFromClass());
	}

	@Test
	public void testGetToClass() {
		assertEquals(translator.getFromClass(), translator.getToClass());
	}

}
