/* Generated By:JJTree: Do not edit this line. ASTAssignmentOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTAssignmentOperator extends SimpleNode {
  public ASTAssignmentOperator(int id) {
    super(id);
  }

  public ASTAssignmentOperator(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTAssignmentOperator(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTAssignmentOperator(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9b4bcdf8add7ce84eeab9e6ec4578c23 (do not edit this line) */
