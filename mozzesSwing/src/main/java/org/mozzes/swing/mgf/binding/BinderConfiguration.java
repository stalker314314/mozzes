package org.mozzes.swing.mgf.binding;

/**
 * Allows applications to configure Binding Module of MGF Framework
 * 
 * @author milos
 */
public abstract class BinderConfiguration {
	private BinderConfiguration() {
	}

	/**
	 * Contributes a {@link BindingHandler} to framework
	 * 
	 * @param bindingHandler Class of the {@link BindingHandler handler} you want to contribute to framework
	 * 
	 * @throws IllegalArgumentException if the passed handler handles same GUI component as some other contributed(or
	 *             default) handler
	 */
	@SuppressWarnings("unchecked")
	public static void addBindingHandler(Class<? extends BindingHandler> bindingHandler) {
		Binder.addBindingHandler(bindingHandler, false);
	}

	/**
	 * Contributes a {@link BindingHandler} to framework, if the passed handler handles same GUI component as some other
	 * contributed(or default) handler it overrides the old one.
	 * 
	 * @param bindingHandler Class of the {@link BindingHandler handler} you want to contribute to framework
	 */
	@SuppressWarnings("unchecked")
	public static void overrideBindingHandler(Class<? extends BindingHandler> bindingHandler) {
		Binder.addBindingHandler(bindingHandler, true);
	}

}
