package org.mozzes.cache.map;

import org.mozzes.cache.instance.InstanceProvider;

/**
 * Adapter class which allows {@link MapProvider} to act like {@link InstanceProvider}.
 *
 * @param <K> Key class
 * @param <T> Instance class
 */
public class MapInstanceProviderAdapter<K, T> implements InstanceProvider<T> {
	
	private final MapProvider<K, T> mapProvider;
	private final K key;
	
	public MapInstanceProviderAdapter(MapProvider<K, T> mapProvider, K key) {
		this.mapProvider = mapProvider;
		this.key = key;
	}

	@Override
	public T get() throws Exception {
		return mapProvider.get(key);
	}

}
