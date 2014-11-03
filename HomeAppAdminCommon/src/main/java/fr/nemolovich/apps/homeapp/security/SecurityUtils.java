/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

/**
 *
 * @author Nemolovich
 */
public class SecurityUtils {

    private static final Logger LOGGER = Logger.getLogger(SecurityUtils.class);

    public static final String getEncryptedPassword(String password) {
        String sha1 = null;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = new BigInteger(1, crypt.digest()).toString(16);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOGGER.error("Can not encrypt password", ex);
        }
        return sha1;
    }
}
