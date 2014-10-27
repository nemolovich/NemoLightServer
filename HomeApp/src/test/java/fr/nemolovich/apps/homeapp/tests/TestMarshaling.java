package fr.nemolovich.apps.homeapp.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.security.GlobalSecurity;
import fr.nemolovich.apps.homeapp.security.Group;
import fr.nemolovich.apps.homeapp.security.SecurityConfiguration;
import fr.nemolovich.apps.homeapp.security.User;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMarshaling {

	@Test
	public void test1() throws JAXBException {
		Group group1 = new Group("admin");
		Group group2 = new Group("user");

		User user1 = new User("root", "root");
		User user2 = new User("Nemolovich", "root");

		group1.addUser(user1);

		group2.addUser(user2);

		SecurityConfiguration.getInstance().addGroup(group1);
		SecurityConfiguration.getInstance().addGroup(group2);

		GlobalSecurity.saveConfig();

		SecurityConfiguration sc = GlobalSecurity.loadConfig();

		assertNotNull(sc);

		SecurityConfiguration.loadConfig(sc);

		assertEquals(sc, SecurityConfiguration.getInstance());

	}

	@Test
	public void test2() throws FileNotFoundException, IOException,
			ClassNotFoundException {

		Map<String, String> passwords;
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(HomeAppConstants.SECURITY_PASSWORDS_FILE)));
		passwords = (Map<String, String>) ois.readObject();
		ois.close();

		System.out.println(passwords);
	}

}
