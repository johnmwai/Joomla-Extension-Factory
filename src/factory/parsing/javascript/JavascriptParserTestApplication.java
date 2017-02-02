/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factory.parsing.javascript;

import factory.parsing.javascript.JavascriptParser.Range;
import java.awt.Color;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author John Mwai
 */
public class JavascriptParserTestApplication {

    private HashMap<String, Object> map;
    private Color backgroundColor;

    public void parse(JTextPane textPane, HashMap<String, Object> map) {
        this.map = map;
        StyledDocument doc = textPane.getStyledDocument();
        JavascriptParser jsp = new JavascriptParser();

        String text = "";
        try {
            text = doc.getText(0, doc.getLength());

        } catch (BadLocationException ex) {
            Logger.getLogger(JavascriptParserTestApplication.class.getName()).log(
                    Level.SEVERE,
                    null, ex);
            System.exit(-1);
        }
        jsp.setSource(text);
        jsp.parse();
        HashMap<Range, JavascriptElements> elementTable = jsp.getElementTable();

        int SIZE = 15;



        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setFontSize(att, SIZE);
        backgroundColor = textPane.getBackground();
        StyleConstants.setBackground(att, backgroundColor);
        StyleConstants.setForeground(att, Color.BLACK);
        StyleConstants.setBold(att, false);
        StyleConstants.setFontFamily(att, "dialog");

        doc.setCharacterAttributes(0, text.length(), att, false);

        SimpleAttributeSet identifier = new SimpleAttributeSet(att);
        StyleConstants.setBold(identifier, true);

        SimpleAttributeSet keyword = new SimpleAttributeSet(att);
        StyleConstants.setBackground(keyword, new Color(0xf5f5f5));
        StyleConstants.setFontFamily(keyword, "monospaced");


        for (Range r : elementTable.keySet()) {
            JavascriptElements elm = elementTable.get(r);
            
            if (elm == JavascriptElements.Identifier) {
                setAttributes(doc, r, identifier, elm);
            } else if (elm == JavascriptElements.Keyword) {
                setAttributes(doc, r, keyword, elm);
            } else {
                setAttributes(doc, r, att, elm);
            }
        }

    }

    private void setAttributes(StyledDocument doc, Range r,
            SimpleAttributeSet att, JavascriptElements elem) {
        Object o = map.get(elem.name() + "_used");
        boolean b;
        if (o == null) {
            b = false;
        } else {
            Boolean used = (Boolean) o;
            b = Boolean.valueOf(used);
        }
        if (b) {
            Color fg = (Color) map.get(elem.name() + "_foreground");
            Color bg = (Color) map.get(elem.name() + "_background");
            if (Color.WHITE.equals(bg)) {
                bg = backgroundColor;
            }
            StyleConstants.setBackground(att, bg);
            StyleConstants.setForeground(att, fg);

            doc.setCharacterAttributes(r.begin, r.end - r.begin, att, false);
        }

    }
}
