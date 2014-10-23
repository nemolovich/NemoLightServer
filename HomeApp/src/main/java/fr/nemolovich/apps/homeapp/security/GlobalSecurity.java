package fr.nemolovich.apps.homeapp.security;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;

public class GlobalSecurity {

    private static final Logger LOGGER = Logger.getLogger(GlobalSecurity.class
        .getName());
    private static boolean SECURED = false;

    private static final JAXBContext CONTEXT;

    static {

        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(Group.class, User.class,
                SecurityConfiguration.class);
        } catch (JAXBException e) {
            LOGGER.error("Can not use JAXB context for security", e);
            System.exit(1);
        }
        CONTEXT = context;
    }

    public static final void enableSecurity() {
        SECURED = true;
    }

    public static final void disableSecurity() {
        SECURED = false;
    }

    public static final void saveConfig() throws JAXBException {
        Marshaller m = CONTEXT.createMarshaller();
        if (!HomeAppConstants.SECURITY_CONFIG_FILE.getParentFile().exists()) {
            if (!HomeAppConstants.SECURITY_CONFIG_FILE.getParentFile().mkdirs()) {
                LOGGER.error("Can not create folder to save security",
                    new IOException("Can not create fodler"));
                return;
            }
        }
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(SecurityConfiguration.getInstance(),
            HomeAppConstants.SECURITY_CONFIG_FILE);
    }

    public static final SecurityConfiguration loadConfig() throws JAXBException {
        if (!HomeAppConstants.SECURITY_CONFIG_FILE.exists()) {
            LOGGER.error("Could not load security file",
                new FileNotFoundException("Security file not found"));
            return null;
        }
        Unmarshaller um = CONTEXT.createUnmarshaller();
        return (SecurityConfiguration) um
            .unmarshal(HomeAppConstants.SECURITY_CONFIG_FILE);
    }

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

    public static List<User> getUsers() {
        return SecurityConfiguration.getInstance().getUsers();
    }

    public static List<Group> getGroups() {
        return (List<Group>) SecurityConfiguration.getInstance().getGroups();
    }

}
