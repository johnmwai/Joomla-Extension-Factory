/* Generated By:JJTree: Do not edit this line. ASTAndExpressionSequence.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTAndExpressionSequence extends SimpleNode {
  public ASTAndExpressionSequence(int id) {
    super(id);
  }

  public ASTAndExpressionSequence(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTAndExpressionSequence(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTAndExpressionSequence(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5d3a580d5bc36b26da9c05eb72bc9c4e (do not edit this line) */
