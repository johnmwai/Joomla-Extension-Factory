/* Generated By:JJTree: Do not edit this line. ASTclass_ident.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package css;

public
class ASTclass_ident extends SimpleNode {
  public ASTclass_ident(int id) {
    super(id);
  }

  public ASTclass_ident(CSSParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTclass_ident(id);
  }

  public static Node jjtCreate(CSSParser p, int id) {
    return new ASTclass_ident(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(CSSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=71d04e39de2daf5bf249113fe5d13b7c (do not edit this line) */
