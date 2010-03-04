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
package org.mozzes.cache.instance;

import org.mozzes.cache.instance.InstanceCache;

import junit.framework.TestCase;

public class TestInstanceCache extends TestCase {

	private MockupInstanceProvider instanceProvider;
	private InstanceCache<Object> cache;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		instanceProvider = new MockupInstanceProvider();
		cache = new InstanceCache<Object>(instanceProvider);
	}

	public void testInitialization() throws Exception {
		assertEquals(false, cache.isInstanceInitialized());
		cache.get();
		assertEquals(true, cache.isInstanceInitialized());
		cache.clear();
		assertEquals(false, cache.isInstanceInitialized());
		cache.get();
		assertEquals(true, cache.isInstanceInitialized());
	}

	public void testCaching() throws Exception {
		assertEquals(0, instanceProvider.getInstanceCount());
		cache.get();
		assertEquals(1, instanceProvider.getInstanceCount());
		cache.get();
		assertEquals(1, instanceProvider.getInstanceCount());
		cache.clear();
		assertEquals(1, instanceProvider.getInstanceCount());
		cache.get();
		assertEquals(2, instanceProvider.getInstanceCount());
		cache.get();
		assertEquals(2, instanceProvider.getInstanceCount());
	}

	public void testConcurrent() throws Exception {
		MockupWaitingInstanceProvider mwip = new MockupWaitingInstanceProvider();
		InstanceCache<Object> waitingCache = new InstanceCache<Object>(mwip);
		assertEquals(0, mwip.getInstanceCount());

		final int instancesPerConsumer = 100;
		MockupConsumer[] consumers = new MockupConsumer[5];
		for (int i = 0; i < consumers.length; i++) {
			consumers[i] = new MockupConsumer(waitingCache, instancesPerConsumer);
			consumers[i].start();
		}
		
		while (!mwip.isReady())
			Thread.sleep(instancesPerConsumer);
		assertEquals(0, mwip.getInstanceCount());
		
		for (int i = 0; i < consumers.length; i++) 
			assertEquals(0, consumers[i].getInstanceCount());

		mwip.go();

		for (int i = 0; i < consumers.length; i++)
			consumers[i].join();

		assertEquals(1, mwip.getInstanceCount());
		
		
		for (int i = 0; i < consumers.length; i++) 
			assertEquals(instancesPerConsumer, consumers[i].getInstanceCount());
	}

}
