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
package org.mozzes.utils.rulesengine;

import org.mozzes.utils.rulesengine.rules.CompositeRule;
import org.mozzes.utils.rulesengine.rules.InvertedRule;

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
