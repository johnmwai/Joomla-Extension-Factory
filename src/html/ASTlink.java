/* Generated By:JJTree: Do not edit this line. ASTlink.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTlink extends SimpleNode {
  public ASTlink(int id) {
    super(id);
  }

  public ASTlink(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTlink(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTlink(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=67be2f5f7d8e2371eb9f3310e785e173 (do not edit this line) */