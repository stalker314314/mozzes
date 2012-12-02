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
