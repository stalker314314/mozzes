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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.TranslationException;


public class TimestampToString extends AbstractTranslator<Timestamp, String> {
	private static final DateFormat defaultFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
	private final DateFormat format;

	public TimestampToString() {
		this.format = null;
	}

	public TimestampToString(DateFormat format) {
		this.format = format;
	}

	@Override
	public Class<Timestamp> getFromClass() {
		return Timestamp.class;
	}

	@Override
	public Class<String> getToClass() {
		return String.class;
	}

	@Override
	public Timestamp translateFrom(String object) throws TranslationException {
		try {
			DateFormat df = format == null ? defaultFormat : format;
			return (Timestamp) df.parse(object);
		} catch (ParseException e) {
			throw new TranslationException(e.getMessage(), e);
		}
	}

	/**
	 * @throws TranslationException if date in not well formated
	 */
	@Override
	public String translateTo(Timestamp object) throws TranslationException {
		DateFormat df = format == null ? defaultFormat : format;
		return object == null ? "" : df.format(object);
	}
}
