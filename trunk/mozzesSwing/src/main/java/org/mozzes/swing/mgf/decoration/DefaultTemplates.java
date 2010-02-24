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
