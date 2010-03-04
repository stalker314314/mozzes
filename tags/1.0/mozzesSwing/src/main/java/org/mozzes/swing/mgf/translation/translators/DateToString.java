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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mozzes.swing.mgf.localization.Localization;
import org.mozzes.swing.mgf.localization.LocalizationKey;
import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.TranslationException;


public class DateToString extends AbstractTranslator<Date, String> {
	private static final DateFormat defaultFormat = new SimpleDateFormat("dd.MM.yyyy.");
	private final DateFormat format;

	public DateToString() {
		this.format = null;
	}

	public DateToString(DateFormat format) {
		this.format = format;
	}

	@Override
	public Class<Date> getFromClass() {
		return Date.class;
	}

	@Override
	public Class<String> getToClass() {
		return String.class;
	}

	@Override
	public Date translateFrom(String object) throws TranslationException {
		try {
			DateFormat df = format == null ? defaultFormat : format;
			return df.parse(object);
		} catch (ParseException e) {
			throw new TranslationException(Localization.getValue(
					LocalizationKey.INVALID_DATE_FORMAT_MESSAGE), e);
		}
	}

	/**
	 * @throws TranslationException if date in not well formated
	 */
	@Override
	public String translateTo(Date object) throws TranslationException {
		DateFormat df = format == null ? defaultFormat : format;
		return object == null ? "" : df.format(object);
	}

}
