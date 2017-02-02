package factory.xml;

import java.awt.Container;
import java.awt.HeadlessException;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 *
 * @author John Mwai
 */
public class XMLFrame extends JFrame{
    public static void main(String[] args) {
//        String jaxPropertyName = "javax.xml.parsers.DocumentBuilderFactory";
//        if(System.getProperty(jaxPropertyName) == null){
//            String apacheXercesPropertyValue = 
//                    "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl";
//            System.setProperty(jaxPropertyName, apacheXercesPropertyValue);
//        }
        
        String filename = null;
        if(args.length > 0){
            filename = args[0];
        }else{
            String[] extensions = {"xml", "tld"};
//            WindoUtilities.setNativeLookAndFeel();
            if(filename == null){
                //C:\Users\John Mwai\Documents\NetBeansProjects\Joomla Extension Factory\src\factory\xml\myXml.xml
                filename = "C:\\Users\\John Mwai\\Documents\\NetBeansProjects\\Joomla Extension Factory\\src\\factory\\xml\\myXml.xml";
            }
        }
        new XMLFrame(filename);        
    }

    public XMLFrame(String filename) {
        try{
//            WindoUtilities.setNativeLookAndFeel();
            JTree tree = new XMLTree(filename);
            JFrame frame = new JFrame(filename);
            Container content = frame.getContentPane();
            content.add(new JScrollPane(tree));
            frame.pack();
            frame.setVisible(true);
        }catch(IOException ioe){
            System.out.println("Error creating tree: " + ioe);
        }
    }
    
    
}
