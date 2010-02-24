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