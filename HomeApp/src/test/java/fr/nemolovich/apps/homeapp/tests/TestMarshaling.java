package fr.nemolovich.apps.homeapp.tests;

import fr.nemolovich.apps.homeapp.admin.commands.AddGroup;
import fr.nemolovich.apps.homeapp.admin.commands.RemoveGroup;
import fr.nemolovich.apps.homeapp.admin.commands.constants.CommandConstants;
import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.security.GlobalSecurity;
import fr.nemolovich.apps.homeapp.security.Group;
import fr.nemolovich.apps.homeapp.security.SecurityConfiguration;
import fr.nemolovich.apps.homeapp.security.User;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import javax.xml.bind.JAXBException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMarshaling {

    @Test
    public void test1() throws JAXBException {
        Group group1 = new Group("admin");
        Group group2 = new Group("default");
//
        User user1 = new User("root", "root");
        User user2 = new User("Nemolovich", "root");

        group1.addUser(user1);
//
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
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
            new FileInputStream(HomeAppConstants.SECURITY_PASSWORDS_FILE)))) {
            passwords = (Map<String, String>) ois.readObject();
        }

        System.out.println(passwords);
    }

    @Test
    public void test3() throws JAXBException {
        SecurityConfiguration.getInstance().reset();

        assertEquals(0, SecurityConfiguration.getInstance().getGroups().size());

        AddGroup ag = new AddGroup();

        assertEquals(Integer.parseInt(ag.doCommand("default")),
            CommandConstants.SUCCESS_CODE);

        assertEquals(Integer.parseInt(ag.doCommand("admin")),
            CommandConstants.SUCCESS_CODE);

        assertEquals(2, SecurityConfiguration.getInstance().getGroups().size());

        SecurityConfiguration.getInstance().reset();

        assertEquals(0, SecurityConfiguration.getInstance().getGroups().size());

        SecurityConfiguration sc = GlobalSecurity.loadConfig();

        SecurityConfiguration.loadConfig(sc);

        assertEquals(2, SecurityConfiguration.getInstance().getGroups().size());
    }

    @Test
    public void test4() throws JAXBException {

        RemoveGroup rg = new RemoveGroup();

        int size = SecurityConfiguration.getInstance().getGroups().size();

        assertTrue(SecurityConfiguration.getInstance()
            .containsGroup("default"));

        assertEquals(Integer.parseInt(rg.doCommand("default")),
            CommandConstants.SUCCESS_CODE);

        assertEquals(size - 1,
            SecurityConfiguration.getInstance().getGroups().size());

        SecurityConfiguration.getInstance().reset();

        assertEquals(0, SecurityConfiguration.getInstance().getGroups().size());

        SecurityConfiguration sc = GlobalSecurity.loadConfig();

        SecurityConfiguration.loadConfig(sc);

        assertEquals(size - 1,
            SecurityConfiguration.getInstance().getGroups().size());
    }

}
