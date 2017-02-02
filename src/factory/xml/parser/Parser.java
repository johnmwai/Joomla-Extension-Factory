package factory.xml.parser;

import java.util.LinkedList;

/**
 *
 * @author John Mwai
 */
public class Parser {

    private String source;
    private LinkedList<Range> ranges = new LinkedList<>();

    public Parser(String source) {
        this.source = source;
    }

    public boolean document() {
        return false;
    }

    public LinkedList<Range> getRanges() {
        return ranges;
    }
}
