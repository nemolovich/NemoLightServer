package fr.nemolovich.apps.nemolight.deploy;

import fr.nemolovich.apps.mavendependenciesdownloader.DependenciesDownloader;
import fr.nemolovich.apps.mavendependenciesdownloader.DependenciesException;
import fr.nemolovich.apps.nemolight.Launcher;
import fr.nemolovich.apps.nemolight.admin.AdminConnection;
import fr.nemolovich.apps.nemolight.config.WebConfig;
import fr.nemolovich.apps.nemolight.constants.NemoLightConstants;
import fr.nemolovich.apps.nemolight.reflection.AnnotationTypeFilter;
import fr.nemolovich.apps.nemolight.reflection.ClassPathScanner;
import fr.nemolovich.apps.nemolight.reflection.SuperClassFilter;
import fr.nemolovich.apps.nemolight.route.WebRoute;
import fr.nemolovich.apps.nemolight.route.WebRouteServlet;
import fr.nemolovich.apps.nemolight.route.annotations.PageField;
import fr.nemolovich.apps.nemolight.route.annotations.RouteElement;
import fr.nemolovich.apps.nemolight.route.file.FileRoute;
import fr.nemolovich.apps.nemolight.route.file.utils.DeployConfig;
import fr.nemolovich.apps.nemolight.utils.SearchFileOptionException;
import fr.nemolovich.apps.nemolight.utils.Utils;
import freemarker.template.Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import spark.Spark;

/**
 *
 * @author Nemolovich
 */
public final class DeployResourceManager {

	private static final boolean IS_WINDOWS_SYSTEM
		= System.getProperty("os.name").startsWith("Windows");
	
	private static final Logger LOGGER = Logger
		.getLogger(DeployResourceManager.class.getName());

	private static final Map<String, WebRouteServlet> SERVLETS
		= new ConcurrentHashMap<>();
	private static final List<FileRoute> FILES = new ArrayList<>();

