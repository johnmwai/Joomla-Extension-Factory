/* Generated By:JJTree: Do not edit this line. ASTbr.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTbr extends SimpleNode {
  public ASTbr(int id) {
    super(id);
  }

  public ASTbr(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTbr(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTbr(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3695fd9d1ed912ab55a5ed0ff75546c6 (do not edit this line) */
