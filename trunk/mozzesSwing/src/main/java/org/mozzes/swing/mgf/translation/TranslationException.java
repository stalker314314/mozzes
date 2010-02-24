package org.mozzes.swing.mgf.translation;

/**
 * This exception is thrown by {@link Translator translators} when translation cannot be done for some reason
 * 
 * @author milos
 */
public class TranslationException extends Exception {
	private static final long serialVersionUID = 15L;

	/**
	 * @param message Reason of failure(the message that will be shown to the user through component decoration)F
	 */
	public TranslationException(String message) {
		super(message);
	}

	/**
	 * @param message Reason of failure(the message that will be shown to the user through component decoration)
	 * @param cause {@link Exception} that happened while translating a value
	 */
	public TranslationException(String message, Throwable cause) {
		super(message, cause);
	}
}