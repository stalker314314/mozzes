package org.mozzes.remoting.common;

/**
 * Listener for remoting client.
 * 
 * @author Vladimir Todorovic
 */
public abstract class RemotingClientListener {

  /**
   * Executed when client is connected.
   * 
   * @param clientId
   *          - client's ID
   */
  public abstract void clientIsConnected(Integer clientId);

  /**
   * Executed when failed to login on remoting server.
   */
  public abstract void loginClientFailed();

  /**
   * Executed when client execute logout.
   */
  public abstract void clientLogout();
}
