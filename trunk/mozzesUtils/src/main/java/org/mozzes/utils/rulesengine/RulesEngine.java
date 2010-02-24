package org.mozzes.utils.rulesengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesEngine<C, D> {
	private final Map<Rule<C>, D> rules = new HashMap<Rule<C>, D>();

	public void addRule(Rule<C> rule, D object) {
		rules.put(rule, object);
	}

	public void removeRule(Rule<C> rule) {
		rules.remove(rule);
	}

	public List<D> getAppliedRules(C context) {
		ArrayList<D> applies = new ArrayList<D>();
		for (Rule<C> rule : rules.keySet()) {
			if (rule.appliesTo(context))
				applies.add(rules.get(rule));
		}
		return applies;
	}
}
