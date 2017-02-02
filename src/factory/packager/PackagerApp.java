package factory.packager;

import com.fuscard.commons.GUIUtils;
import com.fuscard.commons.INIInputOutput;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author John Mwai
 */
public class PackagerApp {

    static final String iniFilePath = "C:\\wamp\\www\\jef\\joomla extension packager.ini";
    private final INIInputOutput ini_io = new INIInputOutput(iniFilePath);
    private Credentials credentials;
    private String db_url;
    private boolean LoggedIn = false;

    public PackagerApp() {
        prepareLogIn();
    }

    private void prepareLogIn() {
        db_url = ini_io.get("Default Database Url");
        if (db_url == null) {
            db_url = "";
        }
        String usr = ini_io.get("Default User");
        String pwd = ini_io.get("Default Password");
        credentials = new Credentials(usr, pwd);
    }

    public void readManifest(Packager packager, File f) {
        JTextPane tp = packager.jTextPaneManifest;
        try {
            tp.read(new FileReader(f), "Manifest");
        } catch (IOException ex) {
            /**
             * This will occur if the file that this method is given is invalid
             * or something like that.
             */
            Logger.getLogger(PackagerApp.class.getName()).log(Level.SEVERE, null,
                    ex);
            packager.writeAttLn(
                    "A java.io.IOException occurred while trying to read the file.");
            return;
        }

        StyledDocument sd = tp.getStyledDocument();
        String manifest = "";
        try {

            manifest = sd.getText(0, sd.getLength());
        } catch (BadLocationException ex) {
            /**
             * This is highly unlikely. That said...
             */
            Logger.getLogger(PackagerApp.class.getName()).log(Level.SEVERE, null,
                    ex);
            packager.writeErrLn("A javax.swing.text.BadLocationException occurred while "
                    + "trying to recover the contents of the file.");
            return;
        }
        String name = getName(manifest);
        String version = getVersion(manifest);
        String description = getDescription(manifest);

        if (name == null || version == null) {
            packager.writeErrLn("The XML file you selected doesn't have some of the required "
                    + "elements of a well formed Joomla manifest file.\n"
                    + "You may have selected the wrong file.");
        }

        packager.write("The name of the joomla extension is: $t\n",
                name != null ? name : "null");
        packager.write("The version of the joomla extension is: $t\n",
                version != null ? version : "null");
        packager.write("The description of the joomla extension is: $t\n",
                description != null ? description : "null");
        String location;
    }

    private String getDescription(String manifest) {
        return findXMLElement(manifest, "description", 0);
    }

    void saveManifestLocation(String location) {
        ini_io.save("Last Manifest Location", location);
    }

    String getLastManifestLocation(Packager packager) {
        return ini_io.get("Last Manifest Location");
    }

    private String getName(String manifest) {
        return findXMLElement(manifest, "name", 0);
    }

    private String findXMLElement(String manifest, String name, int offset) {
        Pattern start = Pattern.compile("<" + name,
                Pattern.CASE_INSENSITIVE);
        Matcher st = start.matcher(manifest);
        if (!st.find(offset)) {
            return null;
        }

        int ca = consumeAttributes(manifest, st.end());
        Pattern closing = Pattern.compile(">");
        Matcher cl = closing.matcher(manifest);
        cl.region(ca, manifest.length());

        if (!cl.lookingAt()) {
            return null;
        }

        Pattern end = Pattern.compile("</" + name + ">",
                Pattern.CASE_INSENSITIVE);
        Matcher en = end.matcher(manifest);
        if (!en.find(cl.end())) {
            return null;
        }

        return manifest.substring(cl.end(), en.start());
    }

    private int consumeAttributes(String manifest, int offset) {
        String p = "^\\s*\\w*\\s*=\"(?:[^\"]|\\\\\")*\"(?:\\s|\\n)*";
        Pattern attribute = Pattern.compile(p);
        Matcher m = attribute.matcher(manifest);
        m.region(offset, manifest.length());
        boolean match = false;
        while (m.lookingAt()) {
            match = true;
            m.region(m.end(), manifest.length());
        }
        if (!match) {
            return offset;
        }
        return m.end();
    }

    private String getVersion(String manifest) {
        return findXMLElement(manifest, "version", 0);
    }

    private void getSite() {
    }

    private void getAdmin() {
    }

    public boolean logOff(Packager packager) {

        if (LoggedIn) {
            LoggedIn = false;
            credentials = new Credentials("", "");
            packager.writeSucLn("You have successfully logged off.");
            JOptionPane.showMessageDialog(packager,
                    "You have logged off successfully.");
            return true;
        }
        return false;
    }

    private String getPasswordAsterisks(String password) {
        String res = "'";

        for (int i = 0; i < password.length(); i++) {
            res += "*";
        }
        return res + "'";
    }

    public boolean logIn(Packager packager) {


        String url = packager.jTextFieldDBUrl.getText(),
                usr = packager.jTextFieldUsrName.getText(),
                pwd = packager.jTextFieldPword.getText();

        String pwAst = getPasswordAsterisks(pwd);

        packager.write("Attempting to log in with name: $t and password: $t\n",
                "'" + usr + "'", pwAst);

        Connection conn = connectToDatabase(url, usr, pwd);
        boolean s = false;
        if (conn == null) {
            /**
             * Failed to log in. Alert user.
             */
            packager.writeErrLn(
                    "Log in failed. "
                    + "This may be because you have not turned on your server "
                    + "or your log in credentials are wrong. Please check and try again.");
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(packager,
                    "Log in failed. Please try again");
        } else {
            /**
             * Log in was successful.
             *
             * If the user opted to save his password save the username and
             * password in the ini file. Save the database url and credentials
             * in this object for later use. Close the dialog and close the
             * connection.
             */
            packager.writeSucLn("Log in was successful.");
            s = true;
            LoggedIn = true;
            ini_io.save("Default Database Url", url);
            boolean save_pw = packager.jCheckBoxSavePword.isSelected();
            if (save_pw) {
                ini_io.save("Default User", usr);
                ini_io.save("Default Password", pwd);
            }
            db_url = url;
            credentials = new Credentials(usr, pwd);
            displayLoginDialog(packager, false);
            try {
                conn.close();
            } catch (SQLException ex) {
                /* nothing to do */
            }
        }
        return s;
    }

    boolean getPasswordChanged(String password) {
        return (credentials.pword == null ? password != null
                : !credentials.pword.equals(password));
    }

    public void displayLoginDialog(Packager packager, boolean b) {
        if (b) {
            prepareLogIn();
            packager.jTextFieldDBUrl.setText(db_url);
            packager.jTextFieldPword.setText(credentials.pword);
            packager.jTextFieldUsrName.setText(credentials.uname);
        }
        GUIUtils.showModalDialog(packager, packager.jDialogDatabaseConnection,
                b, GUIUtils.FUSCARD_LOGO_16X16_RELATIVE_PATH);
    }

    public void setManifest() {
    }

    public Connection connectToDatabase(String url, String usr, String pwd) {

        java.sql.Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection(
                    url,//url
                    usr,//user
                    pwd);//password
        } catch (SQLException ex) {
            //Fail silently
        }
        return connection;

    }

    private static class Credentials {

        private String uname;
        private String pword;

        public Credentials(String uname, String pword) {
            if (uname == null) {
                uname = "";
            }
            if (pword == null) {
                pword = "";
            }
            this.uname = uname;
            this.pword = pword;
        }
    }
}
