package fr.nemolovich.apps.nemolight.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.nemolovich.apps.nemolight.security.exceptions.EncryptingException;

public final class DefaultEncryptor implements Encryptor {

    private static final Encryptor INSTANCE;
    private static final int BYTES_SIZE = 16;

    static {
        INSTANCE = new DefaultEncryptor();
    }

    public static Encryptor getInstance() {
        return INSTANCE;
    }

    private DefaultEncryptor() {
    }

    @Override
    public String getEncryptedPassword(String password)
        throws EncryptingException {
        String sha1 = null;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = new BigInteger(1, crypt.digest()).toString(BYTES_SIZE);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new EncryptingException("Can not encrypt password", ex);
        }
        return sha1;
    }

}
