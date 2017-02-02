package factory.packager;

import com.fuscard.commons.FileUtils;
import com.fuscard.commons.INIInputOutput;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Simple packager is a complete java application to copy Joomla! files into an
 * update server folder in order to create the extension's (read the
 * component's) installer package. It will be used as a concept demonstrator in
 * order to test the concept to be applied in Joomla! Extension Factory.
 *
 * @author John Mwai
 */
public class SimplePackager {

    public SimplePackager(boolean solicitComment, boolean mayUploadOldInstaller) {

        if (mayUploadOldInstaller) {
            boolean b = false;
            try {
                b = checkLastPackageUploaded();
            } catch (Exception ex) {
                //Could not upload
                ex.printStackTrace();
                System.exit(0);
            }
            if (!b && JOptionPane.showConfirmDialog(null,
                    "Your last build did not upload correctly."
                    + " Would you like to try again?") == JOptionPane.YES_OPTION) {
                hasUploadedLastUpdate = true;
                try {
                    upload(new File(lastUpdateServer + "\\" + lastInstallerName));
                } catch (Exception ex) {
                    Logger.getLogger(SimplePackager.class.getName()).log(
                            Level.SEVERE,
                            null, ex);
                }
            }
        }
        if (!hasUploadedLastUpdate) {
            if (solicitComment) {
                solicitComment();
            }
        }
    }

    protected void solicitComment() {
        if (comment == null) {

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter a comment for this build: ");

            comment = sc.nextLine();
        }
    }
    private final String minVersion = "2.0.0";
    private HashMap<String, String> sources = new HashMap<>();
    private File manifest = null;
    private File updateServer;
    private int releaseLevel = 15;
    protected String version;
    private String installationName;
    //
    private String siteFolderName;
    private String adminFolderName;
    private String[] siteFolders;
    private String[] siteFiles;
    private String[] adminFolders;
    private String[] adminFiles;
    private String[] adminLanguageFiles;
    //
    private String rootDir;
    private String siteDir;
    private String adminDir;
    //
    private Path installPath;
    //
    private javax.swing.ProgressMonitor progressMonitor;
    private long fileSize = -1;
    private long zippedSize;
    //
    protected String comment = null;
    protected boolean hasUploadedLastUpdate = false;
    //
    private String siteSrc = null;
    private String adminSrc = null;

    /**
     * This method adds a source file to copy and the destination folder in the
     * installer package where to copy it. The destination path must start with
     * one of root, site and admin. These words determine whether the file will
     * be copied into the root folder or the site folder or the admin folder.
     * This method works with both file and folder paths. If the source is a
     * file, if you specify the destination file name, the source file will be
     * copied and also renamed into the name that you specify. If you specify
     * only the path to the destination folder the file will be copied into the
     * destination folder and given the name of the original file. If you
     * specify a source folder, the contents of the source folder will be copied
     * into the destination folder.
     *
     * @param sourcePath The fully qualified name of the source file or folder
     * @param targetPath The relative path of the destination file or folder
     * where to copy the source file in the installer folder starting with
     * either admin, site or root.
     */
    protected void addSource(String sourcePath, String targetPath) {
        sources.put(sourcePath, targetPath);
        resolveSources();
    }

    protected long dirSize(File dir) {

        if (dir.isFile()) {
            return dir.length();
        }
        int l = 0;
        for (File f : dir.listFiles()) {
            l += dirSize(f);
        }
        return l;
    }
    private String lastUpdateServer,
            lastInstallerName,
            lastInstallerVersion;

    protected boolean checkLastPackageUploaded() throws Exception {
        URL iniUrl = SimplePackager.class.getResource("PackagerInfo.ini");
        try {
            INIInputOutput ini = new INIInputOutput(iniUrl);

            lastUpdateServer = ini.get("last_update_folder");
            lastInstallerName = ini.get("last_update_installer");
            lastInstallerVersion = ini.get("last_update_version");

            Path lus = Paths.get(lastUpdateServer);
            Path bld = lus.resolve("builds\\builds.xml");


            File buildsRecord = bld.toFile();
            File pr = buildsRecord.getParentFile();
            if (!pr.isDirectory()) {
                throw new Exception();
            }
            if (!buildsRecord.isFile()) {
                throw new Exception();
            }

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder();
            Document document = builder.parse(buildsRecord);

            XPath xpath = XPathFactory.newInstance().newXPath();
            //Expressions
            String buildExp = "/builds/build[@version=\"" + lastInstallerVersion + "\"]";


            Element elem = (Element) xpath.evaluate(buildExp, document,
                    XPathConstants.NODE);

            String upd = elem.getAttribute("uploaded");

            switch (upd) {
                case "no":
                    return false;
                case "yes":
                    return false;
                default:
                    throw new Exception();
            }

        } catch (URISyntaxException ex) {
            Logger.getLogger(SimplePackager.class.getName()).log(Level.SEVERE,
                    null, ex);
            throw new Exception(ex);
        }

    }

