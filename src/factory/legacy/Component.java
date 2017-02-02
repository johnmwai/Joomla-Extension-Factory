package factory.legacy;
import com.fuscard.commons.FileWriter;
import com.fuscard.commons.MyFileReader;
import factory.packager.VersionWrapper;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 23, 2012 -- 9:41:03 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Component extends Extension {

    private EnumMap<MVCClassType, String> defaultViews = new EnumMap<MVCClassType, String>(
            MVCClassType.class);
    private View[] views = new View[0];
    private Model[] models = new Model[0];
    private Controller[] controllers = new Controller[0];
    private ComponentProperties properties = new ComponentProperties();
    private LinkedList<Table> tables = new LinkedList<Table>();
    protected View viewIndex = null;
    private ScriptAssistance scriptAssistance = new ScriptAssistance(this);
    private String imagesFolder = null;
    private String adminMenuName = null;
    private String adminTitle = null;
    private String displayName = null;
    protected View controlPanel = null;
    private View rootPanel = null;
    private String tableDataFile = null;
    private boolean deleteDirectoryOnZip = false;

    public Component(ComponentProperties properties) {
        setProperties(properties);
    }

    public Component() {
    }

    public Component(View[] views, Model[] models, Controller[] controllers) {
        this.views = views;
        this.models = models;
        this.controllers = controllers;
    }

    public Component(View[] views) {
        this.views = views;
    }

    public void setControlPanel(View controlPanel) {
        this.controlPanel = controlPanel;
    }

    public void setRootPanel(View controlPanel) {
        this.rootPanel = controlPanel;
    }

    public View getRootPanel() {
        return rootPanel;
    }

    public View getControlPanel() {
        return controlPanel;
    }

    public String getAdminMenuName() {
        String res = adminMenuName;
        if (res == null) {
            res = getName();
        }
        return res;
    }

    public void setAdminMenuName(String adminMenuName) {
        this.adminMenuName = adminMenuName;
    }

    public Model[] getModels() {
        return models;
    }

    public View[] getViews() {
        return views;
    }

    public Table[] getTables() {
        return tables.toArray(new Table[0]);
    }

    public Controller[] getControllers() {
        return controllers;
    }

    public void setControllers(Controller[] controllers) {
        this.controllers = controllers;
    }

    public void setModels(Model[] models) {
        this.models = models;
    }

    public void setViews(View[] views) {
        this.views = views;
    }

    public ComponentProperties getProperties() {
        return properties;
    }

    public final void setProperties(ComponentProperties properties) {
        this.properties = properties;
        name = properties.getName();
        name = name.replaceAll("[^\\w\\d_]", "_");
        name = name.replaceFirst("^[\\d_]", "Ext");
        properties.setName(name);
        String pf = properties.getPropertiesFile();
        if (pf != null) {
            setPropertiesFile(pf);
        }
    }

    @Override
    public void setClient(String client) {
        super.setClient(client);
        properties.setClient(client);
    }

    @Override
    public void setVendor(String vendor) {
        super.setVendor(vendor);
        properties.setVendor(vendor);
    }

    @Override
    public void setVersion(String version) {
        properties.setVersion(version);
        super.setVersion(properties.getVersion());
    }

    @Override
    public void inspect() {
    }

    public void makeFolder(String key) {
        String path = generateComponentPath(key);
        makeDirRecursive(path);
    }

    private void makeDirRecursive(String path) {
        File file = new File(path);
        String ps = file.getParent();
        File parent = new File(ps);
        if (!parent.isDirectory()) {
            makeDirRecursive(file.getParent());
        }
        file.mkdir();
    }

    public String getAdminFolder() {
        return getExtensionRoot() + DS + properties.getAdmin();
    }

    public String getSiteFolder() {
        return getExtensionRoot() + DS + properties.getSite();
    }

    private ComponentProperties getOldProperties() {
        ComponentProperties oldProperties = null;
        try {
            oldProperties = (ComponentProperties) getPropertiesFromDirectory();
        } catch (IOException | ClassNotFoundException ignore) {
        }
        return oldProperties;
    }

    private void zipProxy() {
        try {
            zip();
        } catch (Exception ex) {
        }
    }

    private void storePropertiesObj() {
        try {
            storePropertiesObj(properties);
        } catch (IOException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    private void processVersion(ComponentProperties oldProperties) {
        if (oldProperties != null) {
            //Make sure we make a later version
            VersionWrapper vo = new VersionWrapper(oldProperties.getVersion());
            VersionWrapper vc = new VersionWrapper(properties.getVersion());
            int comp = vo.compare(vc);
            //if the older version is greater than the the one we set then use the older version incrementing it
            if (comp == VersionWrapper.SAME || comp == VersionWrapper.EARLIER) {
                vo.increment(properties.getReleaseIndex());
                setVersion(vo.getVersion());
            }
        }
        refreshDirectory();
    }

    private void refreshDirectory() {
        properties.setExtensionRoot(getExtensionRoot());
    }

    @Override
    public void setDirectory(String directory) {
        super.setDirectory(directory);
        refreshDirectory();
    }

    private void makeTableHistory(ComponentProperties oldProperties) {
        for (Model m : models) {
            m.requireTable();
        }
        readTableHistory();
        for (Table t : tdc.tables) {
            for (Field f : t.getFields()) {
                if (VersionWrapper.comp(t.getVersion(), f.getVersion()) == VersionWrapper.LESS_THAN) {
                    f.setVersion(t.getVersion());
                }
            }
        }
        makeTableHistory();
        writeTableHistory();
        makeSqlUpdateFiles();
    }

    public String getTableDataFile() {
        return tableDataFile;
    }

    public void setTableDataFile(String tableDataFile) {
        this.tableDataFile = tableDataFile;
    }

    private void readTableData() throws IOException {
        if (tableDataFile == null) {
            return;
        }

        File f = new File(tableDataFile);
        if (!f.isFile()) {
            return;
        }
        MyFileReader fr = new MyFileReader(tableDataFile);
        FileWriter fw = getSqlInstallAppendingFileWriter();
        String line = fr.readLine();
        while (line != null) {
            if (!"".equals(line.trim())) {

                StringTokenizer st = new StringTokenizer(line);

                if ("TABLE_DATA_INIT".equals(st.nextToken())) {
                    String tn = st.nextToken();
                    Table t = getTableByName(tn);
                    if (t == null) {
                        throw new IllegalStateException("Unknown table");
                    }
                    Record r = t.getRecord();
                    TableRecord tr = new TableRecord(t, r);
                    tr.initiate(fw);
                    line = fr.readLine();
                    while (line != null) {
                        if ("TABLE_DATA_TERMINATE".equals(line)) {
                            tr.terminate();
                            break;
                        } else {
                            HashMap<String, String> map = new HashMap<String, String>();
                            StringTokenizer stab = new StringTokenizer(line,
                                    "\t");
                            String mlin = stab.nextToken();
                            while (mlin != null) {
                                StringTokenizer sspac = new StringTokenizer(mlin,
                                        ";");
                                map.put(sspac.nextToken().trim(),
                                        sspac.nextToken().trim());
                                try {
                                    mlin = stab.nextToken();
                                } catch (Exception e) {
                                    mlin = null;
                                }
                            }
                            tr.putRow(map);
                        }
                        line = fr.readLine();
                    }
                }
            }
            line = fr.readLine();
        }
    }

    private void makeSqlUpdateFiles() {
        for (String s : tdc.releases) {
            makeFile("admin.sql.updates.mysql|" + s + ".sql");
        }
        for (Table t : tdc.tables) {
            if (t.getFields().length > 0) {
                FileWriter fw = obtainWriter(makeFile(
                        "admin.sql.updates.mysql|" + t.getVersion() + ".sql"),
                        true);
                FileWriter fi = obtainWriter(makeFile(
                        "admin.sql|install.mysql.utf8.sql"), true);
                t.writeSql(fw);
                t.writeSql(fi, true);
            }
            if (t.getTerminationVersion() != null) {
                FileWriter fwu = obtainWriter(
                        makeFile(
                        "admin.sql.updates.mysql|" + t.getTerminationVersion() + ".sql"),
                        true);
                t.writeUninstallSql(fwu);
            }
            for (Field f : t.getFields()) {
                if (f.getTerminationVersion() != null) {
                    scriptAssistance.addFieldToRemove(f);
                }
                if (VersionWrapper.comp(t.getVersion(), f.getVersion()) == VersionWrapper.LATER) {
                    scriptAssistance.addFieldToAdd(f);
                }
            }
        }
        makeFile("admin.sql|index.html");
        makeFile("admin.sql|install.mysql.utf8.sql");
        makeFile("admin.sql|uninstall.mysql.utf8.sql");
        makeFile("admin.sql.updates|index.html");
        makeFile("admin.sql.updates.mysql|index.html");
        makeFile("admin.sql.updates.mysql|" + version + ".sql");
    }

    private void writeTableHistory() {
        FileWriter fr;
        try {
            fr = getHistoryWriter();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot write the table history.");
        }
        fr.pushLine(" -- Table history file ");
        writeHeader(fr, " -- ");
        fr.pushLine(" -- Joomla Extension Factory");
        fr.pushLine(" -- ");
        fr.pushLine(" -- Beginning data");
        fr.pushLine(" -- ");
        String last = null;
        for (String s : tdc.releases) {
            if (last != null && VersionWrapper.comp(s, last) == VersionWrapper.LESS_THAN || last == null) {
                fr.pushLine("release " + s);
            }
            last = s;
        }

        fr.pushLine("release " + version);
        for (Table t : tdc.tables) {
            fr.pushLine("tabledef " + t.getName());
            fr.pushLine("init");//installation_name
            fr.pushLine("version " + t.getVersion());
            fr.pushLine("installation_name " + t.getInstallationName());
            if (t.getTerminationVersion() != null) {
                fr.pushLine("termination " + t.getTerminationVersion());
            }
            for (Field f : t.getFields()) {
                writeFieldHistory(fr, f);
            }
            fr.pushLine("end");
        }
        try {
            commitHistory();
        } catch (IOException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    private void writeFieldHistory(FileWriter fr, Field f) {
        fr.pushLine("fielddef");
        fr.pushLine("type " + f.getType().name());
        fr.pushLine("name " + f.getName());
        fr.pushLine("version " + f.getVersion());
        fr.pushLine("display_name");
        fr.pushLine(f.getDisplayName());
        fr.pushLine("desc_admin");
        fr.pushLine(f.getDescriptionAdmin());
        if (f.getTerminationVersion() != null) {
            fr.pushLine("termination_version " + f.getTerminationVersion());
        }
        fr.pushLine("desc_site");
        fr.pushLine(f.getDescriptionSite());
        fr.pushLine("end");
    }

    private boolean stringsEqual(String... strings) {
        return ScriptAssistance.stringsEqual(strings);
    }

    private void makeTableHistory() {

        for (Table t1 : tables) {
            boolean newTable = true;
            for (Table t : tdc.tables) {
                if (stringsEqual(t.getName(), t1.getName())) {
                    newTable = false;
                    for (Field f1 : t1.getFields()) {
                        boolean newField = true;
                        for (Field f : t.getFields()) {
                            if (stringsEqual(f.getName(), f1.getName())) {
                                newField = false;
                                break;
                            }
                        }
                        if (newField) {
                            f1.setVersion(version);
                            if (f1.type == DataTypes.PrimaryKey) {
                                for (Field ff : t.getFields()) {
                                    if (ff.type == DataTypes.PrimaryKey) {
                                        t.getRecord().removeField(ff);
                                    }
                                }
                            }

                            try {
                                t.getRecord().addField(f1);
                            } catch (Exception ex) {
                                System.err.println(
                                        "Table " + t1.getName() + " couldn't add field " + f1.name
                                        + "\nRecord Said: " + ex.getMessage());
                                Logger.getLogger(Component.class.getName()).log(
                                        Level.SEVERE, null, ex);
                            }
                        }
                    }
                    break;
                }
            }

            if (newTable) {
                t1.setVersion(version);
                for (Field f : t1.getFields()) {
                    f.setVersion(version);
                }
                tdc.tables.add(t1);
            }
        }
        for (Table t : tdc.tables) {
            boolean removeTable = true;
            for (Table t2 : tables) {
                if (stringsEqual(t.getName(), t2.getName())) {
                    removeTable = false;
                    if (t.getTerminationVersion() != null) {
                        t.setTerminationVersion(null);
                    }
                    for (Field f : t.getFields()) {
                        boolean removeField = true;
                        for (Field f2 : t2.getFields()) {
                            if (stringsEqual(f.getName(), f2.getName())) {
                                removeField = false;
                                f.setTerminationVersion(null);
                                break;
                            }
                        }
                        if (removeField) {
                            if (f.getTerminationVersion() == null) {
                                f.setTerminationVersion(version);
                            }
                        }
                    }
                    break;
                }
            }
            if (removeTable) {
                if (t.getTerminationVersion() == null
                        || VersionWrapper.comp(t.getTerminationVersion(),
                        version) == VersionWrapper.LESS_THAN) {
                    t.setTerminationVersion(version);

                }
            }
        }
        for (Table t : tdc.tables) {
            if (t.getTerminationVersion() != null) {
                for (Field f : t.getFields()) {
                    t.getRecord().removeField(f);
                }
            }
        }
    }

    private void readTableHistory() {
        MyFileReader fr;
        try {
            fr = getHistoryReader();
        } catch (IOException ex) {
            return;
        }
        String line = fr.readLine();
        String tablename = "";
        Component c = new Component();
        c.setVersion(version);
        while (line != null) {
            StringTokenizer st = new StringTokenizer(line);
            if (st.countTokens() > 0) {
                String t1 = st.nextToken();
                if ("tabledef".equals(t1)) {
                    tablename = st.nextToken();
                } else if ("init".equals(t1)) {
                    tdc.startTable(tablename, c);
                } else if ("end".equals(t1)) {
                    tdc.endTable();
                } else if ("version".equals(t1)) {
                    String s = st.nextToken();
                    tdc.t.setVersion(s);
                } else if ("installation_name".equals(t1)) {//
                    tdc.t.setInstallationName(st.nextToken());
                } else if ("termination".equals(t1)) {//
                    String s = st.nextToken();
                    tdc.t.setTerminationVersion(s);
                } else if ("fielddef".equals(t1)) {
                    tdc.parseField(fr);
                } else if ("release".equals(t1)) {//
                    tdc.releases.add(st.nextToken());
                }
            }
            line = fr.readLine();
        }
    }
    TableDataConsumer tdc = new TableDataConsumer();

    public void setImagesSourceFolder(String path) {
        this.imagesFolder = path;
    }

    public String getImagesFolder() {
        return imagesFolder;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        if (this.displayName == null) {
            return name;
        }
        return displayName;
    }

    public Table getTableByName(String referencesTableName) {
        for (Table t : tables) {
            if (stringsEqual(referencesTableName, t.getName())) {
                return t;
            }
        }
        return null;
    }

    public void setDeleteDirectoryOnZip(boolean b) {
        this.deleteDirectoryOnZip = b;
    }

    private static class TableDataConsumer {

        LinkedList<String> releases = new LinkedList<String>();
        LinkedList<Table> tables = new LinkedList<Table>();
        Table t;
        Record r;

        void startTable(String name, Component comp) {
            r = new Record();
            t = new Table(name, r, comp);
        }

        void endTable() {
            tables.add(t);
            r = null;
            t = null;
        }

        void parseField(MyFileReader fr) {
            Field f = null;
            DataTypes type = null;
            String version = null;
            String display_name = null;
            String desc_admin = null;
            String name = null;
            String desc_site = null;
            String termination_version = null;
            String line = fr.readLine();
            while (line != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() > 0) {
                    String t1 = st.nextToken();
                    if ("version".equals(t1)) {
                        version = st.nextToken();
                    } else if ("display_name".equals(t1)) {
                        display_name = fr.readLine();
                    } else if ("type".equals(t1)) {
                        type = DataTypes.valueOf(st.nextToken());
                    } else if ("desc_admin".equals(t1)) {
                        desc_admin = fr.readLine();
                    } else if ("name".equals(t1)) {
                        name = st.nextToken();
                    } else if ("termination_version".equals(t1)) {
                        termination_version = st.nextToken();
                    } else if ("desc_site".equals(t1)) {
                        desc_site = fr.readLine();
                    } else if ("end".equals(t1)) {

                        f = new Field(name, type, true);
                        if (version != null) {
                            f.setVersion(version);
                        }
                        if (display_name != null) {
                            f.setDisplayName(display_name);
                        }
                        if (termination_version != null) {
                            f.setTerminationVersion(termination_version);
                        }
                        if (desc_admin != null) {
                            f.setDescriptionAdmin(desc_admin);
                        }
                        if (desc_site != null) {
                            f.setDescriptionSite(desc_site);
                        }
                        break;
                    } else {
                        throw new IllegalStateException(
                                "Parse error. Cannot continue");
                    }
                }
                line = fr.readLine();
            }
            f.setTable(t);
            try {
                r.addField(f);
            } catch (Exception ex) {
                System.err.println(
                        "Table " + t.getName() + " couldn't add field " + f.name);
                Logger.getLogger(Component.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }

        void setVersion(String version) {
            t.setVersion(version);
        }

        void setTermination(String version) {
            t.setTerminationVersion(version);
        }
    }

    private void deleteDir(File src) {
        if (src.isFile()) {
            _delete(src);
        }
        if (src.isDirectory()) {
            for (File ff : src.listFiles()) {
                if (ff.isDirectory()) {
                    deleteDir(ff);
                } else {
                    _delete(ff);
                }
            }
            _delete(src);
        }
    }

    private void _delete(File f) {
        f.delete();
    }

    private void copyImages() throws IOException {
        String dest = generateComponentPath("admin.assets.images");
        makeDirRecursive(dest);
        if (imagesFolder == null) {
            return;
        }
        File f = new File(imagesFolder);
        copyFiles(f, new File(dest));
    }

    private void copyFiles(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            for (File ff : src.listFiles()) {
                if (ff.isDirectory()) {
                    File subdir = new File(dest.getPath() + "\\" + ff.getName());
                    subdir.mkdir();
                    copyFiles(ff, subdir);
                } else {
                    copyFile(ff, new File(dest.getPath() + "\\" + ff.getName()));
                }
            }
        }
    }

    private void copyComponent() throws IOException {
        File f = new File(getExtensionRoot());
        makeDirRecursive(getExtensionTempRoot());
        copyFiles(f, new File(getExtensionTempRoot()));
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    @Override
    public void build() {
        eventTimer eb = new eventTimer("Building component");
        eb.start();
        eventTimer et = new eventTimer("Getting old properties");

        et.start();
        ComponentProperties oldProperties = getOldProperties();
        et.stop();
        processVersion(oldProperties);

        System.out.println("Deleting the directory...");
        et = new eventTimer("Deleting old directory");
        et.start();
        deleteDir(new File(getExtensionRoot()));
        et.stop();
        et = new eventTimer("Making table history");
        et.start();
        makeTableHistory(oldProperties);
        et.stop();
        properties.printData();
        //Start making the componen`t folders
        et = new eventTimer("Dumping");
        et.start();
        System.out.println("Dumping ...");
        dump();
        et.stop();
        //
        System.out.println("Creating manifest ...");
        makeManifest();
        System.out.println("Making entry points ...");
        makeEntries();
        System.out.println("Creating component controllers ...");
        makeControllers();
        System.out.println("Making script ...");
        makeScript();
        System.out.println("Making index files ...");
        makeIndexFiles();
        System.out.println("Serializing properties object ...");
        storePropertiesObj();
        System.out.println("Making language files ...");
        makeLanguageFiles();
        System.out.println("Making Change log ...");
        makeChangelog();
        System.out.println("Making configuration file ...");
        makeConfigFile();
        System.out.println("Making access file ...");
        makeAccessFile();
        et = new eventTimer("Putting sample data");
        et.start();
        System.out.println("Putting Sample data");
        try {
            readTableData();
        } catch (IOException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null,
                    ex);
            System.exit(1);
        }
        et.stop();
        System.out.println("Emiting helper file ...");
        (new Helper(this)).emit();
        System.out.println("Copying images from " + getImagesFolder() + "\n...");

        try {
            et = new eventTimer("Copying images");
            et.start();
            copyImages();
            et.stop();
        } catch (IOException ex) {
            Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
        System.out.println("Zipping ...");
        zipProxy();
        if (deleteDirectoryOnZip) {
            System.out.println("Deleting the source directory ... ");
            deleteDir(new File(getExtensionRoot()));
        }
        System.out.println(
                "Finished Building " + getInstallationName() + " Version " + version);
        System.out.printf("The time now is %1$tc\n", (new GregorianCalendar()));
        eb.stop();
    }

    public void writeHeader(FileWriter fw) {
        writeHeader(fw, "* @");
    }

    protected void writeHeader(FileWriter fw, String pre) {
        fw.pushLine(pre + "package\t\t" + name);
        fw.pushLine(pre + "version\t\t" + version);
        fw.pushLine(pre + "copyright\t\t" + getCopyrightInfo());
        fw.pushLine(pre + "license\t\t" + getLicenceInfo());
        fw.pushLine(pre + "author\t\t" + getVendor());
        Calendar c = new GregorianCalendar();
        fw.format(pre + "time\t %1$tc%n", c);
    }

    private void makeIndexFiles() {
        makeFile("root|index.html");
        makeFile("admin|index.html");
        makeFile("site|index.html");
        makeFile("admin.helpers|index.html");
        makeFile("admin.views|index.html");
        makeFile("admin.models|index.html");
        makeFile("admin.controllers|index.html");
        makeFile("admin.language|index.html");
        makeFile("admin.language.en-GB|index.html", true);
        makeFile("admin.tables|index.html");
        makeFile("admin.assets|index.html");
        makeFile("site.helpers|index.html");
        makeFile("site.views|index.html");
        makeFile("site.models|index.html");
        makeFile("site.controllers|index.html");
        makeFile("site.language|index.html");
        makeFile("site.language.en-GB|index.html", true);
        makeFile("root.language|index.html");
        makeFile("root.language.en-GB|index.html", true);
        makeFile("site.tables|index.html");
        makeFile("site.assets|index.html");
    }

    private void makeLanguageFiles() {
        makeFile("root.language.en-GB|en-GB." + getInstallationName() + ".ini",
                true);
        makeFile("site.language.en-GB|en-GB." + getInstallationName() + ".ini",
                true);
        makeFile("admin.language.en-GB|en-GB." + getInstallationName() + ".ini",
                true);
        makeFile(
                "admin.language.en-GB|en-GB." + getInstallationName() + ".sys.ini",
                true);
        FileWriter fw_ini = get_enGB_AppendingFileWriter();
        FileWriter fw_ini_s = get_enGB_SiteAppendingFileWriter();
        FileWriter fw_ini_sys = get_enGB_sys_AppendingFileWriter();
        fw_ini.pushLine(
                getInstallationName().toUpperCase() + "_ADMIN_MENU_NAME=\"" + getAdminMenuName() + "\"");
        fw_ini.pushLine(
                getInstallationName().toUpperCase() + "_DISPLAY_NAME=\"" + getDisplayName() + "\"");


        fw_ini_s.pushLine(
                getInstallationName().toUpperCase() + "_DISPLAY_NAME=\"" + getDisplayName() + "\"");
        fw_ini.pushLine(
                getInstallationName().toUpperCase() + "_ADMIN_TITLE=\"" + getAdminTitle() + "\"");
        fw_ini_s.pushLine(
                getInstallationName().toUpperCase() + "_SITE_TITLE=\"" + getAdminTitle() + "\"");


        fw_ini_sys.pushLine(
                getInstallationName().toUpperCase() + "_ADMIN_MENU_NAME=\"" + getAdminMenuName() + "\"");
        fw_ini_sys.pushLine(
                getInstallationName().toUpperCase() + "_DISPLAY_NAME=\"" + getDisplayName() + "\"");
        fw_ini_sys.pushLine(
                getInstallationName().toUpperCase() + "_ADMIN_TITLE=\"" + getAdminTitle() + "\"");
        fw_ini_sys.pushLine(
                getInstallationName().toUpperCase() + "=\"" + getAdminTitle() + "\"");

        fw_ini.pushLine(
                getInstallationName().toUpperCase() + "_CONFIGURATION=\"" + getDisplayName() + " Configuration Settings\"");
        fw_ini.pushLine(
                getInstallationName().toUpperCase() + "_N_ITEMS_DELETED_MORE=\"%d items deleted\"");
        fw_ini.pushLine(
                getInstallationName().toUpperCase() + "_N_ITEMS_DELETED_1=\"One item deleted\"");
        fw_ini.pushLine(
                getInstallationName().toUpperCase() + "_DATABASE_COLUMN_CATID=\"Category Id\"");
        fw_ini.pushLine(
                getInstallationName().toUpperCase() + "_DATABASE_COLUMN_PARAMS=\"Parameters\"");
    }

    private void makeEntries() {
        makeComponentEntry("entry");
        makeComponentEntry("admin");
        makeComponentEntry("site");
    }

    public void setTitle(String adminTitle) {
        this.adminTitle = adminTitle;
    }

    public String getAdminTitle() {
        return adminTitle;
    }

    private void makeControllers() {
        makeComponentControllers("entry");
        makeComponentControllers("admin");
        makeComponentControllers("site");
    }

    private void makeChangelog() {
        makeFile("admin|CHANGELOG.php");
    }

    private void makeConfigFile() {
        String config = makeFile("admin|config.xml");
        FileWriter fw = obtainWriter(config);
        fw.pushLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        fw.pushLine("<!--");
        writeHeader(fw, "- @");
        fw.pushLine("-->");
        fw.pushLine("<config>");
        fw.pushLine("<fieldset");
        fw.pushLine("name=\"permissions\"");
        fw.pushLine("label=\"JCONFIG_PERMISSIONS_LABEL\"");
        fw.pushLine("description=\"JCONFIG_PERMISSIONS_DESC\"");
        fw.pushLine(">");
        fw.pushLine("<field");
        fw.pushLine("name=\"rules\"");
        fw.pushLine("type=\"rules\"");
        fw.pushLine("label=\"JCONFIG_PERMISSIONS_LABEL\"");
        fw.pushLine("class=\"inputbox\"");
        fw.pushLine("validate=\"rules\"");
        fw.pushLine("filter=\"rules\"");
        fw.pushLine("component=\"" + getInstallationName() + "\"");
        fw.pushLine("section=\"component\"");
        fw.pushLine("/>");
        fw.pushLine("</fieldset>");
        fw.pushLine("</config>");
    }

    private void makeAccessFile() {
        String access = makeFile("admin|access.xml");
        FileWriter fw = obtainWriter(access);
        fw.pushLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        fw.pushLine("<!--");
        writeHeader(fw, "@");
        fw.pushLine("-->");
        fw.pushLine("<access component=\"" + getInstallationName() + "\">");
        fw.pushLine("<section name=\"component\">");
        fw.pushLine(
                "<action name=\"core.admin\" title=\"JACTION_ADMIN\" description=\"JACTION_ADMIN_COMPONENT_DESC\" />");
        fw.pushLine(
                "<action name=\"core.manage\" title=\"JACTION_MANAGE\" description=\"JACTION_MANAGE_COMPONENT_DESC\" />");
        fw.pushLine(
                "<action name=\"core.create\" title=\"JACTION_CREATE\" description=\"JACTION_CREATE_COMPONENT_DESC\" />");
        fw.pushLine(
                "<action name=\"core.delete\" title=\"JACTION_DELETE\" description=\"JACTION_DELETE_COMPONENT_DESC\" />");
        fw.pushLine(
                "<action name=\"core.edit\" title=\"JACTION_EDIT\" description=\"JACTION_EDIT_COMPONENT_DESC\" />");
        fw.pushLine("</section>");
        fw.pushLine("</access>");
    }

    private void makeComponentControllers(String directive) {

        boolean root = false, admin = false, site = false;
        String controller = "";
        if ("entry".equals(directive)) {
            controller = makeFile("root|controller.php");
            root = true;
        } else if ("admin".equals(directive)) {
            controller = makeFile("admin|controller.php");
            admin = true;
        } else if ("site".equals(directive)) {
            controller = makeFile("site|controller.php");
            site = true;
        } else {
            throw new IllegalStateException("Unknown directive");
        }
        FileWriter fw = obtainWriter(controller);
        fw.pushLine("<?php");
        fw.pushLine("/**");
        writeHeader(fw);
        fw.pushLine("*/");
        fw.pushLine("defined('_JEXEC') or die('Restricted access');");
        fw.pushLine("jimport('joomla.application.component.controller');");
        fw.pushLine(
                "class " + FileWriter.capitalize(getName()) + "Controller extends JController");
        fw.pushLine("{");
        if (admin || site) {
            fw.pushLine(
                    "public function display($cachable = false, $urlparams = false) {");

            String defView = admin ? getDefaultView(MVCClassType.ADMIN) : getDefaultView(
                    MVCClassType.SITE);
            if (admin) {

                fw.pushLine("$view = \"home\";");
                fw.pushLine("$viewtemp = JRequest::getCmd(\"view\");");
                fw.pushLine("if (!empty($viewtemp)) {");
                fw.pushLine("$view = $viewtemp;");
                fw.pushLine("}");


                fw.pushLine(getHelperName() + "::addSubmenu(strtolower($view));");
            }
            fw.pushLine(
                    "JRequest::setVar('view', JRequest::getCmd('view', '" + defView + "'));");
            fw.pushLine("parent::display($cachable, $urlparams);");
            fw.pushLine("}");
        }
        fw.pushLine("}");
    }

    private void makeComponentEntry(String directive) {
        boolean root = false, admin = false, site = false;
        String entry = "";
        if ("entry".equals(directive)) {
            entry = makeFile("root|" + getName().toLowerCase() + ".php");
            root = true;
        } else if ("admin".equals(directive)) {
            entry = makeFile("admin|" + getName().toLowerCase() + ".php");
            admin = true;
        } else if ("site".equals(directive)) {
            entry = makeFile("site|" + getName().toLowerCase() + ".php");
            site = true;
        } else {
            throw new IllegalStateException("Unknown directive");
        }
        FileWriter fw = obtainWriter(entry);
        fw.pushLine("<?php");
        fw.pushLine("/**");
        writeHeader(fw);
        fw.pushLine("*/");
        fw.pushLine("defined('_JEXEC') or die('Restricted access');");
        if (admin) {
            fw.pushLine(
                    "if (!JFactory::getUser()->authorise('core.manage', '" + getInstallationName() + "')) {");
            fw.pushLine(
                    "return JError::raiseWarning(404, JText::_('JERROR_ALERTNOAUTHOR'));");
            fw.pushLine("}");
        }
        fw.pushLine(
                "JLoader::register('" + FileWriter.capitalize(getName()) + "Helper', JPATH_COMPONENT_ADMINISTRATOR . DS . 'helpers' . DS . '" + getName().toLowerCase() + ".php');");
        fw.pushLine("jimport('joomla.application.component.controller');");
        fw.pushLine(
                "$controller = JController::getInstance('" + getName().toLowerCase() + "');");
        fw.pushLine("$controller->execute(JRequest::getCmd('task'));");
        fw.pushLine("$controller->redirect();");
    }

    private void makeScript() {
        String script = makeFile("root|script.php");
        FileWriter fw = obtainWriter(script);
        scriptAssistance.writeScript(fw);
    }

    public FileWriter getSqlInstallAppendingFileWriter() {
        return obtainWriter(makeFile("admin.sql|install.mysql.utf8.sql"), true);
    }

    public FileWriter get_enGB_AppendingFileWriter() {
        return obtainWriter(makeFile(
                "admin.language.en-GB|en-GB." + getInstallationName() + ".ini",
                true), true);
    }

    public FileWriter get_enGB_SiteAppendingFileWriter() {
        return obtainWriter(makeFile(
                "site.language.en-GB|en-GB." + getInstallationName() + ".ini",
                true), true);
    }

    public FileWriter get_enGB_sys_AppendingFileWriter() {
        return obtainWriter(makeFile(
                "admin.language.en-GB|en-GB." + getInstallationName() + ".sys.ini",
                true), true);
    }

    public FileWriter getSqlUpdateAppendingFileWriter() {
        return obtainWriter(makeFile(
                "admin.sql.updates.mysql|" + version + ".sql"), true);
    }

    public FileWriter getSqlUninstallAppendingFileWriter() {
        return obtainWriter(makeFile("admin.sql|uninstall.mysql.utf8.sql"), true);
    }

    @Override
    public String getInstallationName() {
        String namel = name.toLowerCase();
        return "com_" + namel;
    }

    private void makeManifest() {
        String manifest = makeFile("root|" + name.toLowerCase() + ".xml");
        FileWriter fw = obtainWriter(manifest);
        fw.pushLine("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        fw.pushLine(
                "<extension type=\"component\" version=\"1.6.0\" method=\"upgrade\">");
        fw.pushLine("<name>" + getInstallationName().toUpperCase() + "</name>");
        Calendar c = Calendar.getInstance();
        fw.format("<creationDate>%1$tc</creationDate>%n", c);
        fw.pushLine("<author>" + vendor + "</author>");
        fw.pushLine("<authorEmail>" + getVendorEmail() + "</authorEmail>");
        fw.pushLine("<authorUrl>" + getVendorUrl() + "</authorUrl>");
        fw.pushLine("<copyright>" + getCopyrightInfo() + "</copyright>");
        fw.pushLine("<license>" + getLicenceInfo() + "</license>");
        fw.pushLine("<version>" + version + "</version>");
        fw.pushLine(
                "<description><![CDATA[" + getDescription() + "]]></description>");
        fw.pushLine("<scriptfile>script.php</scriptfile>");
        fw.pushLine("<install>");
        fw.pushLine("<params>");
        fw.pushLine(
                "<param name=\"allowed_mime_types\" default=\"image/jpeg,image/pjpeg,image/png,image/x-png,image/gif,application/pdf,application/x-pdf,application/acrobat,applications/vnd.pdf,text/pdf,text/x-pdf\" />");
        fw.pushLine("</params>");
        fw.pushLine("<sql>");
        fw.pushLine(
                "<file driver=\"mysql\" charset=\"utf8\">sql/install.mysql.utf8.sql</file>");
        fw.pushLine("</sql>");
        fw.pushLine("</install>");
        fw.pushLine("<uninstall>");
        fw.pushLine("<sql>");
        fw.pushLine(
                "<file driver=\"mysql\" charset=\"utf8\">sql/uninstall.mysql.utf8.sql</file>");
        fw.pushLine("</sql>");
        fw.pushLine("</uninstall>");
        fw.pushLine("<update>");
        fw.pushLine("<schemas>");
        fw.pushLine("<schemapath type=\"mysql\">sql/updates/mysql</schemapath>");
        fw.pushLine("</schemas>");
        fw.pushLine("</update>");
        fw.pushLine("<files folder=\"" + properties.getSite() + "\">");
        fw.pushLine("<filename>index.html</filename>");
        fw.pushLine("<filename>" + getName().toLowerCase() + ".php</filename>");
        fw.pushLine("<filename>controller.php</filename>");
        fw.pushLine("<folder>assets</folder>");
        fw.pushLine("<folder>views</folder>");
        fw.pushLine("<folder>models</folder>");
        fw.pushLine("<folder>controllers</folder>");
        fw.pushLine("<folder>language</folder>");
        fw.pushLine("</files>");
        fw.pushLine("<administration>");
        fw.pushLine(
                "<menu img=\"components/" + getInstallationName() + "/assets/images/icons/small_icon-16x16.png\">" + getInstallationName().toUpperCase() + "_ADMIN_MENU_NAME" + "</menu>");
        fw.pushLine("<files folder=\"backend\">");
        fw.pushLine("<filename>index.html</filename>");
        fw.pushLine("<filename>" + getName().toLowerCase() + ".php</filename>");
        fw.pushLine("<filename>controller.php</filename>");
        fw.pushLine("<filename>CHANGELOG.php</filename>");
        fw.pushLine("<filename>config.xml</filename>");
        fw.pushLine("<filename>access.xml</filename>");
        fw.pushLine("<folder>assets</folder>");
        fw.pushLine("<folder>models</folder>");
        fw.pushLine("<folder>tables</folder>");
        fw.pushLine("<folder>sql</folder>");
        fw.pushLine("<folder>views</folder>");
        fw.pushLine("<folder>controllers</folder>");
        fw.pushLine("<folder>helpers</folder>");
        fw.pushLine("</files>");
        fw.pushLine("<languages folder=\"" + properties.getAdmin() + "\">");
        fw.pushLine(
                "<language tag=\"en-GB\">language/en-GB/en-GB." + getInstallationName() + ".ini</language>");
        fw.pushLine(
                "<language tag=\"en-GB\">language/en-GB/en-GB." + getInstallationName() + ".sys.ini</language>");
        fw.pushLine("</languages>");
        fw.pushLine("</administration>");
        fw.pushLine("<updateservers>");
        fw.pushLine(
                "<server type=\"extension\" priority=\"" + properties.getReleaseIndex() + "\" name=\"" + getName() + " Update server\">http://fuscard.com/dev/" + getName().toLowerCase() + "/update_manifest.xml</server>");
        fw.pushLine("</updateservers>");
        fw.pushLine("</extension>");
    }

    public FileWriter obtainWriter(String path) {
        return obtainWriter(path, false);
    }

    public FileWriter obtainWriter(String path, boolean append) {
        FileWriter fw;
        try {
            fw = new FileWriter(path, append);
        } catch (IOException ex) {
            throw new IllegalStateException("No file writer for " + path);
        }
        return fw;
    }

    private void dump() {
        for (Model m : models) {
            m.dumpCode();
        }
        for (Controller c : controllers) {
            c.dumpCode();
        }
        for (View v : views) {
            v.dumpCode();
        }
    }

    public String getHelperName() {
        return FileWriter.capitalize(getName()) + "Helper";
    }

    public String makeFile(String name) {
        return makeFile(name, false);
    }

    public String makeFile(String name, boolean suppressLowerCase) {
        if (!suppressLowerCase) {
            name = name.toLowerCase();
        }
        StringTokenizer tn = new StringTokenizer(name, "|");
        if (tn.countTokens() != 2) {
            throw new IllegalStateException();
        }
        String dir = tn.nextToken();
        String file = tn.nextToken();
        String path = generateComponentPath(dir);
        makeDirRecursive(path);
        path = path + DS + file;
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException ex) {
        }
        return path;
    }

    public String getExtRootFrmOldProp() {
        ComponentProperties oldProperties = getOldProperties();
        return oldProperties == null ? null : oldProperties.getExtensionRoot();
    }

    public boolean makeAdminListDetails(String listName, String detailsName) {
        return makeListDetails(listName, detailsName, MVCClassType.ADMIN);
    }

    public boolean makeSiteListDetails(String listName, String detailsName) {
        return makeListDetails(listName, detailsName, MVCClassType.SITE);
    }

    public boolean makeListDetails(String listName, String detailsName, MVCClassType location) {
        if (!ensureNameAvailable(listName, location) || !ensureNameAvailable(
                detailsName, location)) {
            return false;
        }
        MVCClass.Config listc = new MVCClass.Config(
                MVCClass.Config.Type.IDE_List);
        MVCClass.Config dtlsc = new MVCClass.Config(
                MVCClass.Config.Type.IDE_Details);
        LinkedList<MVCClass> list = makeMVCset(listName, location, listc);
        LinkedList<MVCClass> details = makeMVCset(detailsName, location, dtlsc);
        for (MVCClass c : getAllMVCClasses(new String[]{detailsName, listName})) {
            c.setSingular(detailsName);
            c.setPlural(listName);
        }
        if (list != null && details != null) {
            for (MVCClass l : list) {
                l.makeList();
            }
            for (MVCClass d : details) {
                d.makeDetails();
            }
        }

        return true;
    }

    public void setTableForModel(String model, Table table) {
        for (Model m : models) {
            if (m.getName() == null ? model == null : m.getName().equals(model)) {
                m.setTable(table);
                break;
            }
        }
    }

    public MVCClass[] getAllMVCClasses(String[] names) {
        LinkedList<MVCClass> res = new LinkedList<MVCClass>();
        for (MVCClass c : getAllMVCClasses()) {
            for (String n : names) {
                if (c.getName() == null ? n == null : c.getName().equals(n)) {
                    res.add(c);
                    break;
                }
            }
        }
        return res.toArray(new MVCClass[0]);
    }

    public MVCClass getMVCClass(String name, MVCClassType loc, String type) {
        MVCClass[] src = "model".equals(type) ? models : "view".equals(type) ? views : "controller".equals(
                type) ? controllers : null;
        if (src == null) {
            return null;
        }
        for (MVCClass c : src) {
            if ((c.getName() == null ? name == null : c.getName().equals(name)) && c.type == loc) {
                return c;
            }
        }
        return null;
    }

    public MVCClass[] getAllMVCClasses() {
        LinkedList<MVCClass> res = new LinkedList<MVCClass>();
        res.addAll(Arrays.asList(models));
        res.addAll(Arrays.asList(controllers));
        res.addAll(Arrays.asList(views));
        return res.toArray(new MVCClass[0]);
    }

    public LinkedList<MVCClass> makeMVCset(String name, MVCClassType type, MVCClass.Config config) {
        if (!ensureNameAvailable(name, type)) {
            return null;
        }
        LinkedList<MVCClass> res = new LinkedList<MVCClass>();
        models = (Model[]) addMVCInstance(new Model(name, type, this, config),
                models, Model.class);
        res.add(models[models.length - 1]);
        controllers = (Controller[]) addMVCInstance(new Controller(name, type,
                this, config), controllers, Controller.class);
        res.add(controllers[controllers.length - 1]);
        views = (View[]) addMVCInstance(new View(name, type, this, config),
                views, View.class);
        res.add(views[views.length - 1]);
        return res;
    }

    private <E, T extends E> T[] addMVCInstance(T obj, E[] src, Class type) {
        T[] mtemp;
        mtemp = (T[]) Array.newInstance(type, src.length + 1);
        System.arraycopy(src, 0, mtemp, 0, src.length);
        mtemp[mtemp.length - 1] = obj;
        return mtemp;
    }

    public boolean ensureNameAvailable(String name, MVCClassType type) {
        if ("".equals(name)) {
            return false;
        }
        for (Model m : models) {
            if (m.type == type && m.getName() == null ? name == null : m.getName().equals(
                    name)) {
                return false;
            }
        }
        for (Controller c : controllers) {
            if (c.type == type && c.getName() == null ? name == null : c.getName().equals(
                    name)) {
                return false;
            }
        }
        for (View v : views) {
            if (v.type == type && v.getName() == null ? name == null : v.getName().equals(
                    name)) {
                return false;
            }
        }
        return true;
    }

    private String generateComponentPath(String s) {
        StringTokenizer st = new StringTokenizer(s, ".");
        String res;
        if (st.countTokens() == 0) {
            return "";
        }
        String first = st.nextToken();
        if ("admin".equals(first)) {
            res = getAdminFolder();
        } else if ("site".equals(first)) {
            res = getSiteFolder();
        } else if ("update_server".equals(first)) {
            res = getUpdateSeverFolder();
        } else if ("root".equals(first)) {
            res = getExtensionRoot();
        } else {
            return "";
        }
        while (st.hasMoreTokens()) {
            res += DS + st.nextToken();
        }
        return res;
    }

    @Override
    public void setVendorEmail(String email) {
        super.setVendorEmail(email);
        properties.setVendorEmail(email);
    }

    @Override
    public void setVendorUrl(String url) {
        super.setVendorUrl(url);
        properties.setVendorUrl(url);
    }

    @Override
    public void setCopyrightInfo(String info) {
        super.setCopyrightInfo(info);
        properties.setCopyrightInfo(info);
    }

    @Override
    public void setLicenceInfo(String info) {
        super.setLicenceInfo(info);
        properties.setLicenceInfo(info);
    }

    @Override
    public void setDescription(String desc) {
        super.setDescription(desc);
        properties.setDescription(desc);
    }

    public void injectView(View view) {
        views = (View[]) addMVCInstance(view, views, View.class);
    }

    public void injectModel(Model model) {
        models = (Model[]) addMVCInstance(model, models, Model.class);
    }

    public void injectController(Controller controller) {
        controllers = (Controller[]) addMVCInstance(controller, controllers,
                Controller.class);
    }

    public void setDefaultView(String viewName, MVCClassType mvcClassType) {
        defaultViews.put(mvcClassType, viewName);
    }

    public String getDefaultView(MVCClassType mvcClassType) {
        return defaultViews.get(mvcClassType);
    }

    public boolean hasTable(String name) {
        for (Iterator<Table> it = tables.iterator(); it.hasNext();) {
            Table t = it.next();
            if (t.getName() == null ? name == null : t.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void addTable(Table t) {
        if (tables.contains(t)) {
            return;
        }
        tables.add(t);
    }

    public View getAdminViewIndex() {
        return viewIndex;
    }

    public void setAdminViewIndex(View controlPanel) {
        this.viewIndex = controlPanel;
    }

    private MyFileReader getHistoryReader() throws IOException {
        return new MyFileReader(getTableHistoryFile());
    }

    private FileWriter getHistoryWriter() throws IOException {
        String path = getTableHistoryFile() + "temp";
        File f = new File(path);
        makeDirRecursive(f.getParent());
        return new FileWriter(path, false, true);
    }

    private void commitHistory() throws IOException {
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader(
                    getTableHistoryFile() + "temp"));
            outputStream = new PrintWriter(new java.io.FileWriter(
                    getTableHistoryFile()));

            String l;
            while ((l = inputStream.readLine()) != null) {
                outputStream.println(l);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
