package factory.parsing.javascript;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 *
 * @author John Mwai
 */
public class JavascriptParser {

    int index = 0;
    private String source = "";
    private boolean MARKING_RANGE_MODE = true;
    final String NEW_LINE = "\n";
    final String CARRIAGE_RETURN = "\r";
    final String SPACE = " ";
    final String HORIZONTAL_TAB = "\t";
    final String FORM_FEED = "\f";
    final String[] reserverdWords = {"abstract", "boolean", "break", "byte", "case",
        "catch", "char", "class", "const", "continue", "default", "do", "double",
        "else", "extends", "false", "final", "finally", "float", "for",
        "function", "goto", "if", "implements", "import", "in", "instanceof",
        "int", "interface", "long", "native", "new", "null", "package",
        "private", "protected", "public", "return", "short", "static", "super",
        "switch", "synchronized", "this", "throw", "throws", "transient", "true",
        "try", "var", "void", "while", "with"};
    final String[] keyWords = {"break", "continue", "delete", "else", "false", "for",
        "function", "if", "in", "new", "null", "return", "this", "true", "var",
        "void", "while", "with"};
    final String[] nullLiteral = {"null"};
    final String[] booleanLiteral = {"false", "true"};
    final String[] graphicCharacter = {"~", "‘", "!", "@", "#", "%", "^", "&",
        "*", "(", ")", "-", "+", "=", "{", "[", "}", "]", "|", "\\", ":", "”",
        "’", "<", ",", ">", ".", "?", "/"};
    final String[] escapeSequence = {"\\b", "\\t", "\\n", "\\f", "\\r",
        "\\\"", "\\\\", "\\'"};
    final String[] seperators = {"{", "}", "[", "]", "(", ")", ",", "."};
    final String[] operator = {"=", ">", "<", "!", "~", "?", ":", "==", "<=",
        ">=", "!=", "&&", "||", "++", "--", "+", "-", "*", "/", "&", "|", "^",
        "%", "<<", ">>", ">>>", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=",
        "<<=", ">>=", ">>>="};
    final String[] assignmentOperator = {"=", "*=", "/=", "%=", "+=", "-=",
        "<<=", ">>=", ">>>=", "&=", "^=", "|="};
    //Patterns
    final Pattern letterPattern = Pattern.compile("[a-zA-Z_$]");
    final Pattern digitPattern = Pattern.compile("[\\d]");
    final Pattern letterOrDigitPattern = Pattern.compile("[a-zA-Z_$\\d]");
    final Pattern nonZeroDigitPattern = Pattern.compile("[1-9]");
    final Pattern hexDigitPattern = Pattern.compile("[\\da-fA-F]");
    final Pattern octalDigitPattern = Pattern.compile("[0-7]");
    final Pattern zeroToThreePattern = Pattern.compile("[0-3]");
    //end of patterns
    private HashMap<Range, JavascriptElements> elementTable = new HashMap<Range, JavascriptElements>();
    private ParserSlave slave = new ParserSlave(this);

    public void setSource(String source) {
        this.source = source;
    }

    public void parse() {
        reset();
        while (index < source.length() - 1) {
            inputElements();
            index++;
        }
    }

    private void reset() {
        index = 0;
        elementTable.clear();
    }

    public HashMap<Range, JavascriptElements> getElementTable() {
        return elementTable;
    }

    private boolean lineTerminator() {
        Marker m = new Marker();
        m.mark();
        if (slave.newLine()) {
            m.advance();
        }
        if (!m.pass() && slave.carriageReturn() && slave.newLine()) {
            m.advance();
        }
        m.reset();
        if (!m.pass() && slave.carriageReturn()) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.LineTerminator);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean inputElements() {
        while (true) {
            if (!inputElement()) {
                break;
            }
        }
        return true;
    }

    private void markRange(Marker m, JavascriptElements elms) {
        if (!MARKING_RANGE_MODE) {
            return;
        }
        Range r = new Range(m.initial(), index);
        elementTable.put(r, elms);
    }

