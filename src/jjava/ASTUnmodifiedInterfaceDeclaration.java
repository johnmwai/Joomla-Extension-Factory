/* Generated By:JJTree: Do not edit this line. ASTUnmodifiedInterfaceDeclaration.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTUnmodifiedInterfaceDeclaration extends SimpleNode {
  public ASTUnmodifiedInterfaceDeclaration(int id) {
    super(id);
  }

  public ASTUnmodifiedInterfaceDeclaration(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTUnmodifiedInterfaceDeclaration(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTUnmodifiedInterfaceDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=d1a940bc98ef9a5a9e837a0a26ebc58c (do not edit this line) */
