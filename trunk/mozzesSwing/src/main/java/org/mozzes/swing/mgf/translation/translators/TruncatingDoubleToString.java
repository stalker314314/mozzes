package org.mozzes.swing.mgf.translation.translators;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.mozzes.swing.mgf.translation.TranslationException;


public class TruncatingDoubleToString extends DoubleToString {

	public TruncatingDoubleToString() {
		super();
	}

	public TruncatingDoubleToString(int decimals) {
		super(decimals);
	}

	public TruncatingDoubleToString(DecimalFormat format) {
		super(format);
	}

	@Override
	public Double translateFrom(String object) throws TranslationException {
		Double value = super.translateFrom(object);
		if (value == null)
			return null;
		int decimals = getNumberOfDecimals();
		value = truncate(value, decimals);
		return value;
	}

	public static double truncate(double value, int decimals) {
		if (decimals < 0)
			throw new IllegalArgumentException("Number of decimals must be 0 or more!");
		Double temp = Double.valueOf(value);
		BigDecimal bd = new BigDecimal(temp.toString());
		// ! - izuzetno je vazno da se koristi ovaj konstruktor sa stringom !
		bd = bd.setScale(decimals, BigDecimal.ROUND_DOWN);
		return bd.doubleValue();
	}
}
