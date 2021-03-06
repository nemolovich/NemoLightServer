package fr.nemolovich.apps.nemolight.security;

import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;

public class GlobalSecurity {

	private static final Logger LOGGER = Logger.getLogger(GlobalSecurity.class
		.getName());
	private static boolean SECURED;

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
		SECURED = true;
	}

	public static final void enableSecurity() {
		SECURED = true;
	}

	public static final void disableSecurity() {
		SECURED = false;
	}

	public static boolean isEnabled() {
		return SECURED;
	}

	public static final void saveConfig() throws JAXBException {
		Marshaller m = CONTEXT.createMarshaller();
		if (!NemoLightConstants.SECURITY_CONFIG_FILE.getParentFile().exists()) {
			if (!NemoLightConstants.SECURITY_CONFIG_FILE.getParentFile().mkdirs()) {
				LOGGER.error("Can not create folder to save security",
					new IOException("Can not create fodler"));
				return;
			}
		}
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(SecurityConfiguration.getInstance(),
			NemoLightConstants.SECURITY_CONFIG_FILE);

		try {
			savePasswords();
		} catch (IOException e) {
			LOGGER.error("Can not save passwords", e);
		}
	}

	public static final SecurityConfiguration loadConfig() throws JAXBException {
		SecurityConfiguration result = null;
		if (!NemoLightConstants.SECURITY_CONFIG_FILE.exists()) {
			LOGGER.error("Could not load security file",
				new FileNotFoundException("Security file not found"));
		} else {
			Unmarshaller um = CONTEXT.createUnmarshaller();
			result = (SecurityConfiguration) um
				.unmarshal(NemoLightConstants.SECURITY_CONFIG_FILE);
		}
		return result;
	}

	public static final void savePasswords() throws FileNotFoundException,
		IOException {
		Map<String, String> passwords = new HashMap<>();
		for (Group group : SecurityConfiguration.getInstance().getGroups()) {
			for (User user : group.getUsers()) {
				passwords.put(user.getName(), user.getPassword());
			}
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(
			new BufferedOutputStream(new FileOutputStream(
					NemoLightConstants.SECURITY_PASSWORDS_FILE)))) {
					oos.writeObject(passwords);
				}
	}

	public static final void loadPasswords() throws IOException,
		ClassNotFoundException {
		Map<String, String> passwords;
		try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
			new FileInputStream(NemoLightConstants.SECURITY_PASSWORDS_FILE)))) {
			passwords = (Map<String, String>) ois.readObject();
		}
		for (Group group : SecurityConfiguration.getInstance().getGroups()) {
			for (User user : group.getUsers()) {
				if (passwords.containsKey(user.getName())) {
					user.setPassword(passwords.get(user.getName()));
				}
			}
		}
	}

	public static SecurityStatus submitAuthentication(
		String userName, String encryptedPasswod) {
		SecurityStatus result = SecurityStatus.UNKNOWN_USER;
		if (userName != null) {
			for (User u : GlobalSecurity.getUsers()) {
				if (u.getName().equals(userName)) {
					if (u.getPassword() == null) {
						result = SecurityStatus.INVALID_PASSWORD;
					} else {
						if (u.getPassword().equals(
							encryptedPasswod)) {
							result = SecurityStatus.AUTH_SUCCESS;
						} else {
							result = SecurityStatus.AUTH_FAILED;
						}
					}
					break;
				}
			}
		}
		return result;
	}

	public static List<User> getUsers() {
		return SecurityConfiguration.getInstance().getUsers();
	}

	public static List<Group> getGroups() {
		return (List<Group>) SecurityConfiguration.getInstance().getGroups();
	}

}
