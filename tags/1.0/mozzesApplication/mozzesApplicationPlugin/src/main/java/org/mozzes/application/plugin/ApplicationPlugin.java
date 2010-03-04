/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
