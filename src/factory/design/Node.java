package factory.design;

/**
 * Represents a node in the {@link ControlWeb}. A Node can perform a
 * computation. It also can provide an origin or a termination for an
 * {@link Edge}. A Node that originates an Edge owns it. A node authorizes
 * connections to it from other nodes. It makes the determination whether a
 * request to link to it should be granted or denied. A node should notify all
 * registered {@link NodeListener NodeListers} whenever there is a change in the
 * Node or in the
 * <code>Edges</code> that belong to it.
 *
 * @author John Mwai
 */
public interface Node extends Edge.EdgeListener {

    /**
     * Adds a {@link NodeListener NodeLister} to be notified when there is a
     * change in this Node.
     *
     * @param nodeListener The NodeListener to add to this Node's list of
     * NodeListeners.
     */
    public void addNodeListener(NodeListener nodeListener);

    /**
     * Notify all the interested listeners that the node has changed.
     */
    public void notifyNodeListeners();

    /**
     * Accepts an edge trying to terminate on it. If the request is denied a
     * {@link FuscardControlWebException } is thrown encapsulating useful
     * information about the cause for the denial.
     *
     * @param edge The edge to terminate.
     * @throws FuscardControlWebException If the node does not allow the edge to
     * terminate here.
     */
    public void terminate(Edge edge) throws FuscardControlWebException;

    /**
     * Tests whether this Node will terminate this Edge. This method should
     * throw {@link FuscardControlWebException } if and only if {@link #terminate(factory.design.Edge)
     * } would throw the exception. This exception should encapsulate
     * informative information on cause why the request was denied.
     *
     * @param edge The edge to test on this Node.
     * @throws FuscardControlWebException If the node would not allow the edge
     * to terminate here.
     */
    public void testTerminate(Edge edge) throws FuscardControlWebException;

    /**
     * Accepts a node trying to originate from it. If the request is denied a
     * {@link FuscardControlWebException } is thrown encapsulating useful
     * information about the cause for the denial.
     *
     * @param edge The edge to originate.
     * @throws FuscardControlWebException If the request to accept the
     * origination is denied.
     */
    public void originate(Edge edge) throws FuscardControlWebException;

    /**
     * Tests whether this Node will originate this Node. This method should
     * throw {@link FuscardControlWebException } if and only if {@link #originate(factory.design.Edge)
     * } would throw the exception.
     *
     * @param edge The edge which to test on this Node.
     * @throws FuscardControlWebException If the request to accept the
     * origination would be denied.
     * @see #testTerminate(factory.design.Edge)
     */
    public void testOriginate(Edge edge) throws FuscardControlWebException;

    /**
     * Removes the
     * <code>Edge</code> from this
     * <code>Node</code>. This method should not throw any exceptions and it
     * should just inform the Node that the
     * <code>Edge</code> must be removed. This allows the node to manage the
     * removal of the
     * <code>Edge</code>.
     *
     * @param edge The edge to remove.
     */
    public void remove(Edge edge);

    /**
     * Objects that implement this interface can register themselves to get
     * notified when there is a change in the node.
     */
    public static interface NodeListener {

        /**
         * The method that all
         * <code>NodeListener</code>s should implement in order to get notified
         * when there is a change in the
         * <code>Node</code>.
         *
         * @param node The source <code>Node</code>.
         */
        public void nodeChanged(Node node);
    }
}
