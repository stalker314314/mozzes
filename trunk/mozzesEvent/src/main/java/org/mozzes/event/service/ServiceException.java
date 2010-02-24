package org.mozzes.event.service;

/**
 * Exception nastao prilikom trazenja ili kreiranja servisa. Moze da bude bilo kakva greska koja se desava u radu sa
 * servisima, a u sebi nosi poruku ili uzrok greske
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 182L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}