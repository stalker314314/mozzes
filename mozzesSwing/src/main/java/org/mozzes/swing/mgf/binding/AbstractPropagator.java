package org.mozzes.swing.mgf.binding;

/**
 * Abstract implementation of {@link Propagator} interface
 * 
 * @author milos
 */
public class AbstractPropagator implements Propagator {
	private boolean enabled = true;

	@Override
	public void disable() {
		enabled = false;
	}

	@Override
	public void enable() {
		enabled = true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
