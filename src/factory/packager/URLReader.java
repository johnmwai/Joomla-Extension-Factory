package factory.packager;

import java.io.*;
import java.net.*;

public class URLReader {

    public static void main(String[] args) throws Exception {
        System.setProperty("http.proxyHost", "192.168.0.1");
        System.setProperty("http.proxyPort", "8080");

        URL yahoo = new URL("http://fuscard.com/dev/braviacart/update_manifest.xml");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                yahoo.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }

        in.close();
    }
}