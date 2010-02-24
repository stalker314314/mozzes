package org.mozzes.utils.rulesengine.rules;

import org.mozzes.utils.rulesengine.Rule;

public class InvertedRule<C> extends Rule<C> {
	private final Rule<C> rule;

	public InvertedRule(Rule<C> rule) {
		this.rule = rule;
	}

	@Override
	public boolean appliesTo(C context) {
		return !rule.appliesTo(context);
	}

}
