/* Generated By:JJTree: Do not edit this line. ASTpcdata.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTpcdata extends SimpleNode {
  public ASTpcdata(int id) {
    super(id);
  }

  public ASTpcdata(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTpcdata(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTpcdata(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=bc91218c61bb7da03bddc497b36a3ad5 (do not edit this line) */
