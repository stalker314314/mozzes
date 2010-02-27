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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import javax.swing.JComponent;

import org.mozzes.utils.JavaUtils;


public class DecorationManager {
	private static DecorationManager instance;

	private Map<JComponent, List<Decoration>> decorations =
			new WeakHashMap<JComponent, List<Decoration>>();

	private DecorationManager() {
	}

	public static DecorationManager getInstance() {
		if (instance == null)
			instance = new DecorationManager();

		return instance;
	}

	public <T extends JComponent> void registerComponent(final T component, final Decorator decorator) {
		JavaUtils.runInEventDispatch(new Runnable() {
			@Override
			public void run() {
				Decoration decoration = new Decoration(decorator,
						decorator.getStateRestoreFunction(component));
				if (decorations.get(component) == null)
					decorations.put(component, new ArrayList<Decoration>());

				List<Decoration> list = decorations.get(component);
				list.add(decoration);
	}
		});
	}

	public <T extends JComponent> void clearDecoration(final T component, final Decorator decorator) {
		final List<Decoration> dec = decorations.get(component);
		if (dec == null)
			return;
		if (dec.isEmpty()) {
			decorations.remove(component);
			return;
	}
		JavaUtils.runInEventDispatch(new Runnable() {
			@Override
			public void run() {
				final LinkedList<Decorator> decorators = new LinkedList<Decorator>();
				for (int i = dec.size() - 1; i >= 0; i--) {
					dec.get(i).restoreFunction.restoreState(component);
					Decorator currentDecoratorOnTop = dec.remove(i).decorator;
					if (currentDecoratorOnTop.equals(decorator)) {
						break;
					}
					decorators.addFirst(currentDecoratorOnTop);
				}

				for (Decorator d : decorators) {
					d.decorate(component);
	}
			}
		});
	}

	public <T extends JComponent> void clearAllDecorations(final T component) {
		final List<Decoration> dec = decorations.get(component);
		if (dec == null)
			return;
		if (dec.isEmpty()) {
			decorations.remove(component);
			return;
		}
		JavaUtils.runInEventDispatch(new Runnable() {
			@Override
			public void run() {
				for (int i = dec.size() - 1; i >= 0; i--) {
					dec.get(i).restoreFunction.restoreState(component);
					dec.remove(i);
			}
			}
		});
	}

	public <T extends JComponent> boolean isActive(T component, Decorator decorator) {
		List<Decoration> dec = decorations.get(component);
		if (dec == null)
			return false;
		if (dec.isEmpty()) {
			decorations.remove(component);
			return false;
	}

		for (Decoration decoration : dec) {
			if (decoration.decorator.equals(decorator))
				return true;
	}

		return false;
			}

	public <T extends JComponent> void refreshDecoration(final Decorator decorator) {
		JavaUtils.runInEventDispatch(new Runnable() {
			@Override
			public void run() {
				final Map<JComponent, Integer> affected = new HashMap<JComponent, Integer>();
				for (Entry<JComponent, List<Decoration>> entry : decorations.entrySet()) {
					List<Decoration> dec = entry.getValue();
					if (dec == null || dec.isEmpty())
						continue;

					// To determine if the component decorators should be refreshed:
					// Pass through all decorators for current component
					// and determine the index of the first decorator that either
					// is passed decorator or it's a CompositeDecorator which contains passed decorator
					Integer index = null;
					for (int i = 0; i < dec.size(); i++) {
						Decorator currentDecorator = dec.get(i).decorator;
						if (currentDecorator.equals(decorator)) {
							index = i;
							break;
		}
						if (currentDecorator instanceof CompositeDecorator) {
							if (((CompositeDecorator) currentDecorator).contains(decorator)) {
								index = i;
								break;
							}
						}
					}
					// component not affected by change
					if (index == null)
						continue;
				}

				for (Entry<JComponent, Integer> entry : affected.entrySet()) {
					// component is affected from specified index and above
					// clear all those decorators and re-set them again
					Integer index = entry.getValue();
					List<Decoration> dec = decorations.get(entry.getKey());

					LinkedList<Decorator> decorators = new LinkedList<Decorator>();
					for (int i = dec.size() - 1; i >= index; i--) {
						dec.get(i).restoreFunction.restoreState(entry.getKey());
						Decorator currentDecoratorOnTop = dec.remove(i).decorator;
						decorators.addFirst(currentDecoratorOnTop);
					}
					for (Decorator d : decorators) {
						d.decorate(entry.getKey());
					}
				}
			}
		});
	}

	private static class Decoration {
		Decorator decorator;
		StateRestoreFunction restoreFunction;

		public Decoration(Decorator decorator, StateRestoreFunction restoreFunction) {
			this.decorator = decorator;
			this.restoreFunction = restoreFunction;
		}
	}
}
