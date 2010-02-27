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

class MockupConsumer extends Thread {

	private InstanceProvider<Object> ip;
	private int count;
	private volatile int instanceCount;

	MockupConsumer(InstanceProvider<Object> ip, int count) {
		super();
		this.ip = ip;
		this.count = count;
		this.instanceCount = 0;
	}

	@Override
	public void run() {
		for (int i = 0; i < count; i++) {
			try {
				ip.get();
				increaseInstanceCount();
			} catch (Exception ignore) {
			}
		}
	}

	synchronized int getInstanceCount() {
		return instanceCount;
	}

	private synchronized void increaseInstanceCount() {
		instanceCount++;
	}
}