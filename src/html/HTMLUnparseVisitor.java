package html;

import java.io.PrintStream;

/**
 *
 * @author John Mwai
 */
public class HTMLUnparseVisitor implements html32Visitor{
    protected PrintStream out;

    public HTMLUnparseVisitor(PrintStream o) {
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
    public Object visit(ASTpcdata node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTattribute node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTattlist node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTtt node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTi node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTb node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTu node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTstrike node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTbig node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTsmall node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTsub node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTsup node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTem node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTstrong node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTdfn node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTcode node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTsamp node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTkbd node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(AST_var node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTcite node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTfont node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTbasefont node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTbr node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTbody node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTaddress node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTdiv node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTcenter node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTa node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTmap node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTarea node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTlink node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTimg node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTapplet node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTparam node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASThr node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTp node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTh1 node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTh2 node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTh3 node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTh4 node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTh5 node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTh6 node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTpre node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTblockquote node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTdl node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTdt node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTdd node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTol node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTul node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTdir node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTmenu node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTli node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTform node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTinput node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTselect node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASToption node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTtextarea node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTtable node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTtr node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTth node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTtd node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTcaption node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASThead node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTtitle node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTisindex node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTbase node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTmeta node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTstyle node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTscript node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASThtml node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTerror_skipto node, Object data) {
        return print(node, data);
    }
    
}
