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

import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.TranslationException;
import org.mozzes.swing.mgf.translation.Translator;

public class InverseTranslator<From, To> extends AbstractTranslator<From, To> {
	private final Translator<To, From> toInvert;

	public InverseTranslator(Translator<To, From> toInvert) {
		this.toInvert = toInvert;
	}

	@Override
	public Class<From> getFromClass() {
		return toInvert.getToClass();
	}

	@Override
	public Class<To> getToClass() {
		return toInvert.getFromClass();
	}

	@Override
	public From translateFrom(To object) throws TranslationException {
		return toInvert.translateTo(object);
	}

	@Override
	public To translateTo(From object) throws TranslationException {
		return toInvert.translateFrom(object);
	}

	@Override
	public Translator<To, From> inverse() {
		return toInvert;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((toInvert == null) ? 0 : toInvert.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof InverseTranslator<?, ?>))
			return false;
		InverseTranslator<?, ?> other = (InverseTranslator<?, ?>) obj;
		if (toInvert == null) {
			if (other.toInvert != null)
				return false;
		} else if (!toInvert.equals(other.toInvert))
			return false;
		return true;
	}

}
