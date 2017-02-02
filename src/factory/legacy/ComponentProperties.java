package factory.legacy;

import factory.packager.VersionWrapper;
import java.io.File;
import java.io.Serializable;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 23, 2012 -- 9:54:23 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class ComponentProperties implements Serializable {

    private String name = "newcomponent";
    private String admin = "backend";
    private String site = "frontend";
    private String version = "";
    private String vendor = "";
    private String client = "";
    private int releaseIndex = 8;
    private String propertiesFile = null;
    private String extensionRoot = null;
    private String vendorEmail;
    private String vendorUrl;
    private String copyrightInfo;
    private String description;
    private String licenceInfo;

    public ComponentProperties() {
    }

    public ComponentProperties(String name) {
        this.name = name;
    }

    public ComponentProperties(String name, String admin, String site) {
        this.name = name;
        this.admin = admin;
        this.site = site;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        File path = new File(propertiesFile);
        this.propertiesFile = path.getName();
    }

    public int getReleaseIndex() {
        return releaseIndex;
    }

    public void setReleaseIndex(int releaseIndex) {
        this.releaseIndex = releaseIndex;
    }

    public String getClient() {
        return client;
    }

    public String getVendor() {
        return vendor;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getVersion() {
        return version;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setVersion(String version) {
        VersionWrapper vw = new VersionWrapper(version);
        this.version = vw.getVersion();
    }

    public String getAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getExtensionRoot() {
        return extensionRoot;
    }

    public void setExtensionRoot(String extensionRoot) {
        this.extensionRoot = extensionRoot;
    }

    public void printData() {
        System.out.printf("Properties Output: \n");
        System.out.printf("Name: %s\n", name);
        System.out.printf("Admin: %s\n", admin);
        System.out.printf("Client: %s\n", client);
        System.out.printf("Properties file: %s\n", propertiesFile);
        System.out.printf("Release index: %d\n", releaseIndex);
        System.out.printf("Site: %s\n", site);
        System.out.printf("Vendor: %s\n", vendor);
        System.out.printf("VendorEmail: %s\n", vendorEmail);
        System.out.printf("Vendor url: %s\n", vendorUrl);
        System.out.printf("Copyright Info: %s\n", copyrightInfo);
        System.out.printf("Licence: %s\n", licenceInfo);
        System.out.printf("Description: %s\n", description);
        System.out.printf("Version: %s\n", version);
        System.out.printf("Extension root: %s\n", extensionRoot);
        System.out.printf("[done] \n\n");
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
