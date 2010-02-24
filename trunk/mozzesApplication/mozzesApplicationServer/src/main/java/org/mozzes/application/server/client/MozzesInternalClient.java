package org.mozzes.application.server.client;

import org.mozzes.application.common.client.MozzesClient;
import org.mozzes.application.common.exceptions.MozzesRuntimeException;
import org.mozzes.application.common.service.SessionService;

import com.google.inject.Inject;

/**
 * This is the internal client that runs on the server
 * 
 * @author vita
 */
public class MozzesInternalClient extends MozzesClient {

	public static final String INTERNAL_CLIENT_ID = "INTERNAL_CLIENT_ID";

	@Inject
	public MozzesInternalClient(MozzesInternalClientConfiguration clientConfiguration) {
		super(clientConfiguration);
		setSessionId(INTERNAL_CLIENT_ID);
	}

	/**
	 * Internal client shouldn't store its state on the server so {@link #login(String, String)} method shouldn't be
	 * called.
	 * 
	 * @see MozzesClient#login(String, String)
	 * @see SessionService#login(String, String)
	 */
	@Override
	public void login(String username, String password) {
		throw new MozzesRuntimeException("this method is not enabled for internal client");
	}

	/**
	 * Because {@link #login(String, String)} shouldn't be called also {@link #logout()} shouldn't be called.
	 * 
	 * @see MozzesClient#logout()
	 * @see SessionService#logout()
	 */
	@Override
	public void logout() {
		throw new MozzesRuntimeException("this method is not enabled for internal client");
	}
}
