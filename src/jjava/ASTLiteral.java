/* Generated By:JJTree: Do not edit this line. ASTLiteral.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTLiteral extends SimpleNode {
  public ASTLiteral(int id) {
    super(id);
  }

  public ASTLiteral(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTLiteral(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTLiteral(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ca89143dbd01716e43c24ed1627b3300 (do not edit this line) */
