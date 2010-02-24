package org.mozzes.application.demo.mockups.scopedata;

import org.mozzes.application.module.scope.*;

/**
 * This is the scope data object that is associated with the transaction.<br>
 * In the single session there's only one instance of this class.
 * 
 * @author vita
 */
@TransactionScoped
public class MTransactionData extends AbstractScopeData {

}
