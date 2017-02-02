package factory.codegen.app;

/**
 *
 * @author John Mwai
 */
public class FuscardProjectException extends Exception {

    /**
     * Creates a new instance of
     * <code>FuscardProjectException</code> without detail message.
     */
    public FuscardProjectException() {
    }

    /**
     * Constructs an instance of
     * <code>FuscardProjectException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FuscardProjectException(String msg) {
        super(msg);
    }

    public FuscardProjectException(Throwable cause) {
        super(cause);
    }

    public FuscardProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
