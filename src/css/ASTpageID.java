/* Generated By:JJTree: Do not edit this line. ASTpageID.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package css;

public
class ASTpageID extends SimpleNode {
  public ASTpageID(int id) {
    super(id);
  }

  public ASTpageID(CSSParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTpageID(id);
  }

  public static Node jjtCreate(CSSParser p, int id) {
    return new ASTpageID(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(CSSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f2716dde62d28f553638b9ef2340f84f (do not edit this line) */
