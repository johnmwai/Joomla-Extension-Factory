/* Generated By:JJTree: Do not edit this line. ASTstyle.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTstyle extends SimpleNode {
  public ASTstyle(int id) {
    super(id);
  }

  public ASTstyle(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTstyle(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTstyle(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=99be29b84d4e5c7980432995d2510454 (do not edit this line) */