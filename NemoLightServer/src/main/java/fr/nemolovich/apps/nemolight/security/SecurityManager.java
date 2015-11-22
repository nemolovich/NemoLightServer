package fr.nemolovich.apps.nemolight.security;

public interface SecurityManager {

    SecurityStatus submitAuthentication(String name, String encryptedPassword);

}
