package factory.design;

/**
 * A graph containing {@link Node Nodes} and {@link Edge Edges} that represent
 * the detailed logic flow of the application. A
 * <code>ControlWeb</code> enforces constraints of soundness of the logic of the
 * application. A
 * <code>ControlWeb</code> keeps a set of constraints that the graph must adhere
 * to. It allows Nodes to be linked indirectly through Edges.
 *
 * @author John Mwai
 */
public interface ControlWeb {

    /**
     * Adds a node to this
     * <code>ControlWeb</code>. This method should be overridden to check
     * whether a node can be added. If it can be added it is added to the child
     * nodes of this control web. If the node cannot be added it throws
     * {@link FuscardControlWebException }. The intent of this method is to
     * allow meaningful information to be encapsulated in the
     * FuscardControlWebException that is thrown if the Node cannot be accepted.
     *
     * @param node A {@link Node } to add.
     * @throws FuscardControlWebException If for any reason the add could not be
     * completed.
     */
    public void addNode(Node node) throws FuscardControlWebException;

    /**
     * Determines whether a Node can be added. If the node cannot be added it
     * throws {@link FuscardControlWebException }. This method should throw {@link FuscardControlWebException
     * } if and only if {@link #addNode(factory.design.Node) } would throw the
     * exception.
     *
     * @param node A node to accept.
     * @throws FuscardControlWebException If the node cannot be added.
     */
    public void acceptNode(Node node) throws FuscardControlWebException;
}
