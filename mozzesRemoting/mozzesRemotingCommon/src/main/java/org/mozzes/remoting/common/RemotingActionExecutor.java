package org.mozzes.remoting.common;

/**
 * Interface that all remoting action executors should implements.
 * 
 * @author Perica Milosevic
 * @author Kokan
 */
public interface RemotingActionExecutor {

    /**
     * Executes {@link RemotingAction}
     * 
     * @param action {@link RemotingAction} to be executed
     * 
     * @throws RemotingException If there is error on server while executing action
     * 
     * @return RemotingResponse Response from remoting server to executed action
     */
    public RemotingResponse execute(RemotingAction action) throws RemotingException;

}
