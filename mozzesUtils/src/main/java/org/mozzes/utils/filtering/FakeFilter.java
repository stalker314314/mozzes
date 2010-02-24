package org.mozzes.utils.filtering;

/**
 * Kvazi-filter koji prihvata sve objekte
 * 
 * @author Perica Milosevic
 * @version 1.7
 */
public class FakeFilter<T> implements Filter<T> {

	public boolean isAcceptable(T object) {
		return true;
	}

	@Override
	public boolean equals(Object that) {
		return (that instanceof FakeFilter<?>);
	}

	@Override
	public int hashCode() {
		return 1;
	}
}
