package org.mozzes.utils.reflection;

public interface Invoker<ReturnType, ObjectType> {
	ReturnType invoke(ObjectType object);
}