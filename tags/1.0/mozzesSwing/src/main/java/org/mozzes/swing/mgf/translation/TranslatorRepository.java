/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.swing.mgf.translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mozzes.swing.mgf.translation.translators.InverseTranslator;
import org.mozzes.swing.mgf.translation.translators.NeutralTranslator;
import org.mozzes.utils.ClassBasedObjectMappingRepository;


/**
 * Repository of default and neutral translators
 * 
 * @author milos
 */
public class TranslatorRepository {
	private static final Map<Class<?>, NeutralTranslator<?>> neutrals = new HashMap<Class<?>, NeutralTranslator<?>>();
	private static List<Translator<?, ?>> translators;
	private static List<Translator<?, ?>> inverses;
	private static List<Translator<?, ?>> temporaryList = new ArrayList<Translator<?, ?>>();

	// initialization
	private TranslatorRepository() {
	}

	private static void initializeDefaultTranslators() {
		if (translators != null)
			return;
		translators = new LinkedList<Translator<?, ?>>(DefaultTranslators.getDefaultTranslators());
		initializeInverses();
	}

	private static void initializeInverses() {
		if (inverses != null)
			throw new IllegalStateException("Cannot call initialize inverses more than once!");
		// List<Translator<?, ?>> inverses = new ArrayList<Translator<?, ?>>();
		inverses = new LinkedList<Translator<?, ?>>();
		for (Translator<?, ?> translator : translators) {
			addInverse(translator);
		}
		translators.addAll(inverses);
	}

	@SuppressWarnings("unchecked")
	private static void addInverse(Translator<?, ?> translator) {
		inverses.add(new InverseTranslator<Object, Object>((Translator<Object, Object>) translator));
	}

	// interface
	/**
	 * Returns the default translator for passed parameters or null if there is no such {@link Translator} in repository
	 * 
	 * @param from Class of object from which to translate
	 * @param to Class of object to which to translate
	 */
	@SuppressWarnings("unchecked")
	public static <From, To> Translator<From, To> getDefault(Class<From> from, Class<To> to) {
		initializeDefaultTranslators();

		if (from == null || to == null)
			throw new IllegalArgumentException("From and to parameters cannot be null!");

		ClassBasedObjectMappingRepository<Translator<?, To>> cbor =
				new ClassBasedObjectMappingRepository<Translator<?, To>>(null);

		Translator<From, To> result = null;
		for (Translator<?, ?> translator : getAllTranslators()) {
			if (!translator.getToClass().equals(to))
				continue;
			cbor.setObjects(translator.getFromClass(), (Translator<?, To>) translator);
		}

		result = (Translator<From, To>) cbor.getObject(from);
		return result;
	}

	/**
	 * Cached instance of neutral {@link Translator} for the specified class<br>
	 * Note: Neutral translator is a {@link Translator} for which the from class and to class are the same, so it is
	 * generated and cached by the framework since it has no state and simply returns the passed object as the result of
	 * translation
	 * 
	 * @param clazz Class of object for which the neutral translator is needed
	 */
	@SuppressWarnings("unchecked")
	public static <T> NeutralTranslator<T> getNeutral(Class<T> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("Clazz parameter cannot be null!");
		if (!neutrals.containsKey(clazz)) {
			neutrals.put(clazz, new NeutralTranslator<T>(clazz));
		}
		return (NeutralTranslator<T>) neutrals.get(clazz);
	}

	static <From, To> void addTranslator(Translator<From, To> translator, boolean override) {
		if (translator == null)
			throw new IllegalArgumentException("Tranlator must not be null!");
		initializeDefaultTranslators();

		int i = 0;
		for (Translator<?, ?> t : getAllTranslators()) {
			i = (i + 1) % translators.size();
			if (t.getFromClass().equals(translator.getFromClass()) &&
					t.getToClass().equals(translator.getToClass())) {
				if (!override) {
					throw new IllegalArgumentException(String.format(
							"Translator from %s to %s is allready provided, " +
							"please specify \"override\" if you want to override it!",
							translator.getFromClass().getSimpleName(),
							translator.getToClass().getSimpleName()));
				} else {
					translators.remove(i);
					inverses.remove(i);
}
			}
		}
		translators.add(translator);
		addInverse(translator);
	}

	private static List<Translator<?, ?>> getAllTranslators() {
		temporaryList.clear();
		temporaryList.addAll(translators);
		temporaryList.addAll(inverses);
		return temporaryList;
	}
}
