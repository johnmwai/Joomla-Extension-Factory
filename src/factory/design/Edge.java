package factory.design;

/**
 * Transfers control from one Node to another. An
 * <code>Edge</code> should notify all {@link EdgeListener EdgeListeners} when
 * it changes.
 *
 * @author John Mwai
 */
public interface Edge {

    /**
     * Accepts a {@link Joint Joint} being added on this
     * <code>Edge</code>. A
     * <code>Joint</code> allows edges to fork.
     *
     * @param joint The <code>Joint</code> to add to this <code>Edge</code>.
     * @param after The <code>Joint</code> after which to insert
     * this <code>Joint</code> or null if adding to the beginning of
     * the <code>Edge</code>.
     * @throws FuscardControlWebException If the request to add
     * the <code>Joint</code> is denied encapsulating important information on
     * cause.
     */
    public void join(Joint joint, Joint after) throws FuscardControlWebException;
    
   /**
     * Tests whether a {@link Joint Joint} being added on this
     * <code>Edge</code> would be accepted.
     *
     * @param joint The <code>Joint</code> to test on this <code>Edge</code>.
     * @param after The <code>Joint</code> after which to test insert 
     * this <code>Joint</code> or null if testing at the beginning of 
     * the <code>Edge</code>.
     * @throws FuscardControlWebException If the request to add 
     * the <code>Joint</code> would be denied encapsulating important information on
     * cause.
     */
    public void testJoin(Joint joint, Joint after) throws FuscardControlWebException;

    /**
     * An interface of things interested to know when an
     * <code>Edge</code> changes.
     */
    public static interface EdgeListener {

        /**
         * The method that anFF
         * <code>EdgeListener</code> must implement. Place the code to handle
         * the
         * <code>Edge</code> changes in this method.
         *
         * @param ece The <code>Edge</code> that has changed.
         */
        public void edgeChanged(EdgeChangeEvent ece);
    }

    /**
     * This is the event that gets fired whenever there is a change in an
     * <code>Edge</code>.
     */
    public static class EdgeChangeEvent {

        /**
         * The
         * <code>Edge</code> that fired this event.
         */
        private Edge source;

        /**
         * Creates an
         * <code>EdgeChangeEvent</code>.
         *
         * @param source The source of this event.
         */
        public EdgeChangeEvent(Edge source) {
            this.source = source;
        }

        /**
         * Get the origin of this event.
         *
         * @return The <code>Edge</code> originating this event.
         */
        public Edge getSource() {
            return source;
        }

        /**
         * Set the origin of this event.
         *
         * @param source The <code>Edge</code> originating this event.
         */
        public void setSource(Edge source) {
            this.source = source;
        }
    }

    /**
     * Event fired when a {@link Joint joint} is added to this
     * <code>Edge</code>.
     */
    public static class EdgeJoinnedEvent extends EdgeChangeEvent {

        private Joint joint;//Addeded joint

        /**
         * Creates an instance of an
         * <code>EdgeJoinnedEvent</code>.
         *
         * @param joint The joint that has been added.
         * @param source The source Edge originating this event, and to which
         * joint has been added.
         */
        public EdgeJoinnedEvent(Joint joint, Edge source) {
            super(source);
            this.joint = joint;
        }

        /**
         * Get the joint that has been added.
         *
         * @return The added joint.
         */
        public Joint getJoint() {
            return joint;
        }

        /**
         * Set the added joint.
         *
         * @param joint The added joint.
         */
        public void setJoint(Joint joint) {
            this.joint = joint;
        }
    }

    /**
     * Fired when a child
     * <code>Edge</code> is removed from this
     * <code>Edge</code>.
     */
    public static class EdgeRemovedEvent extends EdgeChangeEvent {

        private Edge removedEdge;// The removed <code>Edge</code>.

        /**
         * Creates an
         * <code>EdgeRemovedEvent</code>.
         *
         * @param removedEdge The <code>Edge</code> that has been removed.
         * @param source The <code>Edge</code> originating this event.
         */
        public EdgeRemovedEvent(Edge removedEdge, Edge source) {
            super(source);
            this.removedEdge = removedEdge;
        }

        /**
         * Set the
         * <code>Edge</code> that has been removed from this
         * <code>Edge</code>.
         *
         * @param removedEdge The removed <code>Edge</code>.
         */
        public void setRemovedEdge(Edge removedEdge) {
            this.removedEdge = removedEdge;
        }

        /**
         * Gets the
         * <code>Edge</code> that has been removed from this
         * <code>Edge</code>.
         *
         * @return The removed <code>Edge</code>.
         */
        public Edge getRemovedEdge() {
            return removedEdge;
        }
    }
}
