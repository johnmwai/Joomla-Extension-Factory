/* Generated By:JJTree: Do not edit this line. ASTsmall.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTsmall extends SimpleNode {
  public ASTsmall(int id) {
    super(id);
  }

  public ASTsmall(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTsmall(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTsmall(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3fb99d5c49f237ee3f27b7271cfbd6e5 (do not edit this line) */
