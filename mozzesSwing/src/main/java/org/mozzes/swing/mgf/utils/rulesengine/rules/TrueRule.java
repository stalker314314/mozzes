package org.mozzes.swing.mgf.utils.rulesengine.rules;

import org.mozzes.swing.mgf.utils.rulesengine.Rule;

public class TrueRule<C> extends Rule<C> {

	public TrueRule() {
	}

	@Override
	public boolean appliesTo(C context) {
		return true;
	}

}
