/* Generated By:JJTree: Do not edit this line. ASTmap.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTmap extends SimpleNode {
  public ASTmap(int id) {
    super(id);
  }

  public ASTmap(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTmap(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTmap(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=a0c174f9bbda198ab22cc336da51f493 (do not edit this line) */
