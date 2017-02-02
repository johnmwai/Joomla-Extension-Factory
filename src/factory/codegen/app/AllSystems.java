/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factory.codegen.app;

import com.fuscard.commons.*;
import factory.Application;
import factory.codegen.CodeGeneraror;
import factory.codegen.ExtensionInformation;
import factory.codegen.CodeGeneratorException;
import factory.codegen.InterfaceJoomlaArtifacts;
import static factory.codegen.InterfaceJoomlaArtifacts.INT_SQL_FILE;
import factory.codegen.JEFDatabaseTable;
import factory.design.element.Form;
import factory.design.element.Table;
import factory.design.element.Text;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import javax.swing.tree.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author John Mwai
 */
public class AllSystems extends javax.swing.JFrame implements InterfaceJoomlaArtifacts {

    private static Theme[] themes = {
        new Theme("Secret  Black & Red",
        Color.red.brighter(),
        Color.black),
        new Theme("Clarity Black & White", Color.black,
        Color.white),
        new Theme("Clarity White & Black", Color.white,
        Color.black),
        new Theme("Clarity Yellow & Black", Color.yellow,
        Color.black),
        new Theme("Secret  Black & Green",
        Color.green,
        Color.black),
        new Theme("Crystal Gray & Cyan", Color.GRAY,
        Color.cyan.brighter()),
        new Theme("Crimson Red & White", Color.red,
        Color.white),
        new Theme("Melancholy Blue & Gray",
        Color.blue.darker(),
        new Color(0xffeeeeee)),
        new Theme("Covert White & Red",
        Color.white,
        Color.red),
        new Theme("Spirit Yellow & Red",
        Color.yellow,
        Color.red),
        new Theme("Passion Blue & Pink", new Color(0, 102, 102),
        Color.pink),
        new Theme("Cheer   Gray and Orange",
        Color.GRAY,
        Color.orange.brighter()),
        new Theme("Serendipity   Gray and Orange",
        Color.GRAY,
        Color.orange),
        new Theme("Enchanted   Orange and Green",
        Color.orange.darker(),
        Color.green.brighter()),
        new Theme("Enchanted   Red and Green",
        Color.red.darker(),
        Color.green.darker()),};
    private Theme currentTheme;
    private FuscardLogo fuscardLogo;

    private void createThemeMenus(Theme current) {
        ButtonGroup bg = new ButtonGroup();
        for (final Theme t : themes) {
            JRadioButtonMenuItem rbmi = new JRadioButtonMenuItem(t.getNameHTML());
            if (t == current) {
                rbmi.setSelected(true);
            }
            rbmi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setThemeColors(t);
                }
            });
            bg.add(rbmi);
            jMenu5.add(rbmi);
        }
    }

    private void setThemeColors(Theme theme) {
        for (Component c : getDeclaredComponents()) {
            Application.setColors(c, theme);
        }
        Application.setColors(this.getRootPane(), theme);
        Application.setColors(getFuscardLogo().getRootPane().getRootPane(),
                theme);
        currentTheme = theme;
        try {
            Application.getInstance().setValue(THEME, theme.getName());
        } catch (FuscardXMLException ex) {
            Logger.getLogger(AllSystems.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }
    private ScrollingTabbedPane tabbedPane = new ScrollingTabbedPane();
    private CodeGeneraror codeGeneraror = new CodeGeneraror();
    private SimpleAttributeSet _infor = new SimpleAttributeSet();
    private SimpleAttributeSet _error = new SimpleAttributeSet();
    private SimpleAttributeSet _warning = new SimpleAttributeSet();
    private SimpleAttributeSet _success = new SimpleAttributeSet();
    private ImageIcon html, js, xml, php, image, unknown, sql, java_icon, directory, text, parent_directory;
    public static final String pathToIcons = "images/";
    private Path icons = Paths.get(pathToIcons);
    private final Dimension smallIconDim = new Dimension(-1, 15);
    private final Dimension bigIconDim = new Dimension(-1, 18);
    private final TreeMouseAdapter myTreeMouseAdapter = new TreeMouseAdapter();
    private final MyTreeSelectionAdapter myTreeSelectionListener = new MyTreeSelectionAdapter();

    public static class ClickListener extends MouseAdapter implements ActionListener {

        private final static int clickInterval = (Integer) Toolkit.getDefaultToolkit().
                getDesktopProperty("awt.multiClickInterval");
        private MouseEvent lastEvent;
        private Timer timer;

        public ClickListener() {
            this(clickInterval);
        }

        public ClickListener(int delay) {
            timer = new Timer(delay, this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int button = e.getButton();
            if (e.getClickCount() > 2) {
                return;
            }
            lastEvent = e;
            if (timer.isRunning()) {
                timer.stop();
                doubleClick(lastEvent);
            } else {
                timer.restart();
            }
        }

        public void actionPerformed(ActionEvent e) {
            timer.stop();
            singleClick(lastEvent);
        }

        public void singleClick(MouseEvent e) {
        }

        public void doubleClick(MouseEvent e) {
        }
    }
    private static final String CLOSE_PROJECTS_ACTION = "close projects";
    private static final String MAKE_MAIN_PROJECT_ACTION = "make main project";
    private static final String SAVE_PROJECT_ACTION = "save project";
    private static final String SAVE_ALL_PROJECTS_ACTION = "save all projects";

    private class PopUpActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case CLOSE_PROJECTS_ACTION:
                    //if multiple projects
                    if (projectsToClose != null) {
                        infor("Closing " + projectsToClose.length + "projects.");
                        closeProjects(projectsToClose);
                    } else {
                        infor("Closing " + selectedProject.getProjectName());
                        selectedProject.close();
                    }

                    break;
                case MAKE_MAIN_PROJECT_ACTION:
                    infor("Making '" + selectedProject.getProjectName() + "' main project");
                    selectedProject.setMain(true);
                    break;
                case SAVE_PROJECT_ACTION:
                    infor("Saving " + selectedProject.getProjectName());
                    try {
                        selectedProject.save();
                    } catch (FuscardProjectException | FuscardXMLException ex) {
                        error(ex.getMessage());
                    }
                    break;
                case SAVE_ALL_PROJECTS_ACTION:
                    infor("Saving all projects");
                    for (Project project : projects) {
                        try {
                            project.save();
                        } catch (FuscardProjectException | FuscardXMLException ex) {
                            error(ex.getMessage());
                        }
                    }
                    break;
            }
            jTreeProject.repaint();
        }
    }
    private PopUpActionListener popUpActionListener = new PopUpActionListener();
    private Project selectedProject;
    private Project[] projectsToClose = null;

    private class TreeMouseAdapter extends ClickListener {

        JPopupMenu popup = new JPopupMenu();

        @Override
        public void doubleClick(MouseEvent e) {
            if (e.getSource() == jTreeProject) {
                Point p = e.getPoint();
                TreePath treePath = jTreeProject.getPathForLocation(p.x, p.y);
                handleTreeDoubleClick(jTreeProject, treePath);
                saveOpenFiles();
            }
        }

        @Override
        public void singleClick(MouseEvent e) {
            //If the tree was right-clicked
            if (e.getSource() == jTreeProject && e.getButton() == MouseEvent.BUTTON3) {
                Point p = e.getPoint();
                TreePath treePath = jTreeProject.getPathForLocation(p.x, p.y);
                TreePath[] selectedPaths = jTreeProject.getSelectionPaths();
                boolean multipleProjectsSelected = false;
                LinkedList<Project> projs = new LinkedList<>();
                if (selectedPaths != null) {
                    for (TreePath path : selectedPaths) {
                        Object obj = path.getLastPathComponent();
                        if (obj instanceof DefaultMutableTreeNode) {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
                            Object userObject = node.getUserObject();
                            if (userObject instanceof Project) {
                                Project prj = (Project) userObject;
                                projs.add(prj);
                                if (projs.size() > 1) {
                                    multipleProjectsSelected = true;
                                }
                            } else {
                                multipleProjectsSelected = false;
                                break;
                            }
                        }
                    }
                }
                if (multipleProjectsSelected) {
                    projectsToClose = projs.toArray(new Project[0]);
                } else {
                    projectsToClose = null;
                }
//Create a context menu for the project
                popup = new JPopupMenu();

                JMenuItem menuItem;
                if (treePath != null) {
                    Object obj = treePath.getLastPathComponent();
                    if (obj instanceof DefaultMutableTreeNode) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
                        Object userObject = node.getUserObject();
                        if (userObject instanceof Project) {
                            Project proj = (Project) userObject;
                            selectedProject = proj;
                            menuItem = new JMenuItem(
                                    "Make '" + proj.getProjectName() + "' default");
                            menuItem.setActionCommand(MAKE_MAIN_PROJECT_ACTION);
                            menuItem.addActionListener(popUpActionListener);
                            popup.add(menuItem);

                            menuItem = new JMenuItem(
                                    "Save '" + proj.getProjectName() + "'");
                            menuItem.setActionCommand(SAVE_PROJECT_ACTION);
                            menuItem.addActionListener(popUpActionListener);
                            popup.add(menuItem);
                            menuItem = new JMenuItem(
                                    "Save all");
                            menuItem.setActionCommand(SAVE_ALL_PROJECTS_ACTION);
                            menuItem.addActionListener(popUpActionListener);
                            popup.add(menuItem);
                            if (multipleProjectsSelected) {
                                menuItem = new JMenuItem(
                                        "Close (" + projs.size() + ") project");
                            } else {
                                menuItem = new JMenuItem(
                                        "Close '" + proj.getProjectName() + "'");
                            }
                            menuItem.setActionCommand(CLOSE_PROJECTS_ACTION);
                            menuItem.addActionListener(popUpActionListener);
                            popup.add(menuItem);
                        }
                    }
                }
                showPopUp(e);
            }
        }

        private void showPopUp(MouseEvent e) {

            popup.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    }
    private static final String OPEN_FILES = "open_files.file";
    private static final String OPEN_PROJECTS = "open_projects.project";

    private void saveOpenFiles() {
        //Save the open files
        LinkedList<String> files = new LinkedList<>();
        for (Iterator<FileHolder> it = tabbedPane.getFileHolders().values().iterator();
                it.hasNext();) {
            FileHolder ffh = it.next();
            String path = ffh.file.toString();
            if ("".equals(path)) {
                continue;
            }
            files.add(path);
        }
        try {
            Application.getInstance().setValues(OPEN_FILES,
                    files.toArray(new String[0]));
        } catch (FuscardXMLException ignore) {
        }
        saveSelectedFile();
    }
    private LinkedList<String> recentProjects = new LinkedList<>();
    private static final String RECENT_PROJECTS = "recent_projects.project";

    private void saveRecentProjects() {
        infor("Committing recent projects");
        try {
            Application.getInstance().setValues(RECENT_PROJECTS,
                    recentProjects.toArray(new String[0]));
        } catch (FuscardXMLException ex) {
            Logger.getLogger(AllSystems.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    private void initiateRecentProjects() {
        infor("Initiating recent projects");
        recentProjects = new LinkedList<>(Arrays.asList(
                Application.getInstance().getValues(RECENT_PROJECTS)));
        infor("There are " + recentProjects.size() + " recent projects");
        for (String path : new LinkedList<>(recentProjects)) {
            Path thePath = Paths.get(path);
            if (!Files.isRegularFile(thePath)) {
                error(thePath + " is not a regular file");
                recentProjects.remove(path);
            }
        }
        infor("After removing invalid files there are " + recentProjects.size() + " files in the recent projects");
        saveRecentProjects();
        setRecentProjectMenuItems();
    }

    private void addToRecentProjects(Project project) {
        String path = project.getProjectConfigurationFile().toString();
        removeFromRecentProjects(path);
        recentProjects.add(0, path);
        while (recentProjects.size() > 10) {
            recentProjects.removeLast();
        }
        saveRecentProjects();
        setRecentProjectMenuItems();
    }

    private void removeFromRecentProjects(Project project) {
        removeFromRecentProjects(
                project.getProjectConfigurationFile().toString());
    }

    private void removeFromRecentProjects(String path) {
        Path thePath = Paths.get(path);
        LinkedList<String> kl = new LinkedList<>();
        for (String string : recentProjects) {
            Path other = Paths.get(string);
            if (thePath.equals(other)) {
                kl.add(string);
            }
        }
        recentProjects.removeAll(kl);
        saveRecentProjects();
        setRecentProjectMenuItems();
    }

    private void setRecentProjectMenuItems() {
        jMenuRecentProjects.removeAll();

        for (final String s : recentProjects) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Project project = new Project();
                    project.setProjectConfigurationFile(Paths.get(s));
                    bootStrapProject(project);
                }
            });
            jMenuRecentProjects.add(item);
        }
    }

    private Project getMainProject() {
        for (Project project : projects) {
            if (project.isMain()) {
                return project;
            }
        }
        return null;
    }

    private void saveSelectedFile() {
        //Save the selected file
        int selectedIndex = tabbedPane.getTabbedPane().getSelectedIndex();
        if (selectedIndex == -1) {
            try {
                Application.getInstance().setValue(SELECTED_FILE,
                        "");
            } catch (FuscardXMLException ex) {
            }
            return;
        }
        ButtonTabComponent buttonTabComponent = (ButtonTabComponent) tabbedPane.getTabbedPane().getTabComponentAt(
                selectedIndex);
        FileHolder fileHolder = buttonTabComponent.getFileHolder();
        if (fileHolder != null) {
            try {
                Application.getInstance().setValue(SELECTED_FILE,
                        fileHolder.file.toString());
            } catch (FuscardXMLException ignore) {
            }
        }
    }
    private static final String SELECTED_FILE = "selected_file";

    private class MyTreeSelectionAdapter implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
