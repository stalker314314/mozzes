package org.mozzes.utils.rulesengine.rules;

import org.mozzes.utils.rulesengine.Rule;

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