/* Generated By:JJTree: Do not edit this line. ASTString.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTString extends SimpleNode {
  public ASTString(int id) {
    super(id);
  }

  public ASTString(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTString(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTString(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e0fda7968e5de01c7052eac33f2d00a2 (do not edit this line) */