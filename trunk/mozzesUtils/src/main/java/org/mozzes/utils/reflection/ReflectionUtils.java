package org.mozzes.utils.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozzes.utils.CollectionUtils;
import org.mozzes.utils.StringUtils;


public class ReflectionUtils {
	private static final Map<Class<?>, Map<MethodDeclaration, ReflectiveMethod<?, ?>>> methodCache =
			Collections.synchronizedMap(new HashMap<Class<?>, Map<MethodDeclaration, ReflectiveMethod<?, ?>>>());

	private ReflectionUtils() {
	}

	/**
	 * @param <ObjectType> Type of object on which the method can be invoked
	 * @param <ReturnType> Return type of the method
	 * @param clazz Class of object on which the method can be invoked
	 * @param returnType Class of the return type of the method
	 * @param name Name of the method
	 * @param parameterTypes List of parameter types(in the same order as defined in method declaration)
	 * @return {@link ReflectiveMethod} object that can be used to invoke the method
	 * @throws NoSuchMethodException If method with such declaration does not exist
	 */
	@SuppressWarnings("unchecked")
	public static <ObjectType, ReturnType> ReflectiveMethod<ObjectType, ReturnType> getMethod(
			Class<? extends ObjectType> clazz, Class<ReturnType> returnType, String name,
			Class<?>... parameterTypes)
			throws NoSuchMethodException {
		if (clazz == null)
			throw new IllegalArgumentException("Clazz must not be null!");
		Class<ReturnType> realReturnType = returnType;
		if (realReturnType == null)
			realReturnType = (Class<ReturnType>) Void.class;
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name must not be null or empty!");

		MethodDeclaration declaration = new MethodDeclaration(name, realReturnType, parameterTypes);

		Map<MethodDeclaration, ReflectiveMethod<?, ?>> map = methodCache.get(clazz);
		if (map == null) {
			map = Collections.synchronizedMap(new HashMap<MethodDeclaration, ReflectiveMethod<?, ?>>());
			methodCache.put(clazz, map);
		}
		ReflectiveMethod<ObjectType, ReturnType> method = (ReflectiveMethod<ObjectType, ReturnType>) map
				.get(declaration);

		if (method != null) {
			checkReturnType(realReturnType, method);
			return method;
		}

		Method m = null;
		Class<?> currentClass = clazz;
		do {
			try {
				m = currentClass.getDeclaredMethod(name, parameterTypes);
			} catch (SecurityException e) {
				throw new ReflectionException(e);
			} catch (NoSuchMethodException e) {
				if (currentClass.getSuperclass() == null) {
					throw new NoSuchMethodException(
							String.format("Method: %s, params: %s", name, Arrays.toString(parameterTypes)));
				} else {
					currentClass = currentClass.getSuperclass();
				}
			}
		} while (m == null);

		method = new ReflectiveMethod<ObjectType, ReturnType>(m);
		checkReturnType(realReturnType, method);

		map.put(declaration, method);

		return method;
	}

	/**
	 * Checks if the specified return type is equal to real return type of the method and throws
	 * IllegalArgumentException exception if not!
	 */
	/**
	 * @param <ReturnType> Expected return type
	 * @param <ObjectType> Type of the object which contains the specified method
	 * @param returnType Expected return class
	 * @param method Method to be checked
	 */
	private static <ReturnType, ObjectType> void checkReturnType(Class<ReturnType> returnType,
			ReflectiveMethod<ObjectType, ReturnType> method) {
//		Class<ReturnType> passedReturnType = resolvePrimitiveType(returnType);
//		Class<?> methodReturnType = resolvePrimitiveType(method.getMethod().getReturnType());
//		if (passedReturnType.equals(Void.class))
//			return;
//		if (!passedReturnType.isAssignableFrom(methodReturnType)) {
//			throw new IllegalArgumentException(String.format(
//					"Specified return type(%s) does not match real return type(%s)",
//					returnType, method.getMethod().getReturnType()));
//		}
	}

