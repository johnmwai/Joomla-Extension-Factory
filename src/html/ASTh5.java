/* Generated By:JJTree: Do not edit this line. ASTh5.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTh5 extends SimpleNode {
  public ASTh5(int id) {
    super(id);
  }

  public ASTh5(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTh5(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTh5(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8190b460a7ed297a417124a81ef274f5 (do not edit this line) */
