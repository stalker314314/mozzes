package org.mozzes.swing.mgf.translation.translators;

import java.nio.channels.UnsupportedAddressTypeException;

import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.TranslationException;


public class ObjectToString<T> extends AbstractTranslator<T, String> {
	private final Class<T> objectType;

	public ObjectToString(Class<T> objectType) {
		this.objectType = objectType;
	}

	@Override
	public Class<T> getFromClass() {
		return objectType;
	}

	@Override
	public Class<String> getToClass() {
		return String.class;
	}

	@Override
	public T translateFrom(String object) throws TranslationException {
		throw new TranslationException(String.format(
				"Translation from String to %s is not supported!", objectType.getClass().getSimpleName()),
				new UnsupportedAddressTypeException());
	}

	/**
	 * @throws TranslationException  if something goes wrong
	 */
	@Override
	public String translateTo(T object) throws TranslationException {
		return object == null ? "" : object.toString();
	}
}
