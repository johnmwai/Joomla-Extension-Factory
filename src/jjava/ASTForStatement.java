/* Generated By:JJTree: Do not edit this line. ASTForStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTForStatement extends SimpleNode {
  public ASTForStatement(int id) {
    super(id);
  }

  public ASTForStatement(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTForStatement(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTForStatement(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=a3bd160839f4dfe0c1f46c6deea8a5b0 (do not edit this line) */
