package factory.design;

import factory.design.element.Form;
import factory.design.element.Table;

/**
 * Represents a fully functional SimulatingControlWeb with the ability to accept
 * actual Elements. This Control Web accepts Nodes
 *
 * @author John Mwai
 */
public class ApplicationUnit extends AbstractSimulatingControlWeb {

    /**
     * This top level ControlWeb allows only forms, tables,
     *
     * @param node The node to add to the Application Unit
     * @throws FuscardControlWebException If the node is not acceptable or it is
     * already registered
     */
    @Override
    public void acceptNode(Node node) throws FuscardControlWebException {
        if (!(node instanceof ApplicationSimulatingNode)) {
            throw new FuscardControlWebException("Expecting a ApplicationSimulatingNode");
        }
        ApplicationSimulatingNode nnNode = (ApplicationSimulatingNode) node;
        Box box = nnNode.getBox();
        if(!(box instanceof Table || box instanceof Form)){
            throw new FuscardControlWebException("Node contains unexpected Box");
        }
        super.acceptNode(node);
    }
}
