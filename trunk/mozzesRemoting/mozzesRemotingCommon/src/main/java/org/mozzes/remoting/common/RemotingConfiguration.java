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

    /**
     * Construction of remoting configuration with specified hostname and port
     */
    public RemotingConfiguration(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
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
