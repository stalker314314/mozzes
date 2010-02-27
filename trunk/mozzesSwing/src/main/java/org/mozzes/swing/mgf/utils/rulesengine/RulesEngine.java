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
package org.mozzes.swing.mgf.utils.rulesengine;

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
