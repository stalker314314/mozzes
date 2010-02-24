package org.mozzes.event;

import junit.framework.TestCase;

import org.junit.Test;
import org.mozzes.event.Event;
import org.mozzes.event.configuration.EventConfiguration;
import org.mozzes.event.configuration.EventConfigurationException;
import org.mozzes.event.manager.EventManagerException;


/**
 * Test cases for local events when using queue
 * 
 * @author Kokan
 */
public class TestLocalEventWithQueue extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testEventsWithQueueDontWork() throws EventConfigurationException {
		Event event = new EventMockup();
		EventConfiguration configuration = new EventConfiguration();
		configuration.addLocal(MockupEvent.class);
		event.configure(configuration);
		try {
			event.getManager(MockupEvent.class).subscribe(new MockupEventHandler(), true);
			fail();
		} catch (EventManagerException e) {
		}
	}
}