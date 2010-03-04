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
