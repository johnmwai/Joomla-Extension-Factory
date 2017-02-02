package factory.design;

/**
 * A Simulating Node with an affiliation to a particular Box type
 *
 * @author John Mwai
 */
public class ApplicationSimulatingNode implements SimulatingNode {

    private final Box box;
    /**
     * The state of this SimulatingNode
     */
    private SimulatingNode.SimulatingNodeState simulatingNodeState;

    public ApplicationSimulatingNode(Box box) {
        this.box = box;
    }

    public Box getBox() {
        return box;
    }

    @Override
    public void goLive() {
        setSimulatingNodeState(SimulatingNodeState.LIVE);
    }

    @Override
    public void takeOffline() {
        setSimulatingNodeState(SimulatingNodeState.DORMANT);
    }

    /**
     * @param simulatingNodeState the simulatingNodeState to set
     */
    public void setSimulatingNodeState(
            SimulatingNode.SimulatingNodeState simulatingNodeState) {
        this.simulatingNodeState = simulatingNodeState;
    }

    @Override
    public SimulatingNodeState getSimulatingNodeState() {
        return this.simulatingNodeState;
    }

    /**
     *
     * @return If this Node is live
     */
    public boolean isLive() {
        return getSimulatingNodeState() == SimulatingNodeState.LIVE;
    }

    /**
     *
     * @return Whether this Node is dormant
     */
    public boolean isDormant() {
        return getSimulatingNodeState() == SimulatingNodeState.DORMANT;
    }

    @Override
    public void takeControl(SimulationState giver) {
        
    }

    @Override
    public void giveControl(SimulationState receiver) {
        
    }

    @Override
    public void start() throws FuscardSimulationException {
        
    }

    @Override
    public void pause() throws FuscardSimulationException {
        
    }

    @Override
    public void run() throws FuscardSimulationException {
        
    }

    @Override
    public void stop() {
        
    }

    @Override
    public void abort() {
       
    }

    @Override
    public SimulationState getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addNodeListener(NodeListener nodeListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyNodeListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void terminate(Edge edge) throws FuscardControlWebException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void testTerminate(Edge edge) throws FuscardControlWebException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void originate(Edge edge) throws FuscardControlWebException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void testOriginate(Edge edge) throws FuscardControlWebException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Edge edge) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edgeChanged(Edge.EdgeChangeEvent ece) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
