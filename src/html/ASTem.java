/* Generated By:JJTree: Do not edit this line. ASTem.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTem extends SimpleNode {
  public ASTem(int id) {
    super(id);
  }

  public ASTem(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTem(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTem(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ed80e9a67726b0082cb1f13159a8dd1a (do not edit this line) */