    private boolean inputElement() {
        Marker m = new Marker();
        m.mark();
        if (whiteSpace() || comment() || token()) {
            markRange(m, JavascriptElements.InputElement);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean whiteSpace() {
        if (slave.space() || slave.horizontalTab() || slave.formFeed() || lineTerminator()) {
            return true;
        }
        return false;
    }

    private boolean comment() {
        if (traditionalComment() || singleLineComment()) {
            return true;
        }
        return false;
    }

    private boolean traditionalComment() {
        Marker m = new Marker();
        m.mark();
        if (ms("/*") && ms("*/")) {
            m.advance();
        }
        m.reset();
        if (!m.pass() && ms("/*") && commentText() && ms("*/")) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.TraditionalComment);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean commentText() {
        boolean characters = false;
        while (true) {
            if (!commmentCharacter()) {
                break;
            }
            characters = true;
        }
        return characters;
    }

    private boolean commmentCharacter() {
        Marker m = new Marker();
        m.mark();
        if (notStarSlash()) {
            return true;
        }
        if (ms("/") && notStar()) {
            return true;
        }
        m.reset();
        if (ms("*") && notSlash()) {
            return true;
        }
        m.reset();
        if (lineTerminator()) {
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean matchCharacterWithExclude(String[] exclude,
            boolean includeLineTerminator) {
        Marker m = new Marker();
        m.mark();
        if (!includeLineTerminator && !slave.inputCharacter()) {
            return false;
        } else if (includeLineTerminator && !slave.asciiCharacter()) {
        }
        for (String s : exclude) {
            if (stringsEqual(slave.cache, s)) {
                m.hardReset();
                return false;
            }
        }
        return true;
    }

    private boolean matchCharacterWithExclude(String[] exclude) {
        return matchCharacterWithExclude(exclude, false);
    }

    private boolean notStarSlash() {
        return matchCharacterWithExclude(new String[]{"/", "*"});
    }

    private boolean notStar() {
        return matchCharacterWithExclude(new String[]{"*"}, true);
    }

    private boolean notSlash() {
        return matchCharacterWithExclude(new String[]{"/"}, true);
    }

    private boolean singleLineComment() {
        Marker m = new Marker();
        m.mark();
        if (ms("//") && lineTerminator()) {
            m.advance();
        }
        m.reset();
        if (ms("//") && charactersInLine() && lineTerminator()) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.SingleLineComment);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean charactersInLine() {
        boolean characters = false;
        while (true) {
            if (!slave.inputCharacter()) {
                break;
            }
            characters = true;
        }
        return characters;
    }

    /**
     * Match string
     *
     * @param string String to match
     * @return If the String is found at the current index
     */
    boolean ms(String string) {
        int l = string.length();
        int m = source.length() - index;
        if (l > m) {
            return false;
        }
        if (stringsEqual(string, source.substring(index, index + l))) {
            index += l;
            return true;
        } else {
            return false;
        }
    }

    boolean testString(String string) {
        int l = string.length();
        int m = source.length() - index;
        if (l > m) {
            return false;
        }
        if (stringsEqual(string, source.substring(index, index + l))) {
            index += l;
            return true;
        } else {
            return false;
        }
    }

    private boolean token() {
        if (literal() || keyword() || identifier() || seperator() || operator()) {
            return true;
        }
        return false;
    }

    private boolean keyword() {
        if (usedKeyWord() || reserverdWord()) {
            return true;
        }
        return false;
    }

    private boolean nullLiteral() {
        return getWord(nullLiteral, JavascriptElements.NullLiteral, true);
    }

    private boolean booleanLiteral() {
        return getWord(booleanLiteral, JavascriptElements.BooleanLiteral, true);
    }

    private void enableMarkingRange() {
        MARKING_RANGE_MODE = true;
    }

    private void disableMarkingRange() {
        MARKING_RANGE_MODE = false;
    }

    private boolean identifier() {
        Marker m = new Marker();
        m.mark();
        disableMarkingRange();
        if (nullLiteral() || booleanLiteral() || reserverdWord()) {
            m.reset();
            enableMarkingRange();
            return false;
        }
        enableMarkingRange();
        if (slave.letter() && identifierOtherCharacters()) {
            m.advance();
        }
        m.reset();
        if (slave.letter()) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.Identifier);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean identifierOtherCharacters() {
        boolean characters = false;
        while (true) {
            if (!slave.letterOrDigit()) {
                break;
            }
            characters = true;
        }
        return characters;
    }

    private boolean literal() {
        Marker m = new Marker();
        m.mark();
        if (floatingPointLiteral(false) || integerLiteral() || stringLiteral()
                || nullLiteral() || booleanLiteral()) {
            markRange(m, JavascriptElements.Literal);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean integerLiteral() {
        if (decimalLiteral() || hexLiteral() || octalLiteral()) {
            return true;
        }
        return false;
    }

    private boolean decimalLiteral() {
        Marker m = new Marker();
        m.mark();
        if (ms("0")) {
            m.advance();
        }
        if (!m.pass() && slave.nonZeroDigit() && digits(false)) {
            m.advance();
        }
        m.reset();
        if (!m.pass() && slave.nonZeroDigit()) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.DecimalLiteral);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean digits(boolean optional) {
        boolean digits = false;
        while (true) {
            if (!slave.digit()) {
                break;
            }
            digits = true;
        }
        return digits || optional;
    }

    private boolean hexLiteral() {
        //TODO add code to verify that this literal is -2147483648 < x < 2147483647
        Marker m = new Marker();
        m.mark();
        if ((ms("0x") || ms("0X")) && slave.hexDigit()) {
            m.advance();
            while (true) {
                if (!slave.hexDigit()) {
                    break;
                }
            }
            markRange(m, JavascriptElements.HexLiteral);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean octalLiteral() {
        //TODO add code to verify that this literal is -2147483648 < x < 2147483647
        Marker m = new Marker();
        m.mark();
        if (ms("0") && slave.octalDigit(false)) {
            m.advance();
            while (true) {
                if (!slave.octalDigit(false)) {
                    break;
                }
            }
            markRange(m, JavascriptElements.OctalLiteral);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean stringLiteral() {
        Marker m = new Marker();
        m.mark();
        if (ms("\"") && stringCharactersDQ(true) && ms("\"")) {
            m.advance();
        }
        m.reset();
        if (ms("'") && stringCharactersSQ(true) && ms("'")) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.StringLiteral);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean stringCharactersDQ(boolean optional) {
        boolean characters = false;
        while (true) {
            if (!stringCharacterDQ()) {
                break;
            }
            characters = true;
        }
        return characters || optional;
    }

    private boolean stringCharactersSQ(boolean optional) {
        boolean characters = false;
        while (true) {
            if (!stringCharacterSQ()) {
                break;
            }
            characters = true;
        }
        return characters || optional;
    }

    private boolean stringCharacterDQ() {
        return matchCharacterWithExclude(new String[]{"\"", "\\"}) || escapeSequence();
    }

    private boolean stringCharacterSQ() {
        return matchCharacterWithExclude(new String[]{"'", "\\"}) || escapeSequence();
    }

    private boolean escapeSequence() {
        return getWord(escapeSequence, JavascriptElements.EscapeSequence);
    }

    private boolean octalEscape() {
        Marker m = new Marker();
        m.mark();
        if (ms("\\") && slave.octalDigit(false) && slave.octalDigit(
                true)) {
            m.advance();
        }
        m.reset();
        if (!m.pass() && ms("\\") && slave.zeroToThree()
                && slave.octalDigit(false) && slave.octalDigit(false)) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.OctalEscape);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean hexEscape() {
        Marker m = new Marker();
        m.mark();
        if ((ms("\\x") || ms("\\X")) && slave.hexDigit() && slave.hexDigit()) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.OctalEscape);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean floatingPointLiteral(boolean optional) {
        //TODO add code to verify that 4.94065645841246544e-324 < x < 1.79769313486231570e+308
        Marker m = new Marker();
        m.mark();
        if (digits(false) && ms(".") && digits(true) && exponentPart(
                true)) {
            m.advance();
        }
        m.reset();
        if (!m.pass() && ms(".") && digits(false) && exponentPart(true)) {
            m.advance();
        }
        m.reset();
        if (!m.pass() && digits(false) && exponentPart(false)) {
            m.advance();
        }
        if (m.pass()) {
            markRange(m, JavascriptElements.FloatingPointLiteral);
            return true;
        }
        m.hardReset();
        return false || optional;
    }

    boolean exponentPart(boolean optional) {
        Marker m = new Marker();
        m.mark();
        if (slave.exponentIndicator() && signedInteger()) {
            return true;
        }
        m.hardReset();
        return false || optional;
    }

    boolean signedInteger() {
        Marker m = new Marker();
        m.mark();
        if (slave.sign() && digits(false)) {
            return true;
        }
        if (digits(false)) {
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean seperator() {
        return getWord(seperators, JavascriptElements.Seperator);
    }

    private boolean operator() {
        return getWord(operator, JavascriptElements.Operator);
    }

    private boolean getWord(String[] list, JavascriptElements elems,
            boolean senseWordBoundary) {
        Marker m = new Marker();
        m.mark();
        for (String s : list) {
            if (ms(s)) {
                if (senseWordBoundary && identifierOtherCharacters()) {
                    break;
                }
                m.advance();
                break;
            }
        }
        if (m.pass()) {
            markRange(m, elems);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean getWord(String[] list, JavascriptElements elems) {
        return getWord(list, elems, false);
    }

    private boolean usedKeyWord() {
        return getWord(keyWords, JavascriptElements.Keyword, true);
    }

    private boolean reserverdWord() {
        return getWord(reserverdWords, JavascriptElements.ReserverdWord, true);
    }

    boolean stringsEqual(String s1, String s2) {
        return (s1 == null ? s2 == null : s1.equals(s2));
    }

    String nextChar() {
        if (index >= source.length()) {
            return null;
        }
        return source.substring(index, index + 1);
    }

    public static class Range {

        int begin;
        int end;

        public Range(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }
    }

    private class Marker {

        int ini = 0;
        boolean succ = false;

        private void mark() {
            ini = index;
        }

        private void reset() {
            if (pass()) {
                return;
            }
            index = ini;
        }

        private void hardReset() {
            index = ini;
        }

        private void advance() {
            succ = true;
        }

        private boolean pass() {
            return succ;
        }

        private int initial() {
            return ini;
        }
    }

    private boolean primaryExpression() {
        Marker m = new Marker();
        m.mark();
        if (literal() || ms("this") || functionInvocation() || arrayAccess()) {
            markRange(m, JavascriptElements.PrimaryExpression);
            return true;
        }
        m.hardReset();
        return false;
    }

    private boolean functionInvocation() {
        Evltr e = new Evltr();
        while (e.opt(functionName(false) && ms("(") && argumentList(
                true) && ms(")"))
                || e.opt(objectName(false) && ms(".") && functionName(
                false) && ms("(") && argumentList(
                true) && ms(")"))) {
            break;
        }
        return e.commit();
    }

    private boolean postfixExpression() {
        Evltr e = new Evltr(false);
        e.opt(primaryExpression() || postIncrementExpression() || postDecrementExpression());
        return e.commit();
    }

    private boolean postIncrementExpression() {
        return postfixExpression() && ms("++");
    }

    private boolean postDecrementExpression() {
        return postfixExpression() && ms("--");
    }

    private boolean unaryExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(preIncrementExpression())
                || e.opt(preDecrementExpression())
                || e.opt(ms("-") && unaryExpression())
                || e.opt(unaryExpressionNotPlusMinus()));
    }

    private boolean preIncrementExpression() {
        Evltr e = new Evltr(false);
        e.opt(ms("++") && unaryExpression());
        return e.commit();
    }

    private boolean preDecrementExpression() {
        Evltr e = new Evltr(false);
        e.opt(ms("--") && unaryExpression());
        return e.commit();
    }

    private boolean unaryExpressionNotPlusMinus() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(postfixExpression())
                || e.opt(preDecrementExpression())
                || e.opt(ms("~") && unaryExpression())
                || e.opt(ms("!") && unaryExpression()));
    }

    private boolean multiplicativeExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(unaryExpression())
                || e.opt(
                multiplicativeExpression() && ms("*")
                && unaryExpression())
                || e.opt(
                multiplicativeExpression() && ms("/")
                && unaryExpression())
                || e.opt(
                multiplicativeExpression() && ms("%")
                && unaryExpression()));
    }

    private boolean additiveExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(multiplicativeExpression())
                || e.opt(
                additiveExpression() && ms("+")
                && multiplicativeExpression())
                || e.opt(
                additiveExpression() && ms("-")
                && multiplicativeExpression()));
    }

    private boolean shiftExpression() {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(additiveExpression())
                || e.opt(
                shiftExpression() && ms("<<") && additiveExpression())
                || e.opt(
                shiftExpression() && ms(">>") && additiveExpression())
                || e.opt(
                shiftExpression() && ms(">>>") && additiveExpression()));
    }

    private boolean relationalExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(shiftExpression())
                || e.opt(
                relationalExpression() && ms("<") && shiftExpression())
                || e.opt(
                relationalExpression() && ms(">") && shiftExpression())
                || e.opt(
                relationalExpression() && ms("<=") && shiftExpression())
                || e.opt(
                relationalExpression() && ms(">=") && shiftExpression()));
    }

    private boolean equalityExpression() {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(relationalExpression())
                || e.opt(
                equalityExpression() && ms("==") && relationalExpression())
                || e.opt(
                equalityExpression() && ms("=!") && relationalExpression()));
    }

    private boolean andExpression() {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(equalityExpression())
                || e.opt(andExpression() && ms("&") && equalityExpression()));
    }

    private boolean exclusiveOrExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(equalityExpression())
                || e.opt(exclusiveOrExpression() && ms("^") && andExpression()));
    }

    private boolean inclusiveOrExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(equalityExpression())
                || e.opt(
                inclusiveOrExpression() && ms("^") && exclusiveOrExpression()));
    }

    private boolean conditionalAndExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(inclusiveOrExpression())
                || e.opt(
                conditionalAndExpression() && ms("&&") && inclusiveOrExpression()));
    }

    private boolean conditionalOrExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(conditionalAndExpression())
                || e.opt(
                conditionalOrExpression() && ms("||") && conditionalAndExpression()));
    }

    private boolean conditionalExpression() {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(conditionalOrExpression())
                || e.opt(conditionalOrExpression() && ms("?") && expression() && ms(
                ":") && conditionalExpression()));
    }

    private boolean assignmentExpression() {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(conditionalExpression())
                || e.opt(assignment()));
    }

    private boolean assignment() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(
                leftHandSide() && assignmentOperator() && assignmentExpression()));
    }

    private boolean leftHandSide() {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(expressionName())
                || e.opt(identifierName())
                || e.opt(arrayAccess()));
    }

    private boolean assignmentOperator() {
        return getWord(assignmentOperator, JavascriptElements.AssignmentOperator,
                false);
    }

    private boolean newExpression() {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(ms("new") && constructor()));
    }

    private boolean constructor() {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(constructorFunction())
                || e.opt(ms("Array"))
                || e.opt(ms("Date"))
                || e.opt(ms("Math"))
                || e.opt(ms("String") && ms("(") && stringValue() && ms(
                ")"))
                || e.opt(ms("Function") && ms("(") && parameterList() && ms(
                ",") && ms("\"") && block() && ms("\"") && ms(")"))
                || e.opt(ms("Boolean") && ms("(") && booleanValue() && ms(
                ")")));
    }

    private boolean constructorFunction() {
        return false;
    }

    private boolean stringValue() {
        return false;
    }

    private boolean parameterList() {
        return false;
    }

    private boolean block() {
        return false;
    }

    private boolean booleanValue() {
        return false;
    }

    private boolean deleteExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(ms("delete") && ms("(") && identifierName() && ms(
                ")"))
                || e.opt(ms("delete") && ms("(") && identifierName()));
    }

    private boolean identifierName() {
        return false;
    }

    private boolean expressionName() {
        return false;
    }

    private boolean typeOfExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(ms("typeof") && expression()));
    }

    private boolean voidExpression() {
        Evltr e = new Evltr(false);
        return e.commit(
                e.opt(ms("void") && expression()));
    }

    private class Evltr {

        private JavascriptElements elem = null;
        private final Marker marker;
        private boolean markRange = true;

        public Evltr() {
            marker = new Marker();
            marker.mark();
        }

        /**
         * Calling this constructor with a false argument will turn off range
         * marking mode
         *
         * @param markRange whether this evaluator marks ranges of the elements
         * it encounters.
         */
        public Evltr(boolean markRange) {
            this();
            MARKING_RANGE_MODE = markRange;
        }

        public Evltr(JavascriptElements elem) {
            this();
            this.elem = elem;
        }

        /**
         * If you supply this constructor with JavaScriptelements argument which
         * is non null it will mark the range with the type whether or not the
         * second argument is true or false.
         *
         * @param elem the type of this range.
         * @param markRangesIntermediate whether the range marking mode is on or
         * off.
         */
        public Evltr(JavascriptElements elem, boolean markRangesIntermediate) {
            this(elem);
            this.markRange = markRangesIntermediate;
        }

        private boolean opt(boolean option) {
            if (!marker.pass() && option) {
                marker.advance();
            } else {
                marker.reset();
            }
            return marker.pass();
        }

        private boolean commit() {
            return commit(marker.pass());
        }

        private boolean commit(boolean bool) {
            boolean b = false;
            MARKING_RANGE_MODE = true;
            if (bool) {
                if (elem != null && markRange) {
                    markRange(marker, elem);
                }
                b = true;
            }
            marker.hardReset();
            return b;
        }
    }

    private boolean objectName(boolean optional) {
        return false || optional;
    }

    private boolean functionName(boolean optional) {
        return false || optional;
    }

    private boolean argumentList(boolean optional) {
        Evltr e = new Evltr(false);
        return e.commit(e.opt(expression())
                || e.opt(argumentList(false) && ms(",") && expression())) || optional;
    }

    private boolean expression() {
        return false;
    }

    private boolean arrayAccess() {
        return false;
    }
}
