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

import java.io.*;
import java.util.*;

/**
 * Encapsulates concrete action remoting client sends to remoting server
 * 
 * @author Perica Milosevic
 */
public final class RemotingAction implements Serializable {

    private static final long serialVersionUID = -806957954023327547L;

    /** Name of the action that clients wants to execute */
    private final String actionName;

    /** Action parameters represented as Map */
    private Map<? extends Object, ? extends Object> actionParams = null;

    public RemotingAction(String actionName, Map<? extends Object, ? extends Object> actionParams) {
        if (actionName == null)
            throw new IllegalArgumentException("Invalid action name - null");

        this.actionName = actionName;
        this.actionParams = actionParams;
    }

    public String getActionName() {
        return actionName;
    }

    public Map<? extends Object, ? extends Object> getParams() {
        return Collections.unmodifiableMap(actionParams);
    }

    /**
     * Returns specified parameter based on parameter key
     */
    public Object getParam(Object key) {
        if (actionParams == null)
            return null;
        return actionParams.get(key);
    }

    @Override
    public String toString() {
        return getActionName();
    }

}
