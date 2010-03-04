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
package org.mozzes.application.demo.apps.simple;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.mozzes.application.demo.apps.*;
import org.mozzes.application.demo.mockups.services.basic.*;
import org.mozzes.application.demo.mockups.services.basic.impl.*;
import org.mozzes.application.demo.mockups.services.scopedata.*;


/**
 * In this test case we're testing that Server's services can call each other
 * 
 * {@link ServiceWithRequestData2} is calling {@link BasicService} in the implementation.
 * 
 * @author vita
 */
public class TestServiceInjectingBad extends TestBase {

	/**
	 * This method should fail because the injecting of the implementation is not good practice
	 * 
	 * TODO: should fail injecting implementation
	 */
	@Test
	public void testServiceMethodCallInjectedImplementation() {
		int result = getClient().getService(ServiceThatInjectOtherService.class).getIntegerFromInjectedService();
		assertEquals(BasicService.returnedValue.intValue(), result);
	}

	/**
	 * {@link ServiceThatInjectOtherServiceImplBad} is bad implementation because it's injecting implementation rather
	 * than interface so test should fail
	 */
	@Override
	protected HashMap<Class<?>, Class<?>> getServices() {
		HashMap<Class<?>, Class<?>> map = super.getServices();
		map.put(ServiceThatInjectOtherService.class, ServiceThatInjectOtherServiceImplBad.class);
		return map;
	}
}