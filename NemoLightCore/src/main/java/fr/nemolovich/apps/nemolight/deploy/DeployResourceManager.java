package fr.nemolovich.apps.nemolight.deploy;

import fr.nemolovich.apps.nemolight.Launcher;
import fr.nemolovich.apps.nemolight.admin.AdminConnection;
import fr.nemolovich.apps.nemolight.config.WebConfig;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.reflection.AnnotationTypeFilter;
import fr.nemolovich.apps.nemolight.reflection.ClassPathScanner;
import fr.nemolovich.apps.nemolight.reflection.SuperClassFilter;
import fr.nemolovich.apps.nemolight.route.WebRoute;
import fr.nemolovich.apps.nemolight.route.WebRouteServletInterface;
import fr.nemolovich.apps.nemolight.route.annotations.PageField;
import fr.nemolovich.apps.nemolight.route.annotations.RouteElement;
import fr.nemolovich.apps.nemolight.route.file.FileRoute;
import fr.nemolovich.apps.nemolight.route.file.utils.DeployConfig;
import fr.nemolovich.apps.nemolight.utils.SearchFileOptionException;
import fr.nemolovich.apps.nemolight.utils.Utils;
import freemarker.template.Configuration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import spark.Spark;

/**
 *
 * @author Nemolovich
 */
public final class DeployResourceManager {

	private static final boolean IS_WINDOWS_SYSTEM
		= System.getProperty("os.name").startsWith("Windows");

	protected static final Logger LOGGER = Logger
		.getLogger(DeployResourceManager.class.getName());

	private static final Map<String, WebRouteServletInterface> SERVLETS
		= new ConcurrentHashMap<>();
	private static final List<FileRoute> FILES = new ArrayList<>();

	private static int APPLICATION_NUMBER = 0;
	private static final List<Map<String, Object>> ALLICATIONS_PROPERTIES
		= new ArrayList<>();

	public static void initFromPackage(String resourcesPath) {
		ClassLoader classLoader = (ClassLoader) WebConfig
			.getValue(WebConfig.DEPLOYMENT_CLASSLOADER);
		URL url = classLoader.getResource(resourcesPath);
		if (url == null) {
			url = classLoader.getResource(String.format("%s/%s",
				NemoLightConstants.SRC_FOLDER, resourcesPath));
		}
		if (url != null) {
			List<String> files = null;
			String basePath = null;
			String protocol = url.getProtocol();
			try {
				if (protocol.equalsIgnoreCase(
					NemoLightConstants.FILE_PROTOCOL)) {
					File path = new File(url.toURI());
					files = Utils.getAllFilesFrom("", path,
						NemoLightConstants.EXCLUDE_CLASS_FILES
						| NemoLightConstants.EXCLUDE_FOLDERS);
					basePath = url.toURI().getPath();
					if (IS_WINDOWS_SYSTEM) {
						/*
						 * Remove drive letter for Windows.
						 */
						basePath = basePath.substring(1);
					}

				} else if (protocol
					.equalsIgnoreCase(NemoLightConstants.JAR_PROTOCOL)) {
					JarFile jar = Utils.extractJar(url);
					files = Utils.getAllFilesFrom(jar, resourcesPath,
						NemoLightConstants.EXCLUDE_CLASS_FILES
						| NemoLightConstants.EXCLUDE_FOLDERS);

					basePath = resourcesPath;
				} else {
					LOGGER.error(String.format("Unknown protocol '%s'",
						protocol));
				}
			} catch (SearchFileOptionException | URISyntaxException ex) {
				LOGGER.error("Error while searching files", ex);
			}
			if (files != null) {
				DeployUtils.extractFiles(files, basePath,
					protocol);
			}
		}
	}

