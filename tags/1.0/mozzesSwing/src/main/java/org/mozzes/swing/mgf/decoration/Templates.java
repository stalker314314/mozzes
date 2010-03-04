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

import java.util.Map.Entry;

import javax.swing.JComponent;

import org.mozzes.swing.mgf.decoration.decorators.MessageDecorator;
import org.mozzes.swing.mgf.decoration.decorators.PopupMessageDecorator;
import org.mozzes.utils.ClassBasedObjectMappingRepository;


public class Templates {
	private static boolean initialized = false;

	private static final ClassBasedObjectMappingRepository<Decorator> templates =
			new ClassBasedObjectMappingRepository<Decorator>(null);

	private static void initializeTemplates() {
		if (initialized)
			return;
		initialized = true;

		for (Entry<Class<? extends JComponent>, Decorator> entry : DefaultTemplates.getDecorations().entrySet()) {
			templates.setObjects(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * @param <T> Type of the component
	 * @param componentType Class of the component
	 * @return {@link Decorator} Default decorator template for specified component type
	 */
	public static <T extends JComponent> Decorator getTemplateFor(Class<T> componentType) {
		initializeTemplates();
		return templates.getObject(componentType);
	}

	/**
	 * @param <T> Type of the component
	 * @param componentType Class of the component
	 * @return {@link MessageDecorator} instance (instance is always different)
	 */
	public static <T extends JComponent> MessageDecorator getMessageDecoratorFor(Class<T> componentType) {
		return new PopupMessageDecorator();
		// return new TooltipDecorator();
		}

	static <T extends JComponent> void setTemplate(Class<T> componentType, Decorator decorator, boolean override) {
		if (componentType == null)
			throw new IllegalArgumentException("Component Type must not be null!");
		if (decorator == null)
			throw new IllegalArgumentException("Decorator must not be null!");
		Decorator template = getTemplateFor(componentType);
		if (template != null && !override)
			throw new IllegalArgumentException(String.format(
					"Decoration template for %s already exists!", componentType.getSimpleName()));
		templates.setObjects(componentType, decorator);
	}

	static <T extends JComponent> void removeTemplate(Class<T> componentType) {
		if (componentType == null)
			throw new IllegalArgumentException("Component Type must not be null!");
		if (componentType == JComponent.class)
			throw new IllegalArgumentException("Template for JComponent cannot be removed!");
		templates.setObjects(componentType, null);
	}
}
