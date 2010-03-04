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
package org.mozzes.event;

import junit.framework.TestCase;

import org.junit.Test;
import org.mozzes.event.Event;
import org.mozzes.event.configuration.EventConfiguration;
import org.mozzes.event.configuration.EventConfigurationException;


/**
 * Test configuration for events
 * 
 * @author Kokan
 */
public class TestConfiguration extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testSameInterfaceAdding() throws EventConfigurationException {
		Event event = new EventMockup();
		EventConfiguration configuration = new EventConfiguration();
		configuration.addLocal(MockupEvent.class);
		configuration.addLocal(MockupEvent.class);
		try {
			event.configure(configuration);
			fail();
		} catch (EventConfigurationException e) {
		}
	}

	@Test
	public void testSetNull() {
		EventConfiguration configuration = new EventConfiguration();
		try {
			configuration.addLocal(null);
			fail();
		} catch (EventConfigurationException e) {
		}
		try {
			configuration.setEventDispatcher(null);
			fail();
		} catch (EventConfigurationException e) {
		}
		try {
			configuration.setNumberOfDispatchThreads(0);
		} catch (EventConfigurationException e) {
			fail();
		}
		try {
			configuration.setNumberOfDispatchThreads(-1);
			fail();
		} catch (EventConfigurationException e) {
		}
		try {
			configuration.setNumberOfDispatchThreads(10);
			fail();
		} catch (EventConfigurationException e) {
		}
		try {
			new EventMockup().configure(null);
			fail();
		} catch (EventConfigurationException e) {
		}
	}
}