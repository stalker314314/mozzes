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
package org.mozzes.remoting.common;

/**
 * Holds all information about remoting configuration. This should be all remoting clients should know to connect and
 * disconnect to remoting server
 * 
 * @author Perica Milosevic
 */
public class RemotingConfiguration {

  /** hostname of remoting server */
  private final String host;

  /** port of remoting server */
  private final Integer port;

  /** should client try to recconect if connection becomes broken */
  private boolean reconnect;

  /**
   * Construction of remoting configuration with specified hostname and port
   * 
   * @param host
   *          Hostname to connect to
   * @param port
   *          Port to connect to
   */
  public RemotingConfiguration(String host, Integer port) {
    this(host, port, false);
  }

  /**
   * Construction of remoting configuration with specified hostname and port
   * 
   * @param host
   *          Hostname to connect to
   * @param port
   *          Port to connect to
   */
  public RemotingConfiguration(String host, Integer port, boolean reconnect) {
    this.reconnect = reconnect;
    this.port = port;
    this.host = host;
  }

  /**
   * @return should client try to recconect if connection becomes broken
   */
  public boolean isReconnect() {
    return reconnect;
  }

  /**
   * @return Hostname to connect to
   */
  public String getHost() {
    return host;
  }

  /**
   * @return Port to connect to
   */
  public Integer getPort() {
    return port;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    if (host != null)
      result = prime * result + host.hashCode();
    if (port != null)
      result = prime * result + port.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof RemotingConfiguration))
      return false;
    final RemotingConfiguration other = (RemotingConfiguration) obj;
    if (host == null) {
      if (other.host != null)
        return false;
    } else if (!host.equals(other.host))
      return false;
    if (port == null) {
      if (other.port != null)
        return false;
    } else if (!port.equals(other.port))
      return false;
    return true;
  }
}
