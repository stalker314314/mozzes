package org.mozzes.swing.mgf.translation;

import org.mozzes.swing.mgf.translation.translators.InverseTranslator;

public abstract class AbstractTranslator<From, To> implements Translator<From, To> {
	public Translator<To, From> inverse() {
		return new InverseTranslator<To, From>(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getClass().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractTranslator<?, ?>))
			return false;
		if (!getClass().equals(obj.getClass()))
			return false;
		return true;
	}
	}
