/* Generated By:JJTree: Do not edit this line. ASTConditionalOrExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTConditionalOrExpression extends SimpleNode {
  public ASTConditionalOrExpression(int id) {
    super(id);
  }

  public ASTConditionalOrExpression(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTConditionalOrExpression(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTConditionalOrExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e5987b68c64c8fe060e748a521cf82f0 (do not edit this line) */
