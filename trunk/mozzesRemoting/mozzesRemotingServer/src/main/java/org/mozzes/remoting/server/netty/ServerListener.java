package org.mozzes.remoting.server.netty;

/**
 * Listener for Netty remoting server.
 * 
 * @author Vladimir Todorovic
 */
public interface ServerListener {

  /**
   * Executed when client disconnected from Netty remoting server.
   * 
   * @param clientId
   *          - ID of disconnected client
   */
  public void clientDisconnected(Integer clientId);
}