	public static final boolean deployWebPages(Configuration config,
		String packageName, int appIdentifier) {
		ClassPathScanner scanner = new ClassPathScanner();
		scanner.addIncludeFilter(new AnnotationTypeFilter(
			RouteElement.class));
		scanner.addIncludeFilter(new SuperClassFilter(
			WebRouteServletInterface.class));

		WebRouteServletInterface servlet;
		boolean loginPageDefined = false;
		RouteElement annotation;
		String path, page;
		boolean isLoginPage, isSecured;
		Constructor<?> cst;
		Object newInstance;

		Field[] declaredFields;
		PageField pageField;
		String pageFieldName;

		List<Class<?>> classes = scanner
			.findCandidateComponents(
				NemoLightConstants.PACKAGE_NAME.replaceAll(
					"/", "."));
		classes.addAll(scanner.findCandidateComponents(
			packageName));

		config.clearTemplateCache();
		DeployResourceManager.SERVLETS.clear();
		DeployResourceManager.FILES.clear();
		DeployResourceManager.ALLICATIONS_PROPERTIES.clear();

		for (Class<?> c : classes) {
			try {
				/*
				 * Get annoation informations.
				 */
				annotation = c.getAnnotation(RouteElement.class);
				path = annotation.path();
				page = annotation.page();
				isLoginPage = annotation.login();
				isSecured = annotation.secured();
				cst = c.getConstructor(String.class, String.class,
					Configuration.class);
				/*
				 * Construct.
				 */
				newInstance = cst.newInstance(path, page, config);

				if (newInstance instanceof WebRouteServletInterface) {
					servlet = (WebRouteServletInterface) newInstance;
					if (isSecured) {
						servlet.enableSecurity();
					}

					/*
					 * Set page fields for servlet.
					 */
					declaredFields = c.getDeclaredFields();
					for (Field field : declaredFields) {
						if (field.getType() == String.class
							&& field.isAnnotationPresent(PageField.class)) {
							pageField = field.getAnnotation(PageField.class);
							pageFieldName = Utils.getFieldName(
								pageField.name());
							pageFieldName = pageFieldName.isEmpty()
								? field.getName() : pageFieldName;
							servlet.addPageField(field,
								pageFieldName);
						}
					}

					/*
					 * Add to deployer.
					 */
					DeployResourceManager.SERVLETS.put(
						servlet.getName(), servlet);
					LOGGER.info(String.format(
						"Resource page '%s' has been deployed! [%s]",
						c.getName(), path));
					if (!loginPageDefined && isLoginPage) {
						loginPageDefined = true;
						WebRoute.setLoginPage(servlet.getPostRoute());
						LOGGER.info(String
							.format("Resource page '%s' has been set to login page",
								c.getName()));
					} else if (isLoginPage) {
						LOGGER.warn("Login page is already set");
					}
				}

			} catch (InstantiationException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException |
				NoSuchMethodException | SecurityException ex) {
				LOGGER.error("Error while deploying resources", ex);
			}
		}

		return false;
	}

	public static void deployWebApp(String deployFolderPath) {
		File deployFolder = new File(deployFolderPath);
		try {
			if (!deployFolder.exists() || !deployFolder.isDirectory()) {
				throw new FileNotFoundException(String.format(
					"The deploy folder '%s can not be located",
					deployFolderPath));
			}
			FileRoute route;
			String uriPath;
			String routePath;
			for (File f : DeployUtils.getAllFiles(deployFolder,
				NemoLightConstants.RECURSIVE_SEARCH)) {
				uriPath = f.toURI().toString();
				routePath = uriPath.substring(uriPath
					.lastIndexOf(deployFolderPath)
					+ (deployFolderPath.length()));
				route = new FileRoute(routePath, f);
				DeployResourceManager.FILES.add(route);
				LOGGER.info(String.format(
					"Resource file '%s' has been deployed! [%s]",
					f.getName(), routePath));
			}
		} catch (FileNotFoundException ex) {
			LOGGER.error("Error while deploying webapp", ex);
		}
	}

	public static void startServer() {
		startServer(8080);
	}

	public static void startServer(int port) {
		startServer(port, 8181);
	}

