package factory.codegen;

/**
 *
 * @author John Mwai
 */
public class ExtensionInformation {

    private String version;
    private String appName;
    private String copyright;
    private String license;
    private String author;
    private String author_mail;
    private String author_website;
    private String summary;
    private String joomlaDirectory;
    private String releaseIndex;

    /**
     * Instantiate ExtensionInformation.
     *
     * @param version version
     * @param appName Application name
     * @param copyright Copyright information
     * @param license license
     * @param author Author name
     * @param author_mail Author email
     * @param author_website Author web site
     * @param summary Short description
     * @param joomlaDirectory Path to the Joomla Directory
     * @param releaseIndex Release index.
     */
    public ExtensionInformation(String version, String appName, String copyright,
            String license, String author, String author_mail,
            String author_website, String summary, String joomlaDirectory,
            String releaseIndex) {
        this.version = version;
        this.appName = appName;
        this.copyright = copyright;
        this.license = license;
        this.author = author;
        this.author_mail = author_mail;
        this.author_website = author_website;
        this.summary = summary;
        this.joomlaDirectory = joomlaDirectory;
        this.releaseIndex = releaseIndex;
    }

    public ExtensionInformation() {
    }

    /**
     * Set release index
     *
     * @param releaseIndex The release index to set
     */
    public void setReleaseIndex(String releaseIndex) {
        this.releaseIndex = releaseIndex;
    }

    /**
     * Get release index
     *
     * @return The release index.
     */
    public String getReleaseIndex() {
        if (releaseIndex == null) {
            releaseIndex = "3";
        }
        return releaseIndex;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return the copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * @param copyright the copyright to set
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * @return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * @param license the license to set
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the author_mail
     */
    public String getAuthor_mail() {
        return author_mail;
    }

    /**
     * @param author_mail the author_mail to set
     */
    public void setAuthor_mail(String author_mail) {
        this.author_mail = author_mail;
    }

    /**
     * @return the author_website
     */
    public String getAuthor_website() {
        return author_website;
    }

    /**
     * @param author_website the author_website to set
     */
    public void setAuthor_website(String author_website) {
        this.author_website = author_website;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the joomlaDirectory
     */
    public String getJoomlaDirectory() {
        return joomlaDirectory;
    }

    /**
     * @param joomlaDirectory the joomlaDirectory to set
     */
    public void setJoomlaDirectory(String joomlaDirectory) {
        this.joomlaDirectory = joomlaDirectory;
    }
}
