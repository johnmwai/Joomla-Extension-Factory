/* Generated By:JJTree: Do not edit this line. ASTkbd.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTkbd extends SimpleNode {
  public ASTkbd(int id) {
    super(id);
  }

  public ASTkbd(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTkbd(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTkbd(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=86c800cc7c0d13017b357f0b316528e3 (do not edit this line) */
