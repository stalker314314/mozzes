package org.mozzes.swing.mgf.datasource.events;

/**
 * Represents the base class for all DataSource events<br>
 * <b>[NOTICE] All Event instances have to be immutable(except this "propagated" property)</b>
 * 
 * @author milos
 * 
 * @param <T> The type of the object which DataSource provides
 */
public abstract class DataSourceEvent<T> {
	private boolean propagated;

	public boolean isPropagated() {
		return propagated;
	}

	/**
	 * FOR INTERNAL USE BY MGF FRAMEWORK ONLY!!!<br>
	 * Do not use for any reason, there must be another way to solve your problem!!!
	 */
	public void setPropagated(boolean propagated) {
		this.propagated = propagated;
	}
}