	/**
	 * @param <ObjectType> Type of object on which the method can be invoked
	 * @param clazz Class of object on which the method can be invoked
	 * @param name Name of the method
	 * @param parameterTypes List of parameter types(in the same order as defined in method declaration)
	 * @return {@link ReflectiveMethod} object that can be used to invoke the method
	 * @throws NoSuchMethodException If method with such declaration does not exist
	 */
	public static <ObjectType> ReflectiveMethod<ObjectType, Void> getMethod(Class<? extends ObjectType> clazz,
			String name,
			Class<?>... parameterTypes)
			throws NoSuchMethodException {
		return getMethod(clazz, Void.class, name, parameterTypes);
	}

	/**
	 * Retuns the collecion composed of values of the given property for each object<br>
	 * [WARNING: If the collection is not empty, then the first element must not be null,<br>
	 * any other can, but not first]
	 * 
	 * @param <ReturnType> The type of property
	 * @param <ObjectType> The type of object
	 * @param collection The collection of objects to take the properties from
	 * @param propertyName The name of the property (ie: if you pass 'id' object.getId() will be called for each object
	 *            in collection) WARNING: Property name IS case-sensitive where the first letter always has to be
	 *            lower-case, and to others standard camel-case is applied.
	 */
	@SuppressWarnings("unchecked")
	public static <ReturnType, ObjectType> List<ReturnType> getPropertyForCollection(
			Class<ReturnType> clazz, Collection<ObjectType> collection,
			String propertyName) throws ReflectionException {
		if (collection == null)
			throw new NullPointerException();
		if (collection.isEmpty())
			return Collections.emptyList();
		if (propertyName == null)
			throw new NullPointerException();
		List<ReturnType> result = new ArrayList<ReturnType>(collection.size());

		ObjectType first = CollectionUtils.getFirstOrNull(collection);
		if (first == null)
			throw new NullPointerException();

		ReflectiveMethod<ObjectType, ReturnType> getter;
		try {
			getter = getGetter((Class<ObjectType>) first.getClass(), clazz, propertyName);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException(e);
		}

		for (ObjectType o : collection) {

			if (o == null) {
				result.add(null);
				continue;
			}

			ReturnType ret = getter.invoke(o);
			result.add(ret);
		}
		return result;
	}

	/**
	 * Retuns the collecion composed of values of the given property for each object<br>
	 * [WARNING: If the collection is not empty, then the first element must not be null,<br>
	 * any other can, but not first]
	 * 
	 * @param <ReturnType> The type of property
	 * @param <ObjectType> The type of object
	 * @param collection An array of objects to take the properties from
	 * @param propertyName The name of the property (ie: if you pass 'id' object.getId() will be called for each object
	 *            in collection) WARNING: Property name IS case-sensitive where the first letter always has to be
	 *            lower-case, and to others standard camel-case is applied.
	 */
	public static <ReturnType, ObjectType> List<ReturnType> getPropertyForCollection(Class<ReturnType> clazz,
			ObjectType[] collection,
			String propertyName) throws ReflectionException {
		return getPropertyForCollection(clazz, Arrays.asList(collection), propertyName);
	}

	/**
	 * Returns the list of results of Invoker.invoke() for every object in collection.
	 */
	public static <ReturnType, ObjectType> List<ReturnType> getPropertyForCollection(
			Collection<? extends ObjectType> collection, Invoker<ReturnType, ObjectType> invoker) {
		if (collection == null)
			throw new IllegalArgumentException("Collection cannot be null!");
		if (invoker == null)
			throw new IllegalArgumentException("Invoker cannot be null!");
		if (collection.isEmpty())
			return new ArrayList<ReturnType>();

		List<ReturnType> result = new ArrayList<ReturnType>(collection.size());

		for (ObjectType object : collection) {
			if (object == null) {
				result.add(null);
				continue;
			}
			result.add(invoker.invoke(object));
		}

		return result;
	}

