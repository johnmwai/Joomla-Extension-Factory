/* Generated By:JJTree: Do not edit this line. ASTb.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTb extends SimpleNode {
  public ASTb(int id) {
    super(id);
  }

  public ASTb(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTb(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTb(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5d4ebdc5d755b8295505d666b3b996bc (do not edit this line) */
