/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.swing.mgf.translation.translators;

import static org.mozzes.swing.mgf.localization.LocalizationKey.*;

import java.text.DecimalFormat;

import org.mozzes.swing.mgf.localization.Localization;
import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.TranslationException;


public class DoubleToString extends AbstractTranslator<Double, String> {
	private static int default_decimals = 2;
	private final DecimalFormat format;
	private final DecimalFormat defaultDecimalFormat = new DecimalFormat();

	public DoubleToString() {
		format = defaultDecimalFormat;
	}

	public DoubleToString(int decimals) {
		format = new DecimalFormat(buildPattern(decimals));
		format.setGroupingUsed(true);
	}

	public DoubleToString(DecimalFormat format) {
		this.format = format;
	}

	private static String buildPattern(int decimals) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("0.");
		for (int i = 0; i < decimals; i++) {
			pattern.append("0");
		}
		return pattern.toString();
	}

	private static String insertSeparators(double value, String formatted, String separator) {
		StringBuilder withSeparator = new StringBuilder(formatted);
		int n = 0;
		double tmp = value;
		while (tmp >= 1000) {
			tmp = tmp / 1000;
			n++;
		}
		int offset = Integer.valueOf((int) tmp).toString().length();
		if (value > 1000)
			withSeparator.insert(offset, separator);
		for (int i = 1; i < n; i++) {
			offset += separator.length() + 3;
			withSeparator.insert(offset, separator);
		}
		return withSeparator.toString();
	}

	public static void setDefaultNumberOfDecimals(int decimals) {
		default_decimals = decimals;
	}

	@Override
	public Double translateFrom(String object) throws TranslationException {
		try {
			if (object == null || object.isEmpty()) {
				return null;
			} else {
				if (object.equals("-")) {
					return null;
				}
				String strVal = object;
				if (format == defaultDecimalFormat)
					strVal = object.replace(" ", "");
				// double parsedValue = format.parse(strVal).doubleValue();
				double parsedValue = Double.parseDouble(strVal);
				if (breachedMaxDecimals(strVal))
					throw new TranslationException(Localization.getValue(MAX_DECIMALS_EXCEEDED, getMaxDecimals()));

				return parsedValue;
			}
		} catch (Exception e) {
			if (e instanceof TranslationException)
				throw (TranslationException) e;
			throw new TranslationException(Localization.getValue(
					INVALID_NUMBER_FORMAT_MESSAGE), e);
		}
	}

	private Object getMaxDecimals() {
		return format.getMaximumFractionDigits();
	}

	private boolean breachedMaxDecimals(String s) {
		Integer i = null;
		for (char c : s.toCharArray()) {
			if (i != null && Character.isDigit(c))
				i++;
			if (c == format.getDecimalFormatSymbols().getDecimalSeparator()) {
				i = 0;
			}
		}
		if (i != null && i > format.getMaximumFractionDigits())
			return true;
		return false;
	}

	/**
	 * @throws TranslationException Thrown by the subclasses if needed
	 */
	@Override
	public String translateTo(Double object) throws TranslationException {
		if (object == null)
			return "";
		// int n = decimals == null ? default_decimals : decimals;
		// return object == null ? null : String.format("%." + n + "f", object);
		Double value = object;
		if (format == null)
			return value.toString();
		if (format == defaultDecimalFormat) // Intentional comparison by reference
		{
			format.applyPattern(buildPattern(default_decimals));
			return insertSeparators(value, format.format(value), " ");
		}
		return format.format(value);
	}

	public int getNumberOfDecimals() {
		DecimalFormat f = format;
		if (f == null)
			f = defaultDecimalFormat;
		return f.getMaximumFractionDigits();
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
