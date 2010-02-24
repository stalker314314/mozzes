package org.mozzes.swing.mgf.utils.rulesengine.rules;

import org.mozzes.swing.mgf.utils.rulesengine.Rule;

public class IsNull<Context> extends Rule<Context> {
	private final Object object;

	public IsNull(Object object) {
		this.object = object;
	}

	@Override
	public boolean appliesTo(Context context) {
		return object == null;
	}
}