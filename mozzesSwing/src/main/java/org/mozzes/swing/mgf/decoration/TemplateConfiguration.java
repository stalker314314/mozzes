package org.mozzes.swing.mgf.decoration;

import javax.swing.JComponent;

/**
 * Allows applications to configure Decoration Templates for components MGF Framework
 * 
 * @author milos
 */
public class TemplateConfiguration {
	/**
	 * Sets a default {@link Decorator} for the specified component type
	 * 
	 * @throws IllegalArgumentException if the passed
	 */
	public static <T extends JComponent> void setDecorator(Class<T> componentType, Decorator decorator) {
		Templates.setTemplate(componentType, decorator, false);
	}

	/**
	 * Overrides the default {@link Decorator} for the specified component type
	 */
	public static <T extends JComponent> void overrideDecorator(Class<T> componentType, Decorator decorator) {
		Templates.setTemplate(componentType, decorator, true);
	}

	/**
	 * Removes the default {@link Decorator} for the specified component type<br>
	 * <b>NOTICE: {@link Decorator} for JComponent cannot be removed, it can only be overridden!</b>
	 */
	public static <T extends JComponent> void removeDecorator(Class<T> componentType) {
		Templates.removeTemplate(componentType);
	}

}
