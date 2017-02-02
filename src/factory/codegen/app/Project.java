package factory.codegen.app;

import com.fuscard.commons.FuscardXMLException;
import com.fuscard.commons.XMLDocument;
import factory.codegen.CodeGeneraror;
import factory.codegen.ConfigXMLStrings;
import factory.codegen.ExtensionInformation;
import factory.codegen.JEFDatabaseTable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

/**
 * // * Encapsulates information about a project. A project can be uniquely
 * identified by the path to the configuration file.
 *
 * @author John Mwai
 */
public class Project implements ConfigXMLStrings {

    /**
     * The path to the JEF project configuration file.
     */
    private Path projectConfigurationFile;
    /**
     * The type of the project.
     */
    private ProjectType projectType;
    /**
     * Project name.
     */
    private String projectName;
    /**
     * The update server of this project.
     */
    private Path updateServer;
    /**
     * The extension information.
     */
    private ExtensionInformation extensionInformation;
    /**
     * Configuration XMLDocument object.
     */
    private XMLDocument xMLDocument;
    /**
     * Whether this project has no unsaved changes.
     */
    private boolean saved;
    /**
     * The possibility to set this as the main project.
     */
    private boolean main;
    /**
     * Hooks to clear this project wants to close.
     */
    private LinkedList<ProjectHook> projectHooks = new LinkedList<>();
    /**
     * Listeners to be notified when this project closes.
     */
    private LinkedList<ProjectListener> projectListeners = new LinkedList<>();
    /**
     * Drawings in this project.
     */
    private final LinkedList<Drawing> drawings = new LinkedList<>();
    /**
     * Whether this project is closed.
     */
    private boolean closed;
    /**
     * Code generator.
     */
    private CodeGeneraror codeGeneraror = new CodeGeneraror();
    /**
     * Store for general purpose properties
     */
    private HashMap<Object, Object> proreties = new HashMap<>();
    /**
     * List of database tables that this project uses
     */
    private LinkedList<JEFDatabaseTable> jEFDatabaseTables = new LinkedList<>();

    public Project() {
        projectConfigurationFile = Paths.get("");
        projectType = ProjectType.Component;
        projectName = "undefined";
        try {
            codeGeneraror.loadConfigurationFile();
            extensionInformation = codeGeneraror.getExtensionInfo();
        } catch (FuscardXMLException ex) {
            System.err.println("Loading config fialed....");
            extensionInformation = new ExtensionInformation();
        }
        xMLDocument = new XMLDocument("");
        saved = true;
        main = false;
        closed = false;
    }

    /**
     * Add a database table to this project
     *
     * @param databaseTable The database table to add
     */
    public void addDatabaseTable(JEFDatabaseTable databaseTable) {
        if (!jEFDatabaseTables.contains(databaseTable)) {
            jEFDatabaseTables.add(databaseTable);
            databaseTable.setProject(this);
        }
    }

    /**
     * Remove a database table form the list of database tables of this project
     *
     * @param databaseTable The database table to remove
     */
    public void removeDatabaseTable(JEFDatabaseTable databaseTable) {
        jEFDatabaseTables.remove(databaseTable);
    }

    /**
     *
     * @return The database tables
     */
    public LinkedList<JEFDatabaseTable> getjEFDatabaseTables() {
        return jEFDatabaseTables;
    }

    /**
     * Add a drawing to the list of drawings.
     *
     * @param drawing The drawing to add.
     */
    public void addDrawing(Drawing drawing) {
        if (!drawings.contains(drawing)) {
            drawings.add(drawing);
        }
    }

    /**
     * Mark this project as containing unsaved changes;
     */
    public void markNotSaved() {
        this.saved = false;
    }

    /**
     * Remove a drawing from the list of drawings.
     *
     * @param drawing The drawing to remove.
     */
    public void removeDrawing(Drawing drawing) {
        drawings.remove(drawing);
    }

    /**
     * Get a value stored in this project's properties HashMap.
     *
     * @param key The key
     * @return The value
     */
    public Object get(Object key) {
        return proreties.get(key);
    }

