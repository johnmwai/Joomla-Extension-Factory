/* Generated By:JJTree: Do not edit this line. ASTinput.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package html;

public
class ASTinput extends SimpleNode {
  public ASTinput(int id) {
    super(id);
  }

  public ASTinput(html32 p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTinput(id);
  }

  public static Node jjtCreate(html32 p, int id) {
    return new ASTinput(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(html32Visitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=db33d0275740dc1f9c198fd98ebcac87 (do not edit this line) */