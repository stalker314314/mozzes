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
