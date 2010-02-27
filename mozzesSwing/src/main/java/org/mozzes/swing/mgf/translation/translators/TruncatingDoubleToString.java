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
