/* Generated By:JJTree: Do not edit this line. ASThtml.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASThtml extends SimpleNode {
  public ASThtml(int id) {
    super(id);
  }

  public ASThtml(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASThtml(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASThtml(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f15fdf788d572187bdac96c2937ac9bd (do not edit this line) */