    /**
     * Store a value in this project's properties HashMap
     *
     * @param key The key
     * @param value The value
     */
    public void set(Object key, Object value) {
        proreties.put(key, value);
    }

    /**
     * We override to string in order to use the project Node
     *
     * @return the HTML string with the project name for displaying in
     * Components.
     */
    @Override
    public String toString() {
        return getProjectName();
    }

    /**
     * Add a ProjectHook to authorize this project wanting to close.
     *
     * @param hook The ProjectHook to add.
     */
    public void addProjectHook(ProjectHook hook) {
        if (!projectHooks.contains(hook)) {
            projectHooks.add(hook);
        }
    }

    /**
     * Remove a ProjectHook if it is registered in the list of ProjectHook's.
     *
     * @param hook The ProjectHook to remove.
     */
    public void removeProjectHook(ProjectHook hook) {
        projectHooks.remove(hook);
    }

    /**
     * Add a ProjectListener to be notified when the project changes state.
     *
     * @param listener The ProjectListener to add.
     */
    public void addProjectListener(ProjectListener listener) {
        if (!projectListeners.contains(listener)) {
            projectListeners.add(listener);
        }
    }

    /**
     * Remove a ProjectListener if it is registered in the list of
     * ProjectListener's.
     *
     * @param listener The ProjectHook to remove.
     */
    public void removeProjectListener(ProjectListener listener) {
        projectListeners.remove(listener);
    }

    /**
     * @return the projectConfigurationFile
     */
    public Path getProjectConfigurationFile() {
        return projectConfigurationFile;
    }

    /**
     * @param projectConfigurationFile the projectConfigurationFile to set
     */
    public void setProjectConfigurationFile(
            Path projectConfigurationFile) {
        this.projectConfigurationFile = projectConfigurationFile;
    }

    /**
     * Open the project in the IDE.
     */
    public void open() {
        for (ProjectHook ph : projectHooks) {
            if (!ph.beforeProjectOpens(this)) {
                return;
            }
        }
        _open();
        for (ProjectListener pl : projectListeners) {
            pl.projectOpened(this);
        }
    }

    /**
     * Close the project in the IDE.
     */
    public void close() {
        for (ProjectHook ph : projectHooks) {
            if (!ph.beforeProjectCloses(this)) {
                return;
            }
        }
        _close();
        for (ProjectListener pl : projectListeners) {
            pl.projectClosed(this);
        }
    }

    /**
     * Save the project information.
     */
    public void save() throws FuscardProjectException, FuscardXMLException {
        for (ProjectHook ph : projectHooks) {
            if (!ph.beforeProjectSaves(this)) {
                return;
            }
        }
        _save();
        for (ProjectListener pl : projectListeners) {
            pl.projectSaved(this);
        }
    }

    /**
     * Build the project.
     */
    public void build() {
        for (ProjectHook ph : projectHooks) {
            if (!ph.beforeProjectBuilds(this)) {
                return;
            }
        }
        _build();
        for (ProjectListener pl : projectListeners) {
            pl.projectBuilt(this);
        }
    }

    private void _open() {
        this.closed = false;
    }

    private void _save() throws FuscardProjectException, FuscardXMLException {
        saveProject();
        this.saved = true;
    }

    private void _close() {
        this.closed = true;
    }

    private void _build() {
        /*
         * TODO build project.
         */
    }

    /**
     * Attempts to load the project file from the XML file.
     *
     * @throws FuscardXMLException If there is an XML error saving to XML file.
     * @throws FuscardProjectException If there is a project configuration
     * error.
     */
    public void loadFromConfigurationFile() throws FuscardProjectException, FuscardXMLException {
        if (projectConfigurationFile == null) {
            throw new FuscardProjectException(
                    "The configuration file path may not be null");
        }
        xMLDocument = new XMLDocument(projectConfigurationFile.toString());
        xMLDocument.loadFromFile();
        initInfo();
    }

