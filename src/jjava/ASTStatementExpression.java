/* Generated By:JJTree: Do not edit this line. ASTStatementExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTStatementExpression extends SimpleNode {
  public ASTStatementExpression(int id) {
    super(id);
  }

  public ASTStatementExpression(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTStatementExpression(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTStatementExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=51c410eb0bcd967525b7aed391134bd6 (do not edit this line) */