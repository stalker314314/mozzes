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
package org.mozzes.swing.mgf.helpers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.mozzes.swing.mgf.utils.ColorUtil;


public class PopupMessages {
	private static final long DEFAULT_DELAY = 5000;
	private static final Color color = ColorUtil.blend(Color.YELLOW, Color.WHITE, 0.3);

	private PopupMessages() {
	}

	public static Popup showPopupTimed(Component owner, String... messages) {
		return showPopupTimed(owner, DEFAULT_DELAY, messages);
	}

	public static Popup showPopupTimed(Component owner, int xOffset, int yOffset, String... messages) {
		return showPopupTimed(owner, DEFAULT_DELAY, xOffset, yOffset, messages);
	}

	public static Popup showPopupTimed(Component owner, long delay, String... messages) {
		return showPopupTimed(owner, delay, getPopupComponent(messages));
	}

	public static Popup showPopupTimed(Component owner, long delay, int xOffset, int yOffset, String... messages) {
		return showPopupTimed(owner, delay, getPopupComponent(messages), xOffset, yOffset);
	}

	public static Popup showPopupTimed(Component owner, Component popup) {
		return showPopupTimed(owner, DEFAULT_DELAY, popup);
	}

	public static Popup showPopupTimed(Component owner, Component popup, int xOffset, int yOffset) {
		return showPopupTimed(owner, DEFAULT_DELAY, popup, xOffset, yOffset);
	}

	public static Popup showPopupTimed(Component owner, long delay, Component popup) {
		return showPopupTimed(owner, delay, popup, 0, owner.getHeight());
	}

	public static Popup showPopupTimed(Component owner, long delay, Component popup, int xOffset, int yOffset) {
		final Popup popupMessage = getPopup(owner, popup, xOffset, yOffset);
		try {
			popupMessage.show();
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						popupMessage.hide();
					} catch (Exception ignore) {
					}
				}
			}, delay);
		} catch (Exception ignore) {
		}
		return popupMessage;
	}

	public static Popup getPopupMessage(Component owner, String... messages) {
		return getPopup(owner, getPopupComponent(messages));
	}

	public static Popup getPopupMessage(Component owner, int xOffset, int yOffset, String... messages) {
		return getPopup(owner, getPopupComponent(messages), xOffset, yOffset);
	}

	public static Popup getPopup(Component owner, Component popup) {
		return getPopup(owner, popup, 0, owner.getHeight());
	}

	public static Popup getPopup(Component owner, Component popup, int xOffset, int yOffset) {
		Component realOwner = owner;
		// for (Container parent = owner.getParent(); parent != null; parent = parent.getParent()) {
		// realOwner = parent;
		// }

		return PopupFactory.getSharedInstance().getPopup(
				realOwner, popup,
				getX(owner) + xOffset, getY(owner) + yOffset);
	}

	private static JPanel getPopupComponent(String... messages) {
		final JPanel panel = new JPanel();

		panel.setBackground(color);
		panel.setBorder(new LineBorder(Color.BLACK));
		panel.add(new JLabel(getHTMLFormattedMessages(messages)));

		return panel;
	}

	public static String getHTMLFormattedMessages(String... messages) {
		if (messages == null || messages.length == 0)
			return "";
		StringBuilder builder = new StringBuilder();
		String style =
				"list-style-type: disc;" +
				"padding-top:0px;" +
				"padding-bottom:0px;" +
				"padding-right:0px;" +
				"padding-left:10px;" +
				"margin-top:0px;" +
				"margin-bottom:0px;" +
				"margin-right:0px;" +
				"margin-left:0px;";
		builder.append("<html><body>");
		builder.append(String.format("<ul style=\"%s\">", style));
		for (String message : messages) {
			builder.append("<li>");
			builder.append(message);
			builder.append("</li>");
		}
		builder.append("</ul>");
		builder.append("</body></html>");
		return builder.toString();
	}

	private static int getX(Component component) {
		Point location = component.getLocation();
		Point p = new Point(location);
		SwingUtilities.convertPointToScreen(p, component.getParent());
		return p.x;
	}

	private static int getY(Component component) {
		Point location = component.getLocation();
		Point p = new Point(location);
		SwingUtilities.convertPointToScreen(p, component.getParent());
		return p.y;
	}
}