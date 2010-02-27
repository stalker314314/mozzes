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
package org.mozzes.swing.component;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * {@link JTree} whose nodes can't be collapsed.
 * 
 * @author Kokan
 */
public class MNonCollapsableJTree extends JTree {
	private static final long serialVersionUID = -4978601597442251002L;

	public MNonCollapsableJTree() {
		super();
	}

	public MNonCollapsableJTree(Hashtable<?, ?> value) {
		super(value);
	}

	public MNonCollapsableJTree(Object[] value) {
		super(value);
	}

	public MNonCollapsableJTree(TreeModel treeModel) {
		super(treeModel);
	}

	public MNonCollapsableJTree(TreeNode treeNode) {
		super(treeNode);
	}

	public MNonCollapsableJTree(Vector<?> value) {
		super(value);
	}

	public MNonCollapsableJTree(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}

	@Override
	protected void setExpandedState(TreePath path, boolean state) {
		if (state) {
			super.setExpandedState(path, state);
		}
	}
}