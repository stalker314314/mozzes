package org.mozzes.remoting.server.netty;

/**
 * Client's identity.
 * 
 * @author Vladimir Todorovic
 */
public final class ClientIdentity {

  private final int clientId;
  private final boolean isAccepted;

  /**
   * Creates client's identity.
   * 
   * @param clientId
   *          - client's ID
   * @param isAccepted
   *          - {@code true} if identity accepted, {@code false} otherwise
   */
  public ClientIdentity(int clientId, boolean isAccepted) {
    this.clientId = clientId;
    this.isAccepted = isAccepted;
  }

  /**
   * @return client's ID
   */
  public int getClientId() {
    return clientId;
  }

  /**
   * @return {@code true} if identity accepted, {@code false} otherwise
   */
  public boolean isAccepted() {
    return isAccepted;
  }

}
