package factory.design;

import java.util.LinkedList;

/**
 * This is a
 * <code>ControlWeb</code> which supports simulation. During simulation the
 * ControlWeb should not accept any modifications. If any modifications are
 * detected it should abort simulation immediately. An
 * AbstractSimulatingControlWeb acts as the manager for the state of the state
 * of all the SimulatingNodes added in it. The state of such SimulatingNodes is
 * transparent to the AbstractSimulatingControlWeb and is merged to its own
 * state. If the AbstractSimulatingControlWeb is paused, it pauses all the
 * SimulatingNodes in it.
 *
 * @see SimulatingNode
 * @see Simulation
 *
 * @see Simulation#abort()
 * @author John Mwai
 */
public abstract class AbstractSimulatingControlWeb implements ControlWeb, Simulation, Node.NodeListener {

    /**
     * nodes that constitute this ControlWeb.
     */
    private LinkedList<Node> nodes = new LinkedList<>();
    /**
     * The current state that this Simulation is at. The state determines how
     * Simulation control methods behave.
     */
    private SimulationState simulationState;
    /**
     * The SimulatingNode that starts the simulation sequence of this
     * simulation.
     */
    private SimulatingNode initialStage;

    /**
     * Creates an instance of an AbstractSimulatingControlWeb.
     */
    public AbstractSimulatingControlWeb() {
        simulationState = SimulationState.READY;
    }

    /**
     * Sets the initial SimulationStage where Simulation starts when {@link #start()
     * } is called on this Simulation. This SimulationStage will be responsible
     * for passing control to the other SimulationStages.
     *
     * @param initialStage The SimulatingNode that starts simulation.
     * @throws FuscardSimulationException if the simulation Stage is not
     * registered as a Node in this ControlWeb.
     */
    public void setInitialStage(SimulatingNode initialStage) throws FuscardSimulationException {
        if (nodes.contains(initialStage)) {
            this.initialStage = initialStage;
        }
        throw new FuscardSimulationException(
                "SimulationStage not added to ControlWeb");
    }

    /**
     * This method is called by nodes when they are mutated in order to tell
     * this AbstractSimulatingControlWeb to abort simulation. This ensures that
     * simulation only runs in a transparent environment. Mutating the CotrolWeb
     * when simulation is in progress may lead to undefined results.
     *
     * @param node The node that has changed.
     */
    @Override
    public void nodeChanged(Node node) {
        abort();
    }

    /**
     * This method checks whether it is legal to start the simulation sequence
     * then initiates simulation on the initial state. A Simulation should only
     * be started if it is in the READY stage.
     *
     * @throws FuscardSimulationException If the simulation is not in the {@link SimulationState#READY
     * } or the initial SimulationStage is not set.
     */
    @Override
    public void start() throws FuscardSimulationException {
        if (getState() != SimulationState.READY) {
            throw new FuscardSimulationException(
                    "Simulation not in READY state.");
        }
        if (initialStage == null) {
            throw new FuscardSimulationException(
                    "The initial stage is not set.");
        }
        simulationState = SimulationState.STARTED;
        initialStage.start();
    }

    @Override
    public void pause() throws FuscardSimulationException {
        if (getState() != SimulationState.RUNNING) {
            throw new FuscardSimulationException(
                    "Simulation not in RUNNING state.");
        }
        for (Node node : nodes) {
            if (node instanceof SimulatingNode) {
                SimulatingNode sn = (SimulatingNode) node;
                if (sn.getState() == SimulationState.RUNNING) {
                    sn.pause();
                }
            }
        }
        simulationState = SimulationState.PAUSED;
    }

    /**
     * A started Simulation can be in any state apart from DESTROYED and READY
     *
     * @return Whether this Simulation is started.
     */
    private boolean isStarted() {
        SimulationState state = getState();
        return state != SimulationState.DESTROYED
                && state != SimulationState.READY;
    }

    /**
     * If a simulation is in the STARTED or RUNNING states then it is running in
     * that it is executing its simulation routines. RUNNING is a sub-state of
     * STARTED.
     *
     * @return Whether this Simulation is running.
     */
    private boolean isRunning() {
        SimulationState state = getState();
        return state == SimulationState.STARTED
                || state == SimulationState.RUNNING;
    }

    /**
     * This method adheres to the contract of the {@link Simulation#run() }
     * except calling it when the simulation is finished it is restarted.
     *
     * @throws FuscardSimulationException if the simulation is not started or if
     * there is no Initial state.
     */
    @Override
    public void run() throws FuscardSimulationException {
        if (!isStarted()) {
            throw new FuscardSimulationException("Simulation not started.");
        }
        if (isRunning()) {
            throw new FuscardSimulationException("Simulation running.");
        }
        if (getState() == SimulationState.PAUSED) {
            for (Node node : nodes) {
                if (node instanceof SimulatingNode) {
                    SimulatingNode sn = (SimulatingNode) node;
                    if (sn.getState() == SimulationState.PAUSED) {
                        sn.run();
                    }
                }
            }
        }
        if (getState() == SimulationState.FINISHED) {
            simulationState = SimulationState.READY;
            start();
        }
        simulationState = SimulationState.RUNNING;
    }

    @Override
    public void stop() {
        if (getState() == SimulationState.RUNNING) {
            try {
                pause();
            } catch (FuscardSimulationException ex) {
                abort();
            }
            return;
        }
        if (getState() == SimulationState.PAUSED) {
            abort();
        }

    }

    @Override
    public void abort() {
        for (Node node : nodes) {
            if (node instanceof SimulatingNode) {
                SimulatingNode sn = (SimulatingNode) node;
                sn.abort();
            }
        }
        simulationState = SimulationState.DESTROYED;
    }

    @Override
    public SimulationState getState() {
        return simulationState;
    }

    /**
     * Adds a node to this
     * <code>AbstractSimulatingControlWeb's</code> list of Nodes.
     *
     * @param node The node to add.
     * @throws FuscardControlWebException If the node is already in the list.
     */
    @Override
    public void addNode(Node node) throws FuscardControlWebException {
        acceptNode(node);
        nodes.add(node);
        node.addNodeListener(this);
    }

    /**
     * Checks whether a node could be added to this
     * <code>AbstractSimulatingControlWeb's</code> list of Nodes. This method is
     * used by the {@link #addNode(factory.design.Node) } to determine if a node
     * should be added. This method checks whether this
     * <code>AbstractSimulatingControlWeb</code> already has this Node.
     *
     * @param node The node to test.
     * @throws FuscardControlWebException If the Node is already in the list of
     * Nodes.
     */
    @Override
    public void acceptNode(Node node) throws FuscardControlWebException {
        if (nodes.contains(node)) {
            throw new FuscardControlWebException(
                    "Node already registered.");
        }
    }
    /**
     * Get a copy of the nodes list
     * @return Copy of nodes
     */
    public LinkedList<Node> copyNodes(){
        return new LinkedList<>(nodes);
    }
}
