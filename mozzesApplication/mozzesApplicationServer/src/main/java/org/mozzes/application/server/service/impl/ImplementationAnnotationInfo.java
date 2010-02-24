package org.mozzes.application.server.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.mozzes.application.module.ServiceConfiguration;
import org.mozzes.invocation.common.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provides info about annotations declared on service implementation classes. 
 */
public class ImplementationAnnotationInfo {

	private static final Logger logger = LoggerFactory.getLogger(ImplementationAnnotationInfo.class);

	private Map<InfoKey, InfoValue> info = new HashMap<InfoKey, InfoValue>();

	/**
     * Returns annotation for the specified service implementation if such an annotation is present, else null.
     * @param invocation Service invocation 
     * @param annotationClass the Class object corresponding to the  annotation type
     * @return annotation for the specified service implementation if present on this element, else null
     * @throws NullPointerException if the given annotation class is null
	 */
	public <T extends Annotation> T getAnnotation(Invocation<?> invocation, Class<T> annotationClass) {
		try {
			InfoValue infoValue = info.get(new InfoKey(invocation.getInterface(), invocation.getMethod()));
			if (infoValue == null)
				return null;

			return infoValue.getAnnotation(annotationClass);
		} catch (Exception ignore) {
			// will not happen
			logger.error("Unable to get method method of invocation", ignore);
			return null;
		}
	}

	/**
	 * Performed during server configuration, adds info about service implementation
	 */
	public <I> void addImplementationAnnotationInfo(ServiceConfiguration<I> serviceConfiguration) {

		Method[] interfaceMethods = serviceConfiguration.getServiceInterface().getDeclaredMethods();
		for (int i = 0; i < interfaceMethods.length; i++) {
			try {
				Method implMethod = serviceConfiguration.getServiceImplementation().getMethod(
						interfaceMethods[i].getName(), interfaceMethods[i].getParameterTypes());
				
				InfoValue infoValue = new InfoValue();
				info.put(new InfoKey(serviceConfiguration.getServiceInterface(), interfaceMethods[i]), infoValue);

				Annotation annotations[] = implMethod.getAnnotations();
				for (int j = 0; j < annotations.length; j++)
					infoValue.addAnnotation(annotations[j]);
				
			} catch (Exception ignore) {
			}
		}
	}

	private static class InfoKey {

		private final Class<?> serviceClass;
		private final Method method;

		InfoKey(Class<?> serviceClass, Method method) {
			this.serviceClass = serviceClass;
			this.method = method;
		}

		@Override
		public int hashCode() {
			return 31 * (31 + serviceClass.hashCode()) + method.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			InfoKey other = (InfoKey) obj;
			if (!serviceClass.equals(other.serviceClass))
				return false;
			if (!method.equals(other.method))
				return false;
			return true;
		}
	}

	private static class InfoValue {

		private final Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

		InfoValue() {
			this.declaredAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();
		}
		
		public void addAnnotation(Annotation annotation) {
			declaredAnnotations.put(annotation.annotationType(), annotation);
		}

		@SuppressWarnings("unchecked")
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return (T) declaredAnnotations.get(annotationClass);
		}
	}

}
