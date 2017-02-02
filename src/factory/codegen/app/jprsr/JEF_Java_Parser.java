package factory.codegen.app.jprsr;

import jjava.ASTCompilationUnit;
import jjava.JavaParser;
import jjava.ParseException;
import jjava.SimpleNode;
import jjava.Token;
import com.fuscard.commons.FuscardXMLException;
import factory.codegen.app.FuscardXMLConstants;
import factory.codegen.app.JEFParserException;
import factory.codegen.app.ParserDelegate;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import jjava.JavaParserConstants;
import jjava.JavaParserTreeConstants;
import jjava.JavaUnparseVisitor;

/**
 *
 * @author John Mwai
 */
public class JEF_Java_Parser extends JavaUnparseVisitor implements FuscardXMLConstants {

    private ParserDelegate parserDelegate;

    private void applyStyles() {
        parserDelegate.applyStyles();
    }

    public static JEF_Java_Parser createInstance(JTextPane pane) {
        final StringWriter sw = new StringWriter();
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                sw.write(b);
            }
        });
        JEF_Java_Parser jefJavaParser = new JEF_Java_Parser(ps, pane, sw);
        return jefJavaParser;
    }

    private JEF_Java_Parser(PrintStream o, JTextPane pane, StringWriter sw) {
        super(o);
        parserDelegate = new ParserDelegate(sw, o, pane, "java", JavaParserConstants.class, JavaParserTreeConstants.jjtNodeName);
        try {
            //Prepare the styles
            parserDelegate.prepareProductionAttributeSets();
        } catch (FuscardXMLException ex) {
            Logger.getLogger(JEF_Java_Parser.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public static void parse(JTextPane textPane) throws JEFParserException {
        StyledDocument doc = textPane.getStyledDocument();

        String text = "";
        try {
            text = doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {
            throw new JEFParserException(
                    "There was an error retrieving the contents of the textPane");
        }
        StringReader sr = new StringReader(text);
        JavaParser p = new JavaParser(sr);
        ASTCompilationUnit cu;
        try {
            cu = p.CompilationUnit();
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new JEFParserException(ex.getMessage());
        }
        JEF_Java_Parser jefjp = createInstance(textPane);

        cu.jjtAccept(jefjp, null);
        jefjp.applyStyles();
    }

    @Override
    public Object print(SimpleNode node, Object data) {
        String kind = node.toString();
        ParserDelegate.Style style = parserDelegate.registerNode(kind);
        Token t1 = node.getFirstToken();
        Token t = new Token();
        t.next = t1;

        SimpleNode n;
        for (int ord = 0; ord < node.jjtGetNumChildren(); ord++) {
            n = (SimpleNode) node.jjtGetChild(ord);
            while (true) {
                t = t.next;
                if (t == n.getFirstToken()) {
                    break;
                }
                print(t);
            }
            n.jjtAccept(this, data);
            t = n.getLastToken();
        }

        while (t != node.getLastToken()) {
            t = t.next;
            print(t);
        }
        parserDelegate.finishNode(style);
        return data;
    }

    @Override
    protected void print(Token t) {
        Token tt = t.specialToken;
        if (tt != null) {
            while (tt.specialToken != null) {
                tt = tt.specialToken;
            }
            while (tt != null) {
                parserDelegate.tokenPrintProxy(tt.kind, tt.image);
                tt = tt.next;
            }
        }
        parserDelegate.tokenPrintProxy(t.kind, t.image);
    }
}
