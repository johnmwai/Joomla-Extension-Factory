/* Generated By:JJTree: Do not edit this line. ASTVisibility.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTVisibility extends SimpleNode {
  public ASTVisibility(int id) {
    super(id);
  }

  public ASTVisibility(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTVisibility(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTVisibility(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7f01fdadaae6bb4e2640a3dd239681b6 (do not edit this line) */
