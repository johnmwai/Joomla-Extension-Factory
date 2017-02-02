package factory.legacy;

import java.io.File;
import java.util.Arrays;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 25, 2012 -- 4:20:53 AM<br/>
 * <p/>
 * @author John Mwai
 */
public class FilesIO {

    public static void main(String[] args) {
//    Path p1 = Paths.get("/tmp/foo");
//Path p2 = Paths.get(args[0]);
//Path p3 = Paths.get(URI.create("file:///Users/joe/FileTest.java"));
//    File f = new File("");
//    URI.create("file:///Users/joe/FileTest.java");
//    System.out.println("");

        // None of these methods requires that the file corresponding
// to the Path exists.
        File path = new File("bravia order management system\\Sep 25 2012\\BraviaCart update server.dfwer3");    // Microsoft Windows syntax
//        try {
//            //File path = Paths.get("/home/joe/foo");       // Solaris syntax
//                    System.out.format("createNewFile: %b%n", path.createNewFile());
//        } catch (IOException ex) {
//            Logger.getLogger(FilesIO.class.getName()).log(Level.SEVERE, null, ex);
//        }
        System.out.format("getAbsolutePath: %s%n", path.getAbsolutePath());
        System.out.format("toString: %s%n", path.toString());
        System.out.format("getName: %s%n", path.getName());
        System.out.format("getParent: %s%n", path.getParent());
        System.out.format("getAbsoluteFile: %s%n", path.getAbsoluteFile());
        System.out.format("isDirectory: %b%n", path.isDirectory());
        System.out.format("getParentFile: %s%n", path.getParentFile());
        System.out.format("getPath: %s%n", path.getPath());
        System.out.format("isAbsolute: %b%n", path.isAbsolute());
        System.out.format("isFile: %b%n", path.isFile());
        System.out.format("List: %s%n", Arrays.toString(path.list()));
    }
}
