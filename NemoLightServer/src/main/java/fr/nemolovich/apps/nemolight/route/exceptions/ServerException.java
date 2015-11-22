package fr.nemolovich.apps.nemolight.route.exceptions;

/**
 *
 * @author Nemolovich
 */
public class ServerException extends Exception {

    /**
     * UID.
     */
    private static final long serialVersionUID = -7078123065723605500L;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

}
