/* Generated By:JJTree: Do not edit this line. ASTPrimaryExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTPrimaryExpression extends SimpleNode {
  public ASTPrimaryExpression(int id) {
    super(id);
  }

  public ASTPrimaryExpression(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTPrimaryExpression(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTPrimaryExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=42a3b0aad7ef4c93cd65b33f7ad09a6e (do not edit this line) */
