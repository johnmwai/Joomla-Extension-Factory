/* Generated By:JJTree: Do not edit this line. ASTh4.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTh4 extends SimpleNode {
  public ASTh4(int id) {
    super(id);
  }

  public ASTh4(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTh4(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTh4(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2276332dc409fff64972787f1b64db3a (do not edit this line) */