    protected void updatePackagerInfo() {
        URL iniUrl = SimplePackager.class.getResource("PackagerInfo.ini");
        try {

            File zippedDir = new File(rootDir + ".zip");

            INIInputOutput ini = new INIInputOutput(iniUrl);
            ini.save("last_update_folder", updateServer.getPath());
            ini.save("last_update_installer", zippedDir.getName());
            ini.save("last_update_version", version);
        } catch (URISyntaxException ex) {
            Logger.getLogger(SimplePackager.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    protected void zip() throws FileNotFoundException, IOException {

        if (rootDir == null) {
            return;
        }
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
                        rootDir + ".zip"))) {
            fileSize = dirSize(new File(rootDir));
            System.out.
                    println(
                    "Total file size: " + ((double) fileSize) / 1000000 + " mb");
            progressMonitor = new javax.swing.ProgressMonitor(new javax.swing.JComponent() {
            },
                    installationName + " " + version + " zipping.\nPlease wait...",
                    "", 0, (int) fileSize);
            zipDir(rootDir, zos, installationName.toLowerCase());
            endProgress();
        }
    }

    protected void endProgress() {
        if (progressMonitor.isCanceled()) {
            System.err.println("Zipping canceled by user");
        } else {
            System.out.println("Zipping completed successfully");
        }
        progressMonitor.close();
    }

    protected void upload(File zippedDir) throws Exception {

    }

