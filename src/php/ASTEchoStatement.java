/* Generated By:JJTree: Do not edit this line. ASTEchoStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTEchoStatement extends SimpleNode {
  public ASTEchoStatement(int id) {
    super(id);
  }

  public ASTEchoStatement(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTEchoStatement(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTEchoStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2ab711ad57783599d22cb814b97874ae (do not edit this line) */