	/**
	 * Vraca getter metodu za zadati property
	 * 
	 * @param clazz Klasa koja sadrzi metodu
	 * @param property Ime property-a za koji se vraca getter
	 * @return get{Property} ili is{Property} metodu ukoliko postoji, ukoliko ne, vraca null
	 */
	public static <ObjectType, PropertyType> ReflectiveMethod<ObjectType, PropertyType> getGetter(
			Class<ObjectType> clazz, Class<PropertyType> propertyType, String property) throws NoSuchMethodException {
		ReflectiveMethod<ObjectType, PropertyType> getter = null;
		if (property == null || property.isEmpty())
			throw new IllegalArgumentException("Property name cannot be null!");

		try {
			getter = getMethod(clazz, propertyType, "get" + StringUtils.capitalize(property));
		} catch (NoSuchMethodException e) {
			// if (!resolvePrimitiveType(propertyType).equals(Boolean.class))
			// throw e;
			getter = getMethod(clazz, propertyType, "is" + StringUtils.capitalize(property));
			}
		return getter;
	}

	public static <ObjectType> ReflectiveMethod<ObjectType, Object> getGetter(
			Class<ObjectType> clazz, String property) throws NoSuchMethodException {
		return getGetter(clazz, Object.class, property);
	}

	/**
	 * Vraca setter metodu za zadati property
	 * 
	 * @param clazz Klasa koja sadrzi metodu
	 * @param propertyType Tip property-a
	 * @param property Ime property-a za koji se vraca getter
	 * @return set{Property} metodu ukoliko postoji, ukoliko ne, vraca null
	 */
	public static <ObjectType, PropertyType> ReflectiveMethod<ObjectType, Void> getSetter(
			Class<ObjectType> clazz, Class<PropertyType> propertyType, String property) throws ReflectionException {
		if (property == null || clazz == null || propertyType == null)
			throw new IllegalArgumentException("None of the parametars can be null!");
		if (property.isEmpty())
			throw new IllegalArgumentException("Property parametar cannot be empty!");

		ReflectiveMethod<ObjectType, Void> setter;
		String methodName = "set" + StringUtils.capitalize(property);
		try {
			setter = getMethod(clazz, methodName, new Class<?>[] { propertyType });
		} catch (NoSuchMethodException e) {
			Class<PropertyType> alternativePropertyType = null;
			if (propertyType.isPrimitive())
				alternativePropertyType = resolvePrimitiveType(propertyType);
			else {
				alternativePropertyType = resolveToPrimitiveType(propertyType);
			}
			if (alternativePropertyType == null)
				throw new ReflectionException(e);
			try {
				setter = getMethod(clazz, methodName, new Class<?>[] { alternativePropertyType });
			} catch (NoSuchMethodException e1) {
				throw new ReflectionException(e);
			}
		}

		return setter;
	}

	/**
	 * @param <ReturnType> Type of the property
	 * @param <ObjectType> Type of an object which contains the property
	 * @param object Object of type <b>ObjectType</b> for which to fetch the value
	 * @param property Name of the property for which to take the value
	 * @return Property value for specified object
	 * @throws ReflectionException If anything happens while fetching
	 */
	@SuppressWarnings("unchecked")
	public static <ReturnType, ObjectType> ReturnType getPropertyValue(ObjectType object, Class<ReturnType> returnType,
			String property)
			throws ReflectionException {
		if (object == null)
			return null;
		if (property == null || property.isEmpty())
			throw new IllegalArgumentException("Proeprty name cannot be null or empty!");

		ReflectiveMethod<ObjectType, ReturnType> getter;
		try {
			getter = getGetter((Class<ObjectType>) object.getClass(), returnType,
					property);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException(e);
		}

		ReturnType result;
		try {
			result = getter.invoke(object);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} catch (ReflectionException e) {
			throw e;
		}

		return result;
	}

