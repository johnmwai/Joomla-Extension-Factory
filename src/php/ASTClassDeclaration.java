/* Generated By:JJTree: Do not edit this line. ASTClassDeclaration.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package php;

public
class ASTClassDeclaration extends SimpleNode {
  public ASTClassDeclaration(int id) {
    super(id);
  }

  public ASTClassDeclaration(PHP p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTClassDeclaration(id);
  }

  public static Node jjtCreate(PHP p, int id) {
    return new ASTClassDeclaration(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=d68539beb94706397cd12e53f3f7d9fd (do not edit this line) */
