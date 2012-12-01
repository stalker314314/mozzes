package org.mozzes.remoting.common.netty;

/**
 * Ensure that object that implement this interface has a unique state.
 * @author Marko Jovicic <marko.jovicic@mozzartbet.com>
 *
 */
public interface Unique {
	
	/**
	 * Set unique id of some object.
	 * @param id unique id of some object
	 */
	public void setId(Long id);
	
	/**
	 * Get unique id of some object.
	 * @return unique id of some object
	 */
	public Long getId();
}
