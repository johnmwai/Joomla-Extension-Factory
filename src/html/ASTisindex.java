/* Generated By:JJTree: Do not edit this line. ASTisindex.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTisindex extends SimpleNode {
  public ASTisindex(int id) {
    super(id);
  }

  public ASTisindex(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTisindex(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTisindex(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=a203bb9c8760a92c932cce411d0640f5 (do not edit this line) */