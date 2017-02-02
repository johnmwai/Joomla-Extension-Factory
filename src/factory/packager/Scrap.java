package factory.packager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//These classes are for the exceptions that can be thrown when the XML document is parsed:

//These classes read the sample XML file and manage output:

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
//Finally, import the W3C definitions for a DOM, DOM exceptions, entities and nodes:

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author John Mwai
 */
public class Scrap {

    public static void main(String[] args) throws Exception {
        String filename = "C:\\Users\\John Mwai\\Documents\\braviacart\\emails.xml";//


        DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();


        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(filename));

        doc.normalize();
        doc.appendChild(doc.createComment("Another comment"));

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        new Scrap().echo(doc);
        StreamResult result = new StreamResult(new PrintWriter(filename));
        transformer.transform(source, result);


    }

    private void echo(Node n) {
        outputIndentation();

        int type = n.getNodeType();
        switch (type) {
            case Node.ATTRIBUTE_NODE:
                out.print("ATTR:");
                printlnCommon(n);
                break;
            case Node.CDATA_SECTION_NODE:
                out.print("CDATA:");
                printlnCommon(n);
                break;
            case Node.COMMENT_NODE:
                out.print("COMM:");
                printlnCommon(n);
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                out.print("DOC_FRAG:");
                printlnCommon(n);
                break;
            case Node.DOCUMENT_NODE:
                out.print("DOC:");
                printlnCommon(n);
                break;
            case Node.DOCUMENT_TYPE_NODE:
                out.print("DOC_TYPE:");
                printlnCommon(n);

                NamedNodeMap nodeMap = ((DocumentType) n).getEntities();
                indent += 2;
                for (int i = 0; i < nodeMap.getLength(); i++) {
                    Entity entity = (Entity) nodeMap.item(i);
                    echo(entity);
                }
                indent -= 2;
                break;
            case Node.ELEMENT_NODE:
                out.print("ELEM:");
                printlnCommon(n);

                NamedNodeMap atts = n.getAttributes();
                indent += 2;
                for (int i = 0; i < atts.getLength(); i++) {
                    Node att = atts.item(i);
                    echo(att);
                }
                indent -= 2;
                break;
            case Node.ENTITY_NODE:
                out.print("ENT:");
                printlnCommon(n);
                break;
            case Node.ENTITY_REFERENCE_NODE:
                out.print("ENT_REF:");
                printlnCommon(n);
                break;
            case Node.NOTATION_NODE:
                out.print("NOTATION:");
                printlnCommon(n);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                out.print("PROC_INST:");
                printlnCommon(n);
                break;
            case Node.TEXT_NODE:
                out.print("TEXT:");
                printlnCommon(n);
                break;
            default:
                out.print("UNSUPPORTED NODE: " + type);
                printlnCommon(n);
                break;
        }

        indent++;
        for (Node child = n.getFirstChild(); child != null;
                child = child.getNextSibling()) {
            echo(child);
        }
        indent--;
    }

    private void outputIndentation() {
        for (int i = 0; i < indent; i++) {
            out.print(basicIndent);
        }
    }

    private void printlnCommon(Node n) {
        out.print(" nodeName=\"" + n.getNodeName() + "\"");

        String val = n.getNamespaceURI();
        if (val != null) {
            out.print(" uri=\"" + val + "\"");
        }

        val = n.getPrefix();
        if (val != null) {
            out.print(" pre=\"" + val + "\"");
        }

        val = n.getLocalName();
        if (val != null) {
            out.print(" local=\"" + val + "\"");
        }

        val = n.getNodeValue();
        if (val != null) {
            out.print(" nodeValue=");
            if (val.trim().equals("")) {
                // Whitespace
                out.print("[WS]");
            } else {
                out.print("\"" + n.getNodeValue() + "\"");
            }
        }
        out.println();
    }
    private String basicIndent = "  ";
    private int indent = 0;
    private PrintStream out = System.out;
}
