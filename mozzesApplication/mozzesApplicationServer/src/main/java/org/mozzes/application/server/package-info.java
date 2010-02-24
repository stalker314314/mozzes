/**
 * This is the base package of the Mozzes Application Server
 * 
 * Mozzes application server is designed to be the base of all project in the Mozzart betting company.<br>
 * It provides the base for building the custom server that can execute specified services and adds
 * authentication,transactional and session support.
 * 
 * Service is specified as a interface with methods that represents actions that user can execute on the server. for
 * example
 * 
 * <code>
 * public interface ServerService{
 *     public String executeSomeAction();
 *     public String executeSomeOtherAction();
 * }
 * </code>
 * 
 * User only needs to specify the server address and port and after that it can login to the mozzes server and execute
 * some actions.<br>
 * <br>
 * 
 * If user executes without prior logging to the server the corresponding session data will be discarded.<br>
 * <br>
 * 
 * The user's data on the server are stored in three different contexts:<b>Session</b>,<b>Request</b> and
 * <b>Transaction</b>.<br>
 * <br>
 * 
 * Every user after logging to the server has it's own session and session can have it's data stored on the server(for
 * example some user's temporary preferences etc.)<br>
 * <br>
 * 
 * Every session is consisted of multiple requests and every request can also have it's data stored on the server but
 * after request is finished that data is lost so when the new request is started all the data are wiped out.<br>
 * <br>
 * 
 * In the request user can execute only one service method or if the service method is calling some other service
 * methods so that all is making the request.<br>
 * <br>
 * 
 * In the single request there is associated transaction and transactions can be nested(if nested transaction is
 * rollbacked the outer transaction is also rollbacked, but if nested transaction is successful it's result is saved no
 * mater what happens to the outer transaction).<br>
 * <br>
 * 
 * Service invocations are executed inside the transactions and there can be multiple service invocations in the same
 * transaction (for example some service method is called that inserts data in the database but also calls the other's
 * service method for notifying that service about the inserted data)<br>
 * <br>
 * 
 * Also there's corresponding transactionContext that can hold the data but only during the transaction. (it's pretty
 * much the same as a request context because the request can have only one transaction that is started as a "root"
 * transaction and all data stored in that's transaciton's context could easily be in the request context but the data
 * in the nested transactions contexts is durable only during that nested transaction and not available to the parent
 * transaction) <br>
 * <br>
 * 
 */
package org.mozzes.application.server;