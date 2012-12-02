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
package org.mozzes.invocation.common.interceptor;

import java.util.Collection;

import org.mozzes.invocation.common.Invocation;
import org.mozzes.invocation.common.handler.ImplementationInvocationHandler;
import org.mozzes.invocation.common.handler.InvocationHandler;

public class InvocationInterceptorChain<I> implements InvocationHandler<I> {

  private InvocationChainMember<I> head = null;
  private InvocationChainMember<I> tail = null;
  private final InvocationHandler<I> target;

  public <II extends I> InvocationInterceptorChain(II target) {
    this.target = new InvocationChainTail<I>(new ImplementationInvocationHandler<I>(target));
  }

  public InvocationInterceptorChain(InvocationHandler<? extends I> handler) {
    this.target = new InvocationChainTail<I>(handler);
  }

  public InvocationInterceptorChain<I> add(InvocationInterceptor interceptor) {
    InvocationChainMember<I> newNode = new InvocationChainMember<I>(interceptor, this.target);

    if (this.tail != null) {
      this.tail.next = newNode;
      this.tail = newNode;
    } else {
      this.head = this.tail = newNode;
    }

    return this;
  }

  public InvocationInterceptorChain<I> addAll(InvocationInterceptor... interceptors) {
    for (InvocationInterceptor interceptor : interceptors)
      add(interceptor);

    return this;
  }

  public InvocationInterceptorChain<I> addAll(Collection<InvocationInterceptor> interceptors) {
    for (InvocationInterceptor interceptor : interceptors)
      add(interceptor);

    return this;
  }

  @Override
  public Object invoke(Invocation<? super I> invocation) throws Throwable {
    if (head != null)
      return head.invoke(invocation);

    return target.invoke(invocation);
  }

  private static class InvocationChainTail<II> implements InvocationHandler<II> {
    private InvocationHandler<? extends II> nodeTarget;

    public InvocationChainTail(InvocationHandler<? extends II> nodeTarget) {
      this.nodeTarget = nodeTarget;
    }

    @Override
    public Object invoke(Invocation<? super II> invocation) throws Throwable {
      return this.nodeTarget.invoke(invocation);
    }
  }

  private static class InvocationChainMember<II> implements InvocationHandler<II> {

    private InvocationInterceptor nodeTarget;
    private InvocationHandler<II> next;

    public InvocationChainMember(InvocationInterceptor nodeTarget, InvocationHandler<II> next) {
      this.nodeTarget = nodeTarget;
      this.next = next;
    }

    @Override
    public Object invoke(Invocation<? super II> invocation) throws Throwable {
      return nodeTarget.invoke(invocation, next);
    }
  }

}
