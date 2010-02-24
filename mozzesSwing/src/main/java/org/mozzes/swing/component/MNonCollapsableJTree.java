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