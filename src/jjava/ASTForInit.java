/* Generated By:JJTree: Do not edit this line. ASTForInit.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTForInit extends SimpleNode {
  public ASTForInit(int id) {
    super(id);
  }

  public ASTForInit(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTForInit(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTForInit(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=71f3d74428bd623d6c52d817df22bb02 (do not edit this line) */