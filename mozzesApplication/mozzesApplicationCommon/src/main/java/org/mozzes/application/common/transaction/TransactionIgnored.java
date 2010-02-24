package org.mozzes.application.common.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Every exception thrown during service method execution causes transaction rollback. <br>
 * If {@link TransactionIgnored} annotation is present on some {@link Exception} class then that exception will not
 * cause transaction rollback and transaction will be committed normally.
 * 
 * @author Perica Milosevic
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionIgnored {
	// marker annotation
}
