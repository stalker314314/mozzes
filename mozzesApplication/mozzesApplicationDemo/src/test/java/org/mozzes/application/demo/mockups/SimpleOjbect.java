package org.mozzes.application.demo.mockups;

import java.io.Serializable;

public class SimpleOjbect implements Serializable {

	private static final long serialVersionUID = 1L;

	private String simpleAttribute;

	public String getSimpleAttribute() {
		return simpleAttribute;
	}

	public void setSimpleAttribute(String simpleAttribute) {
		this.simpleAttribute = simpleAttribute;
	}
}
