package org.mozzes.application.server.internal;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Scope that is based on some AbstractContext.<br>
 * In the AbstractContextScope instance is first requested from the context and then if it doesn't exists, it's created and
 * put in the context.
 * 
 * @author vita
 */
public class AbstractContextScope implements Scope {

	private final Provider<? extends AbstractContext> contextProvider;

	private final String scopeName;

	public AbstractContextScope(String scopeName, MozzesAbstractProvider<? extends AbstractContext> contextProvider) {
		this.scopeName = scopeName;
		this.contextProvider = contextProvider;
	}

	/** 
	 * get Provider from context if it exists otherwise create new one and put into context
	 * 
	 * @see Scope#scope(Key, Provider)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
		return new Provider<T>() {
			@Override
			public T get() {
				//get the context
				AbstractContext scopeContext = contextProvider.get();
				synchronized (scopeContext) {

					//get the provider
					T t = (T) scopeContext.get(key.toString());
					
					//if it doesn't exists
					if (t == null) {
						t = creator.get();
						//add new one
						scopeContext.set(key.toString(), t);
					}
					return t;
				}
			}
		};
	}

	@Override
	public String toString() {
		return scopeName;
	}
}