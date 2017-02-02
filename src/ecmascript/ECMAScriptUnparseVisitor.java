package ecmascript;

import java.io.PrintStream;

/**
 *
 * @author John Mwai
 */
public class ECMAScriptUnparseVisitor implements EcmaScriptVisitor{
    
    protected PrintStream out;

    public ECMAScriptUnparseVisitor(PrintStream o) {
        out = o;
    }

    public Object print(SimpleNode node, Object data) {
        Token t1 = node.jjtGetFirstToken();
        Token t = new Token();
        t.next = t1;

        SimpleNode n;
        for (int ord = 0; ord < node.jjtGetNumChildren(); ord++) {
            n = (SimpleNode) node.jjtGetChild(ord);
            while (true) {
                t = t.next;
                if (t == n.jjtGetFirstToken()) {
                    break;
                }
                print(t);
            }
            n.jjtAccept(this, data);
            t = n.jjtGetLastToken();
        }
        while (t != node.jjtGetLastToken()) {
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
    public Object visit(ASTThisReference node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTParenExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLiteral node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTIdentifier node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTArrayLiteral node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTObjectLiteral node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLiteralField node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTCompositeReference node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTPropertyValueReference node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTPropertyIdentifierReference node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTFunctionCallParameters node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTPostfixExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTOperator node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTUnaryExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTBinaryExpressionSequence node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTAndExpressionSequence node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTOrExpressionSequence node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTConditionalExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTAssignmentExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTExpressionList node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTExpressionNoIn node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTBlock node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTStatementList node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTVariableStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTVariableDeclarationList node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTVariableDeclaration node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTVariableDeclarationNoIn node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTEmptyExpression node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTEmptyStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTExpressionStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTDoStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTWhileStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTForStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTForVarStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTForVarInStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTForInStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTContinueStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTBreakStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTWithStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTCaseGroups node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTCaseGroup node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTCaseGuard node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTLabelledStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTThrowStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTTryStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTCatchClause node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTFinallyClause node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTFunctionDeclaration node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTFormalParameterList node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTProgram node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTImportStatement node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTName node, Object data) {
        return print(node, data);
    }

    @Override
    public Object visit(ASTinsertSemiColon node, Object data) {
        return print(node, data);
    }
    
}
