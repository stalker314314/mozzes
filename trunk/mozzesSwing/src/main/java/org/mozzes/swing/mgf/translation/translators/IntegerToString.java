package org.mozzes.swing.mgf.translation.translators;

import org.mozzes.swing.mgf.localization.Localization;
import org.mozzes.swing.mgf.localization.LocalizationKey;
import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.TranslationException;

public class IntegerToString extends AbstractTranslator<Integer, String> {

	/**
	 * @throws TranslationException Thrown by the subclasses if needed
	 */
	@Override
	public String translateTo(Integer object) throws TranslationException {
		return object == null ? null : object.toString();
	}

	@Override
	public Integer translateFrom(String object) throws TranslationException {
		try {
			if (object == null || object.isEmpty()) {
				return null;
			} else {
				if (object.equals("-")) {
					return null;
				}
				return Integer.parseInt(object);
			}
		} catch (Exception e) {
			// TODO: Solve the problem of localization
			throw new TranslationException(Localization.getValue(
					LocalizationKey.INVALID_NUMBER_FORMAT_MESSAGE), e);
		}
	}

	@Override
	public Class<Integer> getFromClass() {
		return Integer.class;
	}

	@Override
	public Class<String> getToClass() {
		return String.class;
	}
}
