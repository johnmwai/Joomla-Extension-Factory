/* Generated By:JJTree: Do not edit this line. ASTRelationalExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTRelationalExpression extends SimpleNode {
  public ASTRelationalExpression(int id) {
    super(id);
  }

  public ASTRelationalExpression(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTRelationalExpression(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTRelationalExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7effb08f0df27911336060dd924a33b1 (do not edit this line) */
