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

public class NeutralTranslator<T> extends AbstractTranslator<T, T> {
	private final Class<T> clazz;

	public NeutralTranslator(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T translateFrom(T object) {
		return object;
	}

	@Override
	public T translateTo(T object) {
		return object;
	}

	@Override
	public Class<T> getFromClass() {
		return clazz;
	}

	@Override
	public Class<T> getToClass() {
		return clazz;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NeutralTranslator<?>))
			return false;
		NeutralTranslator<?> other = (NeutralTranslator<?>) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		return true;
	}
}
