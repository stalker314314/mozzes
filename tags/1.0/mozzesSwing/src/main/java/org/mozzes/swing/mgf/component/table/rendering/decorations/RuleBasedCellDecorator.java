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
package org.mozzes.swing.mgf.component.table.rendering.decorations;

import java.util.List;

import org.mozzes.swing.mgf.component.table.rendering.decorations.CellDecorationRule.Context;
import org.mozzes.swing.mgf.decoration.Decorator;
import org.mozzes.utils.rulesengine.Rule;
import org.mozzes.utils.rulesengine.RulesEngine;
import org.mozzes.utils.rulesengine.rules.TrueRule;


public abstract class RuleBasedCellDecorator {
	private final RulesEngine<Context, Decorator> rulesEngine =
			new RulesEngine<CellDecorationRule.Context, Decorator>();
	private Rule<Context> globalRule;
	private final Rule<Context> privateGlobalRule;

	public RuleBasedCellDecorator() {
		// privateGlobalRule = new IsSelected().inverse();
		privateGlobalRule = null;
	}

	protected RuleBasedCellDecorator(Rule<Context> privateGlobalRule) {
		this.privateGlobalRule = privateGlobalRule;
	}

	protected void add(Rule<Context> rule, Decorator decorator) {
		rulesEngine.addRule(rule, decorator);
	}

	protected void add(Decorator decorator) {
		rulesEngine.addRule(new TrueRule<Context>(), decorator);
	}

	public List<Decorator> getDecorations(Context context) {
		if (privateGlobalRule != null && !privateGlobalRule.appliesTo(context))
			return null;
		if (globalRule != null && !globalRule.appliesTo(context))
			return null;

		List<Decorator> appliedRules = rulesEngine.getAppliedRules(context);
		if (appliedRules == null || appliedRules.isEmpty())
			return null;
		return appliedRules;
	}

	public void setGlobalRule(Rule<Context> rule) {
		globalRule = rule;
	}

	public Rule<Context> getGlobalRule() {
		return globalRule;
	}
}
