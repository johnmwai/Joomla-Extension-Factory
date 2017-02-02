package factory.codegen;

import com.fuscard.commons.FileUtils;
import com.fuscard.commons.FuscardXMLException;
import com.fuscard.commons.XMLDocument;
import factory.Application;
import factory.codegen.app.Project;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author John Mwai
 */
public class CodeGeneraror implements ConfigXMLStrings {

    private String configFile;
    private ExtensionInformation context = new ExtensionInformation();
    private boolean isCanonical = false;
    private final String rootXMLPath = Application.globalConfigurationFile;

    public CodeGeneraror() {
        try {
            configFile = Application.getInstance().getJoomlaConfigurationPath();
        } catch (FuscardXMLException ex) {
            Logger.getLogger(CodeGeneraror.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public ExtensionInformation getExtensionInfo() {
        return context;
    }

    public void setContext(ExtensionInformation context) {
        this.context = context;
    }

    public String getConfigFile() {
        if (configFile == null) {
            return "";
        }
        if (!isCanonical) {
            File f = new File(configFile);
            try {
                configFile = f.getCanonicalPath();
            } catch (IOException ex) {
            }
        }
        isCanonical = true;
        return configFile;
    }

    /**
     * Save the settings of the component context in an XML. Also set this as
     * the configuration file to use.
     *
     * @param componentContext the component context to save
     */
    public void saveComponentContext(ExtensionInformation componentContext,
            String fileName) throws CodeGeneratorException {
        try {
            if (fileName != null) {
                setConfigFile(fileName);
            }
            if (getConfigFile() == null) {
                throw new CodeGeneratorException(
                        "There is no configuration file to save the component context");
            }
            //Saving the location of the config file in the root configuration file

            XMLDocument xmldoc = new XMLDocument(rootXMLPath);
            xmldoc.loadFromFile();
            if (!xmldoc.hasValidDocument()) {
                xmldoc.createNewDocument("root");
            }
            String[] parts = {"root", "config-location"};
            xmldoc.setElementValue(parts, getConfigFile());
            xmldoc.saveToFile();
            //now let us save the application context
            XMLDocument configXML = new XMLDocument(getConfigFile());
            configXML.loadFromFile();
            if (!configXML.hasValidDocument()) {
                configXML.createNewDocument("root");
            }
            configXML.setElementValue(new String[]{"root", app_name},
                    componentContext.getAppName());
            configXML.setElementValue(new String[]{"root", author},
                    componentContext.getAuthor());
            configXML.setElementValue(new String[]{"root", author_email},
                    componentContext.getAuthor_mail());
            configXML.setElementValue(new String[]{"root", author_website},
                    componentContext.getAuthor_website());
            configXML.setElementValue(new String[]{"root", copyright},
                    componentContext.getCopyright());
            configXML.setElementValue(new String[]{"root", joomla_directory},
                    componentContext.getJoomlaDirectory());
            configXML.setElementValue(new String[]{"root", license},
                    componentContext.getLicense());
            configXML.setElementValue(new String[]{"root", summary},
                    componentContext.getSummary());
            configXML.setElementValue(new String[]{"root", version},
                    componentContext.getVersion());
            configXML.setElementValue(new String[]{"root", release_index},
                    componentContext.getReleaseIndex());
            configXML.saveToFile();
            context = componentContext;
        } catch (FuscardXMLException ex) {
            Logger.getLogger(CodeGeneraror.class.getName()).log(Level.SEVERE,
                    null, ex);
            throw new CodeGeneratorException(ex);
        }
    }

    public void loadConfigurationFile() throws FuscardXMLException {
        XMLDocument configXML = new XMLDocument(getConfigFile());
        configXML.loadFromFile();
        if (!configXML.hasValidDocument()) {
            return;
        }
        context.setAppName(configXML.getElementValue(new String[]{"root",
            app_name}));
        context.setAuthor(
                configXML.getElementValue(new String[]{"root", author}));
        context.setAuthor_mail(configXML.getElementValue(new String[]{"root",
            author_email}));
        context.setAuthor_website(configXML.getElementValue(new String[]{"root",
            author_website}));
        context.setCopyright(configXML.getElementValue(new String[]{"root",
            copyright}));
        context.setJoomlaDirectory(configXML.getElementValue(
                new String[]{"root",
            joomla_directory}));
        context.setLicense(configXML.getElementValue(
                new String[]{"root", license}));
        context.setReleaseIndex(configXML.getElementValue(
                new String[]{"root",
            release_index}));
        context.setSummary(configXML.getElementValue(
                new String[]{"root", summary}));
        context.setVersion(configXML.getElementValue(
                new String[]{"root", version}));

    }

    public void setConfigFile(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("Cannot set config file to null");
        }
        File f = new File(fileName);
        String extension = FileUtils.getExtension(f);
        if (extension == null || !extension.equals("xml")) {
            throw new IllegalArgumentException(
                    "The file name is not a valid."
                    + " '" + (extension == null
                    ? "" : extension) + "' is not a valid xml extension.");
        }
        isCanonical = false;
        this.configFile = fileName;
    }

    public boolean addComponent(ExtensionInformation info) throws CodeGeneratorException {
        return new Component(info).make();
    }

    public boolean removeComponent(ExtensionInformation info) {
        return new Component(info).remove();
    }

    public boolean addModel(String viewName, boolean admin,
            ExtensionInformation info) throws CodeGeneratorException {
        Model m = new Model(info, admin, viewName);
        return m.make();
    }

    public boolean addController(String viewName, boolean admin,
            ExtensionInformation info) throws CodeGeneratorException {
        Controller c = new Controller(info, admin, viewName);
        return c.make();
    }

    public void addListAndDetails(String list, String details, String table,
            JEFField[] fields,
            boolean admin, ExtensionInformation info) throws CodeGeneratorException {
        /**
         * Create list
         */
        Controller c = new Controller(info, admin, list);
        c.make();
        Model m = new Model(info, admin, list);
        m.make();
        View v = new View(info, admin, list);
        v.make();
        /**
         * Create details
         */
        c = new Controller(info, admin, details);
        c.make();
        m = new Model(info, admin, details);
        m.make();
        v = new View(info, admin, details);
        v.make();
        Form f = new Form(info, admin, details);
        f.make();
        Table t = new Table(info, true, table);
        t.make();

    }

    public boolean addView(String viewName, boolean admin,
            ExtensionInformation info) throws CodeGeneratorException {
        View v = new View(info, admin, viewName);
        return v.make();
    }

    public boolean addTable(String viewName, boolean admin,
            ExtensionInformation info) throws CodeGeneratorException {
        Table t = new Table(info, admin, viewName);
        return t.make();
    }

    public void addScript(ExtensionInformation info) throws CodeGeneratorException {
        Script s = new Script(info, false, "");
        s.make();
    }

    public boolean removeModel(String viewName, boolean admin,
            ExtensionInformation info) throws CodeGeneratorException {
        Model m = new Model(info, admin, viewName);
        return m.remove();
    }

    public void removeController(String viewName, boolean admin,
            ExtensionInformation info) throws CodeGeneratorException {
        Controller c = new Controller(info, admin, viewName);
        c.remove();
    }

    public void removeView(String viewName, boolean admin,
            ExtensionInformation info) throws CodeGeneratorException {
        View v = new View(info, admin, viewName);
        v.remove();
    }

    public void removeTable(String viewName, boolean admin,
            ExtensionInformation info) throws CodeGeneratorException {
        Table t = new Table(info, admin, viewName);
        t.remove();
    }

    public void removeScript(ExtensionInformation info) throws CodeGeneratorException {
        Script s = new Script(info, false, "");
        s.remove();
    }

    public static Document getTemplate(String name) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.
                    newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(CodeGeneraror.class.
                    getResourceAsStream(
                    name));
            return document;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(CodeGeneraror.class.getName()).log(Level.SEVERE,
                    null, ex);
            return null;
        }
    }

