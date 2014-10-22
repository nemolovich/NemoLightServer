package fr.nemolovich.apps.homeapp.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import fr.nemolovich.apps.homeapp.security.GlobalSecurity;
import fr.nemolovich.apps.homeapp.security.Group;
import fr.nemolovich.apps.homeapp.security.SecurityConfiguration;
import fr.nemolovich.apps.homeapp.security.User;

public class TestMarshaling {

	@Test
	public void test1() throws JAXBException {
		Group group1 = new Group("group1");
		Group group2 = new Group("group2");

		User user1 = new User("user1", "p1");
		User user2 = new User("user2", "p2");
		User user3 = new User("user3", "p3");
		User user4 = new User("user4", "p4");

		group1.addUser(user1);
		group1.addUser(user3);

		group2.addUser(user2);
		group2.addUser(user4);

		SecurityConfiguration.getInstance().addGroup(group1);
		SecurityConfiguration.getInstance().addGroup(group2);

		GlobalSecurity.saveConfig();

		SecurityConfiguration sc = GlobalSecurity.loadConfig();

		assertNotNull(sc);

		SecurityConfiguration.loadConfig(sc);

		assertEquals(sc, SecurityConfiguration.getInstance());

	}

}
