/* Generated By:JJTree: Do not edit this line. ASTstrong.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTstrong extends SimpleNode {
  public ASTstrong(int id) {
    super(id);
  }

  public ASTstrong(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTstrong(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTstrong(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8b7c882d368a8f6e516be80a77282891 (do not edit this line) */