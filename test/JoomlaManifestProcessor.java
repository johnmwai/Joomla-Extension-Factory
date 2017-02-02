
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class JoomlaManifestProcessor {

    public static void main(String[] args) throws Exception {
        String dir = "C:\\wamp\\www\\my_joomla\\components\\com_braviacart\\models\\forms";
        String academics = "academic.xml";
        String article = "article.xml";
        Path p = Paths.get(dir);

        Path pac = p.resolve(academics);
        Path par = p.resolve(article);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(par.toFile());

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        String fieldExp = "form/fieldset/field";
        String fieldNoHiddenExp = "form/fieldset/field[@type!=\"hidden\"]";
        NodeList nl = (NodeList) xpath.evaluate(fieldExp, doc,
                XPathConstants.NODESET);


        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            if(e.hasAttribute("name")){
                String att = e.getAttribute("name");
                String ss = "$response['"+att+"'] = BraviaCartHelper::get('xxxxxxxx', $response['"+att+"'], true);";
                String sr = "'"+att+"' => $data['"+att+"'],";
                String st = "'"+att+"',";
                System.out.println(att);
            }
        }
    }
}
