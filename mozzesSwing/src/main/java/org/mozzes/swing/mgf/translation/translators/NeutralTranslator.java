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
