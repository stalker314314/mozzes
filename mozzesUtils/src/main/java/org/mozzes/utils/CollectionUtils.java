package org.mozzes.utils;

import java.lang.reflect.Array;
import java.util.Collection;

import org.mozzes.utils.reflection.ReflectionException;


/**
 * Static helper methods for use with Collections
 * 
 * @author milos
 */
public class CollectionUtils {

	/**
	 * Returns the first object from a List or null if List is empty
	 */
	public static <T> T getFirstOrNull(Collection<T> collection) {
		if (collection == null || collection.isEmpty())
			return null;
		return collection.iterator().next();
	}

	/**
	 * Converts the collection to an array
	 * 
	 * @param <T> Class of the elements in the collection
	 * @param clazz Class of the elements in the collection
	 * @param collection Collection of elements
	 * @return Array of type T containing all elemenents of the collection
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Class<T> clazz, Collection<T> collection) {
		T[] array = (T[]) Array.newInstance(clazz, collection.size());
		return collection.toArray(array);
	}

	/**
	 * Vraca listu u kojoj se nalaze samo elementi iz prve liste, koji se ne nalaze u drugoj listi.
	 * 
	 * @param <T> - tip objekta koji se nalazi u listama
	 * @param c1 - Kolekcija iz koje se izbacuju elementi koji se nalaze u drugoj kolekciji
	 * @param c2 - Kolekcija sa kojom se nalaze elementi, koji ne treba da se nalaze u kolekciji koja je rezultat
	 * @return Kolekcija(Lista) elemenata iz prve kolekcije, koji se ne nalaze u drugoj kolekciji
	 * @author Milorad
	 */
	public static <T> Collection<T> subtract(Collection<T> c1, Collection<T> c2) {
		if (c1 == null)
			throw new NullPointerException();

		Collection<T> result = CollectionUtils.copy(c1);
		if (c2 != null) {
			result.removeAll(c2);
		}

		return result;
	}

	/**
	 * @param collection Kolekcija koju treba kopirati
	 * @return Kopiju kolekcije
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Collection<U>, U> T copy(T collection) throws ReflectionException {
		if (collection == null)
			throw new NullPointerException();
		try {
			return (T) collection.getClass().getConstructor(Collection.class).newInstance(collection);
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}
}
