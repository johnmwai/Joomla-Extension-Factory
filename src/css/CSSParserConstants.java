/* Generated By:JJTree&JavaCC: Do not edit this line. CSSParserConstants.java */
package css;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface CSSParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 7;
  /** RegularExpression Id. */
  int MULTI_COMMENT = 8;
  /** RegularExpression Id. */
  int CHARSET_SYM = 9;
  /** RegularExpression Id. */
  int IMPORT_SYM = 10;
  /** RegularExpression Id. */
  int MEDIA_SYM = 11;
  /** RegularExpression Id. */
  int PAGE_SYM = 12;
  /** RegularExpression Id. */
  int HASH = 13;
  /** RegularExpression Id. */
  int SEPARATOR = 14;
  /** RegularExpression Id. */
  int INCLUDES = 15;
  /** RegularExpression Id. */
  int DASHMATCH = 16;
  /** RegularExpression Id. */
  int PLUS = 17;
  /** RegularExpression Id. */
  int MINUS = 18;
  /** RegularExpression Id. */
  int LPAREN = 19;
  /** RegularExpression Id. */
  int RPAREN = 20;
  /** RegularExpression Id. */
  int LBRACE = 21;
  /** RegularExpression Id. */
  int RBRACE = 22;
  /** RegularExpression Id. */
  int COMMA = 23;
  /** RegularExpression Id. */
  int COLON = 24;
  /** RegularExpression Id. */
  int SLASH = 25;
  /** RegularExpression Id. */
  int ALLGROUP = 26;
  /** RegularExpression Id. */
  int GT = 27;
  /** RegularExpression Id. */
  int IDENT = 28;
  /** RegularExpression Id. */
  int IMPORTANT_SYM = 29;
  /** RegularExpression Id. */
  int LETTER = 30;
  /** RegularExpression Id. */
  int PART_LETTER = 31;
  /** RegularExpression Id. */
  int NUMBER = 32;
  /** RegularExpression Id. */
  int EMS = 33;
  /** RegularExpression Id. */
  int EXS = 34;
  /** RegularExpression Id. */
  int PERCENTAGE = 35;
  /** RegularExpression Id. */
  int LENGTH = 36;
  /** RegularExpression Id. */
  int ANGLE = 37;
  /** RegularExpression Id. */
  int TIME = 38;
  /** RegularExpression Id. */
  int FREQ = 39;
  /** RegularExpression Id. */
  int NUMBER_LITERAL = 40;
  /** RegularExpression Id. */
  int URI = 41;
  /** RegularExpression Id. */
  int STRING = 42;
  /** RegularExpression Id. */
  int CHAR_LITERAL = 43;
  /** RegularExpression Id. */
  int STRING_LITERAL = 44;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_MULTI_COMMENT = 1;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<token of kind 6>",
    "<SINGLE_LINE_COMMENT>",
    "\"-->\"",
    "\"@charset \"",
    "\"@import\"",
    "\"@media\"",
    "\"@page\"",
    "\"#\"",
    "\";\"",
    "\"~=\"",
    "\"|=\"",
    "\"+\"",
    "\"-\"",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\",\"",
    "\":\"",
    "\"/\"",
    "\"*\"",
    "\">\"",
    "<IDENT>",
    "<IMPORTANT_SYM>",
    "<LETTER>",
    "<PART_LETTER>",
    "<NUMBER>",
    "<EMS>",
    "<EXS>",
    "<PERCENTAGE>",
    "<LENGTH>",
    "<ANGLE>",
    "<TIME>",
    "<FREQ>",
    "<NUMBER_LITERAL>",
    "<URI>",
    "<STRING>",
    "<CHAR_LITERAL>",
    "<STRING_LITERAL>",
    "\".\"",
    "\"[\"",
    "\"=\"",
    "\"]\"",
  };

}