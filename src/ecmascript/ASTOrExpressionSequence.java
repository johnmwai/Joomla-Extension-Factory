/* Generated By:JJTree: Do not edit this line. ASTOrExpressionSequence.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTOrExpressionSequence extends SimpleNode {
  public ASTOrExpressionSequence(int id) {
    super(id);
  }

  public ASTOrExpressionSequence(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTOrExpressionSequence(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTOrExpressionSequence(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=438573f4715a51426c7f55bdf954eb4c (do not edit this line) */