package org.mozzes.application.plugin;

import java.util.List;

import org.mozzes.application.module.ApplicationModule;
import org.mozzes.invocation.common.interceptor.InvocationInterceptor;

import com.google.inject.Module;

/**
 * ApplicationPlugin is base class for all Mozzes server plugins.<br>
 */
public abstract class ApplicationPlugin extends ApplicationModule {
	
	public Module getCustomGuiceModule() {
		return null;
	}
	
	/**
	 * @return The service interceptors that this plugin provides.<br>
	 * Interceptors included here are invoked on <b>every</b> service call in every application module.
	 */
	public List<Class<? extends InvocationInterceptor>> getGlobalServiceInterceptors() {
		return null;
	}
	

}
