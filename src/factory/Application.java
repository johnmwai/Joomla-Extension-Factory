package factory;

import com.fuscard.commons.FuscardXMLException;
import com.fuscard.commons.XMLDocument;
import factory.codegen.app.AllSystems;
import factory.codegen.app.DrawingManager;
import factory.codegen.app.JEFCanvas;
import factory.codegen.app.Theme;
import java.awt.Color;
import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author John Mwai
 */
public class Application {

    private XMLDocument root;
    public static final String globalConfigurationFile = "configuration/root.xml";
    private AllSystems allSystems;
    /**
     * Manager for drawings for the application
     */
    private DrawingManager drawingManager;

    private Application() {
    }

    public static Application getInstance() {
        return ApplicationHolder.INSTANCE;
    }

    /**
     * @return the drawingManager
     */
    public DrawingManager getDrawingManager() {
        return drawingManager;
    }

    /**
     * @param drawingManager the drawingManager to set
     */
    public void setDrawingManager(
            DrawingManager drawingManager) {
        this.drawingManager = drawingManager;
    }

    public void error(String message) {
        if(allSystems!=null){
            allSystems.alert(message);
        }
    }

    /**
     * @return the allSystems
     */
    public AllSystems getAllSystems() {
        return allSystems;
    }

    /**
     * @param allSystems the allSystems to set
     */
    public void setAllSystems(AllSystems allSystems) {
        this.allSystems = allSystems;
    }

    private static class ApplicationHolder {

        private static final Application INSTANCE = new Application();
    }

    private synchronized void ensureRoot() throws FuscardXMLException {
        if (root == null) {
            root = new XMLDocument(globalConfigurationFile);
            root.loadFromFile();
        }
    }

    public String getValue(String name) {
        try {
            ensureRoot();
            String[] parts = {"root", convertPath(name)};
            return root.getElementValue(parts);
        } catch (FuscardXMLException ex) {
            return null;
        }
    }

    public void setValue(String name, String value) throws FuscardXMLException {
        ensureRoot();
        String[] parts = {"root", convertPath(name)};
        
        root.setElementValue(parts, value);
        root.saveToFile();
    }

    /**
     * Stores an array of strings in the root configuration XML. You can provide
     * the name in the following form:
     * <pre>
     * tagname (("." | "/"), tagname) *
     * </pre> Where tagname is any valid XML id. The last id is the name that
     * will be used as the tag name of the values. For example:
     * <pre>
     * bla/more-bla/my-things/thing
     * or
     * bla.more-bla.my-things.thing
     * </pre> The above paths are equivalent. They tell this method to create
     * the following nodes:
     * <pre>
     *&lt;root&gt;
     * ...
     *      &lt;bla&gt;
     *          ...
     *          &lt;more-bla&gt;
     *              ...
     *              &lt;my-things&gt;
     *                  &lt;thing&gt;
     *                      value of thing 1
     *                  &lt;/thing&gt;
     *                  &lt;thing&gt;
     *                      value of thing 2
     *                  &lt;/thing&gt;
     *                  .
     *                  .
     *                  .
     *                  &lt;thing&gt;
     *                      value of last thing
     *                  &lt;/thing&gt;
     *              &lt;/my-things&gt;
     *          &lt;/more-bla&gt;
     *          ...
     *      &lt;/bla&gt;
     * ...
     * &lt;root&gt;
     * </pre> where root is the document element. Using this method will delete
     * any data in the parent node.
     *
     * @param name The path of the elements that will be created.
     * @param values
     * @throws FuscardXMLException
     */
    public void setValues(String name, String[] values) throws FuscardXMLException {
        String nodepath = preprocessPath(name);
        //Ensure the path exists
        root.fillNodes(nodepath);
        Node node = root.getNode(nodepath);
        String tagName = nodepath.substring(nodepath.lastIndexOf("/") + 1);
        Node parentNode = node.getParentNode();
        parentNode.setTextContent(null);
        if (values != null) {
            for (String v : values) {
                Element element = root.getDocument().createElement(tagName);
                element.setTextContent(v);
                parentNode.appendChild(element);
            }
        }
        root.saveToFile();
    }

    private String convertPath(String name) throws FuscardXMLException {
        //Be sure we have a valid path
        if (name == null || "".equals(name)) {
            throw new FuscardXMLException("Name cannot be empty or null");
        }
        //convert '.' to '/'
        name = name.replaceAll("\\.", "/");
        //Ensure we have an XML document
        ensureRoot();
        return name;
    }

    private String preprocessPath(String name) throws FuscardXMLException {
        //create a path relative to the document element of the configuration file
        return "root/" + convertPath(name);
    }

    /**
     * Get strings saved in the root configuration.
     *
     * @param name path to retrieve strings.
     * @return array of values.
     * @see #setValues(java.lang.String, java.lang.String[])
     */
    public String[] getValues(String name) {
        String nodepath;
        NodeList nodes;
        try {
            nodepath = preprocessPath(name);
            nodes = root.getNodes(nodepath);
        } catch (FuscardXMLException ex) {
            return new String[]{};
        }
        if (nodes == null) {
            return new String[]{};
        }
        String[] res = new String[nodes.getLength()];
        for (int i = 0; i < res.length; i++) {
            res[i] = nodes.item(i).getTextContent();
        }
        return res;
    }

    public String getJoomlaConfigurationPath() throws FuscardXMLException {
        return getValue("config-location");
    }

    public String getParserConfigurationPath() throws FuscardXMLException {
        return getValue("parser-config");
    }
    private XMLDocument parserDocument;

    public XMLDocument getParserDoc() throws FuscardXMLException {
        if (parserDocument == null) {
            parserDocument = new XMLDocument(getParserConfigurationPath());
            parserDocument.loadFromFile();
        }
        return parserDocument;
    }

    public String getCreditsPath() throws FuscardXMLException {
        return getValue("credits");
    }
    private static Color[] invisibleFGColors = {Color.WHITE, Color.YELLOW, Color.GREEN};

    public static boolean invisibleFG(Color fg) {

        for (Color c : invisibleFGColors) {
            if (c.equals(fg)) {
                return true;
            }
        }
        return false;
    }

    public static void setColors(Component p, Theme theme) {
        if (theme == null || p == null) {
            return;
        }
        if (p instanceof AbstractButton || p instanceof NoTheme) {
            return;
        }

        boolean invisibleColor = invisibleFG(theme.getFg());
        if ((p instanceof JButton
                || p instanceof JMenu
                || p instanceof JMenuItem
                || p instanceof JTabbedPane
                || p instanceof JComboBox) && invisibleColor) {
            p.setForeground(Color.black);
        } else {
            p.setForeground(theme.getFg());
        }
        if (p instanceof JTextComponent) {
            p.setBackground(theme.getTextBg());
        } else {
            p.setBackground(theme.getBg());
        }

        if (p instanceof JComponent) {
            JComponent pp = (JComponent) p;
            for (Component c : pp.getComponents()) {
                setColors(c, theme);
            }
        }
        if (p instanceof JTable) {
            JTable t = (JTable) p;
            if (invisibleColor) {
                t.getTableHeader().setForeground(Color.black);
            } else {
                t.getTableHeader().setForeground(theme.getFg());
            }
        }

    }
    
    public interface NoTheme{
        
    }
}
