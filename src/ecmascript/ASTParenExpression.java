/* Generated By:JJTree: Do not edit this line. ASTParenExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTParenExpression extends SimpleNode {
  public ASTParenExpression(int id) {
    super(id);
  }

  public ASTParenExpression(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTParenExpression(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTParenExpression(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=38305b1c1388820158e49eea47064d09 (do not edit this line) */
