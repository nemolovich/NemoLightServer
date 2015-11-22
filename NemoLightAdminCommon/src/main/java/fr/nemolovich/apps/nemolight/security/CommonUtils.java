package fr.nemolovich.apps.nemolight.security;

import org.apache.log4j.Logger;

import fr.nemolovich.apps.nemolight.security.exceptions.EncryptingException;

/**
 *
 * @author Nemolovich
 */
public final class CommonUtils {

    private static final Logger LOGGER = Logger.getLogger(CommonUtils.class);

    private static Encryptor encryptor = DefaultEncryptor.getInstance();

    private CommonUtils() {
    }

    public static void setEncryptor(Encryptor encryptor) {
        CommonUtils.encryptor = encryptor;
    }

    public static final String getEncryptedPassword(String password) {
        String result = null;
        try {
            result = encryptor.getEncryptedPassword(password);
        } catch (EncryptingException e) {
            LOGGER.error("Encrypting failed: ", e);
        }
        return result;
    }
}
