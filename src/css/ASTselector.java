/* Generated By:JJTree: Do not edit this line. ASTselector.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package css;

public
class ASTselector extends SimpleNode {
  public ASTselector(int id) {
    super(id);
  }

  public ASTselector(CSSParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTselector(id);
  }

  public static Node jjtCreate(CSSParser p, int id) {
    return new ASTselector(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(CSSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=064d52a11b15c36ca4a79a2d2e3766cf (do not edit this line) */
