/* Generated By:JJTree: Do not edit this line. ASTLogicalTextAndExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTLogicalTextAndExpression extends SimpleNode {
  public ASTLogicalTextAndExpression(int id) {
    super(id);
  }

  public ASTLogicalTextAndExpression(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTLogicalTextAndExpression(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTLogicalTextAndExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=aa8627259eb07618c54c918112f644fd (do not edit this line) */
