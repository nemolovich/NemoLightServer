package fr.nemolovich.apps.homeapp.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;

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
			LOGGER.log(Level.SEVERE, "Can not use JAXB context for security", e);
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

	// public static boolean isAllowed(User user) {
	//
	// }

	public static final void saveConfig() throws JAXBException {
		Marshaller m = CONTEXT.createMarshaller();
		if (!HomeAppConstants.SECURITY_CONFIG_FILE.getParentFile().exists()) {
			if (!HomeAppConstants.SECURITY_CONFIG_FILE.getParentFile().mkdirs()) {
				LOGGER.log(Level.SEVERE,
						"Can not create folder to save security",
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
			LOGGER.log(Level.SEVERE, "Could not load security file",
					new FileNotFoundException("Security file not found"));
			return null;
		}
		Unmarshaller um = CONTEXT.createUnmarshaller();
		return (SecurityConfiguration) um
				.unmarshal(HomeAppConstants.SECURITY_CONFIG_FILE);
	}

}
