/* Generated By:JJTree: Do not edit this line. ASTFunctionCallParameters.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTFunctionCallParameters extends SimpleNode {
  public ASTFunctionCallParameters(int id) {
    super(id);
  }

  public ASTFunctionCallParameters(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTFunctionCallParameters(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTFunctionCallParameters(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=311c91fe9b4f3c07e66032095cc8f066 (do not edit this line) */
