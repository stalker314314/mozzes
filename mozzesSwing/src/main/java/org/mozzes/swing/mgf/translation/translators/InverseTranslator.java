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
