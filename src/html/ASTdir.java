/* Generated By:JJTree: Do not edit this line. ASTdir.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTdir extends SimpleNode {
  public ASTdir(int id) {
    super(id);
  }

  public ASTdir(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTdir(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTdir(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=915151e384d7e867ce46cef16d94da5e (do not edit this line) */
