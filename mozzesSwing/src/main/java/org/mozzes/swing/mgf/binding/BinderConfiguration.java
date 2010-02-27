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
