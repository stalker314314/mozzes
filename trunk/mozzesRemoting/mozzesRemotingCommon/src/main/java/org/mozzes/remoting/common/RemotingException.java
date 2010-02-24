package org.mozzes.remoting.common;

/**
 * Represents all possible exception that can happen in remoting client/server communication
 * 
 * @author Perica Milosevic
 */
public class RemotingException extends Exception {
    private static final long serialVersionUID = 4540834361558254985L;

    public RemotingException() {
        super();
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(Throwable cause) {
        super(cause);
    }

}
