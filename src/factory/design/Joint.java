package factory.design;

/**
 * Allows {@link Edge Edges} to be joined at arbitrary points other than
 * {@link Node Nodes}.
 *
 * @author John Mwai
 */
    public interface Joint {
/**
 * A marker for a terminating <code>Joint</code>
 */
    public final int JOINT_TERMINATING = 1;
    /**
     * A marker for an originating <code>Joint</code>.
     */
    public final int JOINT_ORIGINATING = 2;
}
