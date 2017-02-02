package factory.xml.parser;

/**
 *
 * @author John Mwai
 */
public class Range {

    final int start, end;
    final Type type;

    private Range(Type type, int start, int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }
}
