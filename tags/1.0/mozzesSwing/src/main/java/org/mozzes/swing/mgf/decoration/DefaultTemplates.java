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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.mozzes.swing.mgf.decoration.decorators.BackgroundDecorator;
import org.mozzes.swing.mgf.utils.ColorUtil;


public class DefaultTemplates {
	private static final Color LIGHT_RED = ColorUtil.blend(Color.red, Color.white, 0.7);

	public static Map<Class<? extends JComponent>, Decorator> getDecorations() {
		Map<Class<? extends JComponent>, Decorator> map = new HashMap<Class<? extends JComponent>, Decorator>();

		map.put(JComponent.class, new BackgroundDecorator(LIGHT_RED));

		return map;
	}
}
