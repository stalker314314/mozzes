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
