package fr.nemolovich.apps.homeapp.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.utils.SearchFileOptionException;
import fr.nemolovich.apps.homeapp.utils.Utils;

public class TestSearch {

	private static JarFile jar;

	@BeforeClass
	public static void init() throws IOException {
		jar = new JarFile(
				new File(
						"D:/Users/bgohier/Desktop/Tests/HomeApp-0.1-jar-with-dependencies.jar"));
		assertNotNull(jar);
	}

	@Test
	public void testSearch() throws SearchFileOptionException {
		List<String> all = Utils.getAllFilesFrom(jar,
				"fr/nemolovich/apps/homeapp", HomeAppConstants.INCLUDE_ALL);
		List<String> classes = Utils
				.getAllFilesFrom(jar, "fr/nemolovich/apps/homeapp",
						HomeAppConstants.ONLY_CLASS_FILES);
		List<String> notclasses = Utils.getAllFilesFrom(jar,
				"fr/nemolovich/apps/homeapp",
				HomeAppConstants.EXCLUDE_CLASS_FILES);
		List<String> folders = Utils.getAllFilesFrom(jar,
				"fr/nemolovich/apps/homeapp", HomeAppConstants.ONLY_FOLDERS);
		List<String> notfolders = Utils.getAllFilesFrom(jar,
				"fr/nemolovich/apps/homeapp", HomeAppConstants.EXCLUDE_FOLDERS);
		List<String> notclassesandfolders = Utils.getAllFilesFrom(jar,
				"fr/nemolovich/apps/homeapp",
				HomeAppConstants.EXCLUDE_CLASS_FILES
						| HomeAppConstants.EXCLUDE_FOLDERS);

		Collections.sort(all);

		List<String> temp = new ArrayList();
		temp.addAll(classes);
		temp.addAll(notclasses);
		Collections.sort(temp);
		assertEquals(all, temp);
		temp.clear();
		temp.addAll(folders);
		temp.addAll(notfolders);
		Collections.sort(temp);
		assertEquals(all, temp);
		temp.clear();
		temp.addAll(folders);
		temp.addAll(classes);
		temp.addAll(notclassesandfolders);
		Collections.sort(temp);
		assertEquals(all, temp);
	}

	private static boolean calc(boolean i1, boolean f1, boolean i2, boolean f2) {
		boolean result = false;
		result = (i1 && i2 && !f1 && !f2) || (!(i1 & f1) & i1)
				|| (!(i2 & f2) & i2);
		return result;
	}

	@Test
	public void t1() {
		assertTrue(calc(false, true, false, false));
		assertTrue(calc(false, false, false, false));
		assertTrue(calc(true, false, false, false));
		assertFalse(calc(true, true, false, false));
	}

	@Test(expected = SearchFileOptionException.class)
	public void ex1() throws SearchFileOptionException {
		System.out.println(Arrays.toString(Utils.getAllFilesFrom(
				jar,
				"fr/nemolovich/apps/homeapp",
				HomeAppConstants.ONLY_CLASS_FILES
						| HomeAppConstants.EXCLUDE_CLASS_FILES).toArray()));
	}

	@Test(expected = SearchFileOptionException.class)
	public void ex2() throws SearchFileOptionException {
		System.out.println(Arrays.toString(Utils.getAllFilesFrom(
				jar,
				"fr/nemolovich/apps/homeapp",
				HomeAppConstants.ONLY_FOLDERS
						| HomeAppConstants.EXCLUDE_FOLDERS).toArray()));
	}

	@Test(expected = SearchFileOptionException.class)
	public void ex3() throws SearchFileOptionException {
		System.out.println(Arrays.toString(Utils.getAllFilesFrom(
				jar,
				"fr/nemolovich/apps/homeapp",
				HomeAppConstants.ONLY_CLASS_FILES
						| HomeAppConstants.ONLY_FOLDERS).toArray()));
	}
}
