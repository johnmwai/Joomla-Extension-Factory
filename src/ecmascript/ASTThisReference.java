/* Generated By:JJTree: Do not edit this line. ASTThisReference.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package ecmascript;

public
class ASTThisReference extends SimpleNode {
  public ASTThisReference(int id) {
    super(id);
  }

  public ASTThisReference(EcmaScript p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTThisReference(id);
  }

  public static Node jjtCreate(EcmaScript p, int id) {
    return new ASTThisReference(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(EcmaScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=46c7a298667f203dbc5a2738538dc176 (do not edit this line) */
