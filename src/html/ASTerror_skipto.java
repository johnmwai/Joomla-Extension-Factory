/* Generated By:JJTree: Do not edit this line. ASTerror_skipto.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTerror_skipto extends SimpleNode {
  public ASTerror_skipto(int id) {
    super(id);
  }

  public ASTerror_skipto(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTerror_skipto(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTerror_skipto(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=a865c2d9c0d785bb5e8ed6d73f26ae38 (do not edit this line) */