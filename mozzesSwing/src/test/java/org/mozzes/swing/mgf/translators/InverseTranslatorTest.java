package org.mozzes.swing.mgf.translators;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.swing.mgf.translation.TranslationException;
import org.mozzes.swing.mgf.translation.Translator;
import org.mozzes.swing.mgf.translation.translators.IntegerToString;
import org.mozzes.swing.mgf.translation.translators.InverseTranslator;


public class InverseTranslatorTest {

	@Test
	public void testGetFromClass() {
		Translator<String, Integer> translator = new InverseTranslator<String, Integer>(new IntegerToString());
		InverseTranslator<Integer, String> inverse = new InverseTranslator<Integer, String>(translator);

		assertEquals(translator.getToClass(), inverse.getFromClass());
	}

	@Test
	public void testGetToClass() {
		Translator<String, Integer> translator = new InverseTranslator<String, Integer>(new IntegerToString());
		InverseTranslator<Integer, String> inverse = new InverseTranslator<Integer, String>(translator);

		assertEquals(translator.getFromClass(), inverse.getToClass());
	}

	@Test
	public void testTranslateFrom() throws TranslationException {
		Translator<String, Integer> translator = new InverseTranslator<String, Integer>(new IntegerToString());
		InverseTranslator<Integer, String> inverse = new InverseTranslator<Integer, String>(translator);

		assertEquals(translator.translateTo("10"), inverse.translateFrom("10"));
		assertEquals(translator.translateTo("0"), inverse.translateFrom("0"));
	}

	@Test
	public void testTranslateTo() throws TranslationException {
		Translator<String, Integer> translator = new InverseTranslator<String, Integer>(new IntegerToString());
		InverseTranslator<Integer, String> inverse = new InverseTranslator<Integer, String>(translator);

		assertEquals(translator.translateFrom(10), inverse.translateTo(10));
		assertEquals(translator.translateFrom(-1), inverse.translateTo(-1));
	}

}
