package factory.packager;

import java.io.File;

/**
 *
 * @author John Mwai
 */
public class JEFDevPackager extends SimplePackager{

    public JEFDevPackager(boolean solicitComment,
            boolean mayUploadOldInstaller) {
        super(solicitComment, mayUploadOldInstaller);
    }
    @Override
    protected void upload(File zippedDir) throws Exception {

//        File zippedDir = new File(rootDir + ".zip");
        if (!zippedDir.isFile()) {
            throw new IllegalStateException(
                    "This method expects the installer package zipped");
        }
        String URLToUpdateManifest = "http://fuscard.com/dev/braviacart/update_manifest.xml";
        String URLToScript = "http://fuscard.com/dev/braviacart/uploader.php";
        Uploader uld = new Uploader(URLToUpdateManifest, URLToScript);

        String nameOfComponent = "BraviaCart";
        String descriptionOfTheUpdate = comment;
        String installationNameOfComponent = "com_braviacart";
        String typeOfExtension = "component";
        String versionOfExtension = version;
        String infoURLTitle = "Mambo Creative";
        String infoURL = "http://mambocreative.com";
        String downloadFormat = "zip";
        String downloadType = "full";
        String URLToDownloads = "http://fuscard.com/dev/braviacart/downloads";
        String nameOFInstallerPackage = zippedDir.getName();
        String maintainer = "Mambo Creative";
        String maintainerURL = "http://mambocreative.com";
        String section = "some-section";
        String targetPalatformName = "joomla";
        String nameOfFileVariable = "files";
        String fileName = "update_manifest.xml";
        String locationOfInstallerInFileSystem = "C:\\Users\\John Mwai\\Documents\\braviacart";

        uld.upload(nameOfComponent, descriptionOfTheUpdate,
                installationNameOfComponent, typeOfExtension, versionOfExtension,
                infoURLTitle, infoURL, downloadFormat, downloadType,
                URLToDownloads, nameOFInstallerPackage, maintainer,
                maintainerURL, section, targetPalatformName, nameOfFileVariable,
                fileName, locationOfInstallerInFileSystem, zippedDir.length());
    }

    /**
     * Run simple packager
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Set proxy settings
        System.setProperty("http.proxyHost", "192.168.0.1");
        System.setProperty("http.proxyPort", "8080");


        JEFDevPackager sp = new JEFDevPackager(true, false);
        if (sp.hasUploadedLastUpdate) {
            return;
        }

        sp.solicitComment();
        //C:\wamp\www\my_joomla\administrator\components\com_braviacart\braviacart.xml
        sp.
                setManifest(
                "C:\\wamp\\www\\my_joomla\\administrator\\components\\com_braviacart\\braviacart.xml");

        //C:\Users\John Mwai\Documents\braviacart
        sp.setUpdateServerFolder("C:\\Users\\John Mwai\\Documents\\braviacart");
        sp.setReleaseLevel(5);
        sp.processManifest(true);
        //show the sources that have been discovered after processing manifest
        sp.printOutSources();
        System.out.println("\n");
        //add an innocent image to the images folder 
        //C:\Users\John Mwai\Documents\bravia order management system\com_braviacart-1.0.39\com_braviacart\language
        sp.addSource(
                "C:\\Users\\John Mwai\\Documents\\bravia order management system\\com_braviacart-1.0.39\\com_braviacart\\language",
                "root\\language");
        sp.addSource(
                "C:\\Users\\John Mwai\\Documents\\bravia order management system\\com_braviacart-1.0.39\\com_braviacart\\script.php",
                "root");
        sp.addSource(
                "C:\\Users\\John Mwai\\Documents\\bravia order management system\\com_braviacart-1.0.39\\com_braviacart\\controller.php",
                "root");
        sp.addSource(
                "C:\\Users\\John Mwai\\Documents\\bravia order management system\\com_braviacart-1.0.39\\com_braviacart\\braviacart.php",
                "root");

        //display the final list of sources before processing
        sp.printOutSources();
        //dispaly the information in the manifest file
        sp.printOutManifest();



        //make the package zipping it and uploading in the process.
        sp.packageExtension(true, false);
    }
}
