package css;

import java.io.PrintStream;

/**
 *
 * @author John Mwai
 */
public class CSSUnparseVisitor implements CSSParserVisitor{
protected PrintStream out;

    public CSSUnparseVisitor(PrintStream o) {
        out = o;
    }

    public Object print(SimpleNode node, Object data) {
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
        return data;
    }

    protected void print(Token t) {
        Token tt = t.specialToken;

        if (tt != null) {
            while (tt.specialToken != null) {
                tt = tt.specialToken;
            }
            while (tt != null) {
                out.print(addUnicodeEscapes(tt.image));
                tt = tt.next;
            }
        }
        out.print(addUnicodeEscapes(t.image));
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


    @Override
    public Object visit(SimpleNode node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTerror_skipto node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTstylesheet node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTsingleStyle node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTcss_import node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTmedia node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTmedia_list node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTpage node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTpageID node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASToperator_expr node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTcombinator node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTunary_operator node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTruleset node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTselector node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTsimple_selector node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTclass_ident node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTelement_name node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTattrib node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTpseudo node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTdeclaration node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTexpr node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTterm node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTfunction_expr node, Object data) {
        return print(node, data);
    }
    
}
