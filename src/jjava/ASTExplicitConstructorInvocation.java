/* Generated By:JJTree: Do not edit this line. ASTExplicitConstructorInvocation.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTExplicitConstructorInvocation extends SimpleNode {
  public ASTExplicitConstructorInvocation(int id) {
    super(id);
  }

  public ASTExplicitConstructorInvocation(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTExplicitConstructorInvocation(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTExplicitConstructorInvocation(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=95e13261893a419507f8e97373d7fd14 (do not edit this line) */