    static Node getTemplateNode(Document d) {
        String exp;
        Node r;

        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        try {
            exp = "root/template";
            r = (Node) xPath.evaluate(exp, d, XPathConstants.NODE);
            return r;
        } catch (XPathExpressionException ex) {
            Logger.getLogger(CodeGeneraror.class.getName()).log(Level.SEVERE,
                    null, ex);
            return null;
        }
    }

    private static void replace(Node d, String xpath, String value) {
        try {
            NodeList nl;
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            nl = (NodeList) xPath.evaluate(xpath, d, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
                Node n2 = nl.item(i);
                n2.setTextContent(value == null ? "" : value);
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(CodeGeneraror.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
    
    public static String normalizeName(String name){
        if(name.matches("\\d.*")){
            name = "dt" + name;
        }
        name = name.replaceAll("[^a-zA-Z0-9]", "_");
        return name;
    }

    static void putGeneralInfo(Node d, ExtensionInformation context) {
        String exp;
        String prefix = "";
        exp = "component_name";
        replace(d, exp, context.getAppName());
        exp = "component_name_lower";
        replace(d, exp, context.getAppName().toLowerCase());
        exp = "version";
        replace(d, exp, context.getVersion());
        exp = "copyright";
        replace(d, exp, context.getCopyright());
        exp = "licence";
        replace(d, exp, context.getLicense());
        exp = "author";
        replace(d, exp, context.getAuthor());
        exp = "author_mail";
        replace(d, exp, context.getAuthor_mail());
        exp = "author_website";
        replace(d, exp, context.getAuthor_website());
        exp = "summary";
        replace(d, exp, context.getSummary());
    }

    private static interface JEFFile {

        /**
         * Generate a file
         *
         * @return whether the file was generated successfully
         * @throws CodeGeneratorException If there was a problem
         */
        boolean make() throws CodeGeneratorException;

        /**
         * Remove a file
         *
         * @return Whether the file was removed
         * @throws CodeGeneratorException If there was a problem
         */
        boolean remove() throws CodeGeneratorException;
    }

    private static class Component implements JEFFile {

        ExtensionInformation componentContext;

        public Component(ExtensionInformation componentContext) {
            this.componentContext = componentContext;
        }

        private void putIndexFiles() {
        }

        @Override
        public boolean make() throws CodeGeneratorException {
            new Entry(componentContext, true, "").make();
            new Entry(componentContext, false, "").make();
            new Manifest().make();
            putIndexFiles();
            return true;
        }

        @Override
        public boolean remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static class Entry extends MVCClass {

        public Entry(ExtensionInformation componentContext, boolean isAdmin,
                String name) {
            super(componentContext, isAdmin, name);
        }

        @Override
        String getType() {
            return "entry";
        }

        @Override
        protected void processTemplateNode(Node n) {
        }
    }

    private static class Manifest implements JEFFile {

        ExtensionInformation componentContext;

        public Manifest(ExtensionInformation componentContext) {
            this.componentContext = componentContext;
        }

        public Manifest() {
        }

        @Override
        public boolean make() {

            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    /**
     * This is a concrete implementation of JEFFile
     *
     */
    private static abstract class MVCClass implements JEFFile {

        ExtensionInformation myExtensionInfo;
        boolean isAdmin;
        String name;

        public MVCClass(ExtensionInformation componentContext, boolean isAdmin,
                String name) {
            this.myExtensionInfo = componentContext;
            this.isAdmin = isAdmin;
            this.name = name;
        }

        public MVCClass() {
        }

        abstract String getType();

        File getFile() throws CodeGeneratorException {
            String comnam = myExtensionInfo.getAppName(), joomdir = myExtensionInfo.
                    getJoomlaDirectory();
            if (comnam == null) {
                throw new CodeGeneratorException("Component name is not defined");
            }
            if (joomdir == null) {
                throw new CodeGeneratorException(
                        "Joola directory is not defined");
            }
            Path p = Paths.get(joomdir);
            String s = getFileName(comnam);
            if (isAdmin) {
                p = p.resolve("administrator/" + s);
            } else {
                p = p.resolve(s);
            }
            return p.toFile();
        }

        /**
         * Returns the path to the file name of the file type given the name of
         * the class
         *
         * @param componentName The name of the extension
         * @return The file path relative to the site folder in the Joomla!
         * installation
         */
        String getFileName(String componentName) {
            return "components/com_" + componentName.toLowerCase() + "/"
                    + getType() + "s/"
                    + name.toLowerCase() + ".php";
        }

        /**
         * Passes the template node to the individual file type object for
         * further processing after inserting the general information.
         *
         * @param n The template Node
         */
        protected abstract void processTemplateNode(Node n);
        
        @Override
        public boolean make() throws CodeGeneratorException {
            File f = getFile();
            if (f.isFile()) {
                throw new CodeGeneratorException(
                        "The " + getType() + " name is in use");
            }
            Document d = getTemplate(getType() + ".xml");
            if (d == null) {
                throw new CodeGeneratorException(
                        "Template for type \"" + getType() + "\" not found");
            }

            Node n = getTemplateNode(d);
            if (n == null) {
                throw new CodeGeneratorException(
                        "Template node not found");
            }

            putGeneralInfo(n, myExtensionInfo);
            processTemplateNode(n);
            String text = n.getTextContent();
            File par = new File(f.getParent());
            if (!par.isDirectory()) {
                par.mkdirs();
            }
            try {
                f.createNewFile();
                try (FileWriter fw = new FileWriter(f)) {
                    fw.append(text);
                    fw.flush();
                }
                return true;
            } catch (IOException ex) {
                Logger.getLogger(CodeGeneraror.class.getName()).
                        log(Level.SEVERE,
                        null, ex);
                return false;
            }
        }

        @Override
        public boolean remove() throws CodeGeneratorException {
            File f = getFile();
            return f.delete();
        }
    }

    private static class Form extends MVCClass {

        public Form(ExtensionInformation componentContext, boolean isAdmin,
                String name) {
            super(componentContext, isAdmin, name);
        }

        @Override
        String getType() {
            return "form";
        }

        @Override
        protected void processTemplateNode(Node n) {
        }
    }

    private static class View extends MVCClass {

        public View() {
        }

        @Override
        protected void processTemplateNode(Node n) {
        }

        public View(ExtensionInformation componentContext, boolean isAdmin,
                String name) {
            super(componentContext, isAdmin, name);
        }

        @Override
        String getType() {
            return "view";
        }

        @Override
        String getFileName(String componentName) {
            return "components/com_" + componentName.toLowerCase() + "/views/"
                    + name.toLowerCase() + "/view.html.php";
        }

        /**
         * In the case of views what we delete is the folder containing the view
         *
         * @return if we succeeded in removing the view
         */
        @Override
        public boolean remove() throws CodeGeneratorException {
            File f = getFile();
            File p = new File(f.getParent());
            return p.delete();
        }
    }

    private static class Model extends MVCClass {

        public Model() {
        }

        @Override
        protected void processTemplateNode(Node n) {
        }

        public Model(ExtensionInformation componentContext, boolean isAdmin,
                String name) {
            super(componentContext, isAdmin, name);
        }

        @Override
        String getType() {
            return "model";
        }
    }

    private static class Controller extends MVCClass {

        public Controller() {
        }

        @Override
        protected void processTemplateNode(Node n) {
        }

        public Controller(ExtensionInformation componentContext, boolean isAdmin,
                String name) {
            super(componentContext, isAdmin, name);
        }

        @Override
        String getType() {
            return "controller";
        }
    }

    public static enum ViewTypes {

        LIST,
        DETAILS,
        CONTROL_PANEL
    }

    private static class Layout extends MVCClass {

        @Override
        String getType() {
            return "layout";
        }

        @Override
        protected void processTemplateNode(Node n) {
        }
    }

    private static class Table extends MVCClass {

        private String dbTable;

        public Table() {
        }

        @Override
        protected void processTemplateNode(Node n) {
            if (getDbTable() != null) {
                String exp = "the_body/db_table";
                replace(n, exp, getDbTable());
            }
        }

        public Table(ExtensionInformation componentContext, boolean isAdmin,
                String name) {
            super(componentContext, isAdmin, name);
        }

        @Override
        String getType() {
            return "table";
        }

        /**
         * @return the dbTable
         */
        public String getDbTable() {
            return dbTable;
        }

        /**
         * @param dbTable the dbTable to set
         */
        public void setDbTable(String dbTable) {
            this.dbTable = dbTable;
        }
    }

    private static class Script extends MVCClass {

        public Script(ExtensionInformation componentContext, boolean isAdmin,
                String name) {
            super(componentContext, isAdmin, name);
        }

        @Override
        String getType() {
            return "script";
        }

        @Override
        protected void processTemplateNode(Node n) {
        }
    }

    public static class JEFField {

        private String name;
        private String description;
        private String displayName;
        private boolean required;
        private String _default;
        private String type;
        private String alias;
        private int size;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description the description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return the displayName
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * @param displayName the displayName to set
         */
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        /**
         * @return the required
         */
        public boolean isRequired() {
            return required;
        }

        /**
         * @param required the required to set
         */
        public void setRequired(boolean required) {
            this.required = required;
        }

        /**
         * @return the _default
         */
        public String getDefault() {
            return _default;
        }

        /**
         * @param _default the _default to set
         */
        public void setDefault(String _default) {
            this._default = _default;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type the type to set
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return the alias
         */
        public String getAlias() {
            return alias;
        }

        /**
         * @param alias the alias to set
         */
        public void setAlias(String alias) {
            this.alias = alias;
        }

        /**
         * @return the size
         */
        public int getSize() {
            return size;
        }

        /**
         * @param size the size to set
         */
        public void setSize(int size) {
            this.size = size;
        }
    }
}
