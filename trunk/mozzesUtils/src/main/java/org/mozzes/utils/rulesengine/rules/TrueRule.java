package org.mozzes.utils.rulesengine.rules;

import org.mozzes.utils.rulesengine.Rule;

public class TrueRule<C> extends Rule<C> {

	public TrueRule() {
	}

	@Override
	public boolean appliesTo(C context) {
		return true;
	}

}
