package fr.nemolovich.apps.nemolight.tests;

import fr.nemolovich.apps.nemolight.route.file.utils.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Nemolovich
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConfig {

	private static final Config FILES_PATH = new Config();
	private static final File FILE = new File("test.xml").getAbsoluteFile();

	@BeforeClass
	public static void init() {
		List<String> paths = new ArrayList<>();
		paths.add("C:/");
		paths.add("F:/");
		FILES_PATH.put(Config.DEPLOY_FILES_PATH, paths);
		FILES_PATH.put("testString", "A string");
	}

	@Test
	public void test1() throws JAXBException, IOException {
		FileOutputStream os = new FileOutputStream(FILE);
		FILES_PATH.saveConfig(os);
		os.close();
	}

	@Test
	public void test2() throws JAXBException, IOException {
		FileInputStream is = new FileInputStream(FILE);
		Config c = Config.loadConfig(is);
		Assert.assertEquals(c.get("testString"), FILES_PATH.get("testString"));
		is.close();
	}

	@AfterClass
	public static void close() {
		assertTrue(FILE.exists());
		boolean deleted = FILE.delete();
		 assertTrue(deleted);
	}
}
