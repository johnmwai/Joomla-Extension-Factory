/* Generated By:JJTree: Do not edit this line. ASTterm.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package css;

public
class ASTterm extends SimpleNode {
  public ASTterm(int id) {
    super(id);
  }

  public ASTterm(CSSParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTterm(id);
  }

  public static Node jjtCreate(CSSParser p, int id) {
    return new ASTterm(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(CSSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3eb2ce315a0104eb235592cf7ba8d78d (do not edit this line) */
