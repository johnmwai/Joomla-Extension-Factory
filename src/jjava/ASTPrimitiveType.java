/* Generated By:JJTree: Do not edit this line. ASTPrimitiveType.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package jjava;

public
class ASTPrimitiveType extends SimpleNode {
  public ASTPrimitiveType(int id) {
    super(id);
  }

  public ASTPrimitiveType(JavaParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new ASTPrimitiveType(id);
  }

  public static Node jjtCreate(JavaParser p, int id) {
    return new ASTPrimitiveType(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(JavaParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=efe8e968df3915b8c26502c2e9845f7e (do not edit this line) */