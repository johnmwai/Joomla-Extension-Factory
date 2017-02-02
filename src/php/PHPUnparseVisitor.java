package php;

import java.io.PrintStream;

/**
 *
 * @author John Mwai
 */
public class PHPUnparseVisitor implements PHPVisitor {

    protected PrintStream out;

    public PHPUnparseVisitor(PrintStream o) {
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

    public Object visit(SimpleNode node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTPhpPage node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTHtmlBlock node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTThrowStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTTryBlock node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTEndOfStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTEmbeddedHtml node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTDefineStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLabeledStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTExpressionStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTCompoundStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTSelectionStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTIterationStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTJumpStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTParameterList node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTClassDeclaration node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTClassMembers node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTMemberDeclaration node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTInterfaceDeclaration node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTInterfaceMembers node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTInterfaceMember node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTIncludeStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTEchoStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLogicalTextOrExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLogicalTextXorExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLogicalTextAndExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTAssignmentExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTAssignmentOperator node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTConditionalExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLogical_Or_Expression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLogical_And_Expression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTBitwiseOrExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTBitwiseXorExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTBitwiseAndExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTEqualityExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTRelationalExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTShiftExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTAdditiveExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTMultiplicativeExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTCastExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTUnaryExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTPrefixIncDecExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTPostfixIncDecExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTInstanceOfExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTPostfixExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTArray node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTClassInstantiation node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTVariable node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTArgumentExpressionList node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTConstant node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTString node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTDoubleStringLiteral node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTVisibility node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTerror_skipto node, Object data) {
        return print(node, data);
    }
}
