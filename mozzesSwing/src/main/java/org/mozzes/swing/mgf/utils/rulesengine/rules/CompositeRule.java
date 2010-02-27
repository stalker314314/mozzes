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
package org.mozzes.swing.mgf.utils.rulesengine.rules;

import org.mozzes.swing.mgf.utils.rulesengine.Rule;

public class CompositeRule<C> extends Rule<C> {
	private Rule<C> wrappedRule;

	public CompositeRule(Rule<C> rule) {
		if (rule == null)
			throw new IllegalArgumentException("Starting rule cannot be null!");
		wrappedRule = rule;
	}

	@Override
	public boolean appliesTo(C context) {
		return wrappedRule.appliesTo(context);
	}

	@Override
	public CompositeRule<C> and(Rule<C> rule) {
		wrappedRule = new AndRule<C>(wrappedRule, rule);
		return this;
	}

	@Override
	public CompositeRule<C> or(Rule<C> rule) {
		wrappedRule = new OrRule<C>(wrappedRule, rule);
		return this;
	}
}

abstract class BinaryOperationRule<C> extends Rule<C> {
	private final Rule<C> operand1;
	private final Rule<C> operand2;

	public BinaryOperationRule(Rule<C> operand1, Rule<C> operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
	}

	public Rule<C> getOperand1() {
		return operand1;
	}

	public Rule<C> getOperand2() {
		return operand2;
	}
}

class AndRule<C> extends BinaryOperationRule<C> {

	public AndRule(Rule<C> operand1, Rule<C> operand2) {
		super(operand1, operand2);
	}

	@Override
	public boolean appliesTo(C context) {
		return getOperand1().appliesTo(context) && getOperand2().appliesTo(context);
	}
}

class OrRule<C> extends BinaryOperationRule<C> {

	public OrRule(Rule<C> operand1, Rule<C> operand2) {
		super(operand1, operand2);
	}

	@Override
	public boolean appliesTo(C context) {
		return getOperand1().appliesTo(context) || getOperand2().appliesTo(context);
	}
}