//            debug("Tree Selection Listener Value Changed Event handler");
//            debug("----------------------------------------------------");
//            debug("New lead selection path: " + e.getNewLeadSelectionPath());
//            debug("Old lead selection path: " + e.getOldLeadSelectionPath());
//            debug("Path: " + e.getPath());
//            debug("Is added path: " + e.isAddedPath());
//            debug("////////////////////////////////////////////////////");
//            debug("");
        }
    }

    /**
     * Creates new form AllSystems
     */
    public AllSystems() {
        debug("Start");
        initComponents();
//        GUIUtils.modifyAttributeSet(_infor, GUIUtils.TextAttributes.ForeGround,
//                Color.GRAY);
        GUIUtils.modifyAttributeSet(_warning, GUIUtils.TextAttributes.ForeGround,
                Color.ORANGE);
        GUIUtils.modifyAttributeSet(_error, GUIUtils.TextAttributes.ForeGround,
                Color.RED);
        GUIUtils.modifyAttributeSet(_success, GUIUtils.TextAttributes.ForeGround,
                Color.GREEN.darker());
        refreshContext();
        final JTabbedPane tp = tabbedPane.getTabbedPane();
        tp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int div = jSplitPane1.getDividerLocation();
                if (tp.getTabCount() == 0) {
                    jSplitPane1.setRightComponent(jPanel12);
                } else {
                    jSplitPane1.setRightComponent(tp);
                }
                jSplitPane1.setDividerLocation(div);
            }
        });
        tp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveSelectedFile();
            }
        });



        jTreeProject.setCellRenderer(new MyTreeCellRenderer());


        refreshIcons();
        jSplitPaneTools.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setToolMinimumSize();
            }
        });
        jSplitPaneTools.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case JSplitPane.DIVIDER_LOCATION_PROPERTY:
                    case JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY:
                        setToolMinimumSize();
                }
            }
        });

        jTreeProject.addTreeSelectionListener(myTreeSelectionListener);
        jTreeProject.addMouseListener(myTreeMouseAdapter);

        //Enable tool tips
        ToolTipManager.sharedInstance().registerComponent(jTreeProject);

        ParserFactory.initialize();
        Theme t = getSavedTheme();
        createThemeMenus(t);
        setThemeColors(t);
        initDialogs(t);
        //
        initiateRecentProjects();
        openFiles();
        showProjectTree();
        showSelectedFile();
        //Set the drawing manager
        prepareDrawingManager();
        //Maximize window
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        jTableJoomlaInfo.setDefaultRenderer(String.class,
                new JoomlaInfoTableCellRenderer());
        debug("Finish");
    }

    private static class JoomlaInfoTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
            int cc = table.convertColumnIndexToModel(column);
            if (cc == 5) {
                setText("****");
            } else {
                setText(value.toString());
            }
            return this;
        }
    }

    private void prepareDrawingManager() {
        DrawingManager dm = new DrawingManagerBuilder()
                .setDrawinIcon(image)
                .setAllSystems(this)
                .createDrawingManager();
        dm.setQuickOutputArea(jTextFieldDMOutputArea);
        Application.getInstance().setDrawingManager(dm);
        jFormattedTextFieldCanvasPreferredHeight.setText(
                "" + dm.getPreferredCanvasHeight());
        jFormattedTextFieldCanvasPreferredWidth.setText(
                "" + dm.getPreferredCanvasWidth());
        jTextFieldCanvasColor.setText("0x" + Integer.toHexString(
                dm.getCanvasColor().getRGB()));
        jTextFieldCanvasColor.setBackground(dm.getCanvasColor());
    }
    private static final String THEME = "theme";

    private Theme getSavedTheme() {
        Theme t = currentTheme == null ? themes[1] : currentTheme;
        String s;
        s = Application.getInstance().getValue(THEME);
        if (s == null) {
            return t;
        }
        for (Theme tt : themes) {
            if (s.equals(tt.getName())) {
                return tt;
            }
        }
        return t;
    }

    private static class NoThemeTextField extends JTextField implements Application.NoTheme {
    }

    private void initDialogs(final Theme theme) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                colors = JDialogColorCodeEditor.getNewInstance(
                        AllSystems.this, theme);
                System.out.println(""
                        + "creating about dialog");
                aboutDialog = AboutDialog.createNewInstance(AllSystems.this);
            }
        }).start();
        System.out.println("After init dialogs");
    }

    private void refreshIcons() {
        debug("before set icons");
        html = createImageIcon(icons.resolve("html.png"), smallIconDim);
        js = createImageIcon(icons.resolve("js.png"), smallIconDim);
        xml = createImageIcon(icons.resolve("xml.png"), smallIconDim);
        php = createImageIcon(icons.resolve("php.png"), smallIconDim);
        image = createImageIcon(icons.resolve("image.png"), smallIconDim);
        unknown = createImageIcon(icons.resolve("unknown.png"), smallIconDim);
        sql = createImageIcon(icons.resolve("sql.png"), smallIconDim);
        java_icon = createImageIcon(icons.resolve("java.png"), smallIconDim);
        text = createImageIcon(icons.resolve("text.png"), smallIconDim);

        directory = createImageIcon(icons.resolve("directory.png"), bigIconDim);
        parent_directory = createImageIcon(icons.resolve("parent_directory.png"),
                bigIconDim);
        jTreeProject.setRowHeight(20);
        debug("After set Icons");
        debug("before set icon image");
//        setIconImage(createImage(icons.resolve("jef.png"), bigIconDim));
        debug("After setIcon Image");
    }
    private static boolean _debug = true;

    private static void debug(Object mess) {
        if (_debug) {
            System.out.println(mess);
        }
    }
    private String name, type, component_name;
    private boolean isAdmin;
    private boolean isCreate;
    private Project projectToEdit;

    private boolean showInformationGetter(String type) {
        jComboBoxSelectProject.removeAllItems();
        for (Project p : projects) {
            jComboBoxSelectProject.addItem(p);
        }
        jDialogInformationGetter.setTitle("Create something cool");
        jDialogInformationGetter.setSize(
                jDialogInformationGetter.getPreferredSize());
        jDialogInformationGetter.setLocationRelativeTo(this);
        jComboBoxJoomlaArtifact.setSelectedItem(type);
        jDialogInformationGetter.pack();
        jDialogInformationGetter.setVisible(true);
        return isCreate;
    }

    private void add(String what) {
        try {
            if (!saveMainState()) {
                warning("Cannot add anything after failing to save");
                return;
            }
            if (!showInformationGetter(what)) {
                warning("Information retriever cancelled. Returning");
                return;
            }
            if (projectToEdit == null) {
                warning("Project not specified");
                return;
            }
            what = type;
            boolean success;
            ExtensionInformation info = projectToEdit.getExtensionInformation();
            switch (what) {
                case INT_CONTROLLER:
                    success = codeGeneraror.addController(name, isAdmin, info);
                    break;
                case INT_MODEL:
                    success = codeGeneraror.addModel(name, isAdmin, info);
                    break;
                case INT_VIEW:
                    success = codeGeneraror.addView(name, isAdmin, info);
                    break;
                default:
                    error("Creation of " + what + " is not yet supported");
                    return;
            }
            if (success) {
                success(what + " '" + name + "' added successfully");
                refreshProjectsTree();
            } else {
                error(what + " '" + name + "' could not be added");
            }

        } catch (CodeGeneratorException ex) {
            error(ex.getMessage());
        }
    }

    private Image getWindowIcon() {
        debug(icons);
        Image img = createImage(icons.resolve("jef.png"), null);
        debug(img);
        return img;
    }

    private void open(FileHolder fh) {
        Path path = fh.file;
        if (Files.isRegularFile(path)) {
            infor("The file is a regular file. We will try to open it");
            File f = path.toFile();
            String ext = FileUtils.getExtension(f);
            ImageIcon img = getIconForExtension(
                    ext, path);
            if (ext == null) {
                error("The file has an invalid extension. We are not going to open that.");
                return;
            }
            switch (ext) {
                case "xml":
                case "php":
                case "html":
                case "htm":
                case "css":
                case "js":
                case "java":
                case "sql":
                case "txt":
                    infor("opening text file of type : " + ext);
                    TextFileTab tft = new TextFileTab(path, img,
                            fh);
                    tft.open();
                    return;
                case "png":
                case "jpg":
                case "jpeg":
                case "gif":
                case "bmp":
                case "tif":
                    infor("Opening image file of type : " + ext);
                    ImageFileTab ift = new ImageFileTab(path,
                            img, fh);
                    ift.open();
                    return;
                default:
                    error("The file type is not supported");
            }
        }
    }

    private void handleTreeDoubleClick(JTree tree, TreePath treePath) {
        if (tree == jTreeProject) {
            infor("Trying to open file");
            if (treePath != null) {
                Object obj = treePath.getLastPathComponent();
                if (obj instanceof DefaultMutableTreeNode) {
                    infor("Object is a default mutable tree node");
                    Object obj1 = ((DefaultMutableTreeNode) obj).getUserObject();
                    if (obj1 instanceof FileHolder) {
                        infor("The user object is a file holder.");
                        FileHolder fh = ((FileHolder) obj1);
                        Path path = fh.file;
                        //Check whether the file is already open
                        if (fh.isOpen) {
                            infor("The file is already open");
                            JTabbedPane tp = tabbedPane.getTabbedPane();
                            int index = tp.indexOfComponent(fh.component);
                            if (index != -1) {
                                tp.setSelectedIndex(index);
                            }
                            return;
                        }
                        open(fh);
                    } else if (obj1 instanceof JEFDatabaseTable) {
                        showjDialogDatabaseTableEditor((JEFDatabaseTable) obj1);
                    }
                }
            }
        }
    }

    private static class FieldListComboBoxModel extends DefaultComboBoxModel<String> {

        private FieldListComboBoxModel(String[] items) {
            super(items);
        }

        private static FieldListComboBoxModel createInstance() {
            Field[] fields = FieldTypes.class.getFields();
            ArrayList<String> names = new ArrayList<>();
            for (Field field : fields) {
                if (field.getType() == String.class) {
                    try {
                        names.add((String) field.get(FieldTypes.class));
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(AllSystems.class.getName()).log(
                                Level.SEVERE,
                                null, ex);
                    }
                }
            }
            return new FieldListComboBoxModel(names.toArray(new String[0]));
        }
    }

    private class FileTab {

        Path filePath;
        ImageIcon icon;
        FileHolder holder;
        JComponent component;

        public FileTab(Path filePath, ImageIcon icon, FileHolder holder) {
            this.filePath = filePath;
            this.icon = icon;
            this.holder = holder;
        }

        public FileTab() {
        }

        void open() {
            holder.component = component;
            final ButtonTabComponent tab = tabbedPane.addCloseableTab(
                    filePath.getFileName().toString(), icon,
                    component);
            tab.setFileholder(holder);
            tab.addButtonClosedListener(
                    new ButtonTabComponent.ButtonClosedListener() {
                @Override
                public void tabClosed() {
                    saveOpenFiles();
                }
            });
            tabbedPane.showTabForTabbedComponent(tab);
            holder.isOpen = true;
            Application.setColors(tabbedPane.getTabbedPane(), currentTheme);
            success("File opened successfully");
        }
    }

    public static class CustomEditorKit extends StyledEditorKit {

        @Override
        public ViewFactory getViewFactory() {
            return new CustomUI();
        }
    }

    public static class CustomUI extends BasicTextPaneUI {

        @Override
        public View create(Element elem) {
            View result = null;
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        result = new MyLabelView(elem);
                        break;
                    case AbstractDocument.ParagraphElementName:
                        result = new ParagraphView(elem);
                        break;
                    case AbstractDocument.SectionElementName:
                        result = new BoxView(elem, View.Y_AXIS);
                        break;
                    case StyleConstants.ComponentElementName:
                        result = new ComponentView(elem);
                        break;
                    case StyleConstants.IconElementName:
                        result = new IconView(elem);
                        break;
                    default:
                        result = new LabelView(elem);
                        break;
                }
            } else {
                result = super.create(elem);
            }

            return result;
        }
    }

    public static class MyLabelView extends LabelView {

        public MyLabelView(Element arg0) {
            super(arg0);
        }

        @Override
        public void paint(Graphics g, Shape a) {
            super.paint(g, a);
            //Do whatever other painting here;
            Color c = (Color) getElement().getAttributes().getAttribute(
                    "Underline-Color");
            if (c != null) {
                int y = a.getBounds().y + (int) getGlyphPainter().getAscent(this);
                int x1 = a.getBounds().x;
                int x2 = a.getBounds().width + x1;
                float dash1[] = {10.0f, 1.0f};
                BasicStroke dashed = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(dashed);
                g.setColor(c);
                g.drawLine(x1, y, x2, y);
            }

        }
    }
    private final Object fileActivityLock = new Object();

