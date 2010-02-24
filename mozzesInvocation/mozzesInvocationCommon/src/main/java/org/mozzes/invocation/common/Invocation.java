package org.mozzes.invocation.common;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.mozzes.invocation.common.handler.InvocationHandler;


/**
 * This class encapsulates method call of an interface I.
 * 
 * It holds all information about invoked method: interface that declares method, method name, method parameter types,
 * invocation arguments.
 * 
 * @author Perica Milosevic
 * @see InvocationHandler
 */
public class Invocation<I> implements Serializable {

	private static final long serialVersionUID = 6729541150245231631L;

	/** Class which represents interface I that declares invoked method. */
	private final Class<I> interfaceClass;

	/** Invoked method name */
	private final String methodName;

	/** Parameter types of invoked method */
	private final Class<?>[] parameterTypes;

	/** Argument values passed during method invocation */
	private final Object[] invocationArguments;

	protected Invocation(Class<I> interfaceClass, String methodName, Class<?>[] methodParameterTypes,
			Object[] methodInvocationArguments) {
		this.interfaceClass = interfaceClass;
		this.methodName = methodName;
		this.parameterTypes = methodParameterTypes;
		this.invocationArguments = methodInvocationArguments;
	}

	/**
	 * @return {@link Class} which represents interface I that declares invoked method
	 */
	public Class<I> getInterface() {
		return interfaceClass;
	}

	/**
	 * @return Invoked method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @return Parameter types of invoked method
	 */
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	/**
	 * @return Argument values passed during method invocation
	 */
	public Object[] getInvocationArguments() {
		return invocationArguments;
	}

	@Override
	public String toString() {
		StringBuilder returnValue = new StringBuilder(getInterface().getName());
		returnValue.append(".").append(getMethodName()).append("(");
		if (invocationArguments != null) {
			for (int i = 0; i < invocationArguments.length; i++) {
				returnValue.append(invocationArguments[i]);
				if (i < invocationArguments.length - 1) {
					returnValue.append(", ");
				}
			}
		}
		returnValue.append(")");
		return returnValue.toString();
	}

	/**
	 * Performs method invocation represented by this {@link Invocation} on given target
	 * 
	 * @param <II> Class which implements interface I
	 * @param target Object which implements invoked method
	 * @return Invocation result
	 * @throws Throwable - exception thrown during method invocation
	 */
	public <II extends I> Object invoke(II target) throws Throwable {
		try {
			return getMethod().invoke(target, invocationArguments);
		} catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	/**
	 * @return Invoked method
	 */
	public Method getMethod() throws SecurityException, NoSuchMethodException {
		return getInterface().getMethod(methodName, parameterTypes);
	}

	/**
	 * Returns true if other Invocation represents the same method call
	 * 
	 * @param other Invocation
	 * @return true if other Invocation represents the same method call
	 */
	public boolean isSameMethod(Invocation<?> other) {
		if ((interfaceClass == other.interfaceClass) && (methodName.equalsIgnoreCase(other.methodName))) {
			if (parameterTypes.length == other.parameterTypes.length) {
				for (int i = 0; i < parameterTypes.length; i++) {
					if (parameterTypes[i] != other.parameterTypes[i])
						return false;
				}
				return true;
			}
		}
		return false;
	}

	public static <I> Invocation<I> copy(Invocation<I> toCopy) {
		return new Invocation<I>(toCopy.interfaceClass, toCopy.methodName, 
				Arrays.copyOf(toCopy.parameterTypes, toCopy.parameterTypes.length), 
				Arrays.copyOf(toCopy.invocationArguments, toCopy.invocationArguments.length));
	}

}
