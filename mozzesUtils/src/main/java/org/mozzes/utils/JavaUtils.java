package org.mozzes.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.swing.SwingUtilities;

/**
 * Static helper methods to ease the pain caused by Java
 * 
 * @author milos
 */
public class JavaUtils {

	/**
	 * Vraca ISPRAVAN niz objekata bez obzira na to da li je prosledjena kolekcija, niz ili argumenti razdvojeni zarezom <BR>
	 * <B>Napomena: Ovo koristiti samo ako imate var args tipa Object (tj. Object... args)</B>
	 */
	@SuppressWarnings( { "unchecked" })
	public static Object[] getArrayForVarArgs(Object[] args) {
		if (args.length == 1) {
			if (Collection.class.isInstance(args[0])) {
				Collection<Object> collection = (Collection<Object>) args[0];
				return collection.toArray();
			} else if (args[0].getClass().isArray()) {
				return (Object[]) args[0];
			}
		}
		return args;
	}

	/**
	 * Runs the specified <i>runnable</i> in Swing's EventDispatchThread
	 * 
	 * @param runnable Defines a procedure that is going to be ran in EventDispatchThread
	 */
	public static void runInEventDispatch(final Runnable runnable) {
		if (SwingUtilities.isEventDispatchThread()) {
			runnable.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(runnable);
			} catch (InterruptedException e) {
				if (e.getCause() instanceof RuntimeException)
					throw (RuntimeException) e.getCause();
				else
					throw new IllegalStateException(e);
			} catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof RuntimeException)
					throw (RuntimeException) e.getTargetException();
				else
					throw new IllegalStateException(e);
}
		}
	}

	/**
	 * Runs the specified <i>runnable</i> in Swing's EventDispatchThread
	 * 
	 * @param <R> Type of the result
	 * @param runnable Defines a procedure that is going to be ran in EventDispatchThread
	 * @return The result of the runnable method
	 */
	public static <R> R runInEventDispatch(final RunnableWithResult<R> runnable) {
		if (SwingUtilities.isEventDispatchThread()) {
			runnable.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(runnable);
			} catch (InterruptedException e) {
				if (e.getCause() instanceof RuntimeException)
					throw (RuntimeException) e.getCause();
				else
					throw new IllegalStateException(e);
			} catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof RuntimeException)
					throw (RuntimeException) e.getTargetException();
				else
					throw new IllegalStateException(e);
			}
		}
		return runnable.result;
	}

	/**
	 * Helper class for returning some value from other thread
	 * 
	 * @author milos
	 * 
	 * @param <R> The type of result
	 */
	public static abstract class RunnableWithResult<R> implements Runnable {
		private R result;

		@Override
		public void run() {
			result = runWithResult();
		}

		public abstract R runWithResult();
	}

	/**
	 * @param <T> Type of the object to be copied
	 * @param object Object to be copied
	 * @return Deep copy of the object
	 */
	@SuppressWarnings( { "unchecked" })
	public static <T> T getDeepCopy(T object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			// Write the object out to a byte array
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in =
					new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			T copiedData = (T) in.readObject();
			return copiedData;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} catch (ClassNotFoundException notGonnaHappen) {
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ignore) {
			}
		}
		return null;
	}
}
