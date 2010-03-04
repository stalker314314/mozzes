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

import java.util.Random;

import junit.framework.TestCase;

import org.junit.Test;
import org.mozzes.event.Event;
import org.mozzes.event.configuration.EventConfiguration;
import org.mozzes.event.invoker.EventInvokerException;
import org.mozzes.event.manager.EventManagerException;


/**
 * Test cases for local events when not using queue
 * 
 * @author Kokan
 */
public class TestLocalEventWithoutQueue extends TestCase {
	public static final int NUM_HANDLERS = 10;

	private static Event event = null;

	private static MockupEventHandler mockupEventHandlers[] = new MockupEventHandler[NUM_HANDLERS];

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		if (event == null) {
			event = new EventMockup();
			/* event configuration */
			EventConfiguration eventServerConf = new EventConfiguration();
			eventServerConf.addLocal(MockupEvent.class);
			event.configure(eventServerConf);

			/* subscribes all handlers */
			for (int i = 0; i < NUM_HANDLERS; i++) {
				mockupEventHandlers[i] = new MockupEventHandler();
				event.getManager(MockupEvent.class).subscribe(mockupEventHandlers[i], false);
			}
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Invokes an event and checks result.
	 */
	@Test
	public void testSimpleEvent() throws EventInvokerException {
		int rand = new Random().nextInt();
		event.getInvoker(MockupEvent.class).onEvent(rand);

		for (int i = 0; i < NUM_HANDLERS; i++) {
			assertEquals(rand, mockupEventHandlers[i].getCurrentValue());
		}
	}

	/**
	 * Invokes an event and checks result. As {@link #testSimpleEvent()}, just iterates many times.
	 */
	@Test
	public void testManyEvents() throws EventInvokerException {
		for (int tries = 0; tries < 100; tries++) {
			int rand = new Random().nextInt();
			event.getInvoker(MockupEvent.class).onEvent(rand);

			for (int i = 0; i < NUM_HANDLERS; i++) {
				assertEquals(rand, mockupEventHandlers[i].getCurrentValue());
			}
		}
	}

	/**
	 * Checks if unsubscribe works and resubscription. At the end, all handlers are in subscribed state.
	 */
	@Test
	public void testManyUnsubscribeSubscribe() throws EventManagerException, EventInvokerException {
		for (int tries = 0; tries < 100; tries++) {
			Random random = new Random();
			/* take old values */
			int lastValues[] = new int[NUM_HANDLERS];
			for (int i = 0; i < NUM_HANDLERS; i++) {
				lastValues[i] = mockupEventHandlers[i].getCurrentValue();
			}
			/* unsubscribes all handlers */
			for (int i = 0; i < NUM_HANDLERS; i++) {
				event.getManager(MockupEvent.class).unsubscribe(mockupEventHandlers[i]);
			}

			/* invokes event */
			event.getInvoker(MockupEvent.class).onEvent(random.nextInt());
			/* check that old values are there */
			for (int i = 0; i < NUM_HANDLERS; i++) {
				assertEquals(lastValues[i], mockupEventHandlers[i].getCurrentValue());
			}
			/* subscribes all handlers again */
			for (int i = 0; i < NUM_HANDLERS; i++) {
				mockupEventHandlers[i] = new MockupEventHandler();
				event.getManager(MockupEvent.class).subscribe(mockupEventHandlers[i], false);
			}
			int rand = random.nextInt();
			event.getInvoker(MockupEvent.class).onEvent(rand);

			for (int i = 0; i < NUM_HANDLERS; i++) {
				assertEquals(rand, mockupEventHandlers[i].getCurrentValue());
			}
		}
	}

	/**
	 * Tests return type, it must always be <code>null</code>.
	 */
	@Test
	public void testReturnType() throws EventInvokerException {
		assertNull(event.getInvoker(MockupEvent.class).eventWithReturnType());
	}
}