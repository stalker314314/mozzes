package org.mozzes.invocation.guice;

import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * The Class InternalInjectorProvider holds the reference to the Guice {@link Injector} class so the reference to the
 * injector is "lazy loaded" and the internal service reference can be made.
 * 
 * @see <a href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Google
 *      Guice</a>
 */
public class InternalInjectorProvider implements Provider<Injector> {

	/**
	 * The injector.
	 * 
	 * @see <a
	 *      href="http://google-guice.googlecode.com/svn/trunk/javadoc/index.html?com/google/inject/Injector.html">Injector
	 *      API</a>
	 */
	private Injector injector;

	/*
	 * @see Provider#get()
	 */
	@Override
	public Injector get() {
		return injector;
	}

	public void setInjector(Injector injector) {
		this.injector = injector;
	}
}
