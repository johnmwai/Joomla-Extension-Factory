package factory.parsing.javascript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author John Mwai
 */
public class ParserSlave {

    private final JavascriptParser master;
    String cache = "";

    public ParserSlave(final JavascriptParser master) {
        this.master = master;
    }

    private boolean matchCharacter(String character) {
        if (!asciiCharacter()) {
            return false;
        }
        if (master.stringsEqual(cache, character)) {
            master.index++;
            return true;
        }
        return false;
    }

    private boolean matchCharacterToPattern(Pattern p) {
        if (!asciiCharacter()) {
            return false;
        }
        Matcher m = p.matcher(cache);
        if (m.matches()) {
            master.index++;
            return true;
        }
        return false;
    }

    boolean letter() {
        return matchCharacterToPattern(master.letterPattern);
    }

    boolean digit() {
        return matchCharacterToPattern(master.digitPattern);
    }
    
    boolean zeroToThree() {
        return matchCharacterToPattern(master.zeroToThreePattern);
    }
    
    boolean sign(){
        return master.ms("-") || master.ms("+");
    }
    
    boolean exponentIndicator(){
        return master.ms("e") || master.ms("E");
    }

    boolean nonZeroDigit() {
        return matchCharacterToPattern(master.nonZeroDigitPattern);
    }

    boolean letterOrDigit() {
        return matchCharacterToPattern(master.letterOrDigitPattern);
    }

    boolean hexDigit() {
        return matchCharacterToPattern(master.hexDigitPattern);
    }

    boolean octalDigit(boolean optional) {
        return matchCharacterToPattern(master.octalDigitPattern) || optional;
    }

    boolean newLine() {
        return matchCharacter(master.NEW_LINE);
    }

    boolean carriageReturn() {
        return matchCharacter(master.CARRIAGE_RETURN);
    }

    boolean space() {
        return matchCharacter(master.SPACE);
    }

    boolean horizontalTab() {
        return matchCharacter(master.HORIZONTAL_TAB);
    }

    boolean formFeed() {
        return matchCharacter(master.FORM_FEED);
    }

    boolean inputCharacter() {
        if (!asciiCharacter()) {
            return false;
        }
        if (master.stringsEqual(cache, master.CARRIAGE_RETURN) || master.stringsEqual(
                cache, master.NEW_LINE)) {
            return false;
        }
        master.index++;
        return true;
    }

    boolean asciiCharacter() {
        String in = master.nextChar();
        if (in == null) {
            return false;
        }
        char c = in.charAt(0);
        if (isAscii(c)) {
            cache = in;
            return true;
        }
        return false;
    }

    boolean isAscii(char c) {
        return c < 128;
    }
    
    
}
