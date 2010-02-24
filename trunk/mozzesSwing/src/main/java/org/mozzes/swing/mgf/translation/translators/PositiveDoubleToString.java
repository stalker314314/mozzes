package org.mozzes.swing.mgf.translation.translators;

import java.text.DecimalFormat;

import org.mozzes.swing.mgf.localization.Localization;
import org.mozzes.swing.mgf.localization.LocalizationKey;
import org.mozzes.swing.mgf.translation.TranslationException;


public class PositiveDoubleToString extends DoubleToString {
	public PositiveDoubleToString() {
		super();
	}

	public PositiveDoubleToString(int decmials) {
		super(decmials);
	}
	
	public PositiveDoubleToString(DecimalFormat format) {
		super(format);
	}

	@Override
	public String translateTo(Double object) throws TranslationException {
		if (object != null && object < 0) {
			throw new TranslationException(Localization.getValue(
					LocalizationKey.POSITIVE_NUMBER_MESSAGE));
		}
		return super.translateTo(object);
	}

	@Override
	public Double translateFrom(String object) throws TranslationException {
		Double value = super.translateFrom(object);
		if (value != null && value < 0) {
			throw new TranslationException(Localization.getValue(
					LocalizationKey.POSITIVE_NUMBER_MESSAGE));
		}
		return value;
	}

	@Override
	public Class<Double> getFromClass() {
		return Double.class;
	}

	@Override
	public Class<String> getToClass() {
		return String.class;
	}

}
