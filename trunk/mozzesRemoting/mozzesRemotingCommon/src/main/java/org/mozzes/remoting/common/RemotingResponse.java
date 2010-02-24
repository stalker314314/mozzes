package org.mozzes.remoting.common;

import java.io.*;
import java.util.*;

/**
 * Holds all information remoting server returned back to remoting client as response to executed action
 * 
 * @author Perica Milosevic
 */
public class RemotingResponse implements Serializable {

    private static final long serialVersionUID = 4124876546041436541L;

    /** Response parameters */
    private Map<? extends Object, ? extends Object> responseParams = null;

    public RemotingResponse() {
        this(null);
    }

    public RemotingResponse(Map<? extends Object, ? extends Object> params) {
        this.responseParams = params;
    }

    public Map<? extends Object, ? extends Object> getParams() {
        return Collections.unmodifiableMap(responseParams);
    }

    /**
     * Returns specified parameter based on parameter key
     */
    public Object getParam(Object key) {
        if (responseParams == null)
            return null;
        return responseParams.get(key);
    }
}
