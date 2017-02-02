package factory.legacy;

import java.awt.Toolkit;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 23, 2012 -- 9:44:09 PM<br/>
 * <p/>
 * @author John Mwai
 */
public abstract class Extension {
    
    private String directory = "C:\\Joomla Extension Maker";
    private String vendorEmail = "info@example.com";
    protected String vendor = "AN Inc.";
    protected String client = "Other Inc.";
    protected String version = null;
    private String propertiesFile = null;
    protected String name = "SomeExtension";
    private String updateServerSuffix = "installers archive";
    protected String DS = "\\";
    private String vendorUrl;
    private String copyrightInfo;
    private String licenceInfo;
    private String description;
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
        
    }
    
    public String getUpdateServerSuffix() {
        return updateServerSuffix;
    }
    
    public void setUpdateServerSuffix(String updateServerSuffix) {
        this.updateServerSuffix = updateServerSuffix;
    }
    
    public abstract void inspect();
    
    public abstract void build();
    
    public String getDirectory() {
        return directory;
    }
    
    public void setDirectory(String directory) {
        File dir = new File(directory);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("The argument is not a directory");
        }
        try {
            this.directory = dir.getCanonicalPath();
        } catch (IOException ex) {
            System.err.println("Couldn't set the directory");
            System.exit(1);
        }
    }
    
    public String getClient() {
        return client;
    }
    
    public String getVendor() {
        return vendor;
    }
    
    public void setClient(String client) {
        this.client = client;
    }
    
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    public String getPropertiesFile() {
        if (propertiesFile == null) {
            String aname = name.toLowerCase();
            return getUpdateSeverFolder() + "\\" + aname + ".joomlaproperties";
        }
        return propertiesFile;
    }
    
    public String getTableHistoryFile() {
        if (propertiesFile == null) {
            String aname = name.toLowerCase();
            return getUpdateSeverFolder() + "\\" + aname + ".tablhist";
        }
        return propertiesFile;
    }
    
    public void setPropertiesFile(String file) {
        propertiesFile = file;
    }
    
    public String getInstallationName() {
        return name;
    }
    
    public String getName() {
        return name;
    }
    
    protected final Object getPropertiesFromDirectory() throws IOException, ClassNotFoundException {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(
                    getPropertiesFile())));
            Object properties;
            properties = in.readObject();
            return properties;
        } finally {
            in.close();
        }
    }
    
    public String getDirectorySeperator() {
        return DS;
    }
    
    protected String getUpdateSeverFolder() {
        return directory + DS + "installers" + DS + name + " " + updateServerSuffix;
    }
    
    protected String getExtensionRoot() {
        return getUpdateSeverFolder() + DS + name + "-" + version;
    }
    
    protected String getExtensionTempRoot() {
        return getUpdateSeverFolder() + DS + name + "-" + version + " tmp";
    }
    
    protected final void storePropertiesObj(Object obj) throws IOException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(
                    getPropertiesFile())));
            out.writeObject(obj);
        } finally {
            out.close();
        }
    }
    private javax.swing.ProgressMonitor progressMonitor;
    private long fileSize;
    private long zippedSize;
    private Calendar calendar;
    private long time = 0;

    protected void zip() throws FileNotFoundException, IOException {
        String root = getExtensionRoot();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
                     root + ".zip"))) {
            fileSize = dirSize(new File(root));
            progressMonitor = new javax.swing.ProgressMonitor(new javax.swing.JComponent() {
            },
                    getInstallationName() + " " + version + " zipping.\nPlease wait...",
                    "", 0, (int) fileSize);
            eventTimer et = new eventTimer("Zipping");
            et.start();
            zipDir(root, zos, name);
            et.stop();
            endProgress();
        }
    }
    
    private long dirSize(File dir) {
        
        if (dir.isFile()) {
            return dir.length();
        }
        int l = 0;
        for (File f : dir.listFiles()) {
            l += dirSize(f);
        }
        return l;
    }
    
    private void zipDir(String dir2zip, ZipOutputStream zos, String pre) throws FileNotFoundException, IOException {
        if (progressMonitor.isCanceled()) {
            return;
        }
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
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
            progressMonitor.setNote(
                    "Complete: " + String.format("%.3f", getZippedMB()) + "MB File: " + f.getName());
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
    
    protected class eventTimer {

        private String eventName;
        private Calendar c;
        
        protected eventTimer(String eventName) {
            this.eventName = eventName;
        }

        protected void start() {
            c = new GregorianCalendar();
        }

        protected void stop() {
            long t = new GregorianCalendar().getTimeInMillis() - c.getTimeInMillis();
            System.out.println(" -- \n[Event Timer ]\t" + eventName + " took " + String.format("%.3f",
                    (double) t / 1000) + " seconds\n -- ");
        }
    }
    
    private void endProgress() {
        if (progressMonitor.isCanceled()) {
            System.err.println("Zipping canceled by user");
        } else {
            System.out.println("Zipping completed successfully");
        }
        progressMonitor.close();
        Toolkit.getDefaultToolkit().beep();
    }
    
    private double getZippedMB() {
        return ((double) zippedSize) / 1000000;
    }
    
    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }
    
    public String getVendorEmail() {
        return vendorEmail;
    }
    
    public void setVendorUrl(String url) {
        this.vendorUrl = url;
    }
    
    public String getVendorUrl() {
        return vendorUrl;
    }
    
    public void setCopyrightInfo(String info) {
        this.copyrightInfo = info;
    }
    
    public void setLicenceInfo(String info) {
        this.licenceInfo = info;
    }
    
    public void setDescription(String desc) {
        this.description = desc;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCopyrightInfo() {
        return copyrightInfo;
    }
    
    public String getLicenceInfo() {
        return licenceInfo;
    }
}
