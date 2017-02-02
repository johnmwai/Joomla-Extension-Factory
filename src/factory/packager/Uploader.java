package factory.packager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import javax.swing.ProgressMonitor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *
 * @author John Mwai
 */
public class Uploader {

    protected String URLToUpdateManifest;
    protected String URLToScript;
    private ProgressMonitor progressMonitor;

    public Uploader(String URLToUpdateManifest, String URLToScript) {
        this.URLToUpdateManifest = URLToUpdateManifest;
        this.URLToScript = URLToScript;
    }

    protected String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }
    private final static char[] MULTIPART_CHARS =
            "_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();

    private String getXMLManifestFromServer() throws Exception {
        System.out.println(
                "Getting update manifest from\n" + URLToUpdateManifest);
        URL updateMF = new URL(URLToUpdateManifest);
        try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                        updateMF.openStream()))) {
            String XML = "";
            String line;
            while ((line = in.readLine()) != null) {
                XML += line;
            }

            return XML;
        }
    }

    public void processUpdateServerManifest() {
    }

    private void addReleaseNode(
            Document document,
            String nameOfComponent,
            String descriptionOfTheUpdate,
            String installationNameOfComponent,
            String typeOfExtension,
            String versionOfExtension,
            String infoURLTitle,
            String infoURL,
            String downloadFormat,
            String downloadType,
            String URLToDownloads,
            String nameOFInstallerPackage,
            String maintainer,
            String maintainerURL,
            String section,
            String targetPalatformName,
            String targetPlatformVersion) throws Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        //Expressions
        String updatesExp = "/updates";

        Node updates = (Node) xpath.evaluate(updatesExp, document,
                XPathConstants.NODE);

        Element update = document.createElement("update");
        update.appendChild(createElementWithText(document, "name",
                nameOfComponent));
        update.appendChild(createElementWithText(document, "description",
                descriptionOfTheUpdate));
        update.appendChild(createElementWithText(document, "element",
                installationNameOfComponent));
        update.appendChild(createElementWithText(document, "type",
                typeOfExtension));
        update.appendChild(createElementWithText(document, "version",
                versionOfExtension));

        Element infoUrlElm = document.createElement("infourl");
        infoUrlElm.setAttribute("title", infoURLTitle);
        infoUrlElm.setTextContent(infoURL);
        update.appendChild(infoUrlElm);

        Element download = document.createElement("dowmload");
        download.setAttribute("type", downloadType);
        download.setAttribute("format", downloadFormat);
        download.setTextContent(
                URLToDownloads + "/" + nameOFInstallerPackage);

        Element downloads = document.createElement("dowmloads");
        downloads.appendChild(download);
        update.appendChild(downloads);
        update.appendChild(createElementWithText(document, "maintainer",
                maintainer));
        update.appendChild(createElementWithText(document, "maintainerurl",
                maintainerURL));
        update.appendChild(createElementWithText(document, "section",
                section));
        Element targetPLFM = document.createElement("targetplatform");
        targetPLFM.setAttribute("name", targetPalatformName);
        targetPLFM.setAttribute("version", targetPlatformVersion);
        update.appendChild(targetPLFM);
        updates.appendChild(update);
    }

    /**
     * This method gets the XML manifest file from the server, modifies the file
     * and submits the file to a script in the server that replaces the update
     * manifest with the new version update manifest.
     */
    protected void upload(
            String nameOfComponent,
            String descriptionOfTheUpdate,
            String installationNameOfComponent,
            String typeOfExtension,
            String versionOfExtension,
            String infoURLTitle,
            String infoURL,
            String downloadFormat,
            String downloadType,
            String URLToDownloads,
            String nameOFInstallerPackage,
            String maintainer,
            String maintainerURL,
            String section,
            String targetPalatformName,
            String nameOfFileVariable,
            String fileName,
            String locationOfInstallerInFileSystem,
            long sizeOfInstallerPackage) throws Exception {
        progressMonitor = new javax.swing.ProgressMonitor(new javax.swing.JComponent() {
        },
                nameOfComponent + " " + versionOfExtension + " uploading...\nPlease wait...",
                "", 0, (int) sizeOfInstallerPackage);

        try {


            DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(getXMLManifestFromServer()));
            Document document = builder.parse(is);
            addReleaseNode(document, nameOfComponent, descriptionOfTheUpdate,
                    installationNameOfComponent, typeOfExtension,
                    versionOfExtension, infoURLTitle, infoURL, downloadFormat,
                    downloadType, URLToDownloads, nameOFInstallerPackage,
                    maintainer, maintainerURL, section, targetPalatformName,
                    "1.6");

            addReleaseNode(document, nameOfComponent, descriptionOfTheUpdate,
                    installationNameOfComponent, typeOfExtension,
                    versionOfExtension, infoURLTitle, infoURL, downloadFormat,
                    downloadType, URLToDownloads, nameOFInstallerPackage,
                    maintainer, maintainerURL, section, targetPalatformName,
                    "2.5");

            String boundary1 = generateBoundary();
            String boundary2 = generateBoundary();
            System.out.println("Boundary 1: " + boundary1);
            System.out.println("Boundary 2: " + boundary2);
            URLConnection connection = new URL(URLToScript).openConnection();
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary1);

            connection.setDoOutput(true);
            try (OutputStream out = connection.getOutputStream()) {
                PrintStream ps = new PrintStream(out);

                //paramters
                String paramName = "security";
                String paramValue = "my_secret";




                byte[] readBuffer = new byte[2156];
                long uploaded = 0;
                File installerPackage = new File(locationOfInstallerInFileSystem,
                        nameOFInstallerPackage);
                //
                /*
                 * The following lines upload the manifest file to a PHP script
                 * in a remote server.
                 * refer to
                 * http://stackoverflow.com/questions/2469451/upload-files-with-java
                 * and
                 * http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.2
                 * 
                 */

                String CRLF = "\r\n";
                
                System.out.println("Uploading extension .... ");
                ps.print(CRLF);
                ps.print("--" + boundary1 + CRLF);
                ps.print(
                        "Content-Disposition: form-data; name=\"" + paramName + "\"" + CRLF);
                ps.print(CRLF);
                ps.print(paramValue + CRLF);
                ps.print("--" + boundary1 + CRLF);
                ps.print(
                        "Content-Disposition: form-data; name=\"" + nameOfFileVariable + "\"" + CRLF);
                ps.print(
                        "Content-Type: multipart/mixed; boundary=" + boundary2 + CRLF);
                ps.print(CRLF);
                ps.print("--" + boundary2 + CRLF);
                ps.print(
                        "Content-Disposition: file; filename=\"" + fileName + "\"");
                ps.print("Content-Type: text/xml" + CRLF);
                ps.print(CRLF);
                //  ... the update manifest xml file ... 
                TransformerFactory.newInstance().newTransformer().transform(
                        new DOMSource(document),
                        new StreamResult(out));
                ps.print(CRLF);
                ps.print("--" + boundary2 + CRLF);
                //////
                ps.print(
                        "Content-Disposition: file; filename=\"somefile.txt\"");
                ps.print("Content-Type: text/plain" + CRLF);
                ps.print(CRLF);
                //  ... the update manifest xml file ... 
                ps.print("THe content of my file" + CRLF);
                ////////
                ps.print("--" + boundary2 + CRLF);
                ps.print(
                        "Content-Disposition: file; filename=\"" + nameOFInstallerPackage + "\"" + CRLF);
                ps.print("Content-Type: application/octet-stream " + CRLF);
                ps.print("Content-Transfer-Encoding: binary" + CRLF);
                ps.print(CRLF);// 
                // ...contents of the installer package... 
                try (FileInputStream fis = new FileInputStream(installerPackage)) {
                    int bytesIn;
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        if (progressMonitor.isCanceled()) {
                            continue;
                        }
                        out.write(readBuffer, 0, bytesIn);
                        uploaded += bytesIn;
                        progressMonitor.setProgress((int) uploaded);
                        progressMonitor.
                                setNote("Uploaded: " + String.format("%.3f",
                                (((double) uploaded) / 1000000)) + "MB");
                    }
                    endProgress();
                }
                ps.print("--" + boundary2 + "--" + CRLF);
                ps.print("--" + boundary1 + "--" + CRLF);

                //Write the response of the server
                System.out.println("Server said ... ");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                        connection.getInputStream()));

                String decodedString;

                while ((decodedString = in.readLine()) != null) {
                    System.out.println(decodedString);
                }
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
            throw new Exception(ex);
        }
    }

    private void endProgress() {
        if (progressMonitor.isCanceled()) {
            System.err.println("Upload canceled by user");
        } else {
            System.out.println("Upload completed successfully");
        }
        progressMonitor.close();
    }

    private Node createElementWithText(Document document, String tagname,
            String textContent) {
        Element e = document.createElement(tagname);
        e.setTextContent(textContent);
        return e;
    }
}
