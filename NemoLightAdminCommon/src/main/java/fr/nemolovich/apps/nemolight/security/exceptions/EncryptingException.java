package fr.nemolovich.apps.nemolight.security.exceptions;

public class EncryptingException extends Exception {

    /**
     * UID.
     */
    private static final long serialVersionUID = -8986759407075856062L;

    public EncryptingException(String message, Exception ex) {
        super(message, ex);
    }

}
