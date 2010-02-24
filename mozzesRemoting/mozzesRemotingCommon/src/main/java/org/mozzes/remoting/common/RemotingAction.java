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
