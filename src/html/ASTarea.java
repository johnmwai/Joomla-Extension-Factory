/* Generated By:JJTree: Do not edit this line. ASTarea.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTarea extends SimpleNode {
  public ASTarea(int id) {
    super(id);
  }

  public ASTarea(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTarea(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTarea(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=561ca50da5bd78dd262036142f95d603 (do not edit this line) */