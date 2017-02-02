package factory.codegen;

/**
 *
 * @author John Mwai
 */
public class CodeGeneratorException extends Exception {

    /**
     * Creates a new instance of
     * <code>CodeGeneratorException</code> without detail message.
     */
    public CodeGeneratorException() {
    }

    /**
     * Constructs an instance of
     * <code>CodeGeneratorException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CodeGeneratorException(String msg) {
        super(msg);
    }

    public CodeGeneratorException(Throwable cause) {
        super(cause);
    }

    public CodeGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