	public static void initResources(String resourcesPath) {
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
				if (protocol.equalsIgnoreCase(NemoLightConstants.FILE_PROTOCOL)) {
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
				extractFiles(files, basePath, protocol);
			}
		}
	}

	private static void extractFiles(List<String> files, String basePath,
		String protocol) {

		InputStream input = null;
		for (String fileName : files) {
			try {
				if (protocol.equalsIgnoreCase(NemoLightConstants.FILE_PROTOCOL)) {
					File f = new File(String.format("%s%s", basePath, fileName));
					input = new FileInputStream(f);
				} else if (protocol
					.equalsIgnoreCase(NemoLightConstants.JAR_PROTOCOL)) {
					ClassLoader classLoader = (ClassLoader) WebConfig
						.getValue(WebConfig.DEPLOYMENT_CLASSLOADER);
					URL res = classLoader.getResource(String.format("%s%s",
						basePath, fileName));
					if (res != null) {
						input = res.openStream();
					}
				} else {
					LOGGER.error(String.format("Unknown protocol '%s'",
						protocol));
					return;
				}
				if (input == null) {
					throw new IOException("Can not read input file");
				}
				File target = new File(String.format("%s%s",
					NemoLightConstants.RESOURCES_FOLDER, fileName));
				if (!target.exists()) {
					if (!target.getParentFile().mkdirs()
						&& !target.createNewFile()) {
						throw new IOException("Can not create target file");
					}
				}
				Files.copy(input, target.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
				LOGGER.info(String.format("Resource '%s' extracted.", fileName));
			} catch (IOException ex) {
				LOGGER.error(String.format("Can not extract resources: '%s'",
					fileName), ex);
			}
		}
	}

	public static final boolean deployWebPages(Configuration config,
		String packageName) {
		ClassPathScanner scanner = new ClassPathScanner();
		scanner.addIncludeFilter(new AnnotationTypeFilter(RouteElement.class));
		scanner.addIncludeFilter(new SuperClassFilter(WebRouteServlet.class));

		WebRouteServlet servlet;
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
			.findCandidateComponents("fr.nemolovich.apps.nemolight.provided.");
		classes.addAll(scanner.findCandidateComponents(packageName));

		config.clearTemplateCache();
		DeployResourceManager.SERVLETS.clear();
		DeployResourceManager.FILES.clear();

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

				if (newInstance instanceof WebRouteServlet) {
					servlet = (WebRouteServlet) newInstance;
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
			for (File f : getAllFiles(deployFolder,
				NemoLightConstants.RECURSIVE_SEARCH)) {
				String uriPath = f.toURI().toString();
				String routePath = uriPath.substring(uriPath
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

	private static List<File> getAllFiles(File root) {
		return getAllFiles(root, NemoLightConstants.DEFAULT_SEARCH);
	}

	private static List<File> getAllFiles(File root, int options) {
		List<File> files = new ArrayList<>();
		for (File f : root.listFiles()) {
			if (f.isFile()) {
				files.add(f);
			} else if (f.isDirectory()
				&& options == NemoLightConstants.RECURSIVE_SEARCH) {
				files.addAll(getAllFiles(f, options));
			}
		}
		return files;
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
		for (WebRouteServlet servlet
			: DeployResourceManager.SERVLETS.values()) {
			Spark.get(servlet.getGetRoute());
			Spark.post(servlet.getPostRoute());
		}
		for (FileRoute route : DeployResourceManager.FILES) {
			Spark.get(route);
		}
	}

	public static List<String> initializeClassLoader() {

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
			for (File f : deployFolder.listFiles()) {
				URL url;
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
				InputStream is = classLoader
					.getResourceAsStream(
						NemoLightConstants.JAR_DEPLOY_FILE);
				addMavenDependencies(url, classLoader);
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
			for (File f : dependenciesFolder.listFiles()) {
				URL url;
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
			for (InputStream is : deployFiles) {
				DeployConfig deployConfig = DeployConfig.loadConfig(is);
				String packageName = deployConfig
					.getString(DeployConfig.DEPLOY_PACKAGE);
				if (packageName != null) {
					packagesName.add(packageName);
				}
			}
		} catch (JAXBException ex) {
			LOGGER.error("Can not load config", ex);
		}

		return packagesName;
	}

	private static void addMavenDependencies(URL url, URLClassLoader classLoader) {
		MavenXpp3Reader reader = new MavenXpp3Reader();

		try {
			JarFile jarFile = new JarFile(new File(url.toURI()));

			List<String> files = Utils.getAllFilesFrom(jarFile,
				NemoLightConstants.JAR_MAVEN_FOLDER,
				NemoLightConstants.EXCLUDE_CLASS_FILES
				| NemoLightConstants.EXCLUDE_FOLDERS);

			String pomPath = null;
			for (String file : files) {
				if (file.endsWith("pom.xml")) {
					pomPath = file;
					break;
				}
			}
			if (pomPath == null) {
				throw new IOException("Can not locate pom file in JAR file");
			}

			InputStream is = classLoader.getResourceAsStream(String.format(
				"%s%s", NemoLightConstants.JAR_MAVEN_FOLDER, pomPath));
			Model model = reader.read(new InputStreamReader(is));

			String mavenURL = WebConfig
				.getStringValue(WebConfig.MAVEN_REPOSITORY);

			String outputPath = NemoLightConstants.DEPENDENCIES_FOLDER;

			DependenciesDownloader.downloadDependencies(model, outputPath,
				mavenURL);

		} catch (IOException | DependenciesException | XmlPullParserException | SearchFileOptionException | URISyntaxException ex) {
			System.err.printf("Can not read pom file: %s\n", ex.getMessage());
		}
	}

	public static List<String> getBeans() {
		return new ArrayList<>(
			DeployResourceManager.SERVLETS.keySet());
	}

	public static WebRouteServlet getBean(String beanName) {
		return DeployResourceManager.SERVLETS.get(beanName);
	}

}
