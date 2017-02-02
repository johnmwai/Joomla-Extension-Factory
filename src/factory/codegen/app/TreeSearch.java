package factory.codegen.app;

import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * Class to search the tree using breadth first approach.
 *
 * @author John Mwai
 */
public abstract class TreeSearch {

    private JTree tree;

    public TreeSearch(JTree tree) {
        this.tree = tree;
    }

    /**
     * Initiate the search.
     *
     * @return The first element that meets the criteria supplied by the accept
     * method or null if no elements qualify.
     */
    public DefaultMutableTreeNode search() {
        TreeModel tm = tree.getModel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tm.getRoot();
        Enumeration enumeration = node.breadthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) enumeration.nextElement();
            if (accept(n)) {
                return n;
            }
        }
        return null;
    }

    /**
     * The method that the user must supply in order to evaluate each node.
     *
     * @param node The node to evaluate
     * @return If the node is acceptable.
     */
    public abstract boolean accept(DefaultMutableTreeNode node);
}
