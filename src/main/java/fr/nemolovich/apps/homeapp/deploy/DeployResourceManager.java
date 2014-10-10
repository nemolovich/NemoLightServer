/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.nemolovich.apps.homeapp.config.route.RouteElement;
import fr.nemolovich.apps.homeapp.constants.HomeAppConstants;
import fr.nemolovich.apps.homeapp.reflection.AnnotationTypeFilter;
import fr.nemolovich.apps.homeapp.reflection.ClassPathScanner;
import fr.nemolovich.apps.homeapp.reflection.SuperClassFilter;
import fr.nemolovich.apps.homeapp.route.WebRoute;
import fr.nemolovich.apps.homeapp.route.file.FileRoute;
import fr.nemolovich.apps.homeapp.route.pages.FreemarkerRoute;
import fr.nemolovich.apps.homeapp.utils.Utils;
import freemarker.template.Configuration;

/**
 *
 * @author Nemolovich
 */
public final class DeployResourceManager {

	private static final Logger LOGGER = Logger
			.getLogger(DeployResourceManager.class.getName());

	public static List<WebRoute> ROUTES = new ArrayList();

	public static void initResources(String resourcesPath) {
		URL url = DeployResourceManager.class.getClassLoader().getResource(
				resourcesPath);
		if (url == null) {
			url = DeployResourceManager.class.getClassLoader().getResource(
					HomeAppConstants.SRC_FOLDER.concat("/").concat(
							resourcesPath));
		}
		List<String> files = null;
		if (url != null) {
			String basePath = null;
			String protocol = url.getProtocol();
			if (protocol.equalsIgnoreCase(HomeAppConstants.FILE_PROTOCOL)) {
				try {
					File path = new File(url.toURI());
					files = Utils.getAllFilesFrom("", path);
					basePath = url.toURI().getPath().substring(1);
				} catch (URISyntaxException ex) {
					LOGGER.log(Level.SEVERE, "Can not load path as folder", ex);
				}
			} else if (protocol.equalsIgnoreCase(HomeAppConstants.JAR_PROTOCOL)) {
				JarFile jar = Utils.extractJar(url);
				files = Utils.getAllFilesFrom(jar, resourcesPath, HomeAppConstants.EXCLUDE_CLASS_FILES);
				basePath = resourcesPath;
			} else {
				LOGGER.log(Level.SEVERE, "Unknown protocol '".concat(protocol)
						.concat("'"));
			}
			if (files != null) {
				deployFiles(files, basePath, protocol);
			}
		}
	}

	private static void deployFiles(List<String> files, String basePath,
			String protocol) {

		InputStream input = null;
		for (String fileName : files) {
			try {
				if (protocol.equalsIgnoreCase(HomeAppConstants.FILE_PROTOCOL)) {
					File f = new File(basePath.concat(fileName));
					input = new FileInputStream(f);
				} else if (protocol
						.equalsIgnoreCase(HomeAppConstants.JAR_PROTOCOL)) {
					URL res = DeployResourceManager.class.getClassLoader()
							.getResource(basePath.concat(fileName));
					if (res != null) {
						input = res.openStream();
					}
				} else {
					LOGGER.log(Level.SEVERE,
							"Unknown protocol '".concat(protocol).concat("'"));
					return;
				}
				if (input == null) {
					throw new IOException("Can not read input file");
				}
				File target = new File(
						HomeAppConstants.RESOURCES_FOLDER.concat(fileName));
				if (!target.exists()) {
					if (!target.getParentFile().mkdirs()
							&& !target.createNewFile()) {
						throw new IOException("Can not create target file");
					}
				}
				Files.copy(input, target.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				LOGGER.log(Level.INFO,
						"Resource '".concat(fileName).concat("' deployed."));
			} catch (IOException ex) {
				LOGGER.log(
						Level.SEVERE,
						"Can not deploy resources: '".concat(fileName).concat(
								"'"), ex);
			}
		}
	}

	public static final boolean deployWebPages(Configuration config) {
		ClassPathScanner scanner = new ClassPathScanner();
		scanner.addIncludeFilter(new AnnotationTypeFilter(RouteElement.class));
		scanner.addIncludeFilter(new SuperClassFilter(FreemarkerRoute.class));

		FreemarkerRoute route;
		for (Class<?> c : scanner
				.findCandidateComponents("fr.nemolovich.apps.homeapp")) {
			try {
				RouteElement annotation = c.getAnnotation(RouteElement.class);
				String path = annotation.path();
				String page = annotation.page();
				Constructor<?> cst = c.getConstructor(String.class,
						String.class, Configuration.class);
				route = (FreemarkerRoute) cst.newInstance(path, page, config);
				ROUTES.add(route);
				LOGGER.log(
						Level.INFO,
						"Resource '".concat(c.getName()).concat(
								"' has been deployed!"));

			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException ex) {
				LOGGER.log(Level.SEVERE, "Error while deploying resources", ex);
			}
		}
		return false;
	}

	public static void deloyWebApp(String deployFolderPath) {
		File deployFolder = new File(deployFolderPath);
		try {
			if (!deployFolder.exists() || !deployFolder.isDirectory()) {
				throw new FileNotFoundException("The deploy folder '".concat(
						deployFolderPath).concat("' can not be located"));
			}
			FileRoute route;
			for (File f : getAllFile(deployFolder,
					HomeAppConstants.RECURSIVE_SEARCH)) {
				String uriPath = f.toURI().toString();
				route = new FileRoute(uriPath.substring(uriPath
						.lastIndexOf(deployFolderPath)
						+ (deployFolderPath.length())), f);
				ROUTES.add(route);
				LOGGER.log(
						Level.INFO,
						"Resource '".concat(f.getName()).concat(
								"' has been deployed!"));
			}
		} catch (FileNotFoundException ex) {
			LOGGER.log(Level.SEVERE, "Error while deploying webapp", ex);
		}
	}

	private static List<File> getAllFile(File root) {
		return getAllFile(root, HomeAppConstants.DEFAULT_SEARCH);
	}

	private static final List<File> getAllFile(File root, int options) {
		List<File> files = new ArrayList();
		for (File f : root.listFiles()) {
			if (f.isFile()) {
				files.add(f);
			} else if (f.isDirectory()
					&& options == HomeAppConstants.RECURSIVE_SEARCH) {
				files.addAll(getAllFile(f, options));
			}
		}
		return files;
	}

}
