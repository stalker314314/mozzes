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
