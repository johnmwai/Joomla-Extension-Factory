package factory.design;

/**
 * Interface for Nodes that want to advertise a simulation interface. These
 * Nodes are intended to be added to a {@link AbstractSimulatingControlWeb}
 * therefore they implement SimulationStage as opposed to Simulation in order to
 * be part of Simulation of the AbstractSimulatingControlWeb Simulation. The
 * AbstractSimulatingControlWeb manages the Simulating Nodes that are added to
 * it. A simulating node can be LIVE or DORMANT. A live SimulatingNode interacts
 * with the user and does computation. A dormant SimulatingNode on the other
 * hand allows modifications like adding nodes and edges to the section of the
 * controlWeb that it controls. A dormant SimulatingNode does not generate
 * events and is therefore ideal state that the nodes should be in during
 * editing of the control web. When simulating, the node goes live to receive
 * user input.
 *
 * @author John Mwai
 */
public interface SimulatingNode extends SimulationStage, Node {

    /**
     * Takes this SimulatingNode live so that it can start interacting with the
     * user and processing data.
     */
    public void goLive();

    /**
     * Put the node in Dormant state
     */
    public void takeOffline();

    /**
     * @return The SimulatingNodeState
     */
    public SimulatingNodeState getSimulatingNodeState();

    /**
     * An enumeration for the states of a SimulatingNode.
     */
    public static enum SimulatingNodeState {

        /**
         * The state of a SimulatingNode during animation and when accepting
         * user input.
         */
        LIVE,
        /**
         * The state of a SimulatingNode during design. Blocks the node from
         * generating events that may impede the proper working of the design
         * tools
         */
        DORMANT
    }
}
