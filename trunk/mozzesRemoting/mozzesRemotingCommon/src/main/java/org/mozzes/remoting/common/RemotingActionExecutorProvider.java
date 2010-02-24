package org.mozzes.remoting.common;

/**
 * Interface that all {@link RemotingActionExecutor} providers must implement. Enables getting remoting executors from
 * this preconfigured providers.
 * 
 * @author Perica Milosevic
 * @author Kokan
 */
public interface RemotingActionExecutorProvider {

    /**
     * Gets {@link RemotingActionExecutor}.<br>
     * Clients use this method to get {@link RemotingActionExecutor} which they need to execute {@link RemotingAction}.
     */
    public RemotingActionExecutor get();

}
