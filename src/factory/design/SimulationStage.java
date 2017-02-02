package factory.design;

/**
 * An interface for a stage in a simulation sequence that is capable of
 * performing a simulation routine. The simulation stage runs and then gives up
 * control to another simulation state.
 * <code>SimulationStage</code> extends {@link Simulation Simulation} so that it
 * can provide the basic simulation controls to its clients. A
 * <code>SimulationStage</code> can handle the simulation itself or it can
 * delegate the simulation to a
 * <code>Simulation</code>.
 *
 * @author John Mwai
 */
public interface SimulationStage extends Simulation {

    /**
     * This method signals this
     * <code>SimulationStage</code> that it has control so it can perform its
     * simulation routine. This method is called in a by other
     * <code>SimulationStates</code> upstream in the simulation sequence to
     * signal this
     * <code>SimulationStage</code> that it may start to execute.
     *
     * @param giver The SimulationStage that passed control to this
     * SimulationStage.
     */
    public void takeControl(SimulationState giver);

    /**
     * This method is used by the
     * <code>SimulationStage</code> to prepare it to give away control. A
     * <code>SimulationStage</code> should call this method before calling
     * {@link #takeControl(factory.design.Simulation.SimulationStage)} on
     * another
     * <code>SimulationStage</code>.
     *
     * @param receiver The beneficiary of the control.
     */
    public void giveControl(SimulationState receiver);

    /**
     * An interface for things that are interested in being notified when this
     * <code>SimulationStage</code> gives away control to another
     * <code>SimulationStage</code>.
     */
    public static interface ControlListener {

        /**
         * This method will be called by a SimulationState when it is about to
         * give control to another SimulationStage after calling {@link SimulationStage#giveControl(factory.design.Simulation.SimulationState)
         * } on itself. After all calls to all registered ControlListeners have
         * returned it calls {@link SimulationStage#takeControl(factory.design.Simulation.SimulationState)
         * } on the receiver.
         *
         * @param source The SimulationState that is giving control.
         * @param receiver the simulation state that is receiving control.
         */
        public void givingContol(SimulationState source,
                SimulationState receiver);
    }
}
