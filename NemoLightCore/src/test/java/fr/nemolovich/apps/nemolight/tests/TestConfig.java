package fr.nemolovich.apps.nemolight.tests;

import fr.nemolovich.apps.nemolight.route.file.utils.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Nemolovich
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConfig {
	
	private static final Config filesPath = new Config();
	private static final File file = new File("test.xml");
	
	@BeforeClass
	public static void init() {
		List<String> paths = new ArrayList<>();
		paths.add("C:/");
		paths.add("F:/");
		filesPath.put(Config.DEPLOY_FILES_PATH, paths);
		filesPath.put("testString", "A string");
	}
	
	@Test
	public void test1() throws JAXBException, FileNotFoundException {
		filesPath.saveConfig(new FileOutputStream(file));
	}
	
	@Test
	public void test2() throws JAXBException, FileNotFoundException {
		Config c = Config.loadConfig(new FileInputStream(file));
		Assert.assertEquals(c.get("testString"),
			filesPath.get("testString"));
	}
}
