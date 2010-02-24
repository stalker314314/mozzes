package org.mozzes.swing.mgf.utils.rulesengine;

import org.mozzes.swing.mgf.utils.rulesengine.rules.CompositeRule;
import org.mozzes.swing.mgf.utils.rulesengine.rules.InvertedRule;

public abstract class Rule<Context> {
	public abstract boolean appliesTo(Context context);

	public Rule<Context> inverse() {
		return new InvertedRule<Context>(this);
	}

	public CompositeRule<Context> getComposite() {
		return new CompositeRule<Context>(this);
	}

	public CompositeRule<Context> and(Rule<Context> rule) {
		return getComposite().and(rule);
	}

	public CompositeRule<Context> or(Rule<Context> rule) {
		return getComposite().or(rule);
	}
}
