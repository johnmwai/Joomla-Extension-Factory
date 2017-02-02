package factory.design;

/**
 * An exception that encapsulates information about exceptional events occurring
 * in dealing with a {@link ControlWeb control web}.
 *
 * @author John Mwai
 */
public class FuscardControlWebException extends Exception {

    /**
     * Creates a new instance of
     * <code>FuscardControlWebException</code> without detail message.
     */
    public FuscardControlWebException() {
    }

    /**
     * Constructs an instance of
     * <code>FuscardControlWebException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FuscardControlWebException(String msg) {
        super(msg);
    }
}