	public static void startServer(int port, int adminPort) {

		if (adminPort != port) {
			AdminConnection ac = new AdminConnection(adminPort);
			ac.start();
		} else {
			LOGGER.error("The admin port can not be the same as deployment port");
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Thread.currentThread().setName("Shutdown-Hook");
				LOGGER.info("Server stopped... I'll miss you ;)");
			}
		});
		Spark.setPort(port);

		startListening();
	}

	public static void startListening() {
		for (WebRouteServletInterface servlet
			: DeployResourceManager.SERVLETS.values()) {
			Spark.get(servlet.getGetRoute());
			Spark.post(servlet.getPostRoute());
		}
		for (FileRoute route : DeployResourceManager.FILES) {
			Spark.get(route);
		}
	}

	public static List<Map<String, Object>> initializeClassLoader() {

		List<URL> urls = new ArrayList<>();
		ClassLoader parentClassLoader = Launcher.class
			.getClassLoader();
		URLClassLoader classLoader = new URLClassLoader(
			urls.toArray(new URL[0]), parentClassLoader);

		List<InputStream> deployFiles = new ArrayList<>();

		/*
		 * Load applications.
		 */
		File deployFolder = new File(NemoLightConstants.DEPLOY_FOLDER);

		if (deployFolder.listFiles().length > 0) {
			InputStream is;
			URL url;
			for (File f : deployFolder.listFiles()) {
				try {
					url = f.toURI().toURL();
					urls.add(url);
					classLoader = new URLClassLoader(
						(URL[]) urls.toArray(new URL[0]),
						parentClassLoader);
				} catch (MalformedURLException ex) {
					System.err.printf(
						"Can not load deployment file from jar '%s': %s\n",
						f.getName(), ex.getMessage());
					continue;
				}
				is = classLoader
					.getResourceAsStream(
						NemoLightConstants.JAR_DEPLOY_FILE);
				DeployUtils.addMavenDependencies(url, classLoader);
				if (is != null) {
					deployFiles.add(is);
				}
			}
		}

		/*
		 * Load dependencies.
		 */
		File dependenciesFolder = new File(
			NemoLightConstants.DEPENDENCIES_FOLDER);

		if (dependenciesFolder.listFiles().length > 0) {
			URL url;
			for (File f : dependenciesFolder.listFiles()) {
				try {
					url = f.toURI().toURL();
					urls.add(url);
					classLoader = new URLClassLoader(
						(URL[]) urls.toArray(new URL[0]),
						parentClassLoader);
				} catch (MalformedURLException ex) {
					System.err.printf(
						"Can not load deployment file from jar '%s': %s\n",
						f.getName(), ex.getMessage());
				}
			}
		}

		/*
		 * Set Application Deployment ClassLoader.
		 */
		WebConfig.getInstance()
			.setConfig(WebConfig.DEPLOYMENT_CLASSLOADER,
				classLoader);

		/*
		 * Add the package name if specified.
		 */
		List<String> packagesName = new ArrayList<>();

		try {
			DeployConfig deployConfig;
			String packageName;
			String appName;
			Map<String, Object> infos;
			for (InputStream is : deployFiles) {

				deployConfig = DeployConfig.loadConfig(is);
				packageName = deployConfig
					.getString(DeployConfig.DEPLOY_PACKAGE);
				appName = deployConfig
					.getString(DeployConfig.DEPLOY_APP_NAME);
				appName = appName == null ? String.format(
					"Application%02d", (APPLICATION_NUMBER + 1))
					: appName;

				if (packageName != null) {
					infos = new HashMap<>();
					infos.put(NemoLightConstants.APP_IDENTIFIER,
						APPLICATION_NUMBER++);
					infos.put(NemoLightConstants.APP_NAME,
						appName);
					infos.put(NemoLightConstants.APP_PACKAGE,
						packageName);

					ALLICATIONS_PROPERTIES.add(infos);
				}
			}
		} catch (JAXBException ex) {
			LOGGER.error("Can not load config", ex);
		}

		return ALLICATIONS_PROPERTIES;
	}

	public static List<String> getBeans() {
		return new ArrayList<>(
			DeployResourceManager.SERVLETS.keySet());
	}

	public static WebRouteServletInterface getBean(String beanName) {
		return DeployResourceManager.SERVLETS.get(beanName);
	}

}
