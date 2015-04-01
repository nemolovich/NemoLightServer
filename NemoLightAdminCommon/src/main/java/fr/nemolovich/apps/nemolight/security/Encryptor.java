package fr.nemolovich.apps.nemolight.security;

import fr.nemolovich.apps.nemolight.security.exceptions.EncryptingException;

public interface Encryptor {
	public String getEncryptedPassword(String password) throws EncryptingException;
}