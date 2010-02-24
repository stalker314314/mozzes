package org.mozzes.utils.reflection;

import java.util.Arrays;

class MethodDeclaration {
	private final String name;
	private final Class<?>[] params;
	private final Class<?> returnType;

	public MethodDeclaration(String name, Class<?> returnType, Class<?>... params) {
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}

	public String getName() {
		return name;
	}

	public Class<?>[] getParams() {
		return params;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(params);
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MethodDeclaration))
			return false;
		MethodDeclaration other = (MethodDeclaration) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(params, other.params))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}
}