/* Generated By:JJTree: Do not edit this line. ASTName.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTName extends SimpleNode {
  public ASTName(int id) {
    super(id);
  }

  public ASTName(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTName(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTName(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=79e60f1e449b4e62202431e0c56cd1f2 (do not edit this line) */