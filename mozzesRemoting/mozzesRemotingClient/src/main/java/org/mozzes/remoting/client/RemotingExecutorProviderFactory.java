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
package org.mozzes.remoting.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mozzes.remoting.client.ondemand.RemotingOnDemandClientProvider;

import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingConfiguration;

/**
 * Main class for utilizing remoting clients providers. <br>
 * Clients first need to configure pool with either of two supplied methods: {@link #addProvider(String, int, int)} and
 * {@link #addProvider(String, int)}. First one configures factory to return pool provider and former on-demand
 * providers. Remote action executor can then be picked from providers with {@link RemotingActionExecutorProvider#get()}
 * . Nothing else is required from clients.
 * 
 * <p>
 * This class is thread-safe
 * </p>
 * 
 * @author Kokan
 */
public class RemotingExecutorProviderFactory {

  /** map of configuration -> executor provider */
  private ConcurrentMap<RemotingConfiguration, RemotingActionExecutorProvider> pools = new ConcurrentHashMap<RemotingConfiguration, RemotingActionExecutorProvider>();

  /** Factory for creating remoting clients */
  private final RemotingClientFactory clientFactory;

  /**
   * Default constructor
   * 
   * @param clientFactory
   *          Client factory with which this executor should be created
   */
  public RemotingExecutorProviderFactory(RemotingClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }

  /**
   * Adds new configuration to factory that will return on-demand providers. On-demand providers should be used when
   * very small number of actions is going to be executed on remoting server If identical configuration already exists,
   * throws {@link IllegalArgumentException}
   * 
   * @param host
   *          server address
   * @param port
   *          server listening port
   * @return This object for chaining
   */
  public RemotingExecutorProviderFactory addProvider(String host, int port) {
    RemotingConfiguration configuration = new RemotingConfiguration(host, port);
    addConfiguration(configuration, new RemotingOnDemandClientProvider(configuration, clientFactory));
    return this;
  }

  /**
   * Maps configuration with supplied provider
   */
  private void addConfiguration(RemotingConfiguration configuration, RemotingActionExecutorProvider executorProvider) {
    if (pools.putIfAbsent(configuration, executorProvider) != null) {
      throw new IllegalArgumentException("Configuration already exists");
    }
  }

  /**
   * @param configuration
   *          Configuration clients wants to check for existance
   * @return <code>true</code> if specified configuration already exists, <code>false</code> otherwise
   */
  public boolean configurationExists(RemotingConfiguration configuration) {
    return pools.containsKey(configuration);
  }

  /**
   * Gets the action execution provider for specified configuration. If this configuration is not previously configured
   * with this manager, {@link IllegalArgumentException} is thrown
   * 
   * @param host
   *          server address
   * @param port
   *          server listening port
   * @return Provider that knows how to provide remoting clients form given configuration
   */
  public RemotingActionExecutorProvider getActionExecutionProvider(String host, int port) {
    RemotingActionExecutorProvider pool = pools.get(new RemotingConfiguration(host, port));
    if (pool == null) {
      throw new IllegalArgumentException("Configuration does not exists");
    }
    return pool;
  }
}