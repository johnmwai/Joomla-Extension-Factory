/* Generated By:JJTree&JavaCC: Do not edit this line. html32Constants.java */
package html;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface html32Constants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int STAGO = 3;
  /** RegularExpression Id. */
  int ETAGO = 4;
  /** RegularExpression Id. */
  int PCDATA = 5;
  /** RegularExpression Id. */
  int A = 6;
  /** RegularExpression Id. */
  int ADDRESS = 7;
  /** RegularExpression Id. */
  int APPLET = 8;
  /** RegularExpression Id. */
  int AREA = 9;
  /** RegularExpression Id. */
  int B = 10;
  /** RegularExpression Id. */
  int BASE = 11;
  /** RegularExpression Id. */
  int BASEFONT = 12;
  /** RegularExpression Id. */
  int BIG = 13;
  /** RegularExpression Id. */
  int BLOCKQUOTE = 14;
  /** RegularExpression Id. */
  int BODY = 15;
  /** RegularExpression Id. */
  int BR = 16;
  /** RegularExpression Id. */
  int CAPTION = 17;
  /** RegularExpression Id. */
  int CENTER = 18;
  /** RegularExpression Id. */
  int CITE = 19;
  /** RegularExpression Id. */
  int CODE = 20;
  /** RegularExpression Id. */
  int DD = 21;
  /** RegularExpression Id. */
  int DFN = 22;
  /** RegularExpression Id. */
  int DIR = 23;
  /** RegularExpression Id. */
  int DIV = 24;
  /** RegularExpression Id. */
  int DL = 25;
  /** RegularExpression Id. */
  int DT = 26;
  /** RegularExpression Id. */
  int EM = 27;
  /** RegularExpression Id. */
  int FONT = 28;
  /** RegularExpression Id. */
  int FORM = 29;
  /** RegularExpression Id. */
  int H1 = 30;
  /** RegularExpression Id. */
  int H2 = 31;
  /** RegularExpression Id. */
  int H3 = 32;
  /** RegularExpression Id. */
  int H4 = 33;
  /** RegularExpression Id. */
  int H5 = 34;
  /** RegularExpression Id. */
  int H6 = 35;
  /** RegularExpression Id. */
  int HEAD = 36;
  /** RegularExpression Id. */
  int HR = 37;
  /** RegularExpression Id. */
  int HTML = 38;
  /** RegularExpression Id. */
  int I = 39;
  /** RegularExpression Id. */
  int IMG = 40;
  /** RegularExpression Id. */
  int INPUT = 41;
  /** RegularExpression Id. */
  int ISINDEX = 42;
  /** RegularExpression Id. */
  int KBD = 43;
  /** RegularExpression Id. */
  int LI = 44;
  /** RegularExpression Id. */
  int LINK = 45;
  /** RegularExpression Id. */
  int MAP = 46;
  /** RegularExpression Id. */
  int MENU = 47;
  /** RegularExpression Id. */
  int META = 48;
  /** RegularExpression Id. */
  int OL = 49;
  /** RegularExpression Id. */
  int OPTION = 50;
  /** RegularExpression Id. */
  int P = 51;
  /** RegularExpression Id. */
  int PARAM = 52;
  /** RegularExpression Id. */
  int PRE = 53;
  /** RegularExpression Id. */
  int PROMPT = 54;
  /** RegularExpression Id. */
  int SAMP = 55;
  /** RegularExpression Id. */
  int SCRIPT = 56;
  /** RegularExpression Id. */
  int SELECT = 57;
  /** RegularExpression Id. */
  int SMALL = 58;
  /** RegularExpression Id. */
  int STRIKE = 59;
  /** RegularExpression Id. */
  int STRONG = 60;
  /** RegularExpression Id. */
  int STYLE = 61;
  /** RegularExpression Id. */
  int SUB = 62;
  /** RegularExpression Id. */
  int SUP = 63;
  /** RegularExpression Id. */
  int TABLE = 64;
  /** RegularExpression Id. */
  int TD = 65;
  /** RegularExpression Id. */
  int TEXTAREA = 66;
  /** RegularExpression Id. */
  int TH = 67;
  /** RegularExpression Id. */
  int TITLE = 68;
  /** RegularExpression Id. */
  int TR = 69;
  /** RegularExpression Id. */
  int TT = 70;
  /** RegularExpression Id. */
  int U = 71;
  /** RegularExpression Id. */
  int UL = 72;
  /** RegularExpression Id. */
  int VAR = 73;
  /** RegularExpression Id. */
  int TAGC = 76;
  /** RegularExpression Id. */
  int A_EQ = 77;
  /** RegularExpression Id. */
  int ALPHA = 78;
  /** RegularExpression Id. */
  int NUM = 79;
  /** RegularExpression Id. */
  int ALPHANUM = 80;
  /** RegularExpression Id. */
  int A_NAME = 81;
  /** RegularExpression Id. */
  int CDATA = 82;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int TAG = 1;
  /** Lexical state. */
  int ATTLIST = 2;
  /** Lexical state. */
  int ATTRVAL = 3;
  /** Lexical state. */
  int ATTCOMM = 4;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "<token of kind 1>",
    "<token of kind 2>",
    "\"<\"",
    "\"</\"",
    "<PCDATA>",
    "\"a\"",
    "\"address\"",
    "\"applet\"",
    "\"area\"",
    "\"b\"",
    "\"base\"",
    "\"basefont\"",
    "\"big\"",
    "\"blockquote\"",
    "\"body\"",
    "\"br\"",
    "\"caption\"",
    "\"center\"",
    "\"cite\"",
    "\"code\"",
    "\"dd\"",
    "\"dfn\"",
    "\"dir\"",
    "\"div\"",
    "\"dl\"",
    "\"dt\"",
    "\"em\"",
    "\"font\"",
    "\"form\"",
    "\"h1\"",
    "\"h2\"",
    "\"h3\"",
    "\"h4\"",
    "\"h5\"",
    "\"h6\"",
    "\"head\"",
    "\"hr\"",
    "\"html\"",
    "\"i\"",
    "\"img\"",
    "\"input\"",
    "\"isindex\"",
    "\"kbd\"",
    "\"li\"",
    "\"link\"",
    "\"map\"",
    "\"menu\"",
    "\"meta\"",
    "\"ol\"",
    "\"option\"",
    "\"p\"",
    "\"param\"",
    "\"pre\"",
    "\"prompt\"",
    "\"samp\"",
    "\"script\"",
    "\"select\"",
    "\"small\"",
    "\"strike\"",
    "\"strong\"",
    "\"style\"",
    "\"sub\"",
    "\"sup\"",
    "\"table\"",
    "\"td\"",
    "\"textarea\"",
    "\"th\"",
    "\"title\"",
    "\"tr\"",
    "\"tt\"",
    "\"u\"",
    "\"ul\"",
    "\"var\"",
    "<token of kind 74>",
    "\"--\"",
    "\">\"",
    "\"=\"",
    "<ALPHA>",
    "<NUM>",
    "<ALPHANUM>",
    "<A_NAME>",
    "<CDATA>",
    "<token of kind 83>",
    "<token of kind 84>",
    "\"--\"",
  };

}
