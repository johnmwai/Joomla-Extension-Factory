/* Generated By:JJTree: Do not edit this line. ASTapplet.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTapplet extends SimpleNode {
  public ASTapplet(int id) {
    super(id);
  }

  public ASTapplet(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTapplet(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTapplet(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0c85f1265dc74d62bee9485f52a7c34e (do not edit this line) */
