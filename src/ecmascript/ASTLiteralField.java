/* Generated By:JJTree: Do not edit this line. ASTLiteralField.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTLiteralField extends SimpleNode {
  public ASTLiteralField(int id) {
    super(id);
  }

  public ASTLiteralField(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTLiteralField(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTLiteralField(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=601bce40f9c1d07735df6e3bc4c237d9 (do not edit this line) */