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