    private void initInfo() {
        ExtensionInformation info = getExtensionInformation();
        String val, rootElem = "jef_project";

        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            app_name})) != null) {
            info.setAppName(val);
        }
        setProjectName(info.getAppName());

        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            author})) != null) {
            info.setAuthor(val);
        }
        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            author_email})) != null) {
            info.setAuthor_mail(val);
        }
        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            author_website})) != null) {
            info.setAuthor_website(val);
        }
        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            copyright})) != null) {
            info.setCopyright(val);
        }
        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            joomla_directory})) != null) {
            info.setJoomlaDirectory(val);
        }
        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            license})) != null) {
            info.setLicense(val);
        }
        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            release_index})) != null) {
            info.setReleaseIndex(val);
        }
        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            summary})) != null) {
            info.setSummary(val);
        }
        if ((val = xMLDocument.getElementValue(
                new String[]{rootElem,
            version})) != null) {
            info.setVersion(val);
        }
    }

    /**
     * Save the project information in the project configuration file.
     *
     * @throws FuscardXMLException If there is an XML error saving to XML file.
     * @throws FuscardProjectException If there is a project configuration
     * error.
     */
    private void saveProject() throws FuscardXMLException, FuscardProjectException {
        if (xMLDocument == null) {
            throw new FuscardProjectException("The project XML cannot be null.");
        }
        String rootElem = "jef_project";
        ExtensionInformation info = getExtensionInformation();
        xMLDocument.setFilename(getProjectConfigurationFile().toString());
        if (!xMLDocument.hasValidDocument()) {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                throw new FuscardXMLException(ex);
            }
            Document doc = db.newDocument();
            doc.appendChild(doc.createElement(rootElem));
            xMLDocument.setDocument(doc);
        }
        xMLDocument.setElementValue(new String[]{rootElem, app_name},
                info.getAppName());
        xMLDocument.setElementValue(new String[]{rootElem, author},
                info.getAuthor());
        xMLDocument.setElementValue(new String[]{rootElem, author_email},
                info.getAuthor_mail());
        xMLDocument.setElementValue(new String[]{rootElem, author_website},
                info.getAuthor_website());
        xMLDocument.setElementValue(new String[]{rootElem, copyright},
                info.getCopyright());
        xMLDocument.setElementValue(new String[]{rootElem, joomla_directory},
                info.getJoomlaDirectory());
        xMLDocument.setElementValue(new String[]{rootElem, license},
                info.getLicense());
        xMLDocument.setElementValue(new String[]{rootElem, summary},
                info.getSummary());
        xMLDocument.setElementValue(new String[]{rootElem, version},
                info.getVersion());
        xMLDocument.setElementValue(new String[]{rootElem, release_index},
                info.getReleaseIndex());
        xMLDocument.saveToFile();
    }

    /**
     * @return the projectType
     */
    public ProjectType getProjectType() {
        return projectType;
    }

    /**
     * @param projectType the projectType to set
     */
    public void setProjectType(
            ProjectType projectType) {
        this.projectType = projectType;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the updateServer
     */
    public Path getUpdateServer() {
        return updateServer;
    }

    /**
     * @param updateServer the updateServer to set
     */
    public void setUpdateServer(Path updateServer) {
        this.updateServer = updateServer;
    }

    /**
     * @return the extensionInformation
     */
    public ExtensionInformation getExtensionInformation() {
        return extensionInformation;
    }

    /**
     * @param extensionInformation the extensionInformation to set
     */
    public void setExtensionInformation(
            ExtensionInformation extensionInformation) {
        this.extensionInformation = extensionInformation;
    }

    /**
     * @return the saved
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * @return whether this is a the main project
     */
    public boolean isMain() {
        return main;
    }

    /**
     * @param main whether this is the main project
     */
    public void setMain(boolean main) {
        if (main) {
            makeMain();
        } else {
            this.main = false;
        }
    }

    private void makeMain() {
        for (ProjectHook ph : projectHooks) {
            if (!ph.beforeSettingMainProject(this)) {
                return;
            }
        }
        this.main = true;
        for (ProjectListener pl : projectListeners) {
            pl.projectSetMain(this);
        }
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
        return closed;
    }

    public static enum ProjectType {

        Component,
        Module,
        Template
    }

    /**
     * This method tells whether the argument project refers to the same project
     * as this one.
     *
     * @param project The project to test.
     * @return Whether this and the argument are the same project.
     */
    public boolean sameProject(Project project) {
        Path path1 = project.getProjectConfigurationFile();
        if (path1 == null) {
            return false;
        }
        return path1.equals(getProjectConfigurationFile());
    }

    /**
     * Interface for things that want to manage the project state.
     */
    public static interface ProjectHook {

        /**
         * The project will only close if calling {@link #beforeProjectCloses(factory.codegen.app.Project)
         * }
         * on all the installed ProjectHook's return true.
         *
         * @param project The project that wants to close.
         * @return whether this project closing hook is clear to close project.
         */
        public boolean beforeProjectCloses(Project project);

        /**
         * Implement this method to install a method to clear the project to
         * open. The project will only open if calling {@link #beforeProjectOpens(factory.codegen.app.Project)
         * }
         * on all the installed ProjectHook's return true.
         *
         * @param project The project that is opening.
         * @return Whether the project is cleared to open.
         */
        public boolean beforeProjectOpens(Project project);

        /**
         * Implement to install a method to clear the project to save. The
         * project will only save its information if calling {@link #beforeProjectSaves(factory.codegen.app.Project)
         * }
         * on all the installed ProjectHook's return true.
         *
         * @param project The project to save.
         * @return Whether the project is cleared to save.
         */
        public boolean beforeProjectSaves(Project project);

        /**
         * Implement to install a method to clear the project to build. The
         * project will only build if calling {@link #beforeProjectBuilds(factory.codegen.app.Project)
         * }
         * on all the installed ProjectHook's return true.
         *
         * @param project The project to build.
         * @return Whether the project is cleared to build.
         */
        public boolean beforeProjectBuilds(Project project);

        /**
         * Implement to install a method to clear the project to set itself as
         * main project in the IDE. The project will only set itself become main
         * if calling {@link #beforeSettingMainProject(factory.codegen.app.Project)
         * }
         * on all the installed ProjectHook's return true. It would make sense
         * to implement this method to unset main project in the IDE before
         * setting another main project.
         *
         * @param project The project to set main.
         * @return Whether the project is cleared to become main project.
         */
        public boolean beforeSettingMainProject(Project project);
    }

    /**
     * Objects to be notified of project states.
     */
    public static interface ProjectListener {

        /**
         * Implement to handle project closed events.
         *
         * @param project The project that has successfully closed.
         */
        public void projectClosed(Project project);

        /**
         * Implement to handle project opened events.
         *
         * @param project The project that has successfully opened.
         */
        public void projectOpened(Project project);

        /**
         * Implement to handle project saved events.
         *
         * @param project The project that has been successfully saved.
         */
        public void projectSaved(Project project);

        /**
         * Implement to handle project build events.
         *
         * @param project The project that has been successfully built.
         */
        public void projectBuilt(Project project);

        /**
         * Implement to listen to when the project becomes a main project.
         *
         * @param project The project that has become a main project.
         */
        public void projectSetMain(Project project);
    }

    /**
     * ProjectListerner adapter
     */
    public static abstract class DefaultProjectListener implements ProjectListener {

        @Override
        public void projectClosed(Project project) {
        }

        @Override
        public void projectOpened(Project project) {
        }

        @Override
        public void projectSaved(Project project) {
        }

        @Override
        public void projectBuilt(Project project) {
        }

        @Override
        public void projectSetMain(Project project) {
        }
    }

    /**
     * ProjectHook adapter.
     */
    public static abstract class DefaultProjectHook implements ProjectHook {

        @Override
        public boolean beforeProjectCloses(Project project) {
            return true;
        }

        @Override
        public boolean beforeProjectOpens(Project project) {
            return true;
        }

        @Override
        public boolean beforeProjectSaves(Project project) {
            return true;
        }

        @Override
        public boolean beforeProjectBuilds(Project project) {
            return true;
        }

        @Override
        public boolean beforeSettingMainProject(Project project) {
            return true;
        }
    }
}
