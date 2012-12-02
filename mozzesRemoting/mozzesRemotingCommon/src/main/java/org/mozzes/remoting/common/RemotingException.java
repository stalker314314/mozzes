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

import org.mozzes.remoting.common.netty.Unique;

/**
 * Represents all possible exception that can happen in remoting client/server communication
 * 
 * @author Perica Milosevic
 */
public class RemotingException extends Exception implements Unique {
  private static final long serialVersionUID = 4540834361558254985L;

  private Long id;

  /**
   * Default constructor
   */
  public RemotingException() {
    super();
  }

  /**
   * Constructor with both message and a cause
   * 
   * @param message
   *          Exception message
   * @param cause
   *          Exception cause
   */
  public RemotingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor with message
   * 
   * @param message
   *          Exception message
   */
  public RemotingException(String message) {
    super(message);
  }

  /**
   * Constructor with a cause
   * 
   * @param cause
   *          Exception cause
   */
  public RemotingException(Throwable cause) {
    super(cause);
  }

  /**
   * Return id of this remoting response. This remoting response id has the same ID as the RemotingAction which response
   * is this object.
   * 
   * @return a unique id which bind this response to remoting action.
   */
  @Override
  public Long getId() {
    return id;
  }

  /**
   * Set id of this remoting response. This remoting response id has the same ID as the RemotingAction which response is
   * this object.
   * 
   * @param id
   *          a unique id of the response. Must correspond to id of an remoting action.
   */
  @Override
  public void setId(Long id) {
    this.id = id;
  }
}