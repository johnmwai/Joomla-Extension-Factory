/* Generated By:JJTree: Do not edit this line. ASTBinaryExpressionSequence.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTBinaryExpressionSequence extends SimpleNode {
  public ASTBinaryExpressionSequence(int id) {
    super(id);
  }

  public ASTBinaryExpressionSequence(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTBinaryExpressionSequence(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTBinaryExpressionSequence(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=49591bfb7816507c9664a0bd58701520 (do not edit this line) */
