package org.mozzes.rest.jersey;

/**
 * Configuration for Jersey server, consisting of base URI and root package where services will reside
 * @author stalker
 */
class RestJerseyConfiguration {

	private final String baseUri;
	private final String rootResourcePackage;

	RestJerseyConfiguration(String baseUri, String rootResourcePackage) {
		super();
		this.baseUri = baseUri;
		this.rootResourcePackage = rootResourcePackage;
	}

	String getBaseUri() {
		return baseUri;
	}

	String getRootResourcePackage() {
		return rootResourcePackage;
	}
}