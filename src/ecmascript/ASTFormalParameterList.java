/* Generated By:JJTree: Do not edit this line. ASTFormalParameterList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTFormalParameterList extends SimpleNode {
  public ASTFormalParameterList(int id) {
    super(id);
  }

  public ASTFormalParameterList(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTFormalParameterList(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTFormalParameterList(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f4b9f326cb0a1712855b03825f053a9e (do not edit this line) */
