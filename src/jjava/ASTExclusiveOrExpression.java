/* Generated By:JJTree: Do not edit this line. ASTExclusiveOrExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTExclusiveOrExpression extends SimpleNode {
  public ASTExclusiveOrExpression(int id) {
    super(id);
  }

  public ASTExclusiveOrExpression(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTExclusiveOrExpression(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTExclusiveOrExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=30aa6491da45d901599884b4816cb5fe (do not edit this line) */
