/* Generated By:JJTree: Do not edit this line. ASTa.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTa extends SimpleNode {
  public ASTa(int id) {
    super(id);
  }

  public ASTa(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTa(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTa(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0a1c7b75e756b3c9c5fc5bcc5cab0618 (do not edit this line) */
