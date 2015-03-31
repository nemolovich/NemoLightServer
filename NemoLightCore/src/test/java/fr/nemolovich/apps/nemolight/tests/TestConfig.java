package fr.nemolovich.apps.nemolight.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.nemolovich.apps.nemolight.route.file.utils.DeployConfig;

/**
 *
 * @author Nemolovich
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConfig {

	private static final DeployConfig FILES_PATH = new DeployConfig();
	private static final File FILE = new File("test.xml").getAbsoluteFile();
	private static final String STRING_KEY = "testString";
	private static final String STRING_VALUE = "A string";
	private static final List<String> LIST;

	static {
		LIST = new ArrayList<>();
		LIST.add("C:/");
		LIST.add("F:/");
	}

	@BeforeClass
	public static void init() {
		FILES_PATH.put(DeployConfig.DEPLOY_FILES_PATH, LIST);
		FILES_PATH.put(STRING_KEY, STRING_VALUE);
	}

	@Test
	public void test1() throws JAXBException, IOException {
		FileOutputStream os = new FileOutputStream(FILE);
		FILES_PATH.saveConfig(os);
		assertTrue(FILE.exists());
		os.close();
	}

	@Test
	public void test2() throws JAXBException, IOException {
		assertTrue(FILE.exists());
		FileInputStream is = new FileInputStream(FILE);
		DeployConfig c = (DeployConfig) DeployConfig.loadConfig(is);
		assertEquals(c.size(), FILES_PATH.size());
		assertEquals(c.getString(STRING_KEY), FILES_PATH.get(STRING_KEY));
		List<String> paths = c.getList(DeployConfig.DEPLOY_FILES_PATH);
		assertEquals(paths.size(), LIST.size());
		for (String str : paths) {
			assertTrue(LIST.contains(str));
		}
		is.close();
	}

	@AfterClass
	public static void close() {
		assertTrue(FILE.exists());
		 boolean deleted = FILE.delete();
		 assertTrue(deleted);
	}
}
