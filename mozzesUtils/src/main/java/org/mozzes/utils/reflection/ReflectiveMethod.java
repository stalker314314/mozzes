package org.mozzes.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectiveMethod<ObjectType, ReturnType> {
	private final Method method;

	public ReflectiveMethod(Method method) {
		if (method == null)
			throw new IllegalArgumentException("Method must not be null!");
		this.method = method;
	}

	@SuppressWarnings("unchecked")
	public ReturnType invoke(ObjectType object, Object... args) {
		Object result = null;
		try {
			result = method.invoke(object, args);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(
					String.format(
					"Exception occured while invoking reflective method \"%s\".\n" +
					"Exception: %s; Message: %s",
					method,
					e.getCause().getClass().getSimpleName()
					, e.getCause().getMessage()));
		}
		return (ReturnType) result;
	}

	public Method getMethod() {
		return method;
	}

	public boolean isAccessible() {
		return method.isAccessible();
	}

	public void setAccessible(boolean accessible) {
		method.setAccessible(accessible);
	}

	@SuppressWarnings("unchecked")
	public Class<ReturnType> getReturnType() {
		return (Class<ReturnType>) method.getReturnType();
	}

	public Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReflectiveMethod<?, ?>))
			return false;
		ReflectiveMethod<?, ?> other = (ReflectiveMethod<?, ?>) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}
}