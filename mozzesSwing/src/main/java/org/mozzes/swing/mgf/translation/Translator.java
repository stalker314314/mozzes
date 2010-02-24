package org.mozzes.swing.mgf.translation;

/**
 * Defines interface for the translation between <b>From</b> and <b>To</b>
 * 
 * @author milos
 * 
 * @param <From> Type of an object that will be translated to <b>To</b>
 * @param <To> Type of the object to which an object of type <b>From</b> will be translated to
 */
public interface Translator<From, To> {

	/**
	 * Translates <b>From</b> <i>object</i> to <b>To</b> object
	 * 
	 * @param object Object to be translated
	 * @return Translated object of type <b>To</b>
	 * @throws TranslationException If the specified object cannot be translated for some reason
	 */
	To translateTo(From object) throws TranslationException;

	/**
	 * Translates <b>To</b> <i>object</i> to <b>From</b> object
	 * 
	 * @param object Object to be translated
	 * @return Translated object of type <b>From</b>
	 * @throws TranslationException If the specified object cannot be translated for some reason
	 */
	From translateFrom(To object) throws TranslationException;

	/**
	 * @return Class of the <b>From</b>
	 */
	Class<From> getFromClass();

	/**
	 * @return Class of the <b>To</b>
	 */
	Class<To> getToClass();

	/**
	 * @return Translator translating from class <b>To</b> to class <b>From</b>
	 */
	Translator<To, From> inverse();
}
