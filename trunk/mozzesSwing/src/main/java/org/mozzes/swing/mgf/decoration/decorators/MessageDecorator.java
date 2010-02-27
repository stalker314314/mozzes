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

import java.util.Arrays;
import java.util.List;

import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.swing.mgf.helpers.PopupMessages;


public abstract class MessageDecorator extends Decorator {
	private String[] messages;

	public String[] getMessages() {
		if (messages == null)
			return null;
		return Arrays.copyOf(messages, messages.length);
	}

	public void setMessages(String... messages) {
		this.messages = Arrays.copyOf(messages, messages.length);
		refresh();
	}

	public void setMessages(List<String> messages) {
		setMessages(messages.toArray(new String[messages.size()]));
	}

	protected String getHTMLFormattedMessages() {
		return PopupMessages.getHTMLFormattedMessages(messages);
	}
}
