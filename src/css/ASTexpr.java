/* Generated By:JJTree: Do not edit this line. ASTexpr.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package css;

public
class ASTexpr extends SimpleNode {
  public ASTexpr(int id) {
    super(id);
  }

  public ASTexpr(CSSParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTexpr(id);
  }

  public static Node jjtCreate(CSSParser p, int id) {
    return new ASTexpr(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(CSSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0a16a8a00bc91b16d9da12e4e0653fed (do not edit this line) */
