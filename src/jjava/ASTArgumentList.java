/* Generated By:JJTree: Do not edit this line. ASTArgumentList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTArgumentList extends SimpleNode {
  public ASTArgumentList(int id) {
    super(id);
  }

  public ASTArgumentList(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTArgumentList(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTArgumentList(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=157a656245dbf76deb0ec3b76fa6f704 (do not edit this line) */
