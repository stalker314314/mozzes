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

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

public abstract class Decorator {
	private static final long DEFAULT_DELAY = 5000;

	/**
	 * Remove decoration from the specified component<br/>
	 * (Does nothing if the component is not decorated by this decorator);
	 */
	public void clear(final JComponent component) {
		DecorationManager.getInstance().clearDecoration(component, Decorator.this);
	}

	public void decorate(final JComponent component) {
		if (isActive(component))
			clear(component);
		DecorationManager.getInstance().registerComponent(component, Decorator.this);
		decorateComponent(component);
	}

	public void decorateTimed(final JComponent component) {
		decorate(component);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Decorator.this.clear(component);
			}
		}, DEFAULT_DELAY);
	}

	public boolean isActive(JComponent component) {
		return DecorationManager.getInstance().isActive(component, this);
	}

	protected void refresh() {
		DecorationManager.getInstance().refreshDecoration(this);
	}

	protected abstract void decorateComponent(JComponent component);

	protected abstract StateRestoreFunction getStateRestoreFunction(JComponent component);

}
