package org.mozzes.event.dispatcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Event that happened in system and which waits for proccessing in subscribed handlers
 * 
 * @author Perica Milosevic
 * @version 1.7.5
 */
class EventDetails {

	/**
	 * Handler method that is called for dispatch
	 */
	private Method dispatchMethod;

	/**
	 * Parameters to forward during event processing
	 */
	private byte[] dispatchMethodParams;

	/**
	 * Creating informations about event that happened
	 * 
	 * @param dispatchMethod Handler method that is called for dispatch
	 * @param dispatchMethodParams Parameters to forward during event processing
	 * @throws IOException If parameters doesn't implements {@link Serializable}
	 */
	EventDetails(Method dispatchMethod, Object[] dispatchMethodParams) throws IOException {
		this.dispatchMethod = dispatchMethod;
		setDispatchMethodParams(dispatchMethodParams);
	}

	/**
	 * Event processing
	 * 
	 * @param handler Handler to proccess event
	 * @throws Throwable - Original exception from handler or exception that happened during handler invocation
	 */
	void dispatch(Object handler) throws Throwable {
		try {
			this.dispatchMethod.invoke(handler, getDispatchMethodParams());
		} catch (InvocationTargetException e) {
			Throwable originalException = e.getTargetException();
			if (originalException != null)
				throw originalException;
			else
				throw e;
		}
	}

	/**
	 * @return String - method signature in format : &lt;class&gt;.&lt;method&gt;(&lt;parameterType1&gt;,
	 *         &lt;parameterType2&gt;, ...)
	 */
	String getMethodSignature() {
		StringBuffer sb = new StringBuffer();
		sb.append(dispatchMethod.getDeclaringClass().getName());
		sb.append(".").append(dispatchMethod.getName()).append("(");

		final int n = dispatchMethod.getParameterTypes().length;
		for (int i = 0; i < n; i++) {
			sb.append(dispatchMethod.getParameterTypes()[i].getName());
			if (i < (n - 1))
				sb.append(", ");
		}
		sb.append(")");

		return sb.toString();
	}

	@Override
	public String toString() {
		return this.dispatchMethod.getName();
	}

	/**
	 * Event parameters are kept in byte array
	 * 
	 * @param dispatchMethodParams Event parameters
	 * @throws IOException If serialization of event parameters failed
	 */
	private void setDispatchMethodParams(Object[] dispatchMethodParams) throws IOException {
		if (dispatchMethodParams == null) {
			this.dispatchMethodParams = null;
			return;
		}

		ByteArrayOutputStream byteStream = null;
		try {
			byteStream = new ByteArrayOutputStream(512);

			ObjectOutputStream objStream = null;
			try {
				objStream = new ObjectOutputStream(byteStream);
				objStream.writeObject(dispatchMethodParams);
				objStream.flush();
			} finally {
				if (objStream != null)
					try {
						objStream.close();
					} catch (IOException ignore) {
					}
			}

			this.dispatchMethodParams = byteStream.toByteArray();
		} finally {
			if (byteStream != null)
				try {
					byteStream.close();
				} catch (IOException ignore) {
				}
		}
	}

	/**
	 * @return Event parameters retrieved with deserialization
	 * @throws ClassNotFoundException If parameters have unknown class
	 * @throws IOException If deserialization failed
	 */
	private Object[] getDispatchMethodParams() throws IOException, ClassNotFoundException {
		if (this.dispatchMethodParams == null)
			return null;

		Object[] returnValue = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(this.dispatchMethodParams));
			returnValue = (Object[]) in.readObject();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException ignore) {
				}
		}

		return returnValue;
	}

}
