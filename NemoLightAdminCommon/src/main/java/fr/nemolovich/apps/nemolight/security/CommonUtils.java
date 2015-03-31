/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.security;

import org.apache.log4j.Logger;

import fr.nemolovich.apps.nemolight.security.exceptions.EncryptingException;

/**
 *
 * @author Nemolovich
 */
public class CommonUtils {

	private static final Logger LOGGER = Logger.getLogger(CommonUtils.class);

	private static Encryptor ENCRYPTOR = DefaultEncryptor.getInstance();

	public static final String getEncryptedPassword(String password) {
		String result = null;
		try {
			result = ENCRYPTOR.getEncryptedPassword(password);
		} catch (EncryptingException e) {
			LOGGER.error("Encrypting failed: ", e);
		}
		return result;
	}
}
