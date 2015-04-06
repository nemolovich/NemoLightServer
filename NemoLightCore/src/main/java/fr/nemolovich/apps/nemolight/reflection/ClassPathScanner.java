package fr.nemolovich.apps.nemolight.reflection;

import fr.nemolovich.apps.nemolight.config.WebConfig;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.utils.SearchFileOptionException;
import fr.nemolovich.apps.nemolight.utils.Utils;
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
import org.apache.log4j.Logger;

public class ClassPathScanner {

	private static final Logger LOGGER = Logger
			.getLogger(ClassPathScanner.class.getName());
	private static final Field GET_CLASSES;

	static {
		Field f = null;
		try {
			f = ClassLoader.class.getDeclaredField("classes");
		} catch (NoSuchFieldException | SecurityException ex) {
			LOGGER.error("Can not initialize ClassLoader classes", ex);
			System.exit(1);
		}
		GET_CLASSES = f;
	}

	private final List<SearchFilter> filters;
	private ConcurrentLinkedQueue<Class<?>> classes;

	public ClassPathScanner() {
		this.filters = new ArrayList();
		this.classes = new ConcurrentLinkedQueue();
	}

	private void loadClasses() {
		try {
			ClassLoader classLoader = (ClassLoader) WebConfig
					.getValue(WebConfig.DEPLOYMENT_CLASSLOADER);
			GET_CLASSES.setAccessible(true);
			this.classes = new ConcurrentLinkedQueue<>(
					(List<Class<?>>) GET_CLASSES.get(classLoader));
			this.classes.addAll(new ConcurrentLinkedQueue<>(
					(List<Class<?>>) GET_CLASSES.get(classLoader.getParent())));
			GET_CLASSES.setAccessible(false);
		} catch (IllegalArgumentException | IllegalAccessException
				| SecurityException ex) {
			LOGGER.error("Can not retreive ClassLoader classes", ex);
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
			for (SearchFilter filter : this.filters) {
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
			ClassLoader classLoader = (ClassLoader) WebConfig
					.getValue(WebConfig.DEPLOYMENT_CLASSLOADER);
			if (classLoader == null) {
				throw new IOException("Can not get context ClassLoader");
			}
			String protocol = null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<String> files = new ArrayList();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				protocol = resource.getProtocol();
				if (protocol.equalsIgnoreCase(NemoLightConstants.FILE_PROTOCOL)) {
					File f = new File(resource.toURI());
					files.addAll(Utils.getAllFilesFrom("", f,
							NemoLightConstants.ONLY_CLASS_FILES));
				} else if (protocol
						.equalsIgnoreCase(NemoLightConstants.JAR_PROTOCOL)) {
					JarFile jar = Utils.extractJar(resource);
					files.addAll(Utils.getAllFilesFrom(jar, path,
							NemoLightConstants.ONLY_CLASS_FILES));
				} else {
					throw new URISyntaxException(
							resource.toString(),
							String.format("Can not use protocol '%s'", protocol));
				}
			}
			if (protocol != null) {
				if (protocol.equalsIgnoreCase(NemoLightConstants.FILE_PROTOCOL)) {
					loadClasses(files, packageName);
				} else if (protocol
						.equalsIgnoreCase(NemoLightConstants.JAR_PROTOCOL)) {
					loadClasses(files, packageName);
				}
			}
		} catch (IOException | ClassNotFoundException | URISyntaxException
				| SearchFileOptionException ex) {
			LOGGER.error(String.format(
					"The classes could not be load from package '%s'",
					packageName), ex);
		}
	}

	private static List<Class> loadClasses(List<String> files,
			String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList();

		for (String file : files) {
			String classPath = String.format("%s/%s", packageName, file);
			classPath = classPath.replaceAll("/", ".");
			classPath = classPath.replaceAll("\\.{2}", ".");
			if (classPath.endsWith(".class")) {
				classPath = classPath.substring(0,
						classPath.lastIndexOf(".class"));
			}
			ClassLoader classLoader = (ClassLoader) WebConfig
					.getValue(WebConfig.DEPLOYMENT_CLASSLOADER);
			classes.add(classLoader.loadClass(classPath));
		}
		return classes;
	}

	public void addIncludeFilter(SearchFilter filter) {
		this.filters.add(filter);
	}

}
