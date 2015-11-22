package fr.nemolovich.apps.nemolight.security;

public final class SecurityUtils {

    private static boolean IS_SECURED = false;

    private SecurityUtils() {
    }

    public static final void enableSecurity() {
        IS_SECURED = true;
    }

    public static final void disableSecurity() {
        IS_SECURED = false;
    }

    public static final boolean isSecured() {
        return IS_SECURED;
    }

}
