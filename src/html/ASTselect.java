/* Generated By:JJTree: Do not edit this line. ASTselect.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTselect extends SimpleNode {
  public ASTselect(int id) {
    super(id);
  }

  public ASTselect(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTselect(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTselect(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e3e8b42417f3fc718a90532993c89b47 (do not edit this line) */
