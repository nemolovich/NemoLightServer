package fr.nemolovich.apps.homeapp.reflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.utils.Utils;

public class ClassPathScanner {

	private static final Logger LOGGER = Logger
			.getLogger(ClassPathScanner.class.getName());
	private static final Field GET_CLASSES;

	static {
		Field f = null;
		try {
			f = ClassLoader.class.getDeclaredField("classes");
		} catch (NoSuchFieldException | SecurityException ex) {
			LOGGER.log(Level.SEVERE, "Can not initialize ClassLoader classes",
					ex);
			System.exit(1);
		}
		GET_CLASSES = f;
	}

	private List<SearchFilter> filters;
	private ConcurrentLinkedQueue<Class<?>> classes;

	public ClassPathScanner() {
		this.filters = new ArrayList();
		this.classes = new ConcurrentLinkedQueue();
	}

	private void loadClasses() {
		try {
			GET_CLASSES.setAccessible(true);
			this.classes = new ConcurrentLinkedQueue(
					(List<Class<?>>) GET_CLASSES.get(ClassPathScanner.class
							.getClassLoader()));
			GET_CLASSES.setAccessible(false);
		} catch (IllegalArgumentException | IllegalAccessException
				| SecurityException ex) {
			LOGGER.log(Level.SEVERE, "Can not retreive ClassLoader classes", ex);
			System.exit(1);
		}
	}

	public List<Class<?>> findCandidateComponents(String packageName) {
		forceLoadPackage(packageName);
		this.loadClasses();

		List<Class<?>> result = new ArrayList();
		for (Class<?> clazz : this.classes) {
			if (!clazz.getName().startsWith(packageName)
					|| clazz.getName().contains("$")) {
				continue;
			}
			boolean matches = true;
			for (SearchFilter filter : filters) {
				if (!filter.filterMatches(clazz)) {
					matches = false;
					break;
				}
			}
			if (matches) {
				result.add(clazz);
			}
		}
		return result;
	}

	private static void forceLoadPackage(String packageName) {
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			if (classLoader == null) {
				throw new IOException("Can not get context ClassLoader");
			}
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<String> files = new ArrayList();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				String protocol = resource.getProtocol();
				if (protocol.equalsIgnoreCase(HomeAppConstants.FILE_PROTOCOL)) {
					File f = new File(resource.toURI());
					files.addAll(Utils.getAllFilesFrom("", f));
				} else if (protocol
						.equalsIgnoreCase(HomeAppConstants.JAR_PROTOCOL)) {
					JarFile jar = Utils.extractJar(resource);
					files.addAll(Utils.getAllFilesFrom(jar, path));
				}
			}
			for (String f : files) {
				System.out.println("File: " + f);
				// loadClasses(directory, packageName);
				if (false) {
					throw new ClassNotFoundException();
				}
			}
		} catch (IOException | ClassNotFoundException | URISyntaxException ex) {
			LOGGER.log(Level.SEVERE,
					"The classes could not be load from package '"
							+ packageName + "'", ex);
		}
	}

	private static List<Class> loadClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class> classes = new ArrayList();
		if (!directory.exists()) {
			return classes;
		}
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				if (!file.getName().contains(".")) {
					classes.addAll(loadClasses(file,
							packageName + "." + file.getName()));
				}
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - ".class".length())));
			}
		}
		return classes;
	}

	public void addIncludeFilter(SearchFilter filter) {
		this.filters.add(filter);
	}

}
