package factory.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author John Mwai
 */
public class XMLTree extends JTree {

    public XMLTree(String filename) throws IOException{
        this(filename, new FileInputStream(new File(filename)));
    }

    public XMLTree(String filename, InputStream in) {
        super(makeRootNode(in));
        
    }
    
    
    

    private static DefaultMutableTreeNode makeRootNode(InputStream in) {
        try {
            Document doc;
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    in);
            Element rootElement = doc.getDocumentElement();
            DefaultMutableTreeNode treeNode = buildTree(rootElement);
            return treeNode;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Error making root node");
            return new DefaultMutableTreeNode("Error making root node: " + e);
        }
    }

    private static DefaultMutableTreeNode buildTree(Element rootElement) {
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(
                treeNodeLabel(rootElement));
        addChildren(rootTreeNode, rootElement);
        return rootTreeNode;
    }

    private static void addChildren(DefaultMutableTreeNode parentTreeNode,
            Node parentXMLElememt) {
        NodeList childElements = parentXMLElememt.getChildNodes();
        for (int i = 0; i < childElements.getLength(); i++) {
            Node childElement = childElements.item(i);
            if (!(childElement instanceof Text || childElement instanceof Comment)) {
                DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(
                        treeNodeLabel(childElement));
                parentTreeNode.add(childTreeNode);
                addChildren(childTreeNode, childElement);
            }
        }
    }

    private static String treeNodeLabel(Node childElement) {
        NamedNodeMap elementAttributes = childElement.getAttributes();
        String treeNodeLabel = childElement.getNodeName();
        if (elementAttributes != null && elementAttributes.getLength() > 0) {
            treeNodeLabel += " (";
            for (int i = 0; i < elementAttributes.getLength(); i++) {
                Node attribute = elementAttributes.item(i);
                if (i > 0) {
                    treeNodeLabel += ", ";
                }
                treeNodeLabel += attribute.getNodeName() + "=" + attribute.getNodeValue();
            }
            treeNodeLabel += ")";
        }
        return treeNodeLabel;
    }
}
