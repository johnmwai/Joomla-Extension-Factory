/* Generated By:JJTree: Do not edit this line. ASTAndExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTAndExpression extends SimpleNode {
  public ASTAndExpression(int id) {
    super(id);
  }

  public ASTAndExpression(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTAndExpression(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTAndExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3247d8405b59842009afa6d5379a096d (do not edit this line) */
