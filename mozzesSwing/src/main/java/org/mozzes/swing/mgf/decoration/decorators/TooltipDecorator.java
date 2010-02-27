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
package org.mozzes.swing.mgf.decoration.decorators;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import org.mozzes.swing.mgf.decoration.StateRestoreFunction;


public class TooltipDecorator extends MessageDecorator {
	public TooltipDecorator() {
	}

	public TooltipDecorator(String... messages) {
		setMessages(messages);
	}

	@Override
	protected void decorateComponent(JComponent component) {
		ToolTipManager.sharedInstance().registerComponent(component);
		component.setToolTipText(getHTMLFormattedMessages());
		// Two ways to programmatically show tooltip, use dirtier method if the less dirty one doesn't work
		ActionMap actionMap = component.getActionMap();
		if (actionMap != null) {
			actionMap.get("postTip").actionPerformed(
					new ActionEvent(component, ActionEvent.ACTION_PERFORMED, "postTip"));
		} else {
			ToolTipManager.sharedInstance().mouseMoved(new MouseEvent(component, 0, 0, 0, 0, 0, 0, false));
	}
	}

	@Override
	protected StateRestoreFunction getStateRestoreFunction(final JComponent component) {
		final String message = component.getToolTipText();
		return new StateRestoreFunction() {
	@Override
			public void restoreState(JComponent component) {
				component.setToolTipText(message);
	}
		};
	}
	}
