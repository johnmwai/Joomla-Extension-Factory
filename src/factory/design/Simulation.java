package factory.design;

/**
 * This interface represents an instance of a
 * <code>Simulation</code> that is initiated to test the logical soundness of a
 * design. A
 * <code>Simulation</code> object maintains the state of a simulation. It
 * provides the controls to start, pause and stop a simulation sequence.
 * <code>Simulations</code> consist of {@link SimulationState SimulationStates}.
 * These are the stages in the simulation sequence that execute a particular
 * routine before passing control to another
 * <code>SimulationState</code> down the line. A
 * <code>Simulation</code> may be interactive in that a
 * <code>SimulationState</code> may rely on input from the user to determine the
 * next simulation routine to run or it may wait for user input.
 * <p/>
 * <strong>The life cycle of a
 * <code>Simulation</code>:</strong>
 * <ol>
 * <li> {@link SimulationState#READY }: The
 * <code>Simulation</code> is prepared and is ready to start. When the
 * <code>Simulation</code> is in this state it only expects to be started or to
 * be aborted.
 * </li>
 * <li> {@link SimulationState#STARTED }: Calling start on a READY
 * <code>Simulation</code> causes it to go to the STARTED state.
 * </li>
 * <li> {@link SimulationState#RUNNING }: When a
 * <code>Simulation</code> is started it goes to the RUNNING state where it
 * starts executing. RUNNING is a sub-state of STARTED.
 * </li>
 * <li> {@link SimulationState#PAUSED }: Calling {@link #pause()} on a RUNNING
 * <code>Simulation</code> causes it to transition to the PAUSED state. A PAUSED
 * <code>Simulation</code> is in the STARTED state but not in the RUNNING state.
 * </li>
 * <li> {@link SimulationState#FINISHED }: After the
 * <code>Simulation</code> has completed all its simulation routines it enters
 * the finished state.
 * </li>
 * <li> {@link SimulationState#DESTROYED }: Calling {@link #abort()} on a READY
 * or STARTED
 * <code>Simulation</code> takes it to the DESTROYED state. Calling abort() on a
 * DESTROYED
 * <code>Simulation</code> has no effect.
 * </li>
 * </ol>
 *
 * @author John Mwai
 */
public interface Simulation {

    /**
     * Start the simulation sequence. This method should be called once and only
     * once in the life of a
     * <code>Simulation</code>. If the
     * <code>Simulation</code> is started this method should throw
     * {@link FuscardSimulationException FuscardSimulationException}. Otherwise
     * the appropriate way to start a paused
     * <code>Simulation</code> is {@link #run() run()}.
     *
     * @throws FuscardSimulationException If the <code>Simulation</code> is {@link SimulationState#DESTROYED
     * } and hence or otherwise cannot start or the <code>Simulation</code> is
     * already started.
     */
    public void start() throws FuscardSimulationException;

    /**
     * Pause the simulation sequence.
     *
     * @throws FuscardSimulationException If the <code>Simulation</code> is not
     * running.
     */
    public void pause() throws FuscardSimulationException;

    /**
     * Continue a simulation sequence after it is started and is then paused.
     * Calling run on a
     * <code>Simulation</code> that is not started should throw
     * {@link FuscardSimulationException FuscardSimulationException}. Calling
     * run on a running
     * <code>Simulation</code> should throw
     * {@link FuscardSimulationException FuscardSimulationException}.
     */
    public void run() throws FuscardSimulationException;

    /**
     * This method stops the simulation sequence. For a running
     * <code>Simulation</code> it has the effect of {@link #pause() }. For a
     * paused
     * <code>Simulation</code> it has the effect of {@link #abort() }. For a
     * ready
     * <code>Simulation</code> it has no effect. If there is any problem with
     * putting the Simulation in PAUSE, it aborts the simulation.
     */
    public void stop();

    /**
     * Destroys the state of this
     * <code>Simulation</code> so that it cannot be continued.
     */
    public void abort();

    /**
     * Gets the {@link SimulationState} That this
     * <code>Simulation</code> is in.
     *
     * @return The current {@link SimulationState}.
     */
    public SimulationState getState();

    /**
     * The state in which a
     * <code>Simulation<code> is at at a particlar point.
     *
     * @see Simulation
     */
    public static enum SimulationState {

        /**
         * The
         * <code>Simulation</code> is ready to be started
         */
        READY,
        /**
         * The
         * <code>Simulation</code> has been started by calling {@link Simulation#start()
         * }.
         */
        STARTED,
        /**
         * The
         * <code>Simulation</code> has been started by calling {@link Simulation#start()
         * } and is not in the {@link #PAUSED} state.
         */
        RUNNING,
        /**
         * The
         * <code>Simulation</code> has been started and is paused by calling {@link Simulation#pause()
         * }.
         */
        PAUSED,
        /**
         * The
         * <code>Simulation</code> has been destroyed and cannot run again.
         */
        DESTROYED,
        /**
         * If a
         * <code>Simulation</code> is not aborted it finishes normally and
         * enters the FINISHED state.
         */
        FINISHED
    }
}
