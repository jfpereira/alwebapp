package pt.aliancas.webapp.exceptions;

/**
 * Generic Exception
 */
public class GeneralException extends RuntimeException {

    /**
     * @see RuntimeException#RuntimeException(String)
     * @param message
     */
    public GeneralException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     * @param cause
     */
    public GeneralException(Throwable cause) {
        super(cause);
    }
}
