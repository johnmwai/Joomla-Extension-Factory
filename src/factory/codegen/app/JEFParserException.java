package factory.codegen.app;

/**
 *
 * @author John Mwai
 */
public class JEFParserException extends Exception {

    /**
     * Creates a new instance of
     * <code>JEFParserException</code> without detail message.
     */
    public JEFParserException() {
    }

    /**
     * Constructs an instance of
     * <code>JEFParserException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public JEFParserException(String msg) {
        super(msg);
    }

    public JEFParserException(Throwable cause) {
        super(cause);
    }
    
    
}
