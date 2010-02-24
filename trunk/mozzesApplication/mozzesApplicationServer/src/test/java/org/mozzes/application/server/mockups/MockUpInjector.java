package org.mozzes.application.server.mockups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

public class MockUpInjector implements Injector {

	private final HashMap<Class<?>, Object> injectedObjects;

	public MockUpInjector(HashMap<Class<?>, Object> injectedObjects) {
		this.injectedObjects = injectedObjects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(Class<T> key) {
		return (T) injectedObjects.get(key);
	}

	@Override
	public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> arg0) {
		return null;
	}

	@Override
	public <T> Binding<T> getBinding(Key<T> arg0) {
		return null;
	}

	@Override
	public Map<Key<?>, Binding<?>> getBindings() {
		return null;
	}

	@Override
	public <T> T getInstance(Key<T> arg0) {
		return null;
	}

	@Override
	public <T> Provider<T> getProvider(Key<T> arg0) {
		return null;
	}

	@Override
	public <T> Provider<T> getProvider(Class<T> arg0) {
		return null;
	}

	@Override
	public void injectMembers(Object arg0) {
		// ignore
	}

	@Override
	public Injector createChildInjector(Iterable<? extends Module> arg0) {
		return null;
	}

	@Override
	public Injector createChildInjector(Module... arg0) {
		return null;
	}

	@Override
	public <T> Binding<T> getBinding(Class<T> arg0) {
		return null;
	}

	@Override
	public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> arg0) {
		return null;
	}

	@Override
	public <T> MembersInjector<T> getMembersInjector(Class<T> arg0) {
		return null;
	}

	@Override
	public Injector getParent() {
		return null;
	}
}
