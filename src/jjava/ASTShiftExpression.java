/* Generated By:JJTree: Do not edit this line. ASTShiftExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTShiftExpression extends SimpleNode {
  public ASTShiftExpression(int id) {
    super(id);
  }

  public ASTShiftExpression(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTShiftExpression(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTShiftExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0d2eece7c0416c2c2db1d4802754dd78 (do not edit this line) */
