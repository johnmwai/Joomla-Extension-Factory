/* Generated By:JJTree: Do not edit this line. ASTdl.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTdl extends SimpleNode {
  public ASTdl(int id) {
    super(id);
  }

  public ASTdl(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTdl(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTdl(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=98c56eac36e06d62b28e5e01b36d6d0b (do not edit this line) */
