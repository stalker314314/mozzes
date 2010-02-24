package org.mozzes.swing.mgf.decoration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

public class CompositeDecorator extends Decorator{
	private final LinkedList<Decorator> decorators =
			new LinkedList<Decorator>();

	public CompositeDecorator() {
	}

	@Override
	protected void decorateComponent(JComponent component) {
		for (Decorator decorator : decorators) {
			decorator.decorateComponent(component);
		}
	}

	@Override
	protected StateRestoreFunction getStateRestoreFunction(final JComponent component) {
		return new StateRestoreFunction() {
			private final LinkedList<StateRestoreFunction> restoreFunctions =
					new LinkedList<StateRestoreFunction>();
			{
				for (int i = decorators.size() - 1; i >= 0; i--) {
					restoreFunctions.add(decorators.get(i).getStateRestoreFunction(component));
	}
		}

		@Override
			public void restoreState(JComponent component) {
				for (StateRestoreFunction restore : restoreFunctions) {
					restore.restoreState(component);
			}
		}
		};
	}

	public CompositeDecorator add(Decorator decorator) {
		this.decorators.add(decorator);
		DecorationManager.getInstance().refreshDecoration(this);
			return this;
	}

	public void addAll(List<Decorator> decorators) {
		for (Decorator decorator : decorators) {
			add(decorator);
		}
	}

	public CompositeDecorator remove(Decorator decorator) {
		this.decorators.remove(decorator);
		DecorationManager.getInstance().refreshDecoration(this);
		return this;
	}

	public boolean contains(Decorator decorator) {
		ArrayList<CompositeDecorator> composites =
				new ArrayList<CompositeDecorator>();
		for (Decorator d : decorators) {
			if (d instanceof CompositeDecorator) {
				composites.add((CompositeDecorator) d);
				continue;
	}
			if (d.equals(decorator))
				return true;
	}
		for (CompositeDecorator composite : composites) {
			boolean contains = composite.contains(decorator);
			if (contains)
			return true;
		}
			return false;
	}
}
