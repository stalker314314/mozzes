package org.mozzes.swing.mgf.binding;

import static org.junit.Assert.*;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.junit.Test;
import org.mozzes.swing.mgf.DummyClass;
import org.mozzes.swing.mgf.binding.Binder;
import org.mozzes.swing.mgf.datamodel.Field;
import org.mozzes.swing.mgf.datamodel.fields.PropertyField;
import org.mozzes.swing.mgf.datasource.BeanDataSource;
import org.mozzes.swing.mgf.datasource.impl.DefaultBeanDataSource;
import org.mozzes.swing.mgf.translation.translators.IntegerToString;


public class BinderTest {

	@Test
	public void testBindBeanDataSourceOfTFieldOfTQComponent() {
		try {
			// Should throw IllegalArgumentException if ANY one of the parameters is null
			Binder.bind(null, null, (Field<?, ?>) null);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}

		BeanDataSource<DummyClass> source = new DefaultBeanDataSource<DummyClass>(DummyClass.class, (DummyClass) null);
		PropertyField<DummyClass, String> string = new PropertyField<DummyClass, String>(String.class, "strAttr");
		PropertyField<DummyClass, DummyClass> dummy = new PropertyField<DummyClass, DummyClass>(DummyClass.class,
				"dummyClassAttr");

		Binder.bind(new JTextField(), source, string);
		Binder.bind(new TextFieldSubclass(), source, string);
		try {
			// Translator must be provided explicitly since the default one is not specified
			Binder.bind(new JTextField(), source, dummy);
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
		try {
			Binder.bind(new JButton(), source, dummy);
			fail("UnsupportedOperationException should be thrown!");
		} catch (UnsupportedOperationException ok) {
		}
	}

	@Test
	public void testBindBeanDataSourceOfTFieldOfTQComponentTranslatorOfQQ() {
		BeanDataSource<DummyClass> source = new DefaultBeanDataSource<DummyClass>(DummyClass.class, (DummyClass) null);
		PropertyField<DummyClass, String> string = new PropertyField<DummyClass, String>(String.class, "strAttr");
		try {
			// Invalid translator
			Binder.bind(new JTextField(), source, string, new IntegerToString());
			fail("IllegalArgumentException should be thrown!");
		} catch (IllegalArgumentException ok) {
		}
	}

	private static class TextFieldSubclass extends JTextField {
		private static final long serialVersionUID = 1L;
	}
}
