/* Generated By:JJTree: Do not edit this line. ASTContinueStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTContinueStatement extends SimpleNode {
  public ASTContinueStatement(int id) {
    super(id);
  }

  public ASTContinueStatement(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTContinueStatement(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTContinueStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f40b8174bb16a894e719dd6977357280 (do not edit this line) */
