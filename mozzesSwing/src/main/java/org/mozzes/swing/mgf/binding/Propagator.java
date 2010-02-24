package org.mozzes.swing.mgf.binding;

/**
 * Defines interface that all listeners used for propagation of changes between the source and bound component must
 * implement
 * 
 * @author milos
 * 
 */
public interface Propagator {
	/**
	 * Enables propagation through this channel
	 */
	public void enable();

	/**
	 * Disables propagation through this channel
	 */
	public void disable();

	/**
	 * @return <b>true</b> if propagation through this channel is allowed, false otherwise
	 */
	public boolean isEnabled();
}
