package org.mozzes.swing.mgf.translation.translators;

import org.mozzes.swing.mgf.localization.Localization;
import org.mozzes.swing.mgf.localization.LocalizationKey;
import org.mozzes.swing.mgf.translation.TranslationException;

public class PositiveIntegerToString extends IntegerToString {

	@Override
	public Integer translateFrom(String object) throws TranslationException {
		Integer value = super.translateFrom(object);
		if (value != null && value < 0) {
			throw new TranslationException(Localization.getValue(
					LocalizationKey.POSITIVE_NUMBER_MESSAGE));
		}
		return value;
	}

	@Override
	public String translateTo(Integer object) throws TranslationException {
		if (object != null && object < 0) {
			throw new TranslationException(Localization.getValue(
					LocalizationKey.POSITIVE_NUMBER_MESSAGE));
		}
		return super.translateTo(object);
	}

}