//    private boolean isOpeningFiles() {
//        synchronized (fileActivityLock) {
//            if (!openingFiles.isEmpty()) {
//                return true;
//            }
//        }
//        return false;
//    }
//    private void checkFileActivity() throws InterruptedException {
//        synchronized (fileActivityLock) {
//            if (isOpeningFiles()) {
//                fileActivityLock.wait();
//            } else {
//            }
//        }
//    }
//    private void openingFile(File f) {
//        synchronized (fileActivityLock) {
//           openingFiles.add(f);
//        }
//    }
//
//    private void openedFile(File file) {
//        synchronized (fileActivityLock) {
//            openingFiles.remove(file);
//            fileActivityLock.notifyAll();
//        }
//    }
    private class TextFileTab extends FileTab {

        public TextFileTab(Path filePath, ImageIcon icon, FileHolder holder) {
            super(filePath, icon, holder);
        }

        public TextFileTab() {
        }

        @Override
        void open() {

            infor("Opening text file");
            final JTextPane pane = new JTextPane();
            pane.setEditorKit(new CustomEditorKit());
//            pane.setEditable(false);
            pane.setBackground(currentTheme.getBg());
            final JScrollPane scrollPane = new JScrollPane(pane);

            infor("Adding tab");


            try {
                pane.read(new FileReader(filePath.toFile()),
                        filePath.getFileName());
                File f = filePath.toFile();
                String ext = FileUtils.getExtension(f);
                final String type;
                switch (ext) {
                    case "css":
                        type = ParserFactory.PF_CSS;
                        break;
                    case "xml":
                        type = ParserFactory.PF_XML;
                        break;
                    case "php":
                        type = ParserFactory.PF_PHP;
                        break;
                    case "html":
                    case "htm":
                        type = ParserFactory.PF_HTML;
                        break;
                    case "js":
                        type = ParserFactory.PF_JAVASCRIPT;
                        break;
                    case "sql":
                        type = ParserFactory.PF_SQL;
                        break;
                    case "java":
                        type = ParserFactory.PF_JAVA;
                        break;
                    default:
                        return;
                }
                try {
                    ParserFactory.parse(pane, type);
                } catch (JEFParserException ex) {
                    error("Unable to parse file: " + ex.getMessage());
                }
                pane.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        parse();
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        parse();
                    }

                    private void parse() {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ParserFactory.parse(pane, type);
                                } catch (JEFParserException ex) {
                                }

                            }
                        });

                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(AllSystems.class.getName()).log(
                        Level.SEVERE,
                        null, ex);
                error("Unable to load file: " + ex.getMessage());
            }
            TextLineNumber tln = new TextLineNumber(pane);
            tln.setForeground(Color.GRAY.brighter());
            scrollPane.setRowHeaderView(tln);
            component = scrollPane;
            super.open();
        }
    }

    private class ImageFileTab extends FileTab {

        public ImageFileTab(Path filePath, ImageIcon icon, FileHolder holder) {
            super(filePath, icon, holder);
        }

        public ImageFileTab() {
        }

        @Override
        void open() {
            infor("Opening image file");
            JLabel label = new JLabel(createImageIcon(filePath, new Dimension(
                    -1, -1)));
            infor("Adding tab");
            JScrollPane jsl = new JScrollPane(label);
            component = jsl;
            super.open();
        }
    }

    private void setToolMinimumSize() {

        Component top = jSplitPaneTools.getTopComponent(), bottom = jSplitPaneTools.
                getBottomComponent();
        Dimension d1 = top.getMinimumSize(), d2 = bottom.getMinimumSize();
        d1 = new Dimension(d1.width, d1.height + 10);
        d2 = new Dimension(d2.width, d2.height + 10);
        int dividerLocation = jSplitPaneTools.
                getDividerLocation();
        boolean topShowing = true, bottomShowing = true;
        int threshold = 20;
        if (dividerLocation < threshold) {
            topShowing = false;
        } else if (dividerLocation > (jSplitPaneTools.getHeight() - threshold)) {
            bottomShowing = false;
        }
        if (topShowing && bottomShowing) {
            Dimension d3 = new Dimension(50, d1.height + d2.height);
            jSplitPaneTools.setMinimumSize(d3);
        } else if (bottomShowing) {
            jSplitPaneTools.setMinimumSize(d2);
        } else if (topShowing) {
            jSplitPaneTools.setMinimumSize(d1);
        } else {
            jSplitPaneTools.setMinimumSize(new Dimension(50,
                    d1.height + d2.height));
        }
    }

    public static ImageIcon createImageIcon(Path path, Dimension d) {
        return new ImageIcon(createImage(path, d));
    }

    private static Image createImage(Path path, Dimension d) {
        debug("Creating icon image from: " + path.toFile());
        try {
            Image img = ImageIO.read(path.toFile());
            if (d != null) {
                img = img.getScaledInstance(d.width, d.height,
                        Image.SCALE_SMOOTH);
            }
            debug("Icon image created");
            return img;
        } catch (Exception ex) {
            Logger.getLogger(AllSystems.class.getName()).
                    log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void warning(String message) {
        GUIUtils.putStyledText(jTextPane1, message + "\n",
                _warning);
    }

    public void infor(String message) {
        GUIUtils.putStyledText(jTextPane1, message + "\n",
                _infor);
    }

    public void error(String message) {
        GUIUtils.putStyledText(jTextPane1, message + "\n",
                _error);
        Toolkit.getDefaultToolkit().beep();
    }

    public void success(String message) {
        GUIUtils.putStyledText(jTextPane1, message + "\n",
                _success);
    }

    private static class NullFileFilter implements
            DirectoryStream.Filter<Path> {

        @Override
        public boolean accept(Path file) {
            return true;
        }
    }

    private static class ProjectConfigurationFileFilter implements
            DirectoryStream.Filter<Path> {

        private static final String extension = "xml";
        private static final String XMLRootName = "JEFProject";
        private XMLDocument xmld;

        @Override
        public boolean accept(Path entry) throws IOException {
            if (!Files.isRegularFile(entry)) {
                return false;
            }
            File f = entry.toFile();
            if (!extension.equals(FileUtils.getExtension(f))) {
                return false;
            }
            xmld.setFilename(f.getPath());
            try {
                xmld.loadFromFile();
            } catch (FuscardXMLException ex) {
                return false;
            }
            String tagName;
            tagName = xmld.getDocument().getDocumentElement().getTagName();
            if (!XMLRootName.equalsIgnoreCase(tagName)) {
                return false;
            }
            return true;
        }
    }

    private static class FileExtensionFilter implements
            DirectoryStream.Filter<Path> {

        String extension;
        boolean directory = false;

        public FileExtensionFilter(String extension) {
            this.extension = extension.toLowerCase().trim();
        }

        public FileExtensionFilter(boolean directory) {
            this.directory = directory;
        }

        public FileExtensionFilter() {
        }

        @Override
        public boolean accept(Path file) throws IOException {
            if (directory) {
                return Files.isDirectory(file);
            } else {
                if (!Files.isRegularFile(file)) {
                    return false;
                }
                if (extension != null) {
                    File f = file.toFile();
                    if (!extension.equals(FileUtils.getExtension(f))) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private void addNodes(DefaultMutableTreeNode root, Path majorPath,
            String child) {
        Path minorPath = majorPath.resolve(child);
        if (!Files.isDirectory(minorPath)) {
            return;
        }
        //Add directories first then files
        addChildren(minorPath, root);
    }

    private ImageIcon getIconForExtension(String extension, Path path) {
        if (extension == null) {
            return unknown;
        }
        switch (extension) {
            case "xml":
                return xml;
            case "php":
                return php;
            case "java":
                return java_icon;
            case "html":
            case "htm":
                return html;
            case "js":
                return js;
            case "sql":
                return sql;
            case "txt":
            case "css":
                return text;
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":
            case "bmp":
            case "tif":
                Image i = createImage(path, smallIconDim);
                return new ImageIcon(i);
            default:
                return (unknown);
        }
    }

    private class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel,
                    expanded, leaf, row, hasFocus);


            if (!sel) {
                setOpaque(true);
                setBackground(jTreeProject.getBackground());
                setForeground(jTreeProject.getForeground());
            } else {
                setOpaque(false);
            }
            Object userobj = ((DefaultMutableTreeNode) value).
                    getUserObject();

            if (userobj instanceof FileHolder) {
                Path p = ((FileHolder) userobj).file;
                setToolTipText(p.toString());
                if (Files.isRegularFile(p)) {
                    File f = p.toFile();
                    String ext = FileUtils.getExtension(f);
                    setIcon(getIconForExtension(ext, p));
                }
            } else if (userobj instanceof Project) {
                Project project = (Project) userobj;
                if (!project.isSaved()) {
                    if (!sel) {
                        setForeground(Color.GREEN.darker());
                    }

                    setText(project.getProjectName() + " *");
                }

                if (!project.isSaved()) {
                    setToolTipText(
                            project.getProjectName() + " has unsaved changes");
                } else if (project.isMain()) {
                    setToolTipText(
                            project.getProjectName() + " is the main project");
                } else {
                    setToolTipText(null);
                }
            } else {
                setToolTipText(null);
            }
            if (userobj instanceof Project && ((Project) userobj).isMain()) {
                setFont(getFont().deriveFont(Font.BOLD));
            } else {
                setFont(getFont().deriveFont(Font.PLAIN));
            }
            return this;

        }
    }

    private FileHolder searchForFileHolderInTabs(Path forFile) {
        if (forFile == null) {
            return null;
        }
        for (FileHolder ffh : tabbedPane.getFileHolders().values()) {
            if (forFile.equals(ffh.file)) {
                return ffh;
            }
        }
        return null;
    }
    private LinkedList<TreePath> nodesToExpand = new LinkedList<>();

    private void openFiles() {
        String[] openFiles = Application.getInstance().getValues(
                OPEN_FILES);
        for (String f : openFiles) {
            Path file = Paths.get(f);
            FileHolder fh = new FileHolder();
            fh.file = file;
            fh.isOpen = false;
            fh.name = file.getFileName().toString();
            fh.component = null;
            open(fh);
        }
    }

    private void showSelectedFile() {
        String selected = Application.getInstance().getValue(SELECTED_FILE);
        if (selected != null) {
            //select the file
            Path path = Paths.get(selected);
            if (Files.isRegularFile(path)) {
                for (int i = 0; i < tabbedPane.getTabbedPane().getTabCount();
                        i++) {
                    ButtonTabComponent btc = (ButtonTabComponent) tabbedPane.getTabbedPane().getTabComponentAt(
                            i);
                    FileHolder fileHolder = btc.getFileHolder();
                    if (fileHolder != null && path.equals(fileHolder.file)) {
                        //select the tab
                        final int l = i;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                tabbedPane.getTabbedPane().setSelectedIndex(l);
                            }
                        });

                        return;
                    }
                }
            }

        }
    }

    private void addTypeOfFile(Path minorPath,
            DirectoryStream.Filter<Path> filter, DefaultMutableTreeNode root) {
        try (DirectoryStream<Path> stream = Files.
                newDirectoryStream(minorPath, filter)) {
            for (Path file : stream) {
                FileHolder fh;
                fh = searchForFileHolderInTabs(file);
                boolean wasThere = false;
                if (fh == null) {
                    fh = new FileHolder();
                    fh.file = file;
                    fh.isOpen = false;
                    fh.name = file.getFileName().toString();
                    fh.component = null;
                } else {
                    wasThere = true;
                }
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                        fh);
                root.add(node);
                if (wasThere) {
                    //If the node was open expand the tree to show the node in the tree
                    nodesToExpand.add(new TreePath(node.getPath()));
                }
                addNodes(node, minorPath, file.getFileName().toString());
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }
    }

    private void addChildren(Path minorPath, DefaultMutableTreeNode root) {
        addTypeOfFile(minorPath, directoryFilter, root);
        addTypeOfFile(minorPath, fileFilter, root);
    }

    private void createMajorNode(DefaultMutableTreeNode projectNode,
            String nodeName, Path majorPath) {
        DefaultMutableTreeNode majorNode = new DefaultMutableTreeNode(
                nodeName);
        projectNode.add(majorNode);
        addChildren(majorPath, majorNode);
    }
    private static DirectoryStream.Filter<Path> directoryFilter = new FileExtensionFilter(
            true);
    private static DirectoryStream.Filter<Path> projectConfigFilter = new ProjectConfigurationFileFilter();
    private static DirectoryStream.Filter<Path> phpFileFilter = new FileExtensionFilter(
            "php");
    private static DirectoryStream.Filter<Path> nullFilter = new NullFileFilter();
    private static DirectoryStream.Filter<Path> fileFilter = new FileExtensionFilter();
    private LinkedList<Project> projects = new LinkedList<>();

    private void createProjectsTree() {
        infor("Creating projects tree");
        nodesToExpand.clear();

        DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(
                "JEF Projects");
        jTreeProject.setRootVisible(true);
        jTreeProject.setModel(new DefaultTreeModel(projectNode));
        putOpenProjects();
    }

    private void refreshProjectsTree() {
        DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(
                "JEF Projects");
        jTreeProject.setRootVisible(true);
        jTreeProject.setModel(new DefaultTreeModel(projectNode));
        for (Project project : projects) {
            try {
                createProjectTree(project);
            } catch (FuscardProjectException ex) {
                Logger.getLogger(AllSystems.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }
//
//    private void removeClosedProjects() {
//        infor("Removing closed projects.");
//        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) jTreeProject.getModel();
//        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
//        LinkedList<DefaultMutableTreeNode> nodesToRemove = new LinkedList<>();
//        for (int i = 0; i < rootNode.getChildCount(); i++) {
//            DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(
//                    i);
//            if (child.getUserObject() instanceof Project) {
//                Project project1 = (Project) child.getUserObject();
//                Object value = project1.get(FUTURE_PROJECT_CLOSE_MARKER);
//                boolean to_be_closed = value == null ? false : (boolean) value;
//                if (to_be_closed || project1.isClosed()) {
//                    if (!nodesToRemove.contains(child)) {
//                        nodesToRemove.add(child);
//                    }
//                    if (!project1.isClosed()) {
//                        project1.close();
//                    }
//                }
//            }
//        }
//        for (DefaultMutableTreeNode node : nodesToRemove) {
//            if (rootNode.isNodeChild(node)) {
//                rootNode.remove(node);
//            }
//        }
//        for (Project p : projects) {
//            Object value = p.get(FUTURE_PROJECT_CLOSE_MARKER);
//            boolean to_be_closed = value == null ? false : (boolean) value;
//            if (to_be_closed && !p.isClosed()) {
//                p.close();
//            }
//        }
//        LinkedList<Project> kl = new LinkedList<>();
//        for (Project p : projects) {
//            if (p.isClosed()) {
//                kl.add(p);
//            }
//        }
//        error("Removing " + kl.size() + " projects.");
//        projects.removeAll(kl);
//        defaultTreeModel.reload(rootNode);
//
//    }
//    
    private static final Object FUTURE_PROJECT_CLOSE_MARKER = new Object();
    private static final Object DEFAULT_MUTABLE_NODE_PAYLAOD = new Object();

    private void removeProjectNodes(Project project) {
        infor("Removing prior project nodes for " + project);
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) jTreeProject.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
        //Look for the project Node
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(
                    i);
            if (child.getUserObject() instanceof Project) {
                Project project1 = (Project) child.getUserObject();
                if (project.sameProject(project1) || project1.getProjectConfigurationFile() == null) {
                    //If the project Node is already there
                    warning(project1 + " will be closed courtesy of " + project);
                    project1.set(FUTURE_PROJECT_CLOSE_MARKER, true);
                    project1.set(DEFAULT_MUTABLE_NODE_PAYLAOD, child);
                }
            }
        }

        for (Project proj : projects) {
            Object value = proj.get(FUTURE_PROJECT_CLOSE_MARKER);
            boolean to_be_closed = value == null ? false : (boolean) value;
            if (to_be_closed) {
                Object payload = proj.get(DEFAULT_MUTABLE_NODE_PAYLAOD);
                DefaultMutableTreeNode node = payload == null ? null : (DefaultMutableTreeNode) payload;
                if (node != null) {
                    rootNode.remove(node);
                }
                if (proj != project) {
                    proj.close();
                }
            }
        }
        defaultTreeModel.reload(rootNode);
    }

    private void createProjectTree(Project project) throws FuscardProjectException {
        infor("Creating project tree for " + project);
        //We do not want to mess with closed projects
        if (project.isClosed()) {
            throw new FuscardProjectException("The project is closed");
        }
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) jTreeProject.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
        removeProjectNodes(project);

        infor("Creating project node for " + project);
        DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(project);
        rootNode.add(projectNode);

        //put the files belonging to the project
        ExtensionInformation info = project.getExtensionInformation();
        String joomlaDir = info.getJoomlaDirectory();
        if (joomlaDir != null) {
            infor("Creating folder structure for " + project);
            infor(project + "'s Joomla dir is " + joomlaDir);
            //paths
            Path joomlaPath = Paths.get(joomlaDir);
            Path sitePath = joomlaPath.resolve(
                    "components/com_" + info.getAppName());
            Path adminPath = joomlaPath.resolve(
                    "administrator/components/com_" + info.getAppName());
            if (Files.isDirectory(sitePath)) {
                createMajorNode(projectNode, "Site", sitePath);
            }
            if (Files.isDirectory(adminPath)) {
                createMajorNode(projectNode, "Admin", adminPath);
            }

            /**
             * Create A Node for the database tables of the project
             */
            if (!project.getjEFDatabaseTables().isEmpty()) {
                DefaultMutableTreeNode tables = new DefaultMutableTreeNode(
                        "Database tables");
                for (JEFDatabaseTable table : project.getjEFDatabaseTables()) {
                    DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(
                            table);
                    tables.add(dmtn);
                }
                projectNode.add(tables);
            }

        } else {
            error("The joomla directory for " + project + " is null.");
        }
        //expand tree and highlight selected item
        defaultTreeModel.reload(rootNode);
        for (TreePath tp : nodesToExpand) {
            TreePath tpp = tp.getParentPath();
            jTreeProject.expandPath(tpp);
        }

    }

    /**
     * Open a project to be ready to be used in the IDE.
     *
     * @param project The project to boot strap.
     */
    private void bootStrapProject(Project project) {
        infor("Bootstrapping " + project);
        try {
            project.loadFromConfigurationFile();
        } catch (FuscardProjectException | FuscardXMLException ex) {
            error("Project failed to open.");
            return;
        }

        project.addProjectListener(new Project.DefaultProjectListener() {
            @Override
            public void projectOpened(Project project) {
                infor("After " + project + " opening");
                try {
                    createProjectTree(project);
                    if (!projects.contains(project)) {
                        projects.add(project);
                    }

                } catch (FuscardProjectException ex) {
                    error(ex.getMessage());
                }
                saveOpenProjects();
                removeFromRecentProjects(project);
            }

            @Override
            public void projectClosed(Project project) {
                removeProjectNodes(project);
                projects.remove(project);
                addToRecentProjects(project);
                saveOpenProjects();
                success("Project : '" + project.getProjectName() + "' closed successfully");
            }

            @Override
            public void projectSaved(Project project) {
                success("Project : '" + project.getProjectName() + "' saved successfully");
            }

            @Override
            public void projectSetMain(Project project) {
                for (Project p : projects) {
                    if (p != project && p.isMain()) {
                        p.setMain(false);
                    }
                }
            }
        });
        project.addProjectHook(new Project.DefaultProjectHook() {
            @Override
            public boolean beforeProjectCloses(Project project) {
                infor("Before " + project + " opening");
                if (!project.isSaved()) {
                    int response = JOptionPane.showConfirmDialog(
                            rootPane,
                            "Project '" + project.getProjectName() + "' has unsaved changes. Would you like to save them first?",
                            "Attention! Usaved changes.",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (response == JOptionPane.CANCEL_OPTION) {
                        return false;
                    }
                    if (response == JOptionPane.YES_OPTION) {
                        try {
                            project.save();
                        } catch (FuscardProjectException | FuscardXMLException ex) {
                            error(ex.getMessage());
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean beforeProjectOpens(Project project) {
                infor("Before " + project + " opens");
                for (Project p : projects) {
                    if (p.sameProject(project) && p != project) {
                        error(project + " blocked courtesy of " + p);
                        return false;
                    }
                }
                return true;
            }
        });
        project.open();
    }

    private void putOpenProjects() {
        infor("Putting open projects");
        String[] openProjects = Application.getInstance().getValues(
                OPEN_PROJECTS);
        for (String file : openProjects) {

            Path path = Paths.get(file);
            //Create a project.
            Project project = new Project();
            project.setProjectConfigurationFile(path);
            bootStrapProject(project);
        }
        saveOpenProjects();
    }

    private void closeProjects(Project[] projects) {
        for (Project project : projects) {
            project.close();
        }
    }

    private void saveOpenProjects() {
        //Save the open files
        LinkedList<String> proj = new LinkedList<>();
        for (Project project : projects) {
            proj.add(project.getProjectConfigurationFile().toString());
        }
        try {
            Application.getInstance().setValues(OPEN_PROJECTS,
                    proj.toArray(new String[0]));
        } catch (FuscardXMLException ignore) {
        }
        saveSelectedFile();
    }

    private void showProjectTree() {
        if (true) {
            createProjectsTree();
            return;
        }

        infor("About to show project tree");
//        saySelectedTab();
        nodesToExpand.clear();
        String projectName = codeGeneraror.getExtensionInfo().getAppName();
        if (projectName == null) {
            return;
        }
        DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(
                projectName);
        jTreeProject.setRootVisible(true);
        jTreeProject.setModel(new DefaultTreeModel(projectNode));
        //Dig the tree
        ExtensionInformation context = codeGeneraror.getExtensionInfo();
        String joomlaDir = context.getJoomlaDirectory();
        if (joomlaDir == null) {
            return;
        }
        //paths
        Path joomlaPath = Paths.get(joomlaDir);
        Path sitePath = joomlaPath.resolve("components/com_" + projectName);
        Path adminPath = joomlaPath.resolve(
                "administrator/components/com_" + projectName);
        if (Files.isDirectory(sitePath)) {
            createMajorNode(projectNode, "Site", sitePath);
        }
        if (Files.isDirectory(adminPath)) {
            createMajorNode(projectNode, "Admin", adminPath);
        }
        //expand tree and highlight selected item
        for (TreePath tp : nodesToExpand) {
            TreePath tpp = tp.getParentPath();
            jTreeProject.expandPath(tpp);
        }
        infor("About to set the selected tab");
//        saySelectedTab();
        int index = tabbedPane.getTabbedPane().getSelectedIndex();
        if (index != -1) {
            ButtonTabComponent tab = (ButtonTabComponent) tabbedPane.getTabbedPane().getTabComponentAt(
                    index);
            final FileHolder fileHolder = tab.getFileHolder();
            selectFileOnTree(fileHolder);
        }

    }

    private void selectFileOnTree(final FileHolder fileHolder) {
        if (fileHolder != null) {
            //Traverse the tree, look for that node and paint it
            TreeSearch ts = new TreeSearch(jTreeProject) {
                @Override
                public boolean accept(DefaultMutableTreeNode node) {
                    Object obj = node.getUserObject();
                    if (obj instanceof FileHolder) {
                        FileHolder fh = (FileHolder) node.getUserObject();
                        return fileHolder == fh;
                    }
                    return false;
                }
            };
            DefaultMutableTreeNode result = ts.search();
            if (result != null) {
                TreePath tp = new TreePath(result.getPath());
                jTreeProject.scrollPathToVisible(tp);
                jTreeProject.setSelectionPath(tp);
                infor("Tree path " + tp + " is selected");
            }

        }
    }

    private void saySelectedTab() {
        int index = tabbedPane.getTabbedPane().getSelectedIndex();
        if (index != -1) {
            ButtonTabComponent tab = (ButtonTabComponent) tabbedPane.getTabbedPane().getTabComponentAt(
                    index);
            final FileHolder fileHolder = tab.getFileHolder();
            infor("Selected tab " + tab.getTitle());
            if (fileHolder != null) {
                infor("That represents file: " + fileHolder.file);
            } else {
                infor("there is no file holder");
            }
        } else {
            infor("There is no selected tab");
        }
    }

    private void refreshContext() {
        try {
            codeGeneraror.loadConfigurationFile();
        } catch (FuscardXMLException ex) {
            Logger.getLogger(AllSystems.class.getName()).
                    log(Level.SEVERE, null, ex);
            return;
        }

        ExtensionInformation context = codeGeneraror.getExtensionInfo();
        jTextFieldAuthorEmail.setText(context.getAuthor_mail());
        jTextFieldAppName.setText(context.getAppName());
        jTextFieldAuthorName.setText(context.getAuthor());
        jTextFieldAuthorURL.setText(context.getAuthor_website());
        jTextFieldConfigurationFile.setText(codeGeneraror.getConfigFile());
        jTextAreaCopyright.setText(context.getCopyright());
        jTextFieldJoomlaDirectory.setText(context.getJoomlaDirectory());
        jTextAreaLicence.setText(context.getLicense());
        jSpinnerReleaseIndex.setValue(Integer.
                parseInt(context.getReleaseIndex()));
        jTextAreaSummary.setText(context.getSummary());
        jTextFieldVersion.setText(context.getVersion());
//        showProjectTree();
    }
    private Color c1 = new Color(0xff666666, true), c2 = new Color(0xffcccccc,
            true);

    private void drawGradient(Graphics g, Component c) {
        int w = c.getWidth();
        int h = c.getHeight();
        Rectangle r = new Rectangle(w, h);
        GradientPaint p = new GradientPaint(0, 0, c1,
                0, h, c.getBackground());
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(p);
        g2.fill(r);
    }

    public void alert(String message) {
        JOptionPane.showMessageDialog(rootPane, message, "Attention",
                JOptionPane.WARNING_MESSAGE);
    }

    public boolean confirm(String message) {
        int conf = JOptionPane.showConfirmDialog(rootPane, message, "Attention",
                JOptionPane.WARNING_MESSAGE);
        return conf == JOptionPane.OK_OPTION;
    }
    private JTextComponent editorSourceTextComponent;

    private void showTextEditor(JTextComponent textComponent, String title) {
        jTextAreaTextEditor.setText(textComponent.getText());
        JDialogTextEditor.setTitle(title);
        editorSourceTextComponent = textComponent;
        JDialogTextEditor.setSize(JDialogTextEditor.getPreferredSize());
        JDialogTextEditor.setLocationRelativeTo(this);
        JDialogTextEditor.setVisible(true);
    }

    private boolean saveMainState() {

        infor("Saving basic information");
        ExtensionInformation context = codeGeneraror.getExtensionInfo();
        context.setJoomlaDirectory(jTextFieldJoomlaDirectory.getText());
        context.setAppName(jTextFieldAppName.getText());
        try {
            codeGeneraror.saveComponentContext(context,
                    jTextFieldConfigurationFile.getText());
        } catch (CodeGeneratorException ex) {
            error(ex.getMessage());
            return false;
        }
        success("Information successfully saved");
        refreshContext();
        return true;
    }

    private boolean saveExtendedState() {
        infor("Saving extended information");
        ExtensionInformation context = codeGeneraror.getExtensionInfo();
        context.setJoomlaDirectory(jTextFieldJoomlaDirectory.getText());
        context.setAppName(jTextFieldAppName.getText());
        context.setAuthor(jTextFieldAuthorName.getText());
        context.setAuthor_mail(jTextFieldAuthorEmail.getText());
        context.setAuthor_website(jTextFieldAuthorURL.getText());
        context.setCopyright(jTextAreaCopyright.getText());
        context.setLicense(jTextAreaLicence.getText());
        context.setReleaseIndex(jSpinnerReleaseIndex.getValue().toString());
        context.setSummary(jTextAreaSummary.getText());
        context.setVersion(jTextFieldVersion.getText());
        try {
            codeGeneraror.saveComponentContext(context, null);
        } catch (CodeGeneratorException ex) {
            error(ex.getMessage());
            return false;
        }
        success("Information successfully saved");
        refreshContext();
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jFileChooser1 = new javax.swing.JFileChooser();
        JDialogTextEditor = new javax.swing.JDialog(this);
        jPanel17 = new javax.swing.JPanel();
        jButton25 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaTextEditor = new javax.swing.JTextArea();
        jDialogInformationGetter = new javax.swing.JDialog(this);
        jPanel18 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jButton29 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxJoomlaArtifact = new javax.swing.JComboBox();
        jTextFieldArtifactName = new javax.swing.JTextField();
        jTextFieldViewDetailsName = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jCheckBoxArtifactInAdmin = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jComboBoxSelectProject = new javax.swing.JComboBox();
        jDialogNewDrawing = new javax.swing.JDialog(this);
        jPanel3 = new javax.swing.JPanel();
        jButton27 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        jComboBoxChooseProject = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        jTextFieldDrawingName = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jDialogNewProject = new javax.swing.JDialog(this);
        jPanel11 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextAreaNewProjectDescription = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextAreaNewProjectCopyright = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTextAreaNewProjectLicence = new javax.swing.JTextArea();
        jLabel49 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jButton37 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jTextFieldNewProjectAuthor = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jTextFieldNewProjectDefaultVersion = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jTextFieldNewProjectAuthorWebsite = new javax.swing.JTextField();
        jTextFieldNewProjectAuthorEmail = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jTextFieldNewProjectProjectName = new javax.swing.JTextField();
        jTextFieldNewProjectUpdateServer = new javax.swing.JTextField();
        jTextFieldNewProjectJoomlaDirectory = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jDialogNewDetailsList = new javax.swing.JDialog(this);
        jPanel13 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jRadioButtonNewDetilsListLocationAdmin = new javax.swing.JRadioButton();
        jRadioButtonNewDetilsListLocationSite = new javax.swing.JRadioButton();
        jTextFieldNewDetailsListListName = new javax.swing.JTextField();
        jTextFieldNewDetailsListDetailsName = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTableNewListAndDetailsFields = new javax.swing.JTable();
        jButton45 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        jButton50 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();
        jComboBoxNewListAndDetailsSelectProject = new javax.swing.JComboBox();
        jButton47 = new javax.swing.JButton();
        jLabel63 = new javax.swing.JLabel();
        jTextFieldNewDetailsListTableName = new javax.swing.JTextField();
        buttonGroupNewListAndDetailsLocation = new javax.swing.ButtonGroup();
        jDialogAddNewFieldType = new javax.swing.JDialog(this);
        jPanel15 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jTextFieldNewFieldName = new javax.swing.JTextField();
        jTextFieldNewFieldDisplayName = new javax.swing.JTextField();
        jTextFieldNewFieldDescription = new javax.swing.JTextField();
        jTextFieldNewFieldDefault = new javax.swing.JTextField();
        jComboBoxNewFieldType = new javax.swing.JComboBox();
        jCheckBoxNewFieldRequired = new javax.swing.JCheckBox();
        jButton48 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jDialogDatabaseTableEditor = new javax.swing.JDialog(this);
        jPanel19 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jComboBoxDatabaseTableEditorProject = new javax.swing.JComboBox();
        jTextFieldDatabaseTableEditorNameOfTable = new javax.swing.JTextField();
        jButton51 = new javax.swing.JButton();
        jLabel67 = new javax.swing.JLabel();
        jTextFieldDatabaseTableEditorTableAlias = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTableDatabaseTableEditorFields = new javax.swing.JTable();
        jButton52 = new javax.swing.JButton();
        jButton53 = new javax.swing.JButton();
        jDialogDatabaseDetails = new javax.swing.JDialog(this);
        jPanel20 = new javax.swing.JPanel();
        jButton54 = new javax.swing.JButton();
        jButton55 = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTableJoomlaInfo = new javax.swing.JTable();
        jLabel69 = new javax.swing.JLabel();
        jButton56 = new javax.swing.JButton();
        jButton57 = new javax.swing.JButton();
        jButton58 = new javax.swing.JButton();
        jLabel81 = new javax.swing.JLabel();
        jDialogDatabaseDetailsRecordEditor = new javax.swing.JDialog(this);
        jPanel21 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jTextFieldDatabaseDetailsRecordEditorNameOfJoomla = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jTextFieldDatabaseDetailsRecordEditorJoomlaPath = new javax.swing.JTextField();
        jButton59 = new javax.swing.JButton();
        jLabel73 = new javax.swing.JLabel();
        jTextFieldDatabaseDetailsRecordEditorJoomlaTablePrefix = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jTextFieldDatabaseDetailsRecordEditorDbUrl = new javax.swing.JTextField();
        jTextFieldDatabaseDetailsRecordEditorDbUsername = new javax.swing.JTextField();
        jButton60 = new javax.swing.JButton();
        jButton61 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jPasswordFieldDatabaseDetailsRecordEditorPassword = new javax.swing.JPasswordField();
        jPasswordFieldDatabaseDetailsRecordEditorPassword2 = new javax.swing.JPasswordField();
        jLabel80 = new javax.swing.JLabel();
        jButton62 = new javax.swing.JButton();
        jLabel82 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jPanel2 = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                drawGradient(g, this);
            }
        };
        jPanel6 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeProject = new javax.swing.JTree();
        jPanel12 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabelLoading = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jSplitPaneTools = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jFormattedTextFieldCanvasPreferredHeight = new javax.swing.JFormattedTextField();
        jFormattedTextFieldCanvasPreferredWidth = new javax.swing.JFormattedTextField();
        jButton31 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jButton32 = new javax.swing.JButton();
        jTextFieldCanvasColor = new NoThemeTextField();
        jPanel8 = new javax.swing.JPanel();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jTextFieldDMOutputArea = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldAppName = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jTextFieldVersion = new javax.swing.JTextField();
        jSpinnerReleaseIndex = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldJoomlaDirectory = new javax.swing.JTextField();
        jButtonChooseJoomlaDirectory = new javax.swing.JButton();
        jTextFieldAuthorName = new javax.swing.JTextField();
        jTextFieldConfigurationFile = new javax.swing.JTextField();
        jTextFieldAuthorEmail = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextFieldAuthorURL = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaSummary = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaCopyright = new javax.swing.JTextArea();
        jButton24 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextAreaLicence = new javax.swing.JTextArea();
        jButton22 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuRecentProjects = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();

        JDialogTextEditor.setModal(true);

        jButton25.setText("Cancel");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextAreaTextEditor.setColumns(20);
        jTextAreaTextEditor.setRows(5);
        jScrollPane2.setViewportView(jTextAreaTextEditor);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(1, 1, 1))
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton25)
                    .addComponent(jButton1)))
        );

        javax.swing.GroupLayout JDialogTextEditorLayout = new javax.swing.GroupLayout(JDialogTextEditor.getContentPane());
        JDialogTextEditor.getContentPane().setLayout(JDialogTextEditorLayout);
        JDialogTextEditorLayout.setHorizontalGroup(
            JDialogTextEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JDialogTextEditorLayout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        JDialogTextEditorLayout.setVerticalGroup(
            JDialogTextEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogInformationGetter.setModal(true);

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard/g.png"))); // NOI18N

        jButton29.setText("Cancel");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton28.setText("Create");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jLabel13.setText("What?");

        jLabel14.setText("Name");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Create something");

        jComboBoxJoomlaArtifact.setModel(new DefaultComboBoxModel<String>(new String[]{INT_COMPONENT, INT_CONTROLLER, INT_ENTRY, INT_FORM, INT_FORM_FIELD,INT_LANGAUGE,INT_LAYOUT,INT_MANIFEST,INT_MODEL,INT_SCRIPT,INT_SQL_FILE,INT_TABLE,INT_VIEW,INT_VIEW_CP,INT_VIEW_DETAILS,INT_VIEW_LIST, INT_VIEW_LIST_DETAILS,INT_VIEW_PARAMETER}));
        jComboBoxJoomlaArtifact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxJoomlaArtifactActionPerformed(evt);
            }
        });

        jLabel15.setText("In the admin");

        jLabel16.setText("Details");

        jLabel41.setText("Project");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel41)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxSelectProject, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addComponent(jLabel12)
                                .addComponent(jLabel15))
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxJoomlaArtifact, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(jButton29)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton28))
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckBoxArtifactInAdmin)
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addComponent(jTextFieldArtifactName, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel16)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextFieldViewDetailsName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(111, Short.MAX_VALUE))
        );

        jPanel18Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextFieldArtifactName, jTextFieldViewDetailsName});

        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jComboBoxJoomlaArtifact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(jTextFieldArtifactName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldViewDetailsName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jCheckBoxArtifactInAdmin))
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(jComboBoxSelectProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton28)
                            .addComponent(jButton29))
                        .addGap(40, 40, 40))))
        );

        javax.swing.GroupLayout jDialogInformationGetterLayout = new javax.swing.GroupLayout(jDialogInformationGetter.getContentPane());
        jDialogInformationGetter.getContentPane().setLayout(jDialogInformationGetterLayout);
        jDialogInformationGetterLayout.setHorizontalGroup(
            jDialogInformationGetterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogInformationGetterLayout.setVerticalGroup(
            jDialogInformationGetterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogNewDrawing.setTitle("New Drawing");
        jDialogNewDrawing.setModal(true);

        jButton27.setText("Ok");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jLabel35.setText("Name of the drawing");

        jButton30.setText("Cancel");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jComboBoxChooseProject.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel36.setText("Project");

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard/g.png"))); // NOI18N

        jLabel31.setText("Create a new drawing");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                        .addComponent(jButton30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton27))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35)
                                    .addComponent(jLabel36))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldDrawingName)
                                    .addComponent(jComboBoxChooseProject, 0, 223, Short.MAX_VALUE)))
                            .addComponent(jLabel31))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jTextFieldDrawingName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxChooseProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton27)
                        .addComponent(jButton30))
                    .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogNewDrawingLayout = new javax.swing.GroupLayout(jDialogNewDrawing.getContentPane());
        jDialogNewDrawing.getContentPane().setLayout(jDialogNewDrawingLayout);
        jDialogNewDrawingLayout.setHorizontalGroup(
            jDialogNewDrawingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogNewDrawingLayout.setVerticalGroup(
            jDialogNewDrawingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogNewProject.setTitle("New project");
        jDialogNewProject.setModal(true);

        jTextAreaNewProjectDescription.setColumns(20);
        jTextAreaNewProjectDescription.setRows(5);
        jScrollPane11.setViewportView(jTextAreaNewProjectDescription);

        jTextAreaNewProjectCopyright.setColumns(20);
        jTextAreaNewProjectCopyright.setRows(5);
        jScrollPane9.setViewportView(jTextAreaNewProjectCopyright);

        jTextAreaNewProjectLicence.setColumns(20);
        jTextAreaNewProjectLicence.setRows(5);
        jScrollPane10.setViewportView(jTextAreaNewProjectLicence);

        jLabel49.setText("Licence");

        jLabel51.setText("Description");

        jLabel50.setText("Copyright");

        jButton37.setText("preview");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jButton38.setText("preview");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton39.setText("preview");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51)
                    .addComponent(jLabel50)
                    .addComponent(jLabel49))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton37)
                    .addComponent(jButton38)
                    .addComponent(jButton39))
                .addGap(21, 21, 21))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(jLabel49)
                    .addComponent(jButton37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(jLabel50)
                    .addComponent(jButton38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                    .addComponent(jButton39))
                .addContainerGap())
        );

        jScrollPane12.setViewportView(jPanel9);

        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard/g.png"))); // NOI18N

        jButton42.setText("choose");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        jButton43.setText("choose");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        jLabel46.setText("Author");

        jLabel45.setText("Default version");

        jLabel47.setText("Author Email");

        jLabel48.setText("Author website");

        jLabel42.setText("Name of the project");

        jLabel43.setText("Update server");

        jLabel44.setText("joomla directory");

        jPanel22.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton40.setText("Cancel");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jButton41.setText("create");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton41)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton40)
                    .addComponent(jButton41))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addComponent(jLabel45)
                            .addComponent(jLabel47)
                            .addComponent(jLabel43))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jTextFieldNewProjectUpdateServer)
                                .addGap(85, 85, 85))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jTextFieldNewProjectJoomlaDirectory)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton42)
                                    .addComponent(jButton43)))
                            .addComponent(jScrollPane12)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldNewProjectAuthorEmail)
                                    .addComponent(jTextFieldNewProjectDefaultVersion))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel46)
                                    .addComponent(jLabel48))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldNewProjectAuthor)
                                    .addComponent(jTextFieldNewProjectAuthorWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jTextFieldNewProjectProjectName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
            .addComponent(jPanel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel11Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextFieldNewProjectAuthor, jTextFieldNewProjectAuthorEmail, jTextFieldNewProjectAuthorWebsite, jTextFieldNewProjectDefaultVersion});

        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(jTextFieldNewProjectProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel43)
                                    .addComponent(jTextFieldNewProjectUpdateServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel44)
                                    .addComponent(jTextFieldNewProjectJoomlaDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jButton42)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton43)))
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel47))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                            .addGap(29, 29, 29)
                                            .addComponent(jLabel48))
                                        .addComponent(jLabel46))
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                            .addGap(26, 26, 26)
                                            .addComponent(jTextFieldNewProjectAuthorWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jTextFieldNewProjectAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addComponent(jTextFieldNewProjectDefaultVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldNewProjectAuthorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane12))
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogNewProjectLayout = new javax.swing.GroupLayout(jDialogNewProject.getContentPane());
        jDialogNewProject.getContentPane().setLayout(jDialogNewProjectLayout);
        jDialogNewProjectLayout.setHorizontalGroup(
            jDialogNewProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogNewProjectLayout.setVerticalGroup(
            jDialogNewProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogNewDetailsList.setTitle("New List and Details");
        jDialogNewDetailsList.setModal(true);

        jPanel13.setBackground(new java.awt.Color(102, 102, 255));

        jLabel53.setText("List name");

        jLabel54.setText("Details Name");

        buttonGroupNewListAndDetailsLocation.add(jRadioButtonNewDetilsListLocationAdmin);
        jRadioButtonNewDetilsListLocationAdmin.setSelected(true);
        jRadioButtonNewDetilsListLocationAdmin.setText("admin");

        buttonGroupNewListAndDetailsLocation.add(jRadioButtonNewDetilsListLocationSite);
        jRadioButtonNewDetilsListLocationSite.setText("site");

        jPanel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableNewListAndDetailsFields.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Display name", "Description", "Type", "Required", "Default"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane13.setViewportView(jTableNewListAndDetailsFields);

        jButton45.setText("Remove");

        jButton44.setText("Add");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jLabel55.setText("Fields");

        jButton50.setText("edit");
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton45))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel55)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton44)
                    .addComponent(jButton45)
                    .addComponent(jButton50))
                .addContainerGap())
        );

        jButton46.setText("Create");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        jLabel56.setText("Project");

        jButton47.setText("Cancel");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        jLabel63.setText("Table Name");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextFieldNewDetailsListListName, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addComponent(jButton47)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton46))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel13Layout.createSequentialGroup()
                                    .addComponent(jRadioButtonNewDetilsListLocationAdmin)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jRadioButtonNewDetilsListLocationSite))
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54)
                            .addComponent(jLabel56))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNewDetailsListDetailsName, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxNewListAndDetailsSelectProject, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextFieldNewDetailsListTableName, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBoxNewListAndDetailsSelectProject, jTextFieldNewDetailsListDetailsName, jTextFieldNewDetailsListListName, jTextFieldNewDetailsListTableName});

        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jTextFieldNewDetailsListListName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jTextFieldNewDetailsListDetailsName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(jTextFieldNewDetailsListTableName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jComboBoxNewListAndDetailsSelectProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonNewDetilsListLocationAdmin)
                    .addComponent(jRadioButtonNewDetilsListLocationSite))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton46)
                    .addComponent(jButton47))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogNewDetailsListLayout = new javax.swing.GroupLayout(jDialogNewDetailsList.getContentPane());
        jDialogNewDetailsList.getContentPane().setLayout(jDialogNewDetailsListLayout);
        jDialogNewDetailsListLayout.setHorizontalGroup(
            jDialogNewDetailsListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogNewDetailsListLayout.setVerticalGroup(
            jDialogNewDetailsListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogNewDetailsListLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jDialogAddNewFieldType.setTitle("New field");
        jDialogAddNewFieldType.setModal(true);

        jPanel15.setBackground(new java.awt.Color(153, 153, 255));

        jLabel57.setText("Name");

        jLabel58.setText("Display name");

        jLabel59.setText("Description");

        jLabel60.setText("Type");

        jLabel61.setText("Default");

        jLabel62.setText("Required");

        jComboBoxNewFieldType.setModel(FieldListComboBoxModel.createInstance());

        jButton48.setText("Cancel");
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });

        jButton49.setText("Ok");
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel59)
                            .addComponent(jLabel58)
                            .addComponent(jLabel57)
                            .addComponent(jLabel60)
                            .addComponent(jLabel61))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxNewFieldType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNewFieldDisplayName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNewFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNewFieldDefault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBoxNewFieldRequired)
                            .addComponent(jTextFieldNewFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(jButton48)
                        .addGap(18, 18, 18)
                        .addComponent(jButton49)
                        .addGap(38, 38, 38))))
        );

        jPanel15Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBoxNewFieldType, jTextFieldNewFieldDefault, jTextFieldNewFieldDescription, jTextFieldNewFieldDisplayName, jTextFieldNewFieldName});

        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(jTextFieldNewFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60)
                    .addComponent(jComboBoxNewFieldType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel58)
                    .addComponent(jTextFieldNewFieldDisplayName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(jTextFieldNewFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(jTextFieldNewFieldDefault, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(jCheckBoxNewFieldRequired))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton48)
                    .addComponent(jButton49))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogAddNewFieldTypeLayout = new javax.swing.GroupLayout(jDialogAddNewFieldType.getContentPane());
        jDialogAddNewFieldType.getContentPane().setLayout(jDialogAddNewFieldTypeLayout);
        jDialogAddNewFieldTypeLayout.setHorizontalGroup(
            jDialogAddNewFieldTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogAddNewFieldTypeLayout.setVerticalGroup(
            jDialogAddNewFieldTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogDatabaseTableEditor.setTitle("New Database Table");
        jDialogDatabaseTableEditor.setModal(true);

        jPanel19.setBackground(new java.awt.Color(204, 255, 255));

        jLabel64.setText("Edit database table details");

        jLabel65.setText("Name of database table");

        jLabel66.setText("Project");

        jButton51.setText("test availability");
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });

        jLabel67.setText("Alias");

        jLabel68.setText("Fields");

        jTableDatabaseTableEditorFields.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Alias", "Type", "Size", "Required", "Default"
            }
        ));
        jScrollPane14.setViewportView(jTableDatabaseTableEditorFields);

        jButton52.setText("Cancel");
        jButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton52ActionPerformed(evt);
            }
        });

        jButton53.setText("Done");
        jButton53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton53ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel64)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel67)
                                    .addComponent(jLabel65)
                                    .addComponent(jLabel66))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel19Layout.createSequentialGroup()
                                        .addComponent(jComboBoxDatabaseTableEditorProject, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton51))
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTextFieldDatabaseTableEditorTableAlias, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                        .addComponent(jTextFieldDatabaseTableEditorNameOfTable, javax.swing.GroupLayout.Alignment.LEADING))))
                            .addComponent(jLabel68))
                        .addGap(0, 133, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton53)
                .addGap(20, 20, 20))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(jTextFieldDatabaseTableEditorNameOfTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(jTextFieldDatabaseTableEditorTableAlias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(jComboBoxDatabaseTableEditorProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton51))
                .addGap(18, 18, 18)
                .addComponent(jLabel68)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton52)
                    .addComponent(jButton53))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogDatabaseTableEditorLayout = new javax.swing.GroupLayout(jDialogDatabaseTableEditor.getContentPane());
        jDialogDatabaseTableEditor.getContentPane().setLayout(jDialogDatabaseTableEditorLayout);
        jDialogDatabaseTableEditorLayout.setHorizontalGroup(
            jDialogDatabaseTableEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogDatabaseTableEditorLayout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jDialogDatabaseTableEditorLayout.setVerticalGroup(
            jDialogDatabaseTableEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogDatabaseDetails.setTitle("Database Details");
        jDialogDatabaseDetails.setModal(true);

        jPanel20.setBackground(new java.awt.Color(204, 255, 255));

        jButton54.setText("Cancel");
        jButton54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton54ActionPerformed(evt);
            }
        });

        jButton55.setText("Done");
        jButton55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton55ActionPerformed(evt);
            }
        });

        jTableJoomlaInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Joomla Installation Alias", "Joomla Directory", "Database Table Prefix", "Database URL", "Username", "Password"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(jTableJoomlaInfo);

        jLabel69.setText("Edit this list to set the default joomla directory database details");

        jButton56.setText("Add New");
        jButton56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton56ActionPerformed(evt);
            }
        });

        jButton57.setText("Remove");
        jButton57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton57ActionPerformed(evt);
            }
        });

        jButton58.setText("Edit");
        jButton58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton58ActionPerformed(evt);
            }
        });

        jLabel81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard strip.png"))); // NOI18N

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addComponent(jButton56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton58)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel81)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                        .addComponent(jButton54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton55))
                    .addComponent(jScrollPane15))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton54)
                        .addComponent(jButton55)
                        .addComponent(jButton56)
                        .addComponent(jButton57)
                        .addComponent(jButton58))
                    .addComponent(jLabel81))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogDatabaseDetailsLayout = new javax.swing.GroupLayout(jDialogDatabaseDetails.getContentPane());
        jDialogDatabaseDetails.getContentPane().setLayout(jDialogDatabaseDetailsLayout);
        jDialogDatabaseDetailsLayout.setHorizontalGroup(
            jDialogDatabaseDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogDatabaseDetailsLayout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jDialogDatabaseDetailsLayout.setVerticalGroup(
            jDialogDatabaseDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogDatabaseDetailsRecordEditor.setTitle("Database Details Editor");
        jDialogDatabaseDetailsRecordEditor.setModal(true);

        jPanel21.setBackground(new java.awt.Color(204, 255, 255));

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel70.setText("Edit database details for a Joomla Installation");

        jLabel71.setText("Friendly Name of the Joomla Installation*");

        jLabel72.setText("Installation Folder*");

        jButton59.setText("Choose");
        jButton59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton59ActionPerformed(evt);
            }
        });

        jLabel73.setText("Database Table prefix*");

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel74.setText("Database Connection Details");

        jLabel75.setText("This is where you set the connection details of the MySQLdatabase that your testing Joomla uses.");

        jLabel76.setText("This allows JEF to create tables for you automatically.");

        jLabel77.setText("Database URL*");

        jLabel78.setText("Database Username*");

        jLabel79.setText("Database Password");

        jButton60.setText("Cancel");
        jButton60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton60ActionPerformed(evt);
            }
        });

        jButton61.setText("Ok");
        jButton61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton61ActionPerformed(evt);
            }
        });

        jLabel80.setText("Confirm Database Password");

        jButton62.setText("Test");
        jButton62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton62ActionPerformed(evt);
            }
        });

        jLabel82.setText("* Marks a required field");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator9)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGap(342, 342, 342)
                                .addComponent(jButton60)
                                .addGap(18, 18, 18)
                                .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addComponent(jLabel77)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldDatabaseDetailsRecordEditorDbUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel71)
                                        .addComponent(jLabel72)
                                        .addComponent(jLabel73))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel21Layout.createSequentialGroup()
                                            .addComponent(jTextFieldDatabaseDetailsRecordEditorJoomlaPath, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton59))
                                        .addComponent(jTextFieldDatabaseDetailsRecordEditorNameOfJoomla)
                                        .addComponent(jTextFieldDatabaseDetailsRecordEditorJoomlaTablePrefix, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel74)
                                .addComponent(jLabel75)
                                .addComponent(jLabel76)
                                .addGroup(jPanel21Layout.createSequentialGroup()
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel78)
                                        .addComponent(jLabel79))
                                    .addGap(112, 112, 112)
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton62)
                                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldDatabaseDetailsRecordEditorDbUsername)
                                            .addComponent(jPasswordFieldDatabaseDetailsRecordEditorPassword)
                                            .addComponent(jPasswordFieldDatabaseDetailsRecordEditorPassword2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(jSeparator8))
                            .addComponent(jLabel80)
                            .addComponent(jLabel82)
                            .addComponent(jLabel70))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel70)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel82)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(jTextFieldDatabaseDetailsRecordEditorNameOfJoomla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(jTextFieldDatabaseDetailsRecordEditorJoomlaPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton59))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(jTextFieldDatabaseDetailsRecordEditorJoomlaTablePrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel74)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel75)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel76)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(jTextFieldDatabaseDetailsRecordEditorDbUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(jTextFieldDatabaseDetailsRecordEditorDbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(jPasswordFieldDatabaseDetailsRecordEditorPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(jPasswordFieldDatabaseDetailsRecordEditorPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton62)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton60)
                    .addComponent(jButton61))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogDatabaseDetailsRecordEditorLayout = new javax.swing.GroupLayout(jDialogDatabaseDetailsRecordEditor.getContentPane());
        jDialogDatabaseDetailsRecordEditor.getContentPane().setLayout(jDialogDatabaseDetailsRecordEditorLayout);
        jDialogDatabaseDetailsRecordEditorLayout.setHorizontalGroup(
            jDialogDatabaseDetailsRecordEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogDatabaseDetailsRecordEditorLayout.setVerticalGroup(
            jDialogDatabaseDetailsRecordEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogDatabaseDetailsRecordEditorLayout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Joomla Extension Factory :: (c) John Mwai 2013 - mwaimuragz@gmail.com");
        setIconImage(getWindowIcon());

        jPanel2.setBackground(java.awt.Color.darkGray);
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 15));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(153, 153, 0));

        jSplitPane2.setBackground(java.awt.Color.darkGray);
        jSplitPane2.setDividerSize(10);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(1.0);
        jSplitPane2.setOneTouchExpandable(true);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setResizeWeight(0.15);
        jSplitPane1.setOneTouchExpandable(true);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeProject.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTreeProject.setRootVisible(false);
        jScrollPane1.setViewportView(jTreeProject);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jPanel12.setBackground(new java.awt.Color(204, 204, 204));
        jPanel12.setPreferredSize(new java.awt.Dimension(50, 50));

        jLabel30.setFont(new java.awt.Font("Andalus", 0, 14)); // NOI18N
        jLabel30.setText("Fuscard");

        jLabelLoading.setFont(new java.awt.Font("Andalus", 1, 24)); // NOI18N
        jLabelLoading.setForeground(new java.awt.Color(0, 102, 102));
        jLabelLoading.setText("Joomla! Extension Factory");

        jLabel29.setForeground(new java.awt.Color(153, 153, 153));
        jLabel29.setText("Copyright (c)");

        jLabel32.setForeground(new java.awt.Color(0, 153, 153));
        jLabel32.setText("John Mwai 2013");

        jLabel33.setForeground(new java.awt.Color(153, 153, 153));
        jLabel33.setText("mwaimuragz@gmail.com");

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard strip.png"))); // NOI18N

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard/g.png"))); // NOI18N
        jButton26.setContentAreaFilled(false);
        jButton26.setFocusPainted(false);
        jButton26.setFocusable(false);
        jButton26.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard/f.png"))); // NOI18N
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap(318, Short.MAX_VALUE)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel33)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel32))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabelLoading)
                        .addGap(0, 498, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel30)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabelLoading)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                        .addComponent(jLabel30)
                        .addGap(55, 55, 55))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel29))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel33)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setRightComponent(jPanel12);

        jSplitPane2.setTopComponent(jSplitPane1);

        jSplitPaneTools.setDividerSize(10);
        jSplitPaneTools.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPaneTools.setResizeWeight(0.5);
        jSplitPaneTools.setMinimumSize(new java.awt.Dimension(50, 300));
        jSplitPaneTools.setOneTouchExpandable(true);

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel4.setMinimumSize(new java.awt.Dimension(100, 170));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));

        jLabel28.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel28.setText("Create Something");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Extensions");

        jButton10.setText("Component");
        jButton10.setFocusPainted(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Aggregate");

        jButton13.setText("List & Details");
        jButton13.setFocusPainted(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Views");

        jButton16.setText("Details");
        jButton16.setFocusPainted(false);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setText("List");
        jButton17.setFocusPainted(false);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setText("Control Panel");
        jButton18.setFocusPainted(false);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Basic");

        jButton3.setText("Model");
        jButton3.setFocusPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton7.setText("Script");
        jButton7.setFocusPainted(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton4.setText("Controller");
        jButton4.setFocusPainted(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("View");
        jButton5.setFocusPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton8.setText("Layout");
        jButton8.setFocusPainted(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton6.setText("Table");
        jButton6.setFocusPainted(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("SQL");

        jButton9.setText("installer");
        jButton9.setFocusPainted(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton11.setText("Update");
        jButton11.setFocusPainted(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Uninstaller");
        jButton12.setFocusPainted(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Actions");

        jButton14.setText("Edit");
        jButton14.setFocusPainted(false);

        jButton15.setText("Destroy");
        jButton15.setFocusPainted(false);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton19.setText("Package");
        jButton19.setFocusPainted(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton10)
                            .addComponent(jLabel20))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton16)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel22)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton13)
                                    .addComponent(jLabel21)))
                            .addComponent(jButton18)
                            .addComponent(jButton17))))
                .addGap(42, 42, 42)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(184, 184, 184)
                        .addComponent(jLabel24))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addComponent(jButton7)
                            .addComponent(jButton4))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton8)
                            .addComponent(jButton6)
                            .addComponent(jButton5))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton11)
                            .addComponent(jButton9)
                            .addComponent(jButton12))))
                .addGap(55, 55, 55)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton14)
                    .addComponent(jLabel26)
                    .addComponent(jButton19)
                    .addComponent(jButton15))
                .addContainerGap(361, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton3, jButton4, jButton5, jButton6, jButton7, jButton8});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton11, jButton12, jButton9});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton16, jButton17, jButton18});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton14, jButton15});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel23)
                                .addComponent(jLabel21)
                                .addComponent(jLabel22))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel24)
                                .addComponent(jLabel26)))
                        .addGap(5, 5, 5)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton3)
                                    .addComponent(jButton8)
                                    .addComponent(jButton9)
                                    .addComponent(jButton13)
                                    .addComponent(jButton16))
                                .addGap(7, 7, 7)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton7)
                                    .addComponent(jButton6)
                                    .addComponent(jButton11)
                                    .addComponent(jButton18)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton14)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton4)
                                .addComponent(jButton12)
                                .addComponent(jButton5))
                            .addComponent(jButton15)
                            .addComponent(jButton17)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10))))
        );

        jScrollPane8.setViewportView(jPanel5);

        jTabbedPane1.addTab("Favourites", jScrollPane8);

        jLabel25.setText("height");

        jLabel27.setText("width");

        jFormattedTextFieldCanvasPreferredHeight.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jFormattedTextFieldCanvasPreferredHeight.setPreferredSize(new java.awt.Dimension(100, 20));
        jFormattedTextFieldCanvasPreferredHeight.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jFormattedTextFieldCanvasPreferredHeightPropertyChange(evt);
            }
        });

        jFormattedTextFieldCanvasPreferredWidth.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jFormattedTextFieldCanvasPreferredWidth.setPreferredSize(new java.awt.Dimension(100, 20));
        jFormattedTextFieldCanvasPreferredWidth.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jFormattedTextFieldCanvasPreferredWidthPropertyChange(evt);
            }
        });

        jButton31.setText("fit canvas");

        jLabel38.setText("px");

        jLabel39.setText("px");

        jLabel40.setText("Canvas Colour");

        jButton32.setText("Choose");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jTextFieldCanvasColor.setEditable(false);
        jTextFieldCanvasColor.setToolTipText("");
        jTextFieldCanvasColor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextFieldCanvasColorPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextFieldCanvasPreferredHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jFormattedTextFieldCanvasPreferredWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton31)
                        .addGap(141, 141, 141)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldCanvasColor, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton32))
                    .addComponent(jLabel39))
                .addContainerGap(491, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jFormattedTextFieldCanvasPreferredHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton31)
                    .addComponent(jLabel38)
                    .addComponent(jLabel40)
                    .addComponent(jButton32)
                    .addComponent(jTextFieldCanvasColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFormattedTextFieldCanvasPreferredWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel39)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("canvas", jPanel10);

        jButton33.setText("Form");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton34.setText("Table");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jButton35.setText("Filed");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton36.setText("Text");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton33)
                        .addGap(18, 18, 18)
                        .addComponent(jButton35))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton34)
                        .addGap(18, 18, 18)
                        .addComponent(jButton36)))
                .addContainerGap(1015, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton33)
                    .addComponent(jButton35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton34)
                    .addComponent(jButton36))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Draw", jPanel8);

        jTabbedPane1.addTab("drawing", jTabbedPane2);

        jScrollPane3.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane3.setForeground(new java.awt.Color(204, 204, 204));

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane3.setViewportView(jTextPane1);

        jTabbedPane1.addTab("Log", jScrollPane3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addComponent(jTextFieldDMOutputArea)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jTextFieldDMOutputArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPaneTools.setBottomComponent(jPanel4);

        jPanel16.setMinimumSize(new java.awt.Dimension(100, 129));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Default Settings");

        jLabel3.setText("Application Name");

        jButton21.setText("Save");
        jButton21.setFocusPainted(false);
        jButton21.setPreferredSize(new java.awt.Dimension(100, 22));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jSpinnerReleaseIndex.setModel(new javax.swing.SpinnerNumberModel(3, 1, 10, 1));

        jLabel4.setText("Minimum version");

        jButton20.setText("Save");
        jButton20.setFocusPainted(false);
        jButton20.setPreferredSize(new java.awt.Dimension(100, 22));
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Project Settings");

        jLabel1.setText("Joomla Directory");

        jLabel6.setText("name");

        jLabel5.setText("Release Index");

        jLabel8.setText("Url");

        jLabel7.setText("Email");

        jTextFieldJoomlaDirectory.setEditable(false);

        jButtonChooseJoomlaDirectory.setText("Choose");
        jButtonChooseJoomlaDirectory.setFocusPainted(false);
        jButtonChooseJoomlaDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChooseJoomlaDirectoryActionPerformed(evt);
            }
        });

        jTextFieldAuthorName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAuthorNameActionPerformed(evt);
            }
        });

        jTextFieldConfigurationFile.setEditable(false);

        jButton2.setText("Choose");
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Configuration File");

        jPanel1.setBackground(new java.awt.Color(0, 255, 153));
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 150));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 150));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel11.setText("Summary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jLabel11, gridBagConstraints);

        jButton23.setText("...");
        jButton23.setFocusPainted(false);
        jButton23.setPreferredSize(new java.awt.Dimension(44, 18));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 0);
        jPanel1.add(jButton23, gridBagConstraints);

        jScrollPane6.setPreferredSize(new java.awt.Dimension(86, 80));

        jTextAreaSummary.setColumns(10);
        jTextAreaSummary.setRows(2);
        jScrollPane6.setViewportView(jTextAreaSummary);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 317;
        gridBagConstraints.ipady = 112;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jScrollPane6, gridBagConstraints);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(86, 80));

        jTextAreaCopyright.setColumns(10);
        jTextAreaCopyright.setRows(2);
        jScrollPane4.setViewportView(jTextAreaCopyright);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 330;
        gridBagConstraints.ipady = 112;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jScrollPane4, gridBagConstraints);

        jButton24.setText("...");
        jButton24.setFocusPainted(false);
        jButton24.setPreferredSize(new java.awt.Dimension(44, 18));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 0);
        jPanel1.add(jButton24, gridBagConstraints);

        jLabel10.setText("Copyright");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jLabel10, gridBagConstraints);

        jScrollPane5.setPreferredSize(new java.awt.Dimension(86, 80));

        jTextAreaLicence.setColumns(10);
        jTextAreaLicence.setRows(2);
        jScrollPane5.setViewportView(jTextAreaLicence);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 288;
        gridBagConstraints.ipady = 112;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jScrollPane5, gridBagConstraints);

        jButton22.setText("...");
        jButton22.setFocusPainted(false);
        jButton22.setPreferredSize(new java.awt.Dimension(44, 18));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 0, 0);
        jPanel1.add(jButton22, gridBagConstraints);

        jLabel9.setText("Licence");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jLabel9, gridBagConstraints);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldJoomlaDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldConfigurationFile, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(jTextFieldAppName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButtonChooseJoomlaDirectory, javax.swing.GroupLayout.Alignment.TRAILING)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldAuthorURL))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldAuthorEmail))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldAuthorName, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinnerReleaseIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(39, 39, 39)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextFieldAuthorEmail, jTextFieldAuthorName, jTextFieldAuthorURL, jTextFieldVersion});

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextFieldAppName, jTextFieldConfigurationFile, jTextFieldJoomlaDirectory});

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextFieldJoomlaDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonChooseJoomlaDirectory))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldConfigurationFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextFieldAppName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jTextFieldAuthorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jTextFieldAuthorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextFieldVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jSpinnerReleaseIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldAuthorURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
        );

        jScrollPane7.setViewportView(jPanel7);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1171, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
        );

        jSplitPaneTools.setLeftComponent(jPanel16);

        jSplitPane2.setRightComponent(jSplitPaneTools);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jSplitPane2)
                .addGap(3, 3, 3))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new-project-16x16.png"))); // NOI18N
        jMenuItem1.setText("Open");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuRecentProjects.setText("Open recent");
        jMenu1.add(jMenuRecentProjects);
        jMenu1.add(jSeparator1);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("New project");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenu4.setText("New...");

        jMenuItem20.setText("Component");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem20);
        jMenu4.add(jSeparator6);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Model");
        jMenuItem9.setEnabled(false);
        jMenu4.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setText("View");
        jMenuItem10.setEnabled(false);
        jMenu4.add(jMenuItem10);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Controller");
        jMenuItem11.setEnabled(false);
        jMenu4.add(jMenuItem11);
        jMenu4.add(jSeparator4);

        jMenuItem12.setText("Table");
        jMenuItem12.setEnabled(false);
        jMenu4.add(jMenuItem12);

        jMenuItem28.setText("Database Table");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem28);

        jMenuItem16.setText("Installer Script");
        jMenuItem16.setEnabled(false);
        jMenu4.add(jMenuItem16);
        jMenu4.add(jSeparator5);

        jMenuItem17.setText("List View");
        jMenuItem17.setEnabled(false);
        jMenu4.add(jMenuItem17);

        jMenuItem19.setText("Details View");
        jMenuItem19.setEnabled(false);
        jMenu4.add(jMenuItem19);

        jMenuItem18.setText("List and Details");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem18);

        jMenuItem14.setText("Control Panel View");
        jMenuItem14.setEnabled(false);
        jMenu4.add(jMenuItem14);

        jMenu1.add(jMenu4);
        jMenu1.add(jSeparator2);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Save main project");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem15.setText("Save all projects");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem15);
        jMenu1.add(jSeparator3);

        jMenuItem2.setText("Exit Application");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Options");

        jMenuItem4.setText("Close all tabs");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem3.setText("Reload icons");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem26.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem26.setText("Refresh Tree");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem26);
        jMenu2.add(jSeparator7);

        jMenuItem5.setText("Edit syntactic formating");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem27.setText("Edit Database Details");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem27);

        jMenuBar1.add(jMenu2);

        jMenu6.setText("Design");
        jMenu6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu6ActionPerformed(evt);
            }
        });

        jMenuItem21.setText("New Drawing");
        jMenuItem21.setEnabled(false);
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem21);

        jMenu8.setText("Draw");
        jMenu8.setEnabled(false);

        jMenuItem25.setText("Box");
        jMenu8.add(jMenuItem25);

        jMenu6.add(jMenu8);

        jMenu7.setText("Simulation");
        jMenu7.setEnabled(false);

        jMenuItem22.setText("Start");
        jMenu7.add(jMenuItem22);

        jMenuItem23.setText("Pause");
        jMenu7.add(jMenuItem23);

        jMenuItem24.setText("Stop");
        jMenu7.add(jMenuItem24);

        jMenu6.add(jMenu7);

        jMenuBar1.add(jMenu6);

        jMenu5.setText("Theme");
        jMenuBar1.add(jMenu5);

        jMenu3.setText("Help");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem6.setText("About JEF");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem13.setText("JEF Help");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem13);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1181, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        add(INT_SQL_FILE);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        saveMainState();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        saveExtendedState();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButtonChooseJoomlaDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseJoomlaDirectoryActionPerformed

        FileUtils.selectFile(this, jTextFieldJoomlaDirectory, jFileChooser1,
                JFileChooser.DIRECTORIES_ONLY);
    }//GEN-LAST:event_jButtonChooseJoomlaDirectoryActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        FileUtils.selectFile(this, jTextFieldConfigurationFile, jFileChooser1,
                new String[]{"xml"}, "Select the configuration file.");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        editorSourceTextComponent.setText(jTextAreaTextEditor.getText());
        JDialogTextEditor.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        JDialogTextEditor.setVisible(false);
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        showTextEditor(jTextAreaLicence, "Edit licence");
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        showTextEditor(jTextAreaCopyright, "Edit Copyright");
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        showTextEditor(jTextAreaSummary, "Edit Summary");
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jTextFieldAuthorNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAuthorNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAuthorNameActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        add(INT_CONTROLLER);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        add(INT_COMPONENT);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
//        add(INT_VIEW_LIST_DETAILS);
        newListAndDetails();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jComboBoxJoomlaArtifactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxJoomlaArtifactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxJoomlaArtifactActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        add(INT_VIEW_LIST);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        add(INT_VIEW_CP);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        add(INT_MODEL);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        add(INT_VIEW);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        add(INT_SCRIPT);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        add(INT_LAYOUT);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        add(INT_TABLE);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        add(INT_SQL_FILE);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        add(INT_SQL_FILE);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        isCreate = false;
        jDialogInformationGetter.setVisible(false);
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed

        Object obj = jComboBoxSelectProject.getSelectedItem();
        if (!(obj instanceof Project)) {
            Toolkit.getDefaultToolkit().beep();
            alert("Please specify the project");
            return;
        }
        projectToEdit = (Project) obj;
        isCreate = true;
        //Set some variables
        name = jTextFieldArtifactName.getText();
        isAdmin = jCheckBoxArtifactInAdmin.isSelected();
        component_name = codeGeneraror.getExtensionInfo().getAppName();
        type = jComboBoxJoomlaArtifact.getSelectedItem().toString();

        switch (type) {
            case INT_VIEW:
            case INT_MODEL:
            case INT_CONTROLLER:
                if ("".equals(name)) {
                    Toolkit.getDefaultToolkit().beep();
                    alert("Please specify the name of the " + type);
                    return;
                }
        }
        jDialogInformationGetter.setVisible(false);
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        add(INT_VIEW_DETAILS);
    }//GEN-LAST:event_jButton16ActionPerformed
    private JDialogColorCodeEditor colors;

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (colors == null) {
            colors = JDialogColorCodeEditor.getNewInstance(
                    this, currentTheme);
        }
        colors.setThemeColors(currentTheme);
        colors.setLocationRelativeTo(this);
        colors.setVisible(true);

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        tabbedPane.cloaseAllTabs();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        warning("Refreshing Icons ... ");
        refreshIcons();
        refreshProjectsTree();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
    }//GEN-LAST:event_jMenu3ActionPerformed
    private AboutDialog aboutDialog;
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        if (aboutDialog == null) {
            aboutDialog = AboutDialog.createNewInstance(this);
        }
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setThemeColors(currentTheme);
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    protected FuscardLogo getFuscardLogo() {
        if (fuscardLogo == null) {
            fuscardLogo = FuscardLogo.createNewInstance(this);
        }
        return fuscardLogo;
    }
    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed

        getFuscardLogo().setVisible(true);

    }//GEN-LAST:event_jButton26ActionPerformed
    private File lastOpened;
    private static final String LAST_OPENED_PROJECT = "last_opened_project";

    private void ensureLastOpened() {
        if (lastOpened == null) {
            String res = Application.getInstance().getValue(LAST_OPENED_PROJECT);
            res = res == null ? "" : res;
            lastOpened = new File(res);
        }
    }

    private void openProject() {
        ensureLastOpened();
        jFileChooser1.setAcceptAllFileFilterUsed(false);
        File opened = FileUtils.selectFile(this, lastOpened.getPath(),
                jFileChooser1, new String[]{"jefp"},
                "JEF project file (*.jefp)");
        if (opened != null) {
            lastOpened = opened;
            try {
                Application.getInstance().setValue(LAST_OPENED_PROJECT,
                        opened.getPath());
            } catch (FuscardXMLException ex) {
            }
            Project project = new Project();
            project.setProjectConfigurationFile(Paths.get(opened.getPath()));
            bootStrapProject(project);
        }
    }
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        openProject();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Project project = getMainProject();
        if (project != null) {
            try {
                project.save();
            } catch (FuscardProjectException | FuscardXMLException ex) {
                Logger.getLogger(AllSystems.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        for (Project project : projects) {
            try {
                project.save();
            } catch (FuscardProjectException | FuscardXMLException ex) {
                Logger.getLogger(AllSystems.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed
    private JEFHelp jEFHelp;

    private void ensureHelp() {
        if (jEFHelp == null) {
            jEFHelp = new JEFHelp();
            jEFHelp.setIconImage(getIconImage());
            Dimension ad = getSize();
            ad.height -= 100;
            ad.width -= 200;
            jEFHelp.setSize(ad);
            jEFHelp.setLocationRelativeTo(this);
        }
    }
    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        ensureHelp();
        jEFHelp.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu6ActionPerformed
    }//GEN-LAST:event_jMenu6ActionPerformed

    private void showNewDrawingDialog() {
        infor("Show new drawing dialog");
        if (projects.isEmpty()) {
            alert("There is no open drawing.");
            return;
        }
        jComboBoxChooseProject.removeAllItems();
        for (Project project : projects) {
            jComboBoxChooseProject.addItem(project);
        }
        Project main = getMainProject();
        if (main != null) {
            jComboBoxChooseProject.setSelectedItem(main);
        }
        jTextFieldDrawingName.setText("");
        jDialogNewDrawing.pack();
        jDialogNewDrawing.setLocationRelativeTo(this);
        infor("About to set new visible");

        jDialogNewDrawing.setVisible(true);
        infor("After setting visible");
    }

    private void createNewDrawing(String drawingName, Project project) {
        DrawingManager dm = Application.getInstance().getDrawingManager();
        dm.newDrawing(project, tabbedPane, drawingName);
    }
    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        String drawingName = jTextFieldDrawingName.getText().trim();
        Object selectedItem = jComboBoxChooseProject.getSelectedItem();
        if (selectedItem == null || !(selectedItem instanceof Project)) {
            alert("Please select a project");
            return;
        }
        if ("".equals(drawingName)) {
            alert("Please enter the Drawing name");
            return;
        }
        jDialogNewDrawing.setVisible(false);
        createNewDrawing(drawingName, (Project) selectedItem);
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        jDialogNewDrawing.setVisible(false);
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        infor("action performed");
        showNewDrawingDialog();
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jFormattedTextFieldCanvasPreferredHeightPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCanvasPreferredHeightPropertyChange
        String s = evt.getPropertyName();
        if ("value".equals(s)) {
            String ss = ((JFormattedTextField) evt.getSource()).getText();
            if (!"".equals(ss)) {
                Application.getInstance().getDrawingManager().setPreferredCanvasHeight(
                        Integer.parseInt(ss));
            }
        }
    }//GEN-LAST:event_jFormattedTextFieldCanvasPreferredHeightPropertyChange

    private void jFormattedTextFieldCanvasPreferredWidthPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCanvasPreferredWidthPropertyChange
        String s = evt.getPropertyName();
        if ("value".equals(s)) {
            String ss = ((JFormattedTextField) evt.getSource()).getText();
            if (!"".equals(ss)) {
                Application.getInstance().getDrawingManager().setPreferredCanvasWidth(
                        Integer.parseInt(ss));
            }
        }
    }//GEN-LAST:event_jFormattedTextFieldCanvasPreferredWidthPropertyChange
    private Color canvasColor;
    private void jTextFieldCanvasColorPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextFieldCanvasColorPropertyChange
        //
    }//GEN-LAST:event_jTextFieldCanvasColorPropertyChange

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        Color c = JColorChooser.showDialog(rootPane, "Canvas color", canvasColor);
        if (c == null) {
            return;
        }
        jTextFieldCanvasColor.setText("0x" + Integer.toHexString(c.getRGB()));
        Application.getInstance().getDrawingManager().setCanvasColor(c);
        canvasColor = c;
        jTextFieldCanvasColor.setBackground(c);
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        Form form = new Form();
        Application.getInstance().getDrawingManager().setSimulatingNodeToDrop(
                form.getApplicationSimulatingNode());
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        Table table = new Table();
        Application.getInstance().getDrawingManager().setSimulatingNodeToDrop(
                table.getApplicationSimulatingNode());
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        factory.design.element.Field f = new factory.design.element.Field();
        Application.getInstance().getDrawingManager().setSimulatingNodeToDrop(
                f.getApplicationSimulatingNode());
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        Text t = new Text("Hawayu");
        Application.getInstance().getDrawingManager().setSimulatingNodeToDrop(
                t.getApplicationSimulatingNode());
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        refreshProjectsTree();
    }//GEN-LAST:event_jMenuItem26ActionPerformed
    private void newProject() {
        ExtensionInformation context = codeGeneraror.getExtensionInfo();
        jTextFieldNewProjectAuthor.setText(context.getAuthor());
        jTextFieldNewProjectAuthorEmail.setText(context.getAuthor_mail());
        jTextFieldNewProjectAuthorWebsite.setText(context.getAuthor_website());
        jTextFieldNewProjectDefaultVersion.setText(context.getVersion());
        jTextFieldNewProjectJoomlaDirectory.setText(context.getJoomlaDirectory());
        jTextFieldNewProjectProjectName.setText(context.getAppName());
        jTextAreaNewProjectCopyright.setText(context.getCopyright());
        jTextAreaNewProjectDescription.setText(context.getSummary());
        jTextAreaNewProjectLicence.setText(context.getLicense());
        String us = Application.getInstance().getValue(xmlupdateServer);
        jTextFieldNewProjectUpdateServer.setText(us == null ? "" : us);
        jDialogNewProject.pack();
        jDialogNewProject.setLocationRelativeTo(this);

        jDialogNewProject.setVisible(true);
    }
    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        newProject();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        jDialogNewProject.setVisible(false);
    }//GEN-LAST:event_jButton40ActionPerformed
    private String xmlupdateServer = "update_server";
    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        try {
            String version = jTextFieldNewProjectDefaultVersion.getText(),
                    appname = jTextFieldNewProjectProjectName.getText(),
                    updateServer = jTextFieldNewProjectUpdateServer.getText(),
                    joomlaDir = jTextFieldNewProjectUpdateServer.getText();
            if ("".equals(appname)) {
                alert("Please enter the project name");
                return;
            }

            if (!(version.matches(
                    "\\d+(\\.\\d+)+"))) {
                alert("The version has to be in the format i.i(.i)+ where i is a positive integer."
                        + "For example 15.0.2.256");
                return;
            }
            if (!new File(updateServer).isDirectory()) {
                alert("The update server directory you entered is not a valid directory.");
                return;
            }
            if (!new File(joomlaDir).isDirectory()) {
                alert("The joomla directory you entered is not a valid directory.");
                return;
            }

            Application.getInstance().setValue(xmlupdateServer, updateServer);
            ExtensionInformation context = new ExtensionInformation();
            context.setAuthor(jTextFieldNewProjectAuthor.getText());
            context.setAuthor_mail(jTextFieldNewProjectAuthorEmail.getText());
            context.setAuthor_website(
                    jTextFieldNewProjectAuthorWebsite.getText());
            context.setVersion(version);
            context.setJoomlaDirectory(joomlaDir);
            context.setAppName(appname);
            context.setCopyright(jTextAreaNewProjectCopyright.getText());
            context.setSummary(jTextAreaNewProjectDescription.getText());
            context.setLicense(jTextAreaNewProjectLicence.getText());
            Project p = new Project();
            p.setExtensionInformation(context);
            p.setProjectConfigurationFile(Paths.get(
                    updateServer + "\\" + appname + ".jefp"));
            p.save();
            p.loadFromConfigurationFile();
            bootStrapProject(p);
            refreshProjectsTree();
            jDialogNewProject.setVisible(false);
        } catch (FuscardProjectException | FuscardXMLException ex) {
            Logger.getLogger(AllSystems.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        showTextEditor(jTextAreaNewProjectLicence, "Edit Licence");
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        showTextEditor(jTextAreaNewProjectCopyright, "Edit Copyright");
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        showTextEditor(jTextAreaNewProjectDescription, "Edit Description");
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        FileUtils.selectDirectory(this, jTextFieldNewProjectUpdateServer,
                jFileChooser1);
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        FileUtils.selectDirectory(this, jTextFieldNewProjectJoomlaDirectory,
                jFileChooser1);
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        newProject();
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void newListAndDetails() {
        jDialogNewDetailsListcanceled = true;
        DefaultTableModel model = (DefaultTableModel) jTableNewListAndDetailsFields.getModel();
        model.setRowCount(0);
        jComboBoxNewListAndDetailsSelectProject.removeAllItems();
        for (Project p : projects) {
            jComboBoxNewListAndDetailsSelectProject.addItem(p);
        }
        jDialogNewDetailsList.pack();
        jDialogNewDetailsList.setLocationRelativeTo(this);
        jDialogNewDetailsList.setVisible(true);
        if (!jDialogNewDetailsListcanceled) {
            //
            ArrayList<CodeGeneraror.JEFField> fields = new ArrayList<>();
            for (int row = 0; row < model.getRowCount(); row++) {
                CodeGeneraror.JEFField f = new CodeGeneraror.JEFField();
                f.setName((String) model.getValueAt(row, 0));//Name
                f.setDisplayName((String) model.getValueAt(
                        row, 1));//DisplayName
                f.setDescription((String) model.getValueAt(
                        row, 2));//Description
                f.setType((String) model.getValueAt(
                        row, 3));//Type
                f.setRequired((boolean) model.getValueAt(
                        row, 4)); //Required
                f.setDefault((String) model.getValueAt(row, 5));//Default
                fields.add(f);
            }
            //
            Project p = (Project) jComboBoxNewListAndDetailsSelectProject.getSelectedItem();
            String listName = jTextFieldNewDetailsListListName.getText();
            String detailsName = jTextFieldNewDetailsListDetailsName.getText();
            String tableName = jTextFieldNewDetailsListTableName.getText();
            boolean inAdmin = jRadioButtonNewDetilsListLocationAdmin.isSelected();
            try {
                codeGeneraror.addListAndDetails(listName, detailsName, tableName,
                        fields.toArray(new CodeGeneraror.JEFField[0]), inAdmin,
                        p.getExtensionInformation());
            } catch (CodeGeneratorException ex) {
                alert(ex.getMessage());
            }
        }
    }
    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        newListAndDetails();
    }//GEN-LAST:event_jMenuItem18ActionPerformed
    private boolean jDialogNewDetailsListcanceled = true;
    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        if (!(jComboBoxNewListAndDetailsSelectProject.getSelectedItem() instanceof Project)) {
            alert("Please select a project");
            return;
        }
        jDialogNewDetailsListcanceled = false;
        jDialogNewDetailsList.setVisible(false);
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        jDialogNewDetailsList.setVisible(false);
    }//GEN-LAST:event_jButton47ActionPerformed
    private void showNewFieldDialog() {
        jDialogAddNewFieldTypecanceled = true;
        jDialogAddNewFieldType.pack();
        jDialogAddNewFieldType.setLocationRelativeTo(this);
        jDialogAddNewFieldType.setVisible(true);
        if (!jDialogAddNewFieldTypecanceled) {
            DefaultTableModel model = (DefaultTableModel) jTableNewListAndDetailsFields.getModel();
            model.addRow(new Object[]{
                jTextFieldNewFieldName.getText(),//Name
                jTextFieldNewFieldDisplayName.getText(),//DisplayName
                jTextFieldNewFieldDescription.getText(),//Description
                jComboBoxNewFieldType.getSelectedItem(),//Type
                jCheckBoxNewFieldRequired.isSelected(), //Required
                jTextFieldNewFieldDefault.getText(),//Default
            });
        }
    }
    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        showNewFieldDialog();

    }//GEN-LAST:event_jButton44ActionPerformed
    private boolean jDialogAddNewFieldTypecanceled = true;
    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed

        jDialogAddNewFieldType.setVisible(false);
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        String selectedItem = (String) jComboBoxNewFieldType.getSelectedItem(); //Type
        if (selectedItem.equals(FieldTypes.FIELD_TYPE_CHECK)) {
            String def = jTextFieldNewFieldDefault.getText();
            if (!"".equals(def) && !def.matches("[01]")) {
                alert("The default value of type checkbox can only be one of 0 and 1");
                return;
            }
        }
        jDialogAddNewFieldTypecanceled = false;
        jDialogAddNewFieldType.setVisible(false);
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed

        int index = jTableNewListAndDetailsFields.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) jTableNewListAndDetailsFields.getModel();
        if (index != -1) {
            jTextFieldNewFieldName.setText((String) model.getValueAt(index, 0));//Name
            jTextFieldNewFieldDisplayName.setText((String) model.getValueAt(
                    index, 1));//DisplayName
            jTextFieldNewFieldDescription.setText((String) model.getValueAt(
                    index, 2));//Description
            jComboBoxNewFieldType.setSelectedItem((String) model.getValueAt(
                    index, 3));//Type
            jCheckBoxNewFieldRequired.setSelected((boolean) model.getValueAt(
                    index, 4)); //Required
            jTextFieldNewFieldDefault.setText(
                    (String) model.getValueAt(index, 5));//Default
        }
        jDialogAddNewFieldTypecanceled = true;
        jDialogAddNewFieldType.setVisible(true);
        if (!jDialogAddNewFieldTypecanceled) {
            model.removeRow(index);
            model.insertRow(index,
                    new Object[]{jTextFieldNewFieldName.getText(),//Name
                jTextFieldNewFieldDisplayName.getText(),//DisplayName
                jTextFieldNewFieldDescription.getText(),//Description
                jComboBoxNewFieldType.getSelectedItem(),//Type
                jCheckBoxNewFieldRequired.isSelected(), //Required
                jTextFieldNewFieldDefault.getText()
            });
        }

    }//GEN-LAST:event_jButton50ActionPerformed
    private JEFDatabaseTable currentDBTable;

    private void showjDialogDatabaseTableEditor(JEFDatabaseTable table) {
        currentDBTable = table;
        /**
         * Cannot edit table without projects
         */
        if (projects.isEmpty()) {
            alert("Seems like there are no open projects. Please open a projet first or create one to edit a table");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) jTableDatabaseTableEditorFields.getModel();
        /**
         * Prepare the dialog
         */
        boolean isEditing = false;
        if (table != null) {
            isEditing = true;
        }
        jComboBoxDatabaseTableEditorProject.removeAllItems();
        for (Project p : projects) {
            jComboBoxDatabaseTableEditorProject.addItem(p);
        }

        //If we are editing freeze the project combo. We dont want people changing the project in editing mode
        if (isEditing) {
            jComboBoxDatabaseTableEditorProject.setEnabled(false);
        } else {
            jComboBoxDatabaseTableEditorProject.setEnabled(true);
        }
        if (table != null) {
            jTextFieldDatabaseTableEditorNameOfTable.setText(table.getName());
            jTextFieldDatabaseTableEditorTableAlias.setText(table.getAlias());
            jComboBoxDatabaseTableEditorProject.setSelectedItem(
                    table.getProject());
            CodeGeneraror.JEFField[] fields = table.getFields();
            for (CodeGeneraror.JEFField f : fields) {
                model.addRow(new Object[]{
                    f.getName(),//Name
                    f.getAlias(),//Alias
                    f.getType(),//Type
                    f.getSize(),//size
                    f.isRequired(),//Required
                    f.getDefault()//Default
                });
            }
        }
        /**
         * Display the dialog
         */
        jDialogDatabaseTableEditorapproved = false;
        jDialogDatabaseTableEditor.pack();
        jDialogDatabaseTableEditor.setLocationRelativeTo(this);
        jDialogDatabaseTableEditor.setVisible(true);
        if (jDialogDatabaseTableEditorapproved) {
            if (table == null) {
                table = new JEFDatabaseTable();
            }
            String tableName = jTextFieldDatabaseTableEditorNameOfTable.getText();
           if(isEditing){
               for(JEFDatabaseTable jeft : table.getProject().getjEFDatabaseTables()){
                   //Check availability
                   if(jeft != currentDBTable && jeft.getName().equals(CodeGeneraror.normalizeName(tableName))){
                       alert("Name in use");
                       showjDialogDatabaseTableEditor(currentDBTable);
                       return;
                   }
               }
           }
            
            String tableAlias = jTextFieldDatabaseTableEditorTableAlias.getText();
            table.setName(tableName);
            table.setAlias(tableAlias);
            LinkedList<CodeGeneraror.JEFField> fields1 = new LinkedList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                CodeGeneraror.JEFField field = new CodeGeneraror.JEFField();
                int j = 0;
                field.setName((String) model.getValueAt(i, j++));
                field.setAlias((String) model.getValueAt(i, j++));
                field.setType((String) model.getValueAt(i, j++));
                field.setSize((int) model.getValueAt(i, j++));
                field.setRequired((boolean) model.getValueAt(i, j++));
                field.setDefault((String) model.getValueAt(i, j++));
                fields1.add(new CodeGeneraror.JEFField());
            }
            table.setFields(fields1.toArray(new CodeGeneraror.JEFField[0]));
            if (!isEditing) {
                /**
                 * Add this table to the selected project
                 */
                Object selectedItem = jComboBoxDatabaseTableEditorProject.getSelectedItem();
                if (!(selectedItem instanceof Project)) {
                    alert("Please select a project");
                    showjDialogDatabaseTableEditor(
                            currentDBTable);
                    return;
                }
                Project project = (Project) selectedItem;
                project.addDatabaseTable(table);

            }
            refreshProjectsTree();
            /**
             * Clear the editor
             */
            jTextFieldDatabaseTableEditorNameOfTable.setText("");
            jTextFieldDatabaseTableEditorTableAlias.setText("");
            model.setRowCount(
                    0);
            jComboBoxDatabaseTableEditorProject.removeAllItems();

        }
    }
    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        populatejDialogDatabaseDetails();
        showjDialogDatabaseDetails();
    }//GEN-LAST:event_jMenuItem27ActionPerformed
    private boolean jDialogDatabaseDetailsapproved = false;
    private String x_installation = "installation";
    private String x_friendlyName = "friendly-name";
    private String x_dbPrefix = "database-prefix";
    private String x_dbUrl = "database-url";
    private String x_username = "username";
    private String x_password = "password";

    private void populatejDialogDatabaseDetails() {
        try {

            DefaultTableModel model = (DefaultTableModel) jTableJoomlaInfo.getModel();
            model.setRowCount(0);
            String pathToConfig = Application.getInstance().getJoomlaConfigurationPath();
            XMLDocument config = new XMLDocument(pathToConfig);
            config.loadFromFile();
            NodeList nodes = config.getNodes("root/joomla_info/info");
            for (int i = 0; i < nodes.getLength(); i++) {

                Node node = nodes.item(i);
                if (node == null) {
                    continue;
                }
                model.addRow(new Object[]{
                    XMLDocument.getNode(x_friendlyName, node).getTextContent(),
                    XMLDocument.getNode(x_installation, node).getTextContent(),
                    XMLDocument.getNode(x_dbPrefix, node).getTextContent(),
                    XMLDocument.getNode(x_dbUrl, node).getTextContent(),
                    XMLDocument.getNode(x_username, node).getTextContent(),
                    XMLDocument.getNode(x_password, node).getTextContent(),});
            }
        } catch (FuscardXMLException ex) {
            Logger.getLogger(AllSystems.class.getName()).log(Level.SEVERE, null,
                    ex);
            alert("could not populate table: " + ex.getMessage());

        }
    }

    private void showjDialogDatabaseDetails() {

        jDialogDatabaseDetailsapproved = false;
        jDialogDatabaseDetails.pack();
        jDialogDatabaseDetails.setLocationRelativeTo(this);
        jDialogDatabaseDetails.setVisible(true);
        if (jDialogDatabaseDetailsapproved) {

            DefaultTableModel model = (DefaultTableModel) jTableJoomlaInfo.getModel();
            /**
             * Save the info
             */
            String pathToConfig;
            try {
                pathToConfig = Application.getInstance().getJoomlaConfigurationPath();

                XMLDocument config = new XMLDocument(pathToConfig);
                config.loadFromFile();
                String[] pathParts = new String[]{"root", "joomla_info"};
                //Remove the stored iformation
                config.clear(pathParts);
                org.w3c.dom.Document document = config.getDocument();
                Node xinfor;
                xinfor = XMLDocument.getNode("joomla_info",
                        document.getDocumentElement());
                int rowCount = model.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    String friendlyName = (String) model.getValueAt(i, 0),
                            installationFolder = (String) model.getValueAt(i, 1),
                            databasePrefix = (String) model.getValueAt(i, 2),
                            databaseUrl = (String) model.getValueAt(i, 3),
                            username = (String) model.getValueAt(i, 4),
                            password = (String) model.getValueAt(i, 5);
                    org.w3c.dom.Element elem = document.createElement("info");
                    xinfor.appendChild(elem);

                    XMLDocument.addChildNode(document, elem, x_dbPrefix,
                            databasePrefix);
                    XMLDocument.addChildNode(document, elem, x_dbUrl,
                            databaseUrl);
                    XMLDocument.addChildNode(document, elem, x_friendlyName,
                            friendlyName);
                    XMLDocument.addChildNode(document, elem, x_installation,
                            installationFolder);
                    XMLDocument.addChildNode(document, elem, x_password,
                            password);
                    XMLDocument.addChildNode(document, elem, x_username,
                            username);

                }
                config.saveToFile();
            } catch (FuscardXMLException ex) {
                ex.printStackTrace();
                alert("Could not save the Joomla information. " + ex.getMessage());
            }
        }
    }
    private void jButton54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton54ActionPerformed
        jDialogDatabaseDetails.setVisible(false);
    }//GEN-LAST:event_jButton54ActionPerformed
    private boolean jDialogDatabaseTableEditorapproved = false;
    private void jButton55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton55ActionPerformed
        jDialogDatabaseDetailsapproved = true;
        jDialogDatabaseDetails.setVisible(false);
    }//GEN-LAST:event_jButton55ActionPerformed

    private void jButton60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton60ActionPerformed
        jDialogDatabaseDetailsRecordEditor.setVisible(false);
    }//GEN-LAST:event_jButton60ActionPerformed

    private void jButton61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton61ActionPerformed
        jDialogDatabaseDetailsRecordEditorApproved = true;
        jDialogDatabaseDetailsRecordEditor.setVisible(false);
    }//GEN-LAST:event_jButton61ActionPerformed
    private boolean jDialogDatabaseDetailsRecordEditorApproved = false;

    private void showjDialogDatabaseDetailsRecordEditor(int row) {
        DefaultTableModel model = (DefaultTableModel) jTableJoomlaInfo.getModel();
        /**
         * Populate the table first
         */
        if (row != -1) {
            String friendlyName = (String) model.getValueAt(row, 0),
                    installationFolder = (String) model.getValueAt(row, 1),
                    databasePrefix = (String) model.getValueAt(row, 2),
                    databaseUrl = (String) model.getValueAt(row, 3),
                    username = (String) model.getValueAt(row, 4),
                    password = (String) model.getValueAt(row, 5);
            jTextFieldDatabaseDetailsRecordEditorNameOfJoomla.setText(
                    friendlyName);
            jTextFieldDatabaseDetailsRecordEditorJoomlaPath.setText(
                    installationFolder);
            jTextFieldDatabaseDetailsRecordEditorJoomlaTablePrefix.setText(
                    databasePrefix);
            jTextFieldDatabaseDetailsRecordEditorDbUrl.setText(databaseUrl);
            jTextFieldDatabaseDetailsRecordEditorDbUsername.setText(username);
            jPasswordFieldDatabaseDetailsRecordEditorPassword.setText(password);
            jPasswordFieldDatabaseDetailsRecordEditorPassword2.setText(password);
        }

        //

        jDialogDatabaseDetailsRecordEditorApproved = false;
        jDialogDatabaseDetailsRecordEditor.pack();
        jDialogDatabaseDetailsRecordEditor.setLocationRelativeTo(this);

        jDialogDatabaseDetailsRecordEditor.setVisible(true);
        if (jDialogDatabaseDetailsRecordEditorApproved) {
            String friendlyName = jTextFieldDatabaseDetailsRecordEditorNameOfJoomla.getText();
            String installationFolder = jTextFieldDatabaseDetailsRecordEditorJoomlaPath.getText();
            String databasePrefix = jTextFieldDatabaseDetailsRecordEditorJoomlaTablePrefix.getText();
            String databaseUrl = jTextFieldDatabaseDetailsRecordEditorDbUrl.getText();
            String username = jTextFieldDatabaseDetailsRecordEditorDbUsername.getText();
            String password = jPasswordFieldDatabaseDetailsRecordEditorPassword.getText();
            String password2 = jPasswordFieldDatabaseDetailsRecordEditorPassword2.getText();
            boolean complete = true;
            for (String s : new String[]{
                friendlyName,
                installationFolder,
                databasePrefix,
                databaseUrl,
                username,}) {
                if (s == null || "".equals(s.trim())) {
                    complete = false;
                    break;
                }
            }
            if (!complete) {
                alert("Please fill in all the required fields.");
                showjDialogDatabaseDetailsRecordEditor(row);
                return;
            }
            if (password == null ? password2 != null : !password.equals(
                    password2)) {
                alert("Your passwords do not match");
                showjDialogDatabaseDetailsRecordEditor(row);
                return;
            }
            /**
             * Ensure the installation directory is unique
             */
            File install = new File(installationFolder);

            int c = model.getRowCount();
            int rr = -1;
            boolean editing = false;
            if (row != -1) {
                rr = jTableJoomlaInfo.convertRowIndexToModel(row);
                editing = true;
            }
            for (int i = 0; i < c; i++) {
                String frndnm = (String) model.getValueAt(i, 0);
                String inst = (String) model.getValueAt(i, 1);
                File install2 = new File(inst);
                boolean clash = false;
                if (!editing && install.equals(install2)) {
                    clash = true;
                } else if (editing && rr != row && install.equals(install2)) {
                    clash = true;
                }
                if (clash) {
                    alert("Installation folder in use");
                    showjDialogDatabaseDetailsRecordEditor(row);
                    return;
                }
                clash = false;
                if (!editing && frndnm.trim().equalsIgnoreCase(
                        friendlyName.trim())) {
                    clash = true;
                } else if (editing && rr != row && frndnm.trim().equalsIgnoreCase(
                        friendlyName.trim())) {
                    clash = true;
                }
                if (clash) {
                    alert("Friendly name in use.");
                    showjDialogDatabaseDetailsRecordEditor(row);
                    return;
                }
            }
            if (editing) {

                model.setValueAt(friendlyName, row, 0);
                model.setValueAt(installationFolder, row, 1);
                model.setValueAt(databasePrefix, row, 2);
                model.setValueAt(databaseUrl, row, 3);
                model.setValueAt(username, row, 4);
                model.setValueAt(password, row, 5);
            } else {
                model.addRow(new Object[]{
                    friendlyName,
                    installationFolder,
                    databasePrefix,
                    databaseUrl,
                    username,
                    password
                });
            }
            /**
             * Reset the fields
             */
            for (JTextComponent tc : new JTextComponent[]{
                jTextFieldDatabaseDetailsRecordEditorNameOfJoomla,
                jTextFieldDatabaseDetailsRecordEditorJoomlaPath,
                jTextFieldDatabaseDetailsRecordEditorJoomlaTablePrefix,
                jTextFieldDatabaseDetailsRecordEditorDbUrl,
                jTextFieldDatabaseDetailsRecordEditorDbUsername,
                jPasswordFieldDatabaseDetailsRecordEditorPassword,
                jPasswordFieldDatabaseDetailsRecordEditorPassword2
            }) {
                tc.setText("");
            }
        }
    }
    private void jButton58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton58ActionPerformed
        int selectedRow = jTableJoomlaInfo.getSelectedRow();
        if (selectedRow == -1) {
            alert("Please make a row selection first");
        }
        showjDialogDatabaseDetailsRecordEditor(selectedRow);
    }//GEN-LAST:event_jButton58ActionPerformed

    private void removeRows(JTable table, int[] rows) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector dataVector = model.getDataVector();
        LinkedList ll = new LinkedList();
        for (int i : rows) {
            ll.add(dataVector.get(i));
        }
        for (Object obj : ll) {
            dataVector.remove(obj);
        }
        model.fireTableDataChanged();
    }
    private void jButton57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton57ActionPerformed
        int[] selectedRows = jTableJoomlaInfo.getSelectedRows();
        if (selectedRows.length == 0) {
            alert("You have not selected any rows to delete");
            return;
        }
        if (confirm(
                "Do you really want to delete " + selectedRows.length + " rows?")) {
            removeRows(jTableJoomlaInfo, selectedRows);
        }
    }//GEN-LAST:event_jButton57ActionPerformed

    private void jButton56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton56ActionPerformed
        showjDialogDatabaseDetailsRecordEditor(-1);
    }//GEN-LAST:event_jButton56ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        jDialogDatabaseTableEditor.setVisible(false);
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton53ActionPerformed
        jDialogDatabaseTableEditorapproved = true;
        jDialogDatabaseTableEditor.setVisible(false);
    }//GEN-LAST:event_jButton53ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        showjDialogDatabaseTableEditor(null);
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jButton59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton59ActionPerformed
        FileUtils.selectDirectory(this,
                jTextFieldDatabaseDetailsRecordEditorJoomlaPath, jFileChooser1);
    }//GEN-LAST:event_jButton59ActionPerformed

    private void jButton62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton62ActionPerformed
        String databaseUrl = jTextFieldDatabaseDetailsRecordEditorDbUrl.getText(),
                username = jTextFieldDatabaseDetailsRecordEditorDbUsername.getText(),
                password = jPasswordFieldDatabaseDetailsRecordEditorPassword.getText(),
                password2 = jPasswordFieldDatabaseDetailsRecordEditorPassword2.getText();
        if ("".equals(databaseUrl) || "".equals(username)) {
            alert("Please enter the database URL and the username");
            return;
        }
        if (password == null ? password2 != null : !password.equals(password2)) {
            alert("Your passwords do not match");
            return;
        }
        try {
            try (Connection con = DriverManager.getConnection(databaseUrl,
                    username,
                    password)) {
                alert("The connection was successful.");
            }
        } catch (SQLException ex) {
            alert("There was an error with the connection. " + ex.getMessage());
        }

    }//GEN-LAST:event_jButton62ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        Object selectedItem = jComboBoxDatabaseTableEditorProject.getSelectedItem();
        if (selectedItem instanceof Project) {
            Project project = (Project) selectedItem;
            /**
             * Ensure that the project doesn't have a database table by that
             * name.
             */
            String nname = CodeGeneraror.normalizeName(
                    jTextFieldDatabaseTableEditorNameOfTable.getText());
            for (JEFDatabaseTable table : project.getjEFDatabaseTables()) {
                if (table == currentDBTable) {
                    continue;
                }
                if (nname == null ? table.getName() == null : nname.equals(
                        table.getName())) {
                    alert("Database table name in use.");
                    return;
                }
            }
            alert("The name is available");
        } else {
            alert("Please select a project");
        }
    }//GEN-LAST:event_jButton51ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;


                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.
                    getLogger(AllSystems.class
                    .getName()).
                    log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AllSystems allSystems = new AllSystems();
                allSystems.setVisible(true);
            }
        });
    }

    private JComponent[] getDeclaredComponents() {
        Field[] fields = getClass().getDeclaredFields();
        LinkedList<JComponent> components = new LinkedList<>();


        for (Field f : fields) {
            if (JComponent.class
                    .isAssignableFrom(f.getType())) {

                try {
                    components.add((JComponent) f.get(this));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    continue;
                }
            }
        }
        return components.toArray(new JComponent[0]);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog JDialogTextEditor;
    private javax.swing.ButtonGroup buttonGroupNewListAndDetailsLocation;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton55;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton57;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    private javax.swing.JButton jButton62;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonChooseJoomlaDirectory;
    private javax.swing.JCheckBox jCheckBoxArtifactInAdmin;
    private javax.swing.JCheckBox jCheckBoxNewFieldRequired;
    private javax.swing.JComboBox jComboBoxChooseProject;
    private javax.swing.JComboBox jComboBoxDatabaseTableEditorProject;
    private javax.swing.JComboBox jComboBoxJoomlaArtifact;
    private javax.swing.JComboBox jComboBoxNewFieldType;
    private javax.swing.JComboBox jComboBoxNewListAndDetailsSelectProject;
    private javax.swing.JComboBox jComboBoxSelectProject;
    private javax.swing.JDialog jDialogAddNewFieldType;
    private javax.swing.JDialog jDialogDatabaseDetails;
    private javax.swing.JDialog jDialogDatabaseDetailsRecordEditor;
    private javax.swing.JDialog jDialogDatabaseTableEditor;
    private javax.swing.JDialog jDialogInformationGetter;
    private javax.swing.JDialog jDialogNewDetailsList;
    private javax.swing.JDialog jDialogNewDrawing;
    private javax.swing.JDialog jDialogNewProject;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFormattedTextField jFormattedTextFieldCanvasPreferredHeight;
    private javax.swing.JFormattedTextField jFormattedTextFieldCanvasPreferredWidth;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelLoading;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenu jMenuRecentProjects;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordFieldDatabaseDetailsRecordEditorPassword;
    private javax.swing.JPasswordField jPasswordFieldDatabaseDetailsRecordEditorPassword2;
    private javax.swing.JRadioButton jRadioButtonNewDetilsListLocationAdmin;
    private javax.swing.JRadioButton jRadioButtonNewDetilsListLocationSite;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSpinner jSpinnerReleaseIndex;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPaneTools;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableDatabaseTableEditorFields;
    private javax.swing.JTable jTableJoomlaInfo;
    private javax.swing.JTable jTableNewListAndDetailsFields;
    private javax.swing.JTextArea jTextAreaCopyright;
    private javax.swing.JTextArea jTextAreaLicence;
    private javax.swing.JTextArea jTextAreaNewProjectCopyright;
    private javax.swing.JTextArea jTextAreaNewProjectDescription;
    private javax.swing.JTextArea jTextAreaNewProjectLicence;
    private javax.swing.JTextArea jTextAreaSummary;
    private javax.swing.JTextArea jTextAreaTextEditor;
    private javax.swing.JTextField jTextFieldAppName;
    private javax.swing.JTextField jTextFieldArtifactName;
    private javax.swing.JTextField jTextFieldAuthorEmail;
    private javax.swing.JTextField jTextFieldAuthorName;
    private javax.swing.JTextField jTextFieldAuthorURL;
    private javax.swing.JTextField jTextFieldCanvasColor;
    private javax.swing.JTextField jTextFieldConfigurationFile;
    private javax.swing.JTextField jTextFieldDMOutputArea;
    private javax.swing.JTextField jTextFieldDatabaseDetailsRecordEditorDbUrl;
    private javax.swing.JTextField jTextFieldDatabaseDetailsRecordEditorDbUsername;
    private javax.swing.JTextField jTextFieldDatabaseDetailsRecordEditorJoomlaPath;
    private javax.swing.JTextField jTextFieldDatabaseDetailsRecordEditorJoomlaTablePrefix;
    private javax.swing.JTextField jTextFieldDatabaseDetailsRecordEditorNameOfJoomla;
    private javax.swing.JTextField jTextFieldDatabaseTableEditorNameOfTable;
    private javax.swing.JTextField jTextFieldDatabaseTableEditorTableAlias;
    private javax.swing.JTextField jTextFieldDrawingName;
    private javax.swing.JTextField jTextFieldJoomlaDirectory;
    private javax.swing.JTextField jTextFieldNewDetailsListDetailsName;
    private javax.swing.JTextField jTextFieldNewDetailsListListName;
    private javax.swing.JTextField jTextFieldNewDetailsListTableName;
    private javax.swing.JTextField jTextFieldNewFieldDefault;
    private javax.swing.JTextField jTextFieldNewFieldDescription;
    private javax.swing.JTextField jTextFieldNewFieldDisplayName;
    private javax.swing.JTextField jTextFieldNewFieldName;
    private javax.swing.JTextField jTextFieldNewProjectAuthor;
    private javax.swing.JTextField jTextFieldNewProjectAuthorEmail;
    private javax.swing.JTextField jTextFieldNewProjectAuthorWebsite;
    private javax.swing.JTextField jTextFieldNewProjectDefaultVersion;
    private javax.swing.JTextField jTextFieldNewProjectJoomlaDirectory;
    private javax.swing.JTextField jTextFieldNewProjectProjectName;
    private javax.swing.JTextField jTextFieldNewProjectUpdateServer;
    private javax.swing.JTextField jTextFieldVersion;
    private javax.swing.JTextField jTextFieldViewDetailsName;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTree jTreeProject;
    // End of variables declaration//GEN-END:variables
}
