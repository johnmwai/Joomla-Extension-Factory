/* Generated By:JJTree: Do not edit this line. ASTtd.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTtd extends SimpleNode {
  public ASTtd(int id) {
    super(id);
  }

  public ASTtd(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTtd(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTtd(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=61d9b467d22981c9b017cb17d88707d1 (do not edit this line) */
