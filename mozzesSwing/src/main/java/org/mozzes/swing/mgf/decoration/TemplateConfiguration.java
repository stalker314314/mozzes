/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
