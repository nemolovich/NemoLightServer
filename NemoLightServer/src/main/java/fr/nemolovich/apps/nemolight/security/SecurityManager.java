package fr.nemolovich.apps.nemolight.security;

public interface SecurityManager {

	public SecurityStatus submitAuthentication(String name, String encryptedPassword);
	
}
