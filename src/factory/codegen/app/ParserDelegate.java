package factory.codegen.app;

import com.fuscard.commons.FuscardXMLException;
import com.fuscard.commons.XMLDocument;
import factory.Application;
import java.awt.Color;
import java.awt.Font;
import java.io.PrintStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author John Mwai
 */
public class ParserDelegate implements FuscardXMLConstants {

    private HashMap<String, SimpleAttributeSet> jProductionAttributeSets;
    private HashMap<String, SimpleAttributeSet> jTokenAttributeSets;
    private String language;
    private HashMap<Integer, String> tokenKindLookUp = new HashMap<>();
    private Class constantsInterface;
    private String[] nodeNames;
    //
    private JTextPane textPane;
    private int defaultFontSize = 15;
    //
    private StringWriter sw;
    protected PrintStream out;

    public StringWriter getSw() {
        return sw;
    }

    public ParserDelegate(StringWriter sw, PrintStream out, JTextPane textPane,
            String language, Class constantsInterface, String[] nodeNames) {
        this.sw = sw;
        this.out = out;
        this.textPane = textPane;
        this.language = language;
        this.constantsInterface = constantsInterface;
        this.nodeNames = nodeNames;
    }

    /**
     * FIFO list of styles
     */
    public static class Style {

        int begin;
        int length;
        SimpleAttributeSet theStyle;

        public int getBegin() {
            return begin;
        }

        public int getLength() {
            return length;
        }

        @Override
        public String toString() {
            return "Begin: " + begin + " Length: " + length;
        }
    }

    public void reinit(JTextPane textPane, StringWriter sw, PrintStream ps) {
        this.sw = sw;
        this.out = ps;
        this.textPane = textPane;
        productionStyleruns = new LinkedList<>();
        tokenStyleruns = new LinkedList<>();
    }
    private LinkedList<Style> productionStyleruns = new LinkedList<>();
    private LinkedList<Style> tokenStyleruns = new LinkedList<>();

    public Style registerNode(String kind) {
        Style style = null;
        SimpleAttributeSet sas = jProductionAttributeSets.get(kind);
        if (sas != null) {
            style = new Style();
            productionStyleruns.add(style);
            style.theStyle = sas;
            style.begin = sw.getBuffer().length();
        }
        return style;
    }

    public void finishNode(Style style) {
        if (style != null) {
            style.length = sw.getBuffer().length() - style.begin;
        }
    }

    public void tokenPrintProxy(int k, String image) {
        String kind = tokenKindLookUp.get(k);
        Style style = null;
        if (kind != null) {
            SimpleAttributeSet sas = jTokenAttributeSets.get(kind);
            if (sas != null) {
                style = new Style();
                tokenStyleruns.add(style);
                style.theStyle = sas;
                style.begin = sw.getBuffer().length();
            }
        }
        out.print(addUnicodeEscapes(image));
        if (style != null) {
            style.length = sw.getBuffer().length() - style.begin;
        }
    }

