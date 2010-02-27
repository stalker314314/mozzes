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

import org.mozzes.cache.instance.InstanceProvider;

public class MockupWaitingInstanceProvider implements InstanceProvider<Object> {

	private volatile int instanceCount = 0;
	
	private Object lock = new Object();
	private boolean ready = false;
	
	MockupWaitingInstanceProvider() {
	}

	@Override
	public Object get() throws Exception {
		waitSignal();
		instanceCount++;
		return new Object();
	}

	public int getInstanceCount() {
		return instanceCount;
	}
	
	boolean isReady() {
		synchronized (lock) {
			return ready;
		}
	}

	
	void go() {
		synchronized (lock) {
			lock.notify();
		}
	}
	
	private void waitSignal() {
		synchronized (lock) {
			ready = true;
			try {
				lock.wait();
			} catch (InterruptedException ignore) {
			}
		}
	}
	
	
}
