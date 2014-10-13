package fr.nemolovich.apps.homeapp.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;

public final class Utils {

	private static final Logger LOGGER = Logger
			.getLogger(Utils.class.getName());

	public static final JarFile extractJar(URL jarURL) {
		String jarPath = null;

		try {
			System.out.println(jarURL.toURI().toString());
			jarPath = jarURL.toURI().toString();
		} catch (URISyntaxException ex) {
			LOGGER.log(Level.SEVERE, "The JAR URI can not be used", ex);
		}
		jarPath = jarPath.substring(HomeAppConstants.JAR_PROTOCOL.length() + 1,
				jarPath.indexOf("!"));
		if (jarPath.startsWith(HomeAppConstants.FILE_PROTOCOL)) {
			jarPath = jarPath
					.substring(HomeAppConstants.FILE_PROTOCOL.length() + 1);
		}
		jarPath = jarPath.substring(1);

		JarFile jar = null;

		try {
			jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "Can not load path from JAR file", ex);
		}

		return jar;
	}

	public static List<String> getAllFilesFrom(String basePath, File folder) {
		List<String> files = new ArrayList<>();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(getAllFilesFrom(basePath.concat(f.getName())
						.concat("/"), f));
			} else {
				if (!f.getName().endsWith(".class")) {
					files.add(basePath.concat(f.getName()));
				}
			}
		}
		return files;
	}

	public static List<String> getAllFilesFrom(JarFile jar, String resourcesPath)
			throws SearchFileOptionException {
		return getAllFilesFrom(jar, resourcesPath,
				HomeAppConstants.DEFAULT_SEARCH);
	}

	public static List<String> getAllFilesFrom(JarFile jar,
			String resourcesPath, int options) throws SearchFileOptionException {

		/*
		 * Check options
		 */

		boolean all = false;
		boolean only_classes = false;
		boolean exclude_classes = false;
		boolean only_fodlers = false;
		boolean exclude_folders = false;
		if (options == HomeAppConstants.INCLUDE_ALL) {
			all = true;
		} else {
			if (((options & HomeAppConstants.ONLY_CLASS_FILES)) == HomeAppConstants.ONLY_CLASS_FILES) {
				only_classes = true;
			}
			if (((options & HomeAppConstants.EXCLUDE_CLASS_FILES)) == HomeAppConstants.EXCLUDE_CLASS_FILES) {
				exclude_classes = true;
			}
			if (((options & HomeAppConstants.ONLY_FOLDERS)) == HomeAppConstants.ONLY_FOLDERS) {
				only_fodlers = true;
			}
			if (((options & HomeAppConstants.EXCLUDE_FOLDERS)) == HomeAppConstants.EXCLUDE_FOLDERS) {
				exclude_folders = true;
			}

			/*
			 * Check errors
			 */
			if (only_classes && exclude_classes) {
				// Can not include and exclude classes
				throw new SearchFileOptionException(
						"Can not include and exclude classes");
			}
			if (only_fodlers && exclude_folders) {
				// Can not include and exclude folders
				throw new SearchFileOptionException(
						"Can not include and exclude folders");
			}
			if (only_classes && only_fodlers) {
				// Can not get only folders and only classes
				throw new SearchFileOptionException(
						"Can not get only folders and only classes");
			}
		}

		List<String> files = new ArrayList<>();
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			String name = entries.nextElement().getName();
			if (name.startsWith(resourcesPath)) {
				String entry = name.substring(resourcesPath.length());
				if (!entry.isEmpty()) {
					boolean get = false;
					boolean isClass = entry.endsWith(".class");
					boolean isFolder = entry.endsWith("/");
					if (all || (only_classes && isClass)
							|| (only_fodlers && isFolder)) {
						get = true;
					} else {
						get = !(exclude_classes & isClass);
						get = !(exclude_folders & isFolder);
					}
					if (get) {
						files.add(entry);
					}
				}
			}
		}
		return files;
	}

}
