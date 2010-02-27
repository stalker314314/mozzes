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
package org.mozzes.swing.mgf.translators;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozzes.swing.mgf.translation.AbstractTranslator;
import org.mozzes.swing.mgf.translation.Translator;
import org.mozzes.swing.mgf.translation.TranslatorConfiguration;
import org.mozzes.swing.mgf.translation.TranslatorRepository;
import org.mozzes.swing.mgf.translation.translators.IntegerToString;
import org.mozzes.swing.mgf.translation.translators.NeutralTranslator;


public class TranslatorRepositoryTest {

	@Test
	public void testGetDefault() {
		assertEquals(new IntegerToString().inverse(), TranslatorRepository.getDefault(String.class, Integer.class));
		assertEquals(new IntegerToString(), TranslatorRepository.getDefault(Integer.class, String.class));

		assertEquals(null, TranslatorRepository.getDefault(String.class, TranslatorNotInRepo.class));
		assertEquals(null, TranslatorRepository.getDefault(TranslatorNotInRepo.class, TranslatorNotInRepo.class));

		try {
			TranslatorRepository.getDefault(null, null);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
		try {
			TranslatorRepository.getDefault(String.class, null);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
		try {
			TranslatorRepository.getDefault(null, String.class);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
	}

	@Test
	public void testGetNeutral() {
		assertEquals(new NeutralTranslator<String>(String.class), TranslatorRepository.getNeutral(String.class));
		try {
			TranslatorRepository.getNeutral(null);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
	}

	@Test
	public void testConfiguration() {
		assertEquals(null, TranslatorRepository.getDefault(String.class, TranslatorNotInRepo.class));
		assertEquals(null, TranslatorRepository.getDefault(TranslatorNotInRepo.class, String.class));

		Translator<TranslatorNotInRepo, String> translator = new TranslatorNotInRepoToString();

		TranslatorConfiguration.addTranslator(translator);
		assertEquals(translator.inverse(), TranslatorRepository.getDefault(String.class, TranslatorNotInRepo.class));
		assertSame(translator, TranslatorRepository.getDefault(TranslatorNotInRepo.class, String.class));

		translator = new TranslatorNotInRepoToString();
		assertEquals(translator.inverse(), TranslatorRepository.getDefault(String.class, TranslatorNotInRepo.class));
		assertNotSame(translator, TranslatorRepository.getDefault(TranslatorNotInRepo.class, String.class));
		try {
			TranslatorConfiguration.addTranslator(translator);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
		TranslatorConfiguration.overrideTranslator(translator);
		assertEquals(translator.inverse(), TranslatorRepository.getDefault(String.class, TranslatorNotInRepo.class));
		assertSame(translator, TranslatorRepository.getDefault(TranslatorNotInRepo.class, String.class));
	}

	private class TranslatorNotInRepo {
	}

	private class TranslatorNotInRepoToString extends AbstractTranslator<TranslatorNotInRepo, String> {
		@Override
		public Class<TranslatorNotInRepo> getFromClass() {
			return TranslatorNotInRepo.class;
}

		@Override
		public Class<String> getToClass() {
			return String.class;
		}

		@Override
		public TranslatorNotInRepo translateFrom(String object) {
			return null;
		}

		@Override
		public String translateTo(TranslatorNotInRepo object) {
			return null;
		}
	};
}
