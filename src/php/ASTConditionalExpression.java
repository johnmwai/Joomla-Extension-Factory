/* Generated By:JJTree: Do not edit this line. ASTConditionalExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTConditionalExpression extends SimpleNode {
  public ASTConditionalExpression(int id) {
    super(id);
  }

  public ASTConditionalExpression(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTConditionalExpression(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTConditionalExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=33036d8714960866f252cff7165ecd02 (do not edit this line) */
