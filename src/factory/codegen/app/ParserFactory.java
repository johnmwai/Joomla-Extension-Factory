package factory.codegen.app;

import factory.Application;
import factory.codegen.app.cssprsr.JEF_CSS_Parser;
import factory.codegen.app.ecmaprsr.JEF_Ecma_Parser;
import factory.codegen.app.htmlprsr.JEF_HTML_Parser;
import factory.codegen.app.jprsr.JEF_Java_Parser;
import factory.codegen.app.phpprsr.JEF_PHP_Parser;
import javax.swing.JTextPane;

/**
 *
 * @author John Mwai
 */
public class ParserFactory implements ParsableFileInterface {

    public static void initialize() {
        JEF_Java_Parser.createInstance(new JTextPane());
    }
    private static String base_paser = "enabled_parsers.parser";
    private static String sql = "sql", java = "java", php = "php", html = "html", xml = "xml", javascript = "javascript", css = "css";

    private static boolean find(String[] array, String s) {
        for (String ss : array) {
            if (ss.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static void parse(JTextPane pane, String typeOfFile) throws JEFParserException {
        String[] parsers = Application.getInstance().getValues(base_paser);
        switch (typeOfFile) {
            case PF_JAVA:
                if (find(parsers, java)) {
                    JEF_Java_Parser.parse(pane);
                }
                break;
            case PF_PHP:
                if (find(parsers, php)) {
                    JEF_PHP_Parser.parse(pane);
                }
                break;
            case PF_HTML:
                if (find(parsers, html)) {
                    JEF_HTML_Parser.parse(pane);
                }
                break;
            case PF_JAVASCRIPT:
                if (find(parsers, javascript)) {
                    JEF_Ecma_Parser.parse(pane);
                }
                break;
            case PF_CSS:
                if (find(parsers, css)) {
                    JEF_CSS_Parser.parse(pane);
                }
                break;
            default:
                throw new JEFParserException(
                        "File '" + typeOfFile + "' format not supported");
        }
    }
}
