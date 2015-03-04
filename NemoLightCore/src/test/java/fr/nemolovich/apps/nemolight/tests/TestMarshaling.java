package fr.nemolovich.apps.nemolight.tests;

import fr.nemolovich.apps.nemolight.admin.commands.AddGroup;
import fr.nemolovich.apps.nemolight.admin.commands.AddUser;
import fr.nemolovich.apps.nemolight.admin.commands.RemoveGroup;
import fr.nemolovich.apps.nemolight.admin.commands.constants.CommandConstants;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.security.GlobalSecurity;
import fr.nemolovich.apps.nemolight.security.SecurityConfiguration;
import fr.nemolovich.apps.nemolight.security.CommonUtils;
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
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMarshaling {

//	@Test
	public void test1() throws JAXBException {

		SecurityConfiguration.getInstance().addGroup("admin");
		SecurityConfiguration.getInstance().addGroup("default");

		SecurityConfiguration.getInstance().addUser("admin", "root",
			CommonUtils.getEncryptedPassword("root"));
		SecurityConfiguration.getInstance().addUser("default", "Nemolovich",
			CommonUtils.getEncryptedPassword(""));

		GlobalSecurity.saveConfig();

		SecurityConfiguration sc = GlobalSecurity.loadConfig();

		assertNotNull(sc);

		SecurityConfiguration.loadConfig(sc);

		assertEquals(sc, SecurityConfiguration.getInstance());

	}

//    @Test
	public void test2() throws FileNotFoundException, IOException,
		ClassNotFoundException {

		Map<String, String> passwords;
		try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
			new FileInputStream(NemoLightConstants.SECURITY_PASSWORDS_FILE)))) {
			passwords = (Map<String, String>) ois.readObject();
		}

		System.out.println(passwords);
	}

//    @Test
	public void test3() throws JAXBException {
		SecurityConfiguration.getInstance().reset();

		assertEquals(0, SecurityConfiguration.getInstance().getGroups().size());

		AddGroup ag = new AddGroup();

		assertEquals(CommandConstants.SUCCESS_CODE,
			Integer.parseInt(ag.doCommand("default")));

		assertEquals(CommandConstants.SUCCESS_CODE,
			Integer.parseInt(ag.doCommand("admin")));

		assertEquals(CommandConstants.EXECUTION_ERROR_CODE
			+ CommandConstants.GROUP_ALREADY_EXISTS_CODE,
			Integer.parseInt(ag.doCommand("admin")));

		assertEquals(2, SecurityConfiguration.getInstance().getGroups().size());

		SecurityConfiguration.getInstance().reset();

		assertEquals(0, SecurityConfiguration.getInstance().getGroups().size());

		SecurityConfiguration sc = GlobalSecurity.loadConfig();

		SecurityConfiguration.loadConfig(sc);

		assertEquals(2, SecurityConfiguration.getInstance().getGroups().size());
	}

//    @Test
	public void test4() throws JAXBException {

		RemoveGroup rg = new RemoveGroup();

		int size = SecurityConfiguration.getInstance().getGroups().size();

		assertTrue(SecurityConfiguration.getInstance()
			.containsGroup("default"));

		assertEquals(CommandConstants.SUCCESS_CODE,
			Integer.parseInt(rg.doCommand("default")));

		assertEquals(size - 1,
			SecurityConfiguration.getInstance().getGroups().size());

		SecurityConfiguration.getInstance().reset();

		assertEquals(0, SecurityConfiguration.getInstance().getGroups().size());

		SecurityConfiguration sc = GlobalSecurity.loadConfig();

		SecurityConfiguration.loadConfig(sc);

		assertEquals(size - 1,
			SecurityConfiguration.getInstance().getGroups().size());
	}

//    @Test
	public void test5() throws JAXBException {

		AddUser au = new AddUser();

		int size = SecurityConfiguration.getInstance().getGroups().size();

		assertTrue(size > 0);

		assertTrue(SecurityConfiguration.getInstance()
			.containsGroup("admin"));

		assertEquals(CommandConstants.SUCCESS_CODE,
			Integer.parseInt(au.doCommand("admin", "root",
					CommonUtils.getEncryptedPassword("root"))));

		assertEquals(CommandConstants.EXECUTION_ERROR_CODE
			+ CommandConstants.USER_ALREADY_EXISTS_CODE,
			Integer.parseInt(au.doCommand("admin", "root",
					CommonUtils.getEncryptedPassword(""))));

	}

}
