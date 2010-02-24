package org.mozzes.swing.mgf.translation;

/**
 * Allows applications to replace default {@link Translator translators} with custom ones, or provide their own
 * Translators for custom types
 * 
 * @author milos
 */
public class TranslatorConfiguration {
	private TranslatorConfiguration() {
	}

	/**
	 * Contributes a {@link Translator} to framework
	 * 
	 * @param translator Translator that you want to contribute to framework
	 * 
	 * @throws IllegalArgumentException if the passed translator translates between the same types of objects as some
	 *             other translator
	 */
	public static <From, To> void addTranslator(Translator<From, To> translator) {
		TranslatorRepository.addTranslator(translator, false);
	}

	/**
	 * Contributes a {@link Translator} to framework, if the passed translator translates between the same types of
	 * objects as some other contributed(or default) translator it overrides the old one.
	 * 
	 * @param translator Translator that you want to contribute to framework
	 */
	public static <From, To> void overrideTranslator(Translator<From, To> translator) {
		TranslatorRepository.addTranslator(translator, true);
	}

}
