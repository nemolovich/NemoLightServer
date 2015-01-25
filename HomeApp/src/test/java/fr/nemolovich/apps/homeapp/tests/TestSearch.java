//package fr.nemolovich.apps.homeapp.tests;
//
//import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
//import fr.nemolovich.apps.homeapp.utils.SearchFileOptionException;
//import fr.nemolovich.apps.homeapp.utils.Utils;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.jar.JarFile;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class TestSearch {
//
//	private static JarFile jar;
//
//	@BeforeClass
//	public static void init() throws IOException {
//		jar = new JarFile(
//			new File(
//				"C:/Users/Nemolovich/Desktop/Tests/HomeApp-0.1-jar-with-dependencies.jar"));
//		assertNotNull(jar);
//	}
//
//	@Test
//	public void testSearch() throws SearchFileOptionException {
//		List<String> all = Utils.getAllFilesFrom(jar,
//			"fr/nemolovich/apps/homeapp", HomeAppConstants.INCLUDE_ALL);
//		List<String> classes = Utils
//			.getAllFilesFrom(jar, "fr/nemolovich/apps/homeapp",
//				HomeAppConstants.ONLY_CLASS_FILES);
//		List<String> notclasses = Utils.getAllFilesFrom(jar,
//			"fr/nemolovich/apps/homeapp",
//			HomeAppConstants.EXCLUDE_CLASS_FILES);
//		List<String> folders = Utils.getAllFilesFrom(jar,
//			"fr/nemolovich/apps/homeapp", HomeAppConstants.ONLY_FOLDERS);
//		List<String> notfolders = Utils.getAllFilesFrom(jar,
//			"fr/nemolovich/apps/homeapp", HomeAppConstants.EXCLUDE_FOLDERS);
//		List<String> notclassesandfolders = Utils.getAllFilesFrom(jar,
//			"fr/nemolovich/apps/homeapp",
//			HomeAppConstants.EXCLUDE_CLASS_FILES
//			| HomeAppConstants.EXCLUDE_FOLDERS);
//
//		Collections.sort(all);
//
//		List<String> temp = new ArrayList();
//		temp.addAll(classes);
//		temp.addAll(notclasses);
//		Collections.sort(temp);
//		assertEquals(all, temp);
//		temp.clear();
//		temp.addAll(folders);
//		temp.addAll(notfolders);
//		Collections.sort(temp);
//		assertEquals(all, temp);
//		temp.clear();
//		temp.addAll(folders);
//		temp.addAll(classes);
//		temp.addAll(notclassesandfolders);
//		Collections.sort(temp);
//		assertEquals(all, temp);
//	}
//
//	private static boolean calc(boolean filtre1, boolean is1,
//		boolean filtre2, boolean is2) {
//		boolean result = true;
//		result ^= !(filtre1 & is1);
//		result ^= !(filtre2 & is2);
//		return result;
//	}
//
//	@Test
//	public void t1() {
//		assertTrue(calc(false, false, false, false));
//		assertTrue(calc(false, false, false, true));
//		assertTrue(calc(false, false, true, false));
//		assertFalse(calc(false, false, true, true));
//		assertTrue(calc(false, true, false, false));
//		assertTrue(calc(false, true, true, false));
//
//		assertTrue(calc(true, false, false, false));
//		assertTrue(calc(true, false, false, true));
//		assertTrue(calc(true, false, true, false));
//		assertFalse(calc(true, false, true, true));
//		assertFalse(calc(true, true, false, false));
//		assertFalse(calc(true, true, true, false));
//	}
//
//	@Test(expected = SearchFileOptionException.class)
//	public void ex1() throws SearchFileOptionException {
//		System.out.println(Arrays.toString(Utils.getAllFilesFrom(
//			jar,
//			"fr/nemolovich/apps/homeapp",
//			HomeAppConstants.ONLY_CLASS_FILES
//			| HomeAppConstants.EXCLUDE_CLASS_FILES).toArray()));
//	}
//
//	@Test(expected = SearchFileOptionException.class)
//	public void ex2() throws SearchFileOptionException {
//		System.out.println(Arrays.toString(Utils.getAllFilesFrom(
//			jar,
//			"fr/nemolovich/apps/homeapp",
//			HomeAppConstants.ONLY_FOLDERS
//			| HomeAppConstants.EXCLUDE_FOLDERS).toArray()));
//	}
//
//	@Test(expected = SearchFileOptionException.class)
//	public void ex3() throws SearchFileOptionException {
//		System.out.println(Arrays.toString(Utils.getAllFilesFrom(
//			jar,
//			"fr/nemolovich/apps/homeapp",
//			HomeAppConstants.ONLY_CLASS_FILES
//			| HomeAppConstants.ONLY_FOLDERS).toArray()));
//	}
//}