    protected void zipDir(String dir2zip, ZipOutputStream zos, String pre) throws
            FileNotFoundException, IOException {
        if (progressMonitor.isCanceled()) {
            return;
        }
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn;
        for (int i = 0; i < dirList.length; i++) {
            if (progressMonitor.isCanceled()) {
                return;
            }
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                String filePath = f.getPath();
                zipDir(filePath, zos, pre + "\\" + f.getName());
                continue;
            }
            progressMonitor.
                    setNote(
                    "Complete: " + String.format("%.3f", getZippedMB()) + "MB File: " + f.
                    getName());
            try (FileInputStream fis = new FileInputStream(f)) {
                ZipEntry anEntry = new ZipEntry(pre + "\\" + f.getName());
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
            }
            zippedSize += f.length();
            progressMonitor.setProgress((int) zippedSize);
        }
    }

    protected double getZippedMB() {
        return ((double) zippedSize) / 1000000;
    }

    /**
     * Method to execute another application forex. AcrobatReader from Java
     *
     */
    protected void callSystem(String command) {

        System.out.println("Executing the command: \t\t>>\t" + command);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(
                    new String[]{"cmd", "/c", command});
            //execAcrobatPrs.waitFor();
        } catch (Exception ex) {
            if (process != null) {
                process.destroy();
            }
            ex.printStackTrace();
        }
    }

    protected void packageExtension(boolean zip, boolean upload) {

        eventTimer et = new eventTimer("Building extension");
        et.start();
        //put some sql files
        if (adminSrc != null) {
            File sqlupd = new File(
                    adminSrc + "\\sql\\updates\\mysql\\" + version + ".sql");
            try {
                File par = new File(sqlupd.getParent());
                if (!par.isDirectory()) {
                    par.mkdirs();
                }
                sqlupd.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(SimplePackager.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }

        for (String s : sources.keySet()) {
            File src = new File(s);
            File dest = new File(sources.get(s));
            try {
                copyFiles(src, dest);
            } catch (IOException ex) {
                Logger.getLogger(SimplePackager.class.getName()).
                        log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
        }
        updatePackagerInfo();




        //ensure all folders have index.html
        ensureAllFoldersHaveIndexFile(new File(rootDir));


        if (zip) {

            eventTimer et2 = new eventTimer("Zipping extension");
            et2.start();
            try {
                zip();
            } catch (IOException ex) {
                Logger.getLogger(SimplePackager.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
            et2.stop();
        }
        try {
            recordBuild();
        } catch (Exception ex) {
        }

        //open the directory where the installer package is

        callSystem("explorer \"" + updateServer.getPath() + "\"");

        if (upload) {
            eventTimer et3 = new eventTimer("Uploading extension");
            et3.start();
            try {
                upload(new File(rootDir + ".zip"));
                recordBuildUploaded();
                et3.stop();
            } catch (Exception e) {
                System.err.println("Upload failed");
                et3.abort();
            }

        }
        et.stop();

        System.out.println(
                "Finished Building " + installationName + " Version " + version);
        System.out.printf("The time now is %1$tc\n", (new GregorianCalendar()));

        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * This method obtains information from the manifest file. That information
     * includes the type of extension, the current version, the files and
     * folders that are included in the installer, their locations in the
     * installer, etc. This method also optionally increments the version of the
     * extension in question.
     *
     * @param incrementVersion whether to increment the version of the
     * extension.
     */
    protected void processManifest(boolean incrementVersion) {
        if (manifest == null) {
            return;
        }
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder();
            Document document = builder.parse(manifest);

            XPath xpath = XPathFactory.newInstance().newXPath();
            //Expressions
            String extensionExp = "/extension";
            String extensionTypeExp = "@type";
            String methodExp = "@method";
            String creationDateExp = "creationDate";
            String extensionNameExp = "name";
            String authorEmailExp = "authorEmail";
            String authorURLExp = "authorUrl";
            String versionExp = "version";

            /**
             * Handle file copying
             */
            String siteFolderNameExp = "files/@folder";
            String adminFolderNameExp = "administration/files/@folder";

            //Installer files and folders expressions
            String siteFoldersExp = "files/folder";
            String siteFilesExp = "files/filename";
            String adminFoldersExp = "administration/files/folder";
            String adminFilesExp = "administration/files/filename";

            //handle language files
            String adminLanguagesExp = "administration/languages/language";//all these files will be copied into the admin langage file

            Node en = (Node) xpath.evaluate(
                    extensionExp, document,
                    XPathConstants.NODE);

            siteFolderName = getStringValue(xpath, en, siteFolderNameExp);
            adminFolderName = getStringValue(xpath, en, adminFolderNameExp);

            siteFolders = getStringArray(xpath, en, siteFoldersExp);
            adminFolders = getStringArray(xpath, en, adminFoldersExp);
            siteFiles = getStringArray(xpath, en, siteFilesExp);
            adminFiles = getStringArray(xpath, en, adminFilesExp);
            adminLanguageFiles = getStringArray(xpath, en, adminLanguagesExp);


            String extensionType = getStringValue(xpath, en, extensionTypeExp);
            String method = getStringValue(xpath, en, methodExp);
            String creationDate = getStringValue(xpath, en, creationDateExp);
            installationName = getStringValue(xpath, en, extensionNameExp);
            String authorEmail = getStringValue(xpath, en, authorEmailExp);
            String authorURL = getStringValue(xpath, en, authorURLExp);

            Node v = (Node) xpath.evaluate(versionExp, en, XPathConstants.NODE);

            System.out.printf("Extension type: %s\n", extensionType);
            System.out.printf("Method: %s\n", method);
            System.out.printf("Creation date: %s\n", creationDate);
            System.out.printf("Extension name: %s\n", installationName);
            System.out.
                    printf("Author email: %s\n", authorEmail);
            System.out.printf("Author URL: %s\n", authorURL);
            System.out.printf("Old Version: %s\n", v.getTextContent());

            if (!"component".equals(extensionType)) {
                System.out.
                        println(
                        "This application doesn't support extensions of type '"
                        + extensionType + "'.");
                System.exit(-1);
            }

            version = v.getTextContent();

//            extensionName.setTextContent(extensionName.getTextContent() + " ++ ");
            if (incrementVersion) {
                processVersion(v);
            }

            System.out.printf("New Version: %s\n", v.getTextContent());

            processSources();

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new PrintWriter(manifest));
            transformer.transform(source, result);


        } catch (ParserConfigurationException | SAXException | IOException |
                XPathExpressionException | DOMException | TransformerException |
                TransformerFactoryConfigurationError ex) {
            Logger.getLogger(SimplePackager.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    protected class eventTimer {

        private String eventName;
        private Calendar c;
        
        private boolean ENABLED = true;

        protected eventTimer(String eventName) {
            if(!ENABLED){
                return;
            }
            this.eventName = eventName;
        }

        protected void start() {
            if(!ENABLED){
                return;
            }
            System.out.
                    println(
                    " [Event Timer ]: Initiating Task: \t" + eventName + "\n");
            c = new GregorianCalendar();
        }
        private boolean stopped = false;

        protected void stop() {
            if(!ENABLED){
                return;
            }
            if (stopped) {
                throw new IllegalStateException("Timer is already stopped");
            }
            stopped = true;
            long t = new GregorianCalendar().getTimeInMillis() - c.
                    getTimeInMillis();
            System.out.
                    println(" [Event Timer ]: Task Completed:\t" + eventName
                    + "\n [Event Timer ]: Time taken:\t" + String.
                    format("%.3f",
                    (double) t / 1000) + " seconds\n -- ");
        }

        protected void abort() {
            if(!ENABLED){
                return;
            }
            if (stopped) {
                throw new IllegalStateException("Timer is already stopped");
            }
            stopped = true;
            long t = new GregorianCalendar().getTimeInMillis() - c.
                    getTimeInMillis();
            System.err.
                    println(" ++++++++++[Event Timer ]: Task Failed:\t" + eventName
                    + "\n [Event Timer ]: Time taken:\t" + String.
                    format("%.3f",
                    (double) t / 1000) + " seconds\n -- ");
        }
    }

    /**
     * Resolve the explicit mapping of files inside the joomla installation to
     * the files in the installation directory.
     */
    protected void processSources() {
        /**
         * Determine the root folder of the installer folder.
         */
        String instlNm = this.installationName.toLowerCase();
        if (updateServer == null) {
            System.err.println("The update server folder cannot be null");
            return;
        }

        rootDir = updateServer + "\\" + instlNm + "-v" + version;


        //Path to the site folder in the installer package
        siteDir = rootDir + "\\" + siteFolderName;
        adminDir = rootDir + "\\" + adminFolderName;
        /**
         * For a component... site files are always in the
         * components/component_name/ dir
         */
        //C:\wamp\www\my_joomla\administrator\components\com_mycomponent\mycomponent.xml
        Path manifestPath = Paths.get(manifest.getPath());
        Path adminInstallDir = manifestPath.getParent();
        installPath = adminInstallDir.resolve("..\\..\\..").normalize();
        Path siteInstallDir = installPath.resolve("components\\" + instlNm);
        //
        siteSrc = siteInstallDir.toString();
        adminSrc = adminInstallDir.toString();

        for (String s : siteFiles) {
            sources.put(siteSrc + "\\" + s, siteDir + "\\" + s);
        }
        for (String s : siteFolders) {
            sources.put(siteSrc + "\\" + s, siteDir + "\\" + s);
        }
        for (String s : adminFiles) {
            sources.put(adminSrc + "\\" + s, adminDir + "\\" + s);
        }
        for (String s : adminFolders) {
            sources.put(adminSrc + "\\" + s, adminDir + "\\" + s);
        }

        //
        /**
         * process language files<p/>
         * For language files there are two options. The language files can be
         * situated in a folder in the backend section of the installation,
         * alternatively they can be found in the language folder in the Joomla!
         * installation backend.
         */
        //This is the joomla admin language files folder
        //for simlicity I assume all language strings are in the en-GB folder
        for (String s : adminLanguageFiles) {
            /**
             * For each of the admin language files first see whether the file
             * was installed in the Joomla language folder. If it is not there
             * look for it in the language folder of the component. Wherever you
             * find it add an entry in the sources map mapping that file into
             * the language folder in the admin of the installer package.
             */
            Path jal = installPath.resolve("administrator\\" + s);
            Path cal = adminInstallDir.resolve(s);
            if (Files.isRegularFile(jal, LinkOption.NOFOLLOW_LINKS)) {
                sources.put(jal.toString(), adminDir + "\\language\\en-GB");
            } else if (Files.isRegularFile(cal, LinkOption.NOFOLLOW_LINKS)) {
                sources.put(cal.toString(), adminDir + "\\language\\en-GB");
            } else {
                throw new IllegalStateException("Language file not found.\n"
                        + jal + " is not a file and\n" + cal + " is not a file.");
            }
        }

        //Add a mapping for the manifest file
        sources.put(adminInstallDir + "\\" + getComponentName() + ".xml", "root");

        resolveSources();
    }

    protected String getComponentName() {
        return installationName.toLowerCase().replaceFirst("com_", "");
    }

    protected void resolveSources() {
        if (siteDir == null || adminDir == null || rootDir == null) {

            System.err.
                    println("This method does not work if either the site "
                    + "directory or admin directory or even the root directory is undefined."
                    + " Try to process the manifest first.");
            return;
        }
        for (String s : sources.keySet()) {
            String val = sources.get(s);
            Path p = Paths.get(val);
            File sf = new File(s);
            if (sf.isFile()) {
                String name = sf.getName();
                String dname = p.getFileName().toString();
                if (name == null ? dname != null : !dname.equals(name)) {
                    p = p.resolve(name);
                }
            }
            String q = p.toString();
            if (p.getRoot() == null) {
                String r = "\\" + p.subpath(1,
                        p.getNameCount());
                switch (p.getName(0).toString()) {
                    case "admin":
                        q = adminDir + r;
                        break;
                    case "site":
                        q = siteDir + r;
                        break;
                    case "root":
                        q = rootDir + r;
                        break;
                    default:
                        continue;
                }
            }
            p = Paths.get(q).normalize();
            val = p.toString();
            sources.put(s, val);
        }
    }

    protected void ensureAllFoldersHaveIndexFile(File dir) {
        if (dir.isFile()) {
            return;
        }
        File index = new File(dir.getPath() + "\\index.html");

        try {
            boolean fileCreated = index.createNewFile();
            if (fileCreated) {
                try (FileWriter fw = new FileWriter(index)) {
                    fw.append("<html/>");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SimplePackager.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        for (File f : dir.listFiles()) {
            ensureAllFoldersHaveIndexFile(f);
        }
    }

    protected String getStringValue(XPath xpath, Node n, String expression) throws
            XPathExpressionException {
        return (String) xpath.
                evaluate(expression, n,
                XPathConstants.STRING);
    }

    protected String[] getStringArray(XPath xpath, Node node, String expression) throws
            XPathExpressionException {
        NodeList nodelist = (NodeList) xpath.evaluate(
                expression, node,
                XPathConstants.NODESET);
        String[] strings = new String[nodelist.getLength()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = nodelist.item(i).getTextContent().trim();
        }
        return strings;
    }

    /**
     * In order to increment the version of the extension, the current version
     * of the extension and the minimum version setting will be taken into
     * account.
     */
    protected void processVersion(Node versionNode) {
        String vstring = versionNode.getTextContent().trim();
        switch (VersionWrapper.comp(vstring, minVersion)) {
            case VersionWrapper.GREATER_THAN:
                /**
                 * if the version of the extension is below the minimum version
                 * just set the version of the extension to the minimum version.
                 */
                vstring = minVersion;
                break;
            case VersionWrapper.LESS_THAN:
            case VersionWrapper.EQUAL:
                /**
                 * if the version of the extension is equal to or greater than
                 * the minimum version increment the version of the extension
                 * according to the release index.
                 */
                VersionWrapper vw = new VersionWrapper(vstring);
                vw.increment(releaseLevel);
                vstring = vw.getVersion();
                break;
        }
        versionNode.setTextContent(vstring);
        version = vstring;
    }

    protected void setReleaseLevel(int level) {
        this.releaseLevel = level;
    }

    /**
     * This sets the path to the manifest file. It also allows the application
     * to automatically discover sources of the software.
     *
     * @param manifestPath path to the manifest file.
     */
    protected void setManifest(String manifestPath) {
        if (!isXMLFile(manifestPath)) {
            throw new IllegalArgumentException(
                    "The argument is not a path to a valid XML file");
        }
        this.manifest = new File(manifestPath);
    }

    protected void setUpdateServerFolder(String updateServerPath) {
        File f = new File(updateServerPath);
        if (!f.isDirectory()) {
            throw new IllegalArgumentException(
                    "The path provided is not a valid directory");
        }
        this.updateServer = f;
    }

    protected boolean isXMLFile(String fileName) {
        File xml = new File(fileName);
        if (!xml.isFile()) {
            return false;
        }
        String extension = FileUtils.getExtension(xml);
        return extension != null && "xml".equals(extension.toLowerCase().trim());
    }

    /**
     * The purpose of this Method is to record the build of the extension in a
     * convenient XML file so that a developer who wants to easily trace a
     * folder that contains the snapshot of the extension at a given time can do
     * so. It also allows the developer to write comments in an XML file
     * alongside the element of the build in order to aid in the management of
     * the builds.
     *
     * @throws Exception The various exceptions that may be thrown trying to
     * read or write the file or processing the XML record file.
     */
    protected void recordBuild() throws Exception {
        if (updateServer == null) {
            return;
        }
        Path p = Paths.get(updateServer.getPath());
        p = p.resolve("builds\\builds.xml");
        File buildsRecord = p.toFile();
        File pr = buildsRecord.getParentFile();
        if (!pr.isDirectory()) {
            pr.mkdirs();
        }
        if (buildsRecord.createNewFile()) {
            try (FileWriter fw = new FileWriter(buildsRecord)) {
                fw.append("<builds/>");
            }
        }

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                newDocumentBuilder();
        Document document = builder.parse(buildsRecord);

        XPath xpath = XPathFactory.newInstance().newXPath();
        //Expressions
        String buildsExp = "/builds";

        Node builds = (Node) xpath.evaluate(buildsExp, document,
                XPathConstants.NODE);

        Element build = document.createElement("build");
        build.setAttribute("version", version);
        build.setAttribute("time", String.format("%1$tc",
                (new GregorianCalendar())));
        build.setAttribute("uploaded", "no");
        if (comment != null) {
            Comment commElem = document.createComment(comment);
            builds.appendChild(commElem);
        }
        builds.appendChild(build);


        TransformerFactory.newInstance().newTransformer().transform(
                new DOMSource(document),
                new StreamResult(new PrintWriter(buildsRecord)));

    }

    protected void recordBuildUploaded() throws Exception {
        if (updateServer == null) {
            return;
        }
        Path p = Paths.get(updateServer.getPath());
        p = p.resolve("builds\\builds.xml");
        File buildsRecord = p.toFile();
        File pr = buildsRecord.getParentFile();
        if (!pr.isDirectory()) {
            return;
        }
        if (!buildsRecord.isFile()) {
            return;
        }

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                newDocumentBuilder();
        Document document = builder.parse(buildsRecord);

        XPath xpath = XPathFactory.newInstance().newXPath();
        //Expressions
        String buildsExp = "/builds/build";


        NodeList nl = (NodeList) xpath.evaluate(buildsExp, document,
                XPathConstants.NODESET);


        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element e = (Element) n;
                String v = e.getAttribute("version");
                if (v != null && v.equals(version)) {
                    e.setAttribute("uploaded", "yes");
                }
            }
        }


        TransformerFactory.newInstance().newTransformer().transform(
                new DOMSource(document),
                new StreamResult(new PrintWriter(buildsRecord)));

    }

    protected void printOutSources() {
        for (String s : sources.keySet()) {
            System.out.println(s + "\t\t===>>\t" + sources.get(s));
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws
            IOException {
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
            source.close();
            destination.close();
        }
    }

    protected void copyFiles(File src, File dest) throws IOException {
        eventTimer et = new eventTimer("Copying " + src.getPath()
                + " \t\tto\t\t==>>\t " + dest.
                getPath());
        et.start();
        if (src.isDirectory()) {
            if (!dest.isDirectory()) {
                dest.mkdirs();
            }
            for (File ff : src.listFiles()) {
                if (ff.isDirectory()) {
                    File subdir = new File(dest.getPath() + "\\" + ff.getName());
                    subdir.mkdir();
                    copyFiles(ff, subdir);
                } else {
                    copyFile(ff, new File(dest.getPath() + "\\" + ff.getName()));
                }
            }
        } else {
            File f = new File(dest.getParent());
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            copyFile(src, dest);
        }
        et.stop();
    }

    protected void printOutManifest() {
        ///TODO output some of the important information contained in the manifest file.
    }

    
}
