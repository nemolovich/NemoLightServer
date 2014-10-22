package fr.nemolovich.apps.homeapp.tests;

import fr.nemolovich.apps.homeapp.security.GlobalSecurity;
import fr.nemolovich.apps.homeapp.security.Group;
import fr.nemolovich.apps.homeapp.security.SecurityConfiguration;
import fr.nemolovich.apps.homeapp.security.User;
import javax.xml.bind.JAXBException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

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

}
