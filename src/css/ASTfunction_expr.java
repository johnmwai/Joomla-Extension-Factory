/* Generated By:JJTree: Do not edit this line. ASTfunction_expr.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package css;

public
class ASTfunction_expr extends SimpleNode {
  public ASTfunction_expr(int id) {
    super(id);
  }

  public ASTfunction_expr(CSSParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTfunction_expr(id);
  }

  public static Node jjtCreate(CSSParser p, int id) {
    return new ASTfunction_expr(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(CSSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6066e5a1ffa78909dd4bdf73201e2ab8 (do not edit this line) */