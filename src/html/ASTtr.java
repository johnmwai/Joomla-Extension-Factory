/* Generated By:JJTree: Do not edit this line. ASTtr.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTtr extends SimpleNode {
  public ASTtr(int id) {
    super(id);
  }

  public ASTtr(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTtr(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTtr(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8f3fb0cebd36aa211fb42eece948511b (do not edit this line) */
