/* Generated By:JJTree: Do not edit this line. ASTPrimaryExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTPrimaryExpression extends SimpleNode {
  public ASTPrimaryExpression(int id) {
    super(id);
  }

  public ASTPrimaryExpression(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTPrimaryExpression(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTPrimaryExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=0f0d3424f565af31c16ad17b8117c467 (do not edit this line) */