    protected String addUnicodeEscapes(String str) {
        String retval = "";
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if ((ch < 0x20 || ch > 0x7e) && ch != '\t' && ch != '\n'
                    && ch != '\r' && ch != '\f') {
                String s = "0000" + Integer.toString(ch, 16);
                retval += "\\u" + s.substring(s.length() - 4, s.length());
            } else {
                retval += ch;
            }
        }
        return retval;
    }

    public void applyStyles() {
        System.out.println("Apply styles");
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet empty = new SimpleAttributeSet();
        doc.setCharacterAttributes(0, doc.getLength(), empty, true);
        for (Iterator<Style> it = productionStyleruns.iterator(); it.hasNext();) {
            Style s = it.next();
            doc.setCharacterAttributes(s.begin, s.length, s.theStyle, false);
        }
        for (Iterator<Style> it = tokenStyleruns.iterator(); it.hasNext();) {
            Style s = it.next();
            doc.setCharacterAttributes(s.begin, s.length, s.theStyle, false);
        }
    }

    public void prepareProductionAttributeSets() throws FuscardXMLException {
        jProductionAttributeSets = prepareAttributeSets("productions",
                nodeNames);

        Field[] fields = constantsInterface.getFields();
        LinkedList<String> names = new LinkedList<>();
        for (Field field : fields) {
            if (field.getType() == Integer.TYPE) {
                names.add(field.getName());
                try {
                    tokenKindLookUp.put(field.getInt(constantsInterface),
                            field.getName());
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(ParserDelegate.class.getName()).log(
                            Level.SEVERE,
                            null, ex);
                }
            }
        }
        jTokenAttributeSets = prepareAttributeSets("tokens",
                names.toArray(new String[0]));
    }

    private HashMap<String, SimpleAttributeSet> prepareAttributeSets(String type,
            String[] names) throws FuscardXMLException {
        HashMap<String, SimpleAttributeSet> attributeSets = new HashMap<>();
        XMLDocument xml = Application.getInstance().getParserDoc();
        for (int i = 0; i < names.length; i++) {

            String name = names[i];
            String enabled;//enabled
            enabled = xml.getElementValue(
                    new String[]{"root", language, type, name, "enabled"});
            if (enabled != null) {
                switch (enabled) {
                    case "true":
                        break;
                    case "false":
                    default:
                        continue;
                }
            } else if (enabled == null) {
                continue;
            }

            String style =
                    xml.getElementValue(
                    new String[]{"root", language, type, name, XML_STYLE});
            String background =
                    xml.getElementValue(
                    new String[]{"root", language, type, name, XML_BACKGROUND});
            String foreground =
                    xml.getElementValue(
                    new String[]{"root", language, type, name, XML_FOREGROUND});
            String fontsize =
                    xml.getElementValue(
                    new String[]{"root", language, type, name, XML_FONT_SIZE});
            String fontfamily =
                    xml.getElementValue(
                    new String[]{"root", language, type, name, XML_FONT_FAMILY});
            String underlineColor =
                    xml.getElementValue(
                    new String[]{"root", language, type, name, XML_UNDERLINE_COLOR});


            //Style
            int fontStyle = Font.PLAIN;
            if (style != null) {
                fontStyle = Integer.parseInt(style);
            }
            //Font size
            int fontSize = defaultFontSize;
            if (fontsize != null) {
                fontSize = Integer.parseInt(fontsize);
            }
            //Font
            Font f = new Font("monospaced", fontStyle, fontSize);
            if (fontfamily != null) {
                f = new Font(fontfamily, fontStyle, fontSize);
            }
            //Background
            Color bg = Color.WHITE;
            if (background != null) {
                int rgb = Integer.parseInt(background);
                bg = new Color(rgb);
            }
            //Foreground
            Color fg = Color.BLACK;
            if (foreground != null) {
                int rgb = Integer.parseInt(foreground);
                fg = new Color(rgb);
            }
            //Underline
            Color uncol = Color.WHITE;
            if (underlineColor != null) {
                int rgb = Integer.parseInt(underlineColor);
                uncol = new Color(rgb);
            }
            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            if (!fg.equals(Color.BLACK)) {
                StyleConstants.setForeground(attributeSet, fg);
            }
            if (!bg.equals(Color.WHITE)) {
                StyleConstants.setBackground(attributeSet, bg);
            }
            if (!uncol.equals(Color.WHITE)) {
                attributeSet.addAttribute("Underline-Color", uncol);
            }
            StyleConstants.setFontFamily(attributeSet, f.getFamily());
            StyleConstants.setFontSize(attributeSet, fontSize);
            switch (fontStyle) {
                case Font.PLAIN:
                    break;
                case Font.BOLD:
                    StyleConstants.setBold(attributeSet, true);
                    StyleConstants.setItalic(attributeSet, false);
                    break;
                case Font.ITALIC:
                    StyleConstants.setBold(attributeSet, false);
                    StyleConstants.setItalic(attributeSet, true);
                    break;
                case Font.BOLD + Font.ITALIC:
                    StyleConstants.setBold(attributeSet, true);
                    StyleConstants.setItalic(attributeSet, true);
                    break;
            }
            attributeSets.put(name, attributeSet);
        }
        return attributeSets;
    }
}
