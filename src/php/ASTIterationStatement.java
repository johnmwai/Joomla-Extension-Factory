/* Generated By:JJTree: Do not edit this line. ASTIterationStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTIterationStatement extends SimpleNode {
  public ASTIterationStatement(int id) {
    super(id);
  }

  public ASTIterationStatement(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTIterationStatement(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTIterationStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=610957cb86f59c85b528e77b363b3b8f (do not edit this line) */
