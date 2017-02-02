package factory.xml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author John Mwai
 */
public class Tests {

    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException, TransformerException {
        InputStream instream = Tests.class.getResourceAsStream("myXml.xml");

        Document doc;
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                instream);

        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(
                doc), new StreamResult(System.out));

    }
}