	/**
	 * If the passed <i>clazz</i> is a primitive type returns the {@link Class type} of its wrapping clazz, if it's not
	 * a primitive returns it unchanged
	 * 
	 * @param <T> Type of the the {@link Class class}
	 * @param clazz {@link Class} to resolve
	 * @return If the <i>clazz</i> is not primitive, its returned unchanged, else, its wrapper class is returned
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> resolvePrimitiveType(Class<T> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("Class cannot be null!");
		if (!clazz.isPrimitive())
			return clazz;

		if (clazz == int.class)
			return (Class<T>) Integer.class;
		if (clazz == boolean.class)
			return (Class<T>) Boolean.class;
		if (clazz == double.class)
			return (Class<T>) Double.class;
		if (clazz == float.class)
			return (Class<T>) Float.class;
		if (clazz == byte.class)
			return (Class<T>) Byte.class;
		if (clazz == char.class)
			return (Class<T>) Character.class;
		if (clazz == void.class)
			return (Class<T>) Void.class;
		throw new IllegalStateException("This line should never be executed!");
	}

	/**
	 * If the passed <i>clazz</i> is a wrapper of primitive type returns the {@link Class type} of its primitive type,
	 * if it's not a primitive returns it unchanged
	 * 
	 * @param <T> Type of the the {@link Class class}
	 * @param clazz {@link Class} to resolve
	 * @return If the <i>clazz</i> is not primitive wrapper, null is returned, else, its primitive class is returned
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> resolveToPrimitiveType(Class<T> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("Class cannot be null!");

		if (clazz.isPrimitive())
			return clazz;

		if (clazz == Integer.class)
			return (Class<T>) int.class;
		if (clazz == Boolean.class)
			return (Class<T>) boolean.class;
		if (clazz == Double.class)
			return (Class<T>) double.class;
		if (clazz == Float.class)
			return (Class<T>) float.class;
		if (clazz == Byte.class)
			return (Class<T>) byte.class;
		if (clazz == Character.class)
			return (Class<T>) char.class;
		if (clazz == Void.class)
			return (Class<T>) void.class;
		return null;
	}

	/**
	 * Returns the default value for a specified class (if clazz is a primitive type returns the default value for that
	 * type, otherwise returns null
	 * 
	 * @param clazz Class to get the default value for
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDefaultValueOf(Class<T> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("Class cannot be null!");
		if (!clazz.isPrimitive())
			return null;

		if (clazz == int.class)
			return (T) Integer.valueOf(0);
		if (clazz == boolean.class)
			return (T) Boolean.FALSE;
		if (clazz == double.class)
			return (T) Double.valueOf(0);
		if (clazz == float.class)
			return (T) Float.valueOf(0);
		if (clazz == byte.class)
			return (T) Byte.valueOf((byte) 0);
		if (clazz == long.class)
			return (T) Long.valueOf(0);
		if (clazz == char.class)
			return (T) Character.valueOf('\u0000');

		throw new IllegalStateException("This line should never be executed!");
	}

	/**
	 * Resolves the value to a value acceptable for the specified clazz(if value is not null it is returned unchanged),
	 * if the value is null and clazz is a primitive type returns the default value for that type
	 * 
	 * @param clazz Required type (primitive if needed)
	 * @param value Value to be resolved
	 */
	public static <T> T resolvePrimitiveNull(Class<T> clazz, T value) {
		if (value != null)
			return value;
		if (clazz == null)
			return value;
		T defaultValue = getDefaultValueOf(clazz);
		if (defaultValue == null)
			return value;
		return defaultValue;
	}

	/**
	 * Converts a number to a specified number type (cutting of decimals if needed!)
	 * 
	 * @param <T> Type of the result
	 * @param from Value to convert
	 * @param to Required class
	 * @return Value converted to required class
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T convertNumber(Number from, Class<T> to) {
		Class<?> realTo = resolvePrimitiveType(to);
		if (from == null)
			return null;
		if (realTo.isAssignableFrom(Integer.class))
			return (T) (Object) from.intValue();
		if (realTo.isAssignableFrom(Double.class))
			return (T) (Object) from.doubleValue();
		if (realTo.isAssignableFrom(Float.class))
			return (T) (Object) from.floatValue();
		if (realTo.isAssignableFrom(Byte.class))
			return (T) (Object) from.byteValue();
		if (realTo.isAssignableFrom(Long.class))
			return (T) (Object) from.longValue();
		if (realTo.isAssignableFrom(Short.class))
			return (T) (Object) from.shortValue();
		throw new IllegalArgumentException("Cannot convert value to " + realTo.getSimpleName());
	}